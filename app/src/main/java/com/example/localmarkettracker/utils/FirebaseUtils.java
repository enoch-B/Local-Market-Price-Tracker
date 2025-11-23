package com.example.localmarkettracker.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.localmarkettracker.models.Comment;
import com.example.localmarkettracker.models.ItemPrice;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseUtils {

    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final String TAG = "FirebaseUtils";

    // ---------------------- CALLBACKS ----------------------
    public interface OnQueryComplete {
        void onComplete(QuerySnapshot snap, Exception e);
    }

    public interface ListCallback {
        void onResult(List<String> list);
    }

    public interface PriceHistoryCallback {
        void onResult(List<ItemPrice> list, Exception e);
    }

    public interface GenericCallback {
        void onComplete(String id, Exception e);
    }

    public interface CommentCallback {
        void onComplete(List<Comment> list, Exception e);
    }

    public interface ConversionCallback {
        void onResult(double rate, Exception e);
    }

    // ---------------------- PRODUCT NAMES ----------------------
    public static void getProductNames(ListCallback cb) {
        db.collection("products")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> list = new ArrayList<>();
                    if (queryDocumentSnapshots != null) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            String name = doc.getString("name");
                            if (name != null) list.add(name);
                        }
                    }
                    // Ensure UI updates happen on main thread
                    mainHandler.post(() -> cb.onResult(list));
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting product names", e);
                    // Return empty list on failure to prevent UI crash
                    mainHandler.post(() -> cb.onResult(new ArrayList<>()));
                });
    }

    // ---------------------- MARKET NAMES ----------------------
    public static void getMarketNames(ListCallback cb) {
        db.collection("markets")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> list = new ArrayList<>();
                    if (queryDocumentSnapshots != null) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            String name = doc.getString("name");
                            if (name != null) list.add(name);
                        }
                    }
                    mainHandler.post(() -> cb.onResult(list));
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting market names", e);
                    mainHandler.post(() -> cb.onResult(new ArrayList<>()));
                });
    }

    // ---------------------- PRICE HISTORY ----------------------
    public static void getPriceHistoryForItem(String itemId, PriceHistoryCallback cb) {
        db.collection("prices")
                .whereEqualTo("itemId", itemId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ItemPrice> prices = new ArrayList<>();
                    if (queryDocumentSnapshots != null) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            ItemPrice p = doc.toObject(ItemPrice.class);
                            if (p != null) prices.add(p);
                        }
                    }
                    mainHandler.post(() -> cb.onResult(prices, null));
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting price history", e);
                    mainHandler.post(() -> cb.onResult(null, e));
                });
    }

    // ---------------------- COMMENTS ----------------------
    public static void getCommentsForItem(String itemId, CommentCallback cb) {
        db.collection("comments")
                .whereEqualTo("itemId", itemId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Comment> comments = new ArrayList<>();
                    if (queryDocumentSnapshots != null) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Comment c = doc.toObject(Comment.class);
                            if (c != null) comments.add(c);
                        }
                    }
                    mainHandler.post(() -> cb.onComplete(comments, null));
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting comments", e);
                    mainHandler.post(() -> cb.onComplete(null, e));
                });
    }

    public static void addComment(Comment c, GenericCallback cb) {
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", c.getItemId());
        map.put("text", c.getText());
        map.put("userId", c.getUserId());
        map.put("timestamp", c.getTimestamp() != null ? c.getTimestamp() : Timestamp.now());

        db.collection("comments")
                .add(map)
                .addOnSuccessListener(documentReference ->
                        cb.onComplete(documentReference.getId(), null))
                .addOnFailureListener(e ->
                        cb.onComplete(null, e));
    }

    // ---------------------- LATEST PRICES (CRASH FIXED HERE) ----------------------
    public static void getLatestPrices(OnQueryComplete cb) {
        db.collection("prices")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(50)
                .get()
                // Using separate listeners prevents RuntimeExecutionException on PERMISSION_DENIED
                .addOnSuccessListener(queryDocumentSnapshots ->
                        cb.onComplete(queryDocumentSnapshots, null))
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting latest prices", e);
                    cb.onComplete(null, e);
                });
    }

    // ---------------------- ADD PRICE ----------------------
    public static void addPrice(ItemPrice p, GenericCallback cb) {
        Map<String, Object> map = new HashMap<>();
        map.put("productName", p.getProductName());
        map.put("marketName", p.getMarketName());
        map.put("price", p.getPrice());
        map.put("currency", p.getCurrency());
        map.put("reporterId", p.getReporterId());
        map.put("timestamp", p.getTimestamp() != null ? p.getTimestamp() : Timestamp.now());
        map.put("verified", p.isVerified());

        db.collection("prices")
                .add(map)
                .addOnSuccessListener(documentReference ->
                        cb.onComplete(documentReference.getId(), null))
                .addOnFailureListener(e ->
                        cb.onComplete(null, e));
    }

    // ---------------------- USER ID ----------------------
    public static String getCurrentUid() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            return "guest";
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // ---------------------- CURRENCY CONVERSION ----------------------
    public static void getConversionRate(String from, String to, ConversionCallback cb) {
        new Thread(() -> {
            try {
                String api = "https://api.exchangerate.host/latest?base=" + from + "&symbols=" + to;

                java.net.URL url = new java.net.URL(api);
                java.net.HttpURLConnection conn =
                        (java.net.HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int code;
                try {
                    code = conn.getResponseCode();
                } catch (IOException io) {
                    mainHandler.post(() -> cb.onResult(0, io));
                    return;
                }

                if (code != 200) {
                    int finalCode = code;
                    mainHandler.post(() ->
                            cb.onResult(0, new Exception("HTTP " + finalCode))
                    );
                    return;
                }

                java.io.InputStream is = conn.getInputStream();
                java.util.Scanner scanner = new java.util.Scanner(is).useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";

                JSONObject json = new JSONObject(response);
                // Check if "rates" exists to avoid JSONException
                if (json.has("rates")) {
                    double rate = json.getJSONObject("rates").getDouble(to);
                    mainHandler.post(() -> cb.onResult(rate, null));
                } else {
                    mainHandler.post(() -> cb.onResult(0, new Exception("Invalid API response")));
                }

            } catch (Exception ex) {
                mainHandler.post(() -> cb.onResult(0, ex));
            }
        }).start();
    }
}
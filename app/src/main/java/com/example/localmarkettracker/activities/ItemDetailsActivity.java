package com.example.localmarkettracker.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.localmarkettracker.R;
import com.example.localmarkettracker.adapters.CommentAdapter;
import com.example.localmarkettracker.adapters.PriceAdapter;
import com.example.localmarkettracker.models.Comment;
import com.example.localmarkettracker.models.ItemPrice;
import com.example.localmarkettracker.utils.ChartUtils;
import com.example.localmarkettracker.utils.FirebaseUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * ItemDetailsActivity - show price history for one item and comments/ratings
 */
public class ItemDetailsActivity extends AppCompatActivity {

    private String itemId;
    private LineChart chart;
    private RecyclerView rvComments;
    private CommentAdapter commentAdapter;
    private List<Comment> comments = new ArrayList<>();
    private EditText etComment;
    private Button btnSend;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_market_detail_full); // reuse detailed layout

        itemId = getIntent().getStringExtra("itemId");
        chart = findViewById(R.id.lineChartFull);
        rvComments = findViewById(R.id.rvComments);
        etComment = findViewById(R.id.etComment);
        btnSend = findViewById(R.id.btnSendComment);
        progress = findViewById(R.id.progressDetail);

        commentAdapter = new CommentAdapter(comments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.setAdapter(commentAdapter);

        loadPriceHistory();
        loadComments();

        btnSend.setOnClickListener(v -> sendComment());
    }

    private void loadPriceHistory() {
        progress.setVisibility(View.VISIBLE);
        FirebaseUtils.getPriceHistoryForItem(itemId, (list, e) -> {
            progress.setVisibility(View.GONE);
            if (e != null) return;
            // chart helper
            ChartUtils.plotPriceHistory(chart, list);
        });
    }

    private void loadComments() {
        FirebaseUtils.getCommentsForItem(itemId, (list, e) -> {
            if (e != null) return;
            comments.clear();
            comments.addAll(list);
            commentAdapter.notifyDataSetChanged();
        });
    }

    private void sendComment() {
        String text = etComment.getText().toString().trim();
        if (text.isEmpty()) return;
        Comment c = new Comment();
        c.setItemId(itemId);
        c.setText(text);
        c.setUserId(FirebaseUtils.getCurrentUid());
        c.setTimestamp(Timestamp.now());
        FirebaseUtils.addComment(c, (id, err) -> {
            if (err == null) {
                etComment.setText("");
                loadComments();
            } else {
                Toast.makeText(this, "Failed: " + err.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.localmarkettracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.Comment; // ✅ Import Comment model
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying comments in ItemDetailsActivity.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    // ✅ List now holds the correct model: Comment
    private final List<Comment> comments;

    // ✅ Constructor now accepts the correct list type: List<Comment>
    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // ✅ Get a Comment object
        Comment commentEntry = comments.get(position);

        // Show the actual comment text (assuming Comment has a getText() method)
        // Adjust the TextView used (e.g., tvTitle might be better named tvCommentText)
        holder.tvTitle.setText(commentEntry.getText());

        // Show user/reporter information (assuming Comment has a getUserName() or similar)
        // NOTE: If your Comment model only stores UserId, you must fetch the user name asynchronously
        holder.tvReporter.setText("User: " + commentEntry.getUserId()); 

        // Show timestamp safely
        Timestamp ts = commentEntry.getTimestamp();
        if (ts != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
            holder.tvMeta.setText("Posted: " + sdf.format(ts.toDate()));
        } else {
            holder.tvMeta.setText("Posted: N/A");
        }
    }

    @Override
    public int getItemCount() {
        return comments != null ? comments.size() : 0;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle; // Used for Comment Text
        TextView tvReporter; // Used for User ID/Name
        TextView tvMeta; // Used for Timestamp

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvCommentTitle);
            tvReporter = itemView.findViewById(R.id.tvCommentReporter);
            tvMeta = itemView.findViewById(R.id.tvCommentMeta);
        }
    }
}
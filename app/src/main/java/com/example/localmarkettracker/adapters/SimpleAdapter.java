package com.example.localmarkettracker.adapters;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.Holder> {

    List<String> data;

    public SimpleAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView tv = new TextView(parent.getContext());
        tv.setPadding(16, 24, 16, 24);
        tv.setTextSize(16);
        return new Holder(tv);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ((TextView) holder.itemView).setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        public Holder(@NonNull android.view.View itemView) {
            super(itemView);
        }
    }
}

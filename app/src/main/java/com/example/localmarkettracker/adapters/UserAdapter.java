package com.example.localmarkettracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.models.UserProfile;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    public interface OnRoleChangeListener {
        void onRoleChange(UserProfile user, String newRole);
    }

    private List<UserProfile> list;
    private OnRoleChangeListener listener;

    public UserAdapter(List<UserProfile> list, OnRoleChangeListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder h, int i) {
        UserProfile u = list.get(i);

        h.txtName.setText(u.getDisplayName());
        h.txtEmail.setText(u.getEmail());
        h.txtRole.setText(u.getRole());

        h.btnAdmin.setOnClickListener(v -> {
            if (listener != null) listener.onRoleChange(u, "admin");
        });

        h.btnUser.setOnClickListener(v -> {
            if (listener != null) listener.onRoleChange(u, "user");
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class UserHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail, txtRole;
        Button btnAdmin, btnUser;

        public UserHolder(@NonNull View v) {
            super(v);
            txtName = v.findViewById(R.id.txtName);
            txtEmail = v.findViewById(R.id.txtEmail);
            txtRole = v.findViewById(R.id.txtRole);
            btnAdmin = v.findViewById(R.id.btnMakeAdmin);
            btnUser = v.findViewById(R.id.btnMakeUser);
        }
    }
}

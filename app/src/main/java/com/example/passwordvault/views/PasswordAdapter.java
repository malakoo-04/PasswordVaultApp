package com.example.passwordvault.views;

import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passwordvault.R;
import com.example.passwordvault.models.PasswordModel;

import java.util.ArrayList;
import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PasswordModel passwordModel);
    }

    private final List<PasswordModel> passwordList = new ArrayList<>();
    private final OnItemClickListener listener;

    public PasswordAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<PasswordModel> items) {
        passwordList.clear();
        if (items != null) {
            passwordList.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_password, parent, false);
        return new PasswordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        PasswordModel model = passwordList.get(position);
        holder.bind(model, listener);
    }

    @Override
    public int getItemCount() {
        return passwordList.size();
    }

    static class PasswordViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgService;
        private final TextView tvServiceName;
        private final TextView tvUsername;

        public PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            imgService = itemView.findViewById(R.id.imgService);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }

        void bind(PasswordModel model, OnItemClickListener listener) {
            tvServiceName.setText(model.getServiceName());
            tvUsername.setText(model.getUsername());

            imgService.setImageResource(R.drawable.ic_service_default);

            itemView.setOnClickListener(v -> listener.onItemClick(model));
        }
    }
}
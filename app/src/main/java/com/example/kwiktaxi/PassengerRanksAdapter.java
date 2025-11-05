package com.example.kwiktaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.models.PassengerRanksResponse;

import java.util.List;

public class PassengerRanksAdapter extends RecyclerView.Adapter<PassengerRanksAdapter.VH> {
    public interface OnRankClick { void onClick(PassengerRanksResponse.Rank rank); }

    private final List<PassengerRanksResponse.Rank> items;
    private final OnRankClick onRankClick;

    public PassengerRanksAdapter(List<PassengerRanksResponse.Rank> items, OnRankClick onRankClick) {
        this.items = items;
        this.onRankClick = onRankClick;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passenger_rank, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        PassengerRanksResponse.Rank r = items.get(position);
        holder.tvName.setText(r.getName());
        String loc = r.getCity() != null ? r.getCity() : "";
        if (r.getProvince() != null && !r.getProvince().isEmpty()) {
            loc += (loc.isEmpty() ? "" : ", ") + r.getProvince();
        }
        holder.tvLocation.setText(loc);
        holder.itemView.setOnClickListener(v -> onRankClick.onClick(r));
    }

    @Override
    public int getItemCount() { return items == null ? 0 : items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvLocation;
        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
        }
    }
}


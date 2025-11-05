package com.example.kwiktaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.models.PassengerRankDestinationsResponse;

import java.util.List;

public class PassengerDestinationsAdapter extends RecyclerView.Adapter<PassengerDestinationsAdapter.VH> {
    public interface OnDestinationClick { void onClick(PassengerRankDestinationsResponse.Destination dest); }

    private final List<PassengerRankDestinationsResponse.Destination> items;
    private final OnDestinationClick onDestinationClick;

    public PassengerDestinationsAdapter(List<PassengerRankDestinationsResponse.Destination> items, OnDestinationClick onDestinationClick) {
        this.items = items;
        this.onDestinationClick = onDestinationClick;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passenger_destination, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        PassengerRankDestinationsResponse.Destination d = items.get(position);
        holder.tvName.setText(d.getDestination_name());
        holder.tvDetails.setText("Fare: R" + d.getFare() + " • " + d.getDistance_km() + "km • " + d.getEstimated_duration() + "min");
        holder.itemView.setOnClickListener(v -> onDestinationClick.onClick(d));
    }

    @Override
    public int getItemCount() { return items == null ? 0 : items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails;
        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDetails = itemView.findViewById(R.id.tvDetails);
        }
    }
}


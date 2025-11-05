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
        
        // Origin Rank
        if (d.getOrigin_rank() != null && d.getOrigin_rank().getName() != null) {
            holder.tvOriginRank.setText(d.getOrigin_rank().getName());
            String originLoc = "";
            if (d.getOrigin_rank().getCity() != null && !d.getOrigin_rank().getCity().isEmpty()) {
                originLoc = d.getOrigin_rank().getCity();
            }
            if (d.getOrigin_rank().getProvince() != null && !d.getOrigin_rank().getProvince().isEmpty()) {
                originLoc += (originLoc.isEmpty() ? "" : ", ") + d.getOrigin_rank().getProvince();
            }
            holder.tvOriginLocation.setText(originLoc.isEmpty() ? "" : originLoc);
        } else {
            holder.tvOriginRank.setText("Unknown Origin");
            holder.tvOriginLocation.setText("");
        }
        
        // Destination Rank
        if (d.getDestination_rank() != null && d.getDestination_rank().getName() != null) {
            holder.tvDestinationRank.setText(d.getDestination_rank().getName());
            String destLoc = "";
            if (d.getDestination_rank().getCity() != null && !d.getDestination_rank().getCity().isEmpty()) {
                destLoc = d.getDestination_rank().getCity();
            }
            if (d.getDestination_rank().getProvince() != null && !d.getDestination_rank().getProvince().isEmpty()) {
                destLoc += (destLoc.isEmpty() ? "" : ", ") + d.getDestination_rank().getProvince();
            }
            holder.tvDestinationLocation.setText(destLoc.isEmpty() ? "" : destLoc);
        } else {
            holder.tvDestinationRank.setText("Unknown Destination");
            holder.tvDestinationLocation.setText("");
        }
        
        // Route Details
        holder.tvDetails.setText("Fare: R" + d.getFare() + " • " + d.getDistance_km() + "km • " + d.getEstimated_duration() + "min");
        holder.itemView.setOnClickListener(v -> onDestinationClick.onClick(d));
    }

    @Override
    public int getItemCount() { return items == null ? 0 : items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvOriginRank, tvOriginLocation, tvDestinationRank, tvDestinationLocation, tvDetails;
        VH(@NonNull View itemView) {
            super(itemView);
            tvOriginRank = itemView.findViewById(R.id.tvOriginRank);
            tvOriginLocation = itemView.findViewById(R.id.tvOriginLocation);
            tvDestinationRank = itemView.findViewById(R.id.tvDestinationRank);
            tvDestinationLocation = itemView.findViewById(R.id.tvDestinationLocation);
            tvDetails = itemView.findViewById(R.id.tvDetails);
        }
    }
}


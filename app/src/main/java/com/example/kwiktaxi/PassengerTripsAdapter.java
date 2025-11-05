package com.example.kwiktaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.models.PassengerTripsResponse;

import java.util.List;

public class PassengerTripsAdapter extends RecyclerView.Adapter<PassengerTripsAdapter.VH> {
    private final List<PassengerTripsResponse.Trip> items;

    public PassengerTripsAdapter(List<PassengerTripsResponse.Trip> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passenger_trip, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        PassengerTripsResponse.Trip t = items.get(position);
        if (t.getRank_destination() != null) {
            holder.tvDestination.setText(t.getRank_destination().getDestination_name());
            holder.tvFare.setText("Fare: R" + t.getRank_destination().getFare());
        }
        if (t.getTaxi() != null && t.getTaxi().getRegistration_number() != null) {
            holder.tvTaxi.setText("Taxi: " + t.getTaxi().getRegistration_number());
        }
        holder.tvStatus.setText("Status: " + t.getStatus());
    }

    @Override
    public int getItemCount() { return items == null ? 0 : items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvDestination, tvTaxi, tvFare, tvStatus;
        VH(@NonNull View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvTaxi = itemView.findViewById(R.id.tvTaxi);
            tvFare = itemView.findViewById(R.id.tvFare);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}


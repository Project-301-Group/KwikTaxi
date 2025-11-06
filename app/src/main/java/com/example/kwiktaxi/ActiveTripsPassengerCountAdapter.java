package com.example.kwiktaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.models.ActiveTripsPassengerCountResponse;

import java.util.List;

public class ActiveTripsPassengerCountAdapter extends RecyclerView.Adapter<ActiveTripsPassengerCountAdapter.VH> {
    private final List<ActiveTripsPassengerCountResponse.Trip> items;

    public ActiveTripsPassengerCountAdapter(List<ActiveTripsPassengerCountResponse.Trip> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_active_trip_passenger_count, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ActiveTripsPassengerCountResponse.Trip t = items.get(position);

        // Trip ID
        holder.tvTripId.setText("Trip ID: " + t.getTrip_id());

        // Vehicle info
        if (t.getTaxi() != null) {
            holder.tvRegistration.setText("Taxi: " + t.getTaxi().getRegistration_number());
            holder.tvCapacity.setText("Capacity: " + t.getTaxi().getCapacity());
        } else {
            holder.tvRegistration.setText("Taxi: N/A");
            holder.tvCapacity.setText("Capacity: N/A");
        }

        // Driver info
        if (t.getDriver() != null) {
            String driverName = t.getDriver().getName() != null ? t.getDriver().getName() : "Unknown";
            holder.tvDriver.setText("Driver: " + driverName);
            if (t.getDriver().getPhone() != null) {
                holder.tvDriverPhone.setText("Phone: " + t.getDriver().getPhone());
            } else {
                holder.tvDriverPhone.setText("Phone: N/A");
            }
        } else {
            holder.tvDriver.setText("Driver: N/A");
            holder.tvDriverPhone.setText("Phone: N/A");
        }

        // Passenger count vs capacity
        int passengerCount = t.getPassenger_count();
        int capacity = t.getTaxi() != null ? t.getTaxi().getCapacity() : 0;
        String passengerInfo = "Passengers: " + passengerCount + " / " + capacity;
        holder.tvPassengerCount.setText(passengerInfo);

        // Show "FULL" if passenger_count == capacity
        if (capacity > 0 && passengerCount >= capacity) {
            holder.tvFullBadge.setVisibility(View.VISIBLE);
            holder.tvFullBadge.setText("FULL");
        } else {
            holder.tvFullBadge.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return items == null ? 0 : items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTripId, tvRegistration, tvCapacity, tvDriver, tvDriverPhone, tvPassengerCount, tvFullBadge;
        VH(@NonNull View itemView) {
            super(itemView);
            tvTripId = itemView.findViewById(R.id.tvTripId);
            tvRegistration = itemView.findViewById(R.id.tvRegistration);
            tvCapacity = itemView.findViewById(R.id.tvCapacity);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvDriverPhone = itemView.findViewById(R.id.tvDriverPhone);
            tvPassengerCount = itemView.findViewById(R.id.tvPassengerCount);
            tvFullBadge = itemView.findViewById(R.id.tvFullBadge);
        }
    }
}


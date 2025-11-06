package com.example.kwiktaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.models.PassengerTripsResponse;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PassengerTripsAdapter extends RecyclerView.Adapter<PassengerTripsAdapter.VH> {
    public interface OnViewPassengersClick {
        void onClick(int tripId);
    }

    private final List<PassengerTripsResponse.Trip> items;
    private final OnViewPassengersClick onViewPassengersClick;

    public PassengerTripsAdapter(List<PassengerTripsResponse.Trip> items, OnViewPassengersClick onViewPassengersClick) {
        this.items = items;
        this.onViewPassengersClick = onViewPassengersClick;
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
        
        // Destination info
        if (t.getRank_destination() != null) {
            PassengerTripsResponse.RankDestination rd = t.getRank_destination();
            holder.tvDestination.setText(rd.getDestination_name() != null ? rd.getDestination_name() : "Unknown Destination");
            holder.tvFare.setText("Fare: R" + rd.getFare());
            holder.tvDistance.setText("Distance: " + rd.getDistance_km() + " km");
            holder.tvDuration.setText("Duration: " + rd.getEstimated_duration() + " min");
        } else {
            holder.tvDestination.setText("Unknown Destination");
            holder.tvFare.setText("Fare: N/A");
            holder.tvDistance.setText("Distance: N/A");
            holder.tvDuration.setText("Duration: N/A");
        }
        
        // Taxi info
        if (t.getTaxi() != null && t.getTaxi().getRegistration_number() != null) {
            holder.tvTaxi.setText("Taxi: " + t.getTaxi().getRegistration_number());
        } else {
            holder.tvTaxi.setText("Taxi: N/A");
        }
        
        // Driver info
        if (t.getDriver() != null) {
            String driverName = t.getDriver().getName() != null ? t.getDriver().getName() : "Unknown Driver";
            holder.tvDriver.setText("Driver: " + driverName);
            if (t.getDriver().getPhone_number() != null) {
                holder.tvDriverPhone.setText("Phone: " + t.getDriver().getPhone_number());
            } else {
                holder.tvDriverPhone.setText("Phone: N/A");
            }
        } else {
            holder.tvDriver.setText("Driver: N/A");
            holder.tvDriverPhone.setText("Phone: N/A");
        }
        
        // Status
        holder.tvStatus.setText("Status: " + (t.getStatus() != null ? t.getStatus() : "Unknown"));
        
        // View Passengers button - only show for active trips
        if ("active".equalsIgnoreCase(t.getStatus())) {
            holder.btnViewPassengers.setVisibility(View.VISIBLE);
            holder.btnViewPassengers.setOnClickListener(v -> {
                if (onViewPassengersClick != null) {
                    onViewPassengersClick.onClick(t.getTrip_id());
                }
            });
        } else {
            holder.btnViewPassengers.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return items == null ? 0 : items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvDestination, tvTaxi, tvFare, tvStatus, tvDistance, tvDuration, tvDriver, tvDriverPhone;
        MaterialButton btnViewPassengers;
        VH(@NonNull View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvTaxi = itemView.findViewById(R.id.tvTaxi);
            tvFare = itemView.findViewById(R.id.tvFare);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvDriver = itemView.findViewById(R.id.tvDriver);
            tvDriverPhone = itemView.findViewById(R.id.tvDriverPhone);
            btnViewPassengers = itemView.findViewById(R.id.btnViewPassengers);
        }
    }
}


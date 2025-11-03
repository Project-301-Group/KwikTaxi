package com.example.kwiktaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kwiktaxi.models.TaxiResponse;
import java.util.List;

public class TaxiAdapter extends RecyclerView.Adapter<TaxiAdapter.ViewHolder> {

    private final List<TaxiResponse> taxiList;

    public TaxiAdapter(List<TaxiResponse> taxiList) {
        this.taxiList = taxiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rank_destination, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaxiResponse taxi = taxiList.get(position);
        holder.taxiRegNumber.setText("Registration: " + taxi.getRegistrationNumber());
        
        if (taxi.getDriver() != null) {
            holder.taxiDriver.setText("Driver: " + taxi.getDriver().getFullName());
        } else {
            holder.taxiDriver.setText("Driver: Not assigned");
        }
        
        holder.taxiStatus.setText("Status: " + taxi.getStatus());
        holder.taxiCapacity.setText("Capacity: " + taxi.getCapacity());
    }

    @Override
    public int getItemCount() {
        return taxiList != null ? taxiList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taxiRegNumber;
        TextView taxiDriver;
        TextView taxiStatus;
        TextView taxiCapacity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taxiRegNumber = itemView.findViewById(R.id.tvDestinationName);
            taxiDriver = itemView.findViewById(R.id.tvFare);
            taxiStatus = itemView.findViewById(R.id.tvStatus);
            taxiCapacity = itemView.findViewById(R.id.tvStatus); // Will use status for now
        }
    }
}


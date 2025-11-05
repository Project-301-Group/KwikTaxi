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

        String driverText = (taxi.getDriver() != null && taxi.getDriver().getFullName() != null)
                ? ("Driver: " + taxi.getDriver().getFullName())
                : "Driver: Not assigned";
        holder.taxiDetails.setText(driverText + " • Capacity: " + taxi.getCapacity());

        holder.taxiStatus.setText("Status: " + taxi.getStatus());
    }

    @Override
    public int getItemCount() {
        return taxiList != null ? taxiList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taxiRegNumber;
        TextView taxiDetails;
        TextView taxiStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taxiRegNumber = itemView.findViewById(R.id.tvDestinationName);
            taxiDetails = itemView.findViewById(R.id.tvDetails);
            taxiStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}


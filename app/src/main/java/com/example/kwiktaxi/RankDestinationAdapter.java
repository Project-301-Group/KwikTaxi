package com.example.kwiktaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kwiktaxi.R;
import com.example.kwiktaxi.models.RankDestinationResponse;
import java.util.List;

public class RankDestinationAdapter extends RecyclerView.Adapter<RankDestinationAdapter.ViewHolder> {

    private final List<RankDestinationResponse> destinationList;

    public RankDestinationAdapter(List<RankDestinationResponse> destinationList) {
        this.destinationList = destinationList;
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
        RankDestinationResponse d = destinationList.get(position);
        holder.destinationName.setText(d.getDestinationName());
        String details = "Fare: R" + d.getFare() + " • " + d.getDistanceKm() + "km • " + d.getEstimatedDuration() + "min";
        holder.details.setText(details);
        holder.status.setText(d.isActive() ? "Active" : "Inactive");
        holder.status.setTextColor(holder.itemView.getResources().getColor(d.isActive() ? android.R.color.holo_green_dark : android.R.color.darker_gray));
        holder.btnToggle.setText(d.isActive() ? "Deactivate" : "Activate");
    }

    @Override
    public int getItemCount() {
        return destinationList != null ? destinationList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView destinationName;
        TextView details;
        TextView status;
        Button btnToggle;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationName = itemView.findViewById(R.id.tvDestinationName);
            details = itemView.findViewById(R.id.tvDetails);
            status = itemView.findViewById(R.id.tvStatus);
            btnToggle = itemView.findViewById(R.id.btnToggle);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

package com.example.kwiktaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kwiktaxi.models.DriverResponse;
import java.util.List;

public class DriverSelectionAdapter extends RecyclerView.Adapter<DriverSelectionAdapter.ViewHolder> {

    private final List<DriverResponse> driverList;
    private final OnDriverSelectedListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnDriverSelectedListener {
        void onDriverSelected(int driverId);
    }

    public DriverSelectionAdapter(List<DriverResponse> driverList, OnDriverSelectedListener listener) {
        this.driverList = driverList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rank_selection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DriverResponse driver = driverList.get(position);
        holder.driverName.setText(driver.getFullName());
        
        // Show driver info (license, phone)
        String driverInfo = driver.getLicenseNumber();
        if (driver.getPhone() != null && !driver.getPhone().isEmpty()) {
            driverInfo += " - " + driver.getPhone();
        }
        if (driver.isHasTaxi()) {
            driverInfo += " (Has Taxi)";
        }
        holder.driverInfo.setText(driverInfo);
        
        // Visual feedback for selection
        boolean isSelected = selectedPosition == position;
        holder.itemView.setSelected(isSelected);
        if (isSelected) {
            holder.itemView.setAlpha(0.8f);
        } else {
            holder.itemView.setAlpha(1.0f);
        }
        
        holder.itemView.setOnClickListener(v -> {
            // Skip if driver already has a taxi
            if (driver.isHasTaxi()) {
                return;
            }
            
            int adapterPosition = holder.getBindingAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return;
            }
            int previous = selectedPosition;
            selectedPosition = adapterPosition;
            if (previous != RecyclerView.NO_POSITION) {
                notifyItemChanged(previous);
            }
            notifyItemChanged(selectedPosition);
            listener.onDriverSelected(driverList.get(adapterPosition).getId());
        });
    }

    @Override
    public int getItemCount() {
        return driverList != null ? driverList.size() : 0;
    }

    public void clearSelection() {
        int previous = selectedPosition;
        selectedPosition = RecyclerView.NO_POSITION;
        if (previous != RecyclerView.NO_POSITION) {
            notifyItemChanged(previous);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView driverName;
        TextView driverInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            driverName = itemView.findViewById(R.id.tvRankName);
            driverInfo = itemView.findViewById(R.id.tvRankLocation);
        }
    }
}


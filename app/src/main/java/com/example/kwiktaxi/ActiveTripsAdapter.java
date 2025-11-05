package com.example.kwiktaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.models.ActiveTripsResponse;

import java.util.List;

public class ActiveTripsAdapter extends RecyclerView.Adapter<ActiveTripsAdapter.VH> {
    public interface OnShowQrClicked { void onShowQr(); }

    private final List<ActiveTripsResponse.ActiveTrip> items;
    private final OnShowQrClicked onShowQrClicked;

    public ActiveTripsAdapter(List<ActiveTripsResponse.ActiveTrip> items, OnShowQrClicked onShowQrClicked) {
        this.items = items;
        this.onShowQrClicked = onShowQrClicked;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_active_trip, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        ActiveTripsResponse.ActiveTrip t = items.get(position);
        String title = (t.getRank_destination() != null ? t.getRank_destination().getDestination_name() : "")
                + " • " + (t.getTaxi() != null ? t.getTaxi().getRegistration_number() : "");
        holder.tvTitle.setText(title);
        holder.tvStatus.setText("Status: " + t.getStatus());
        holder.btnShowQr.setOnClickListener(v -> onShowQrClicked.onShowQr());
    }

    @Override
    public int getItemCount() { return items == null ? 0 : items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvStatus;
        Button btnShowQr;
        VH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnShowQr = itemView.findViewById(R.id.btnShowQr);
        }
    }
}



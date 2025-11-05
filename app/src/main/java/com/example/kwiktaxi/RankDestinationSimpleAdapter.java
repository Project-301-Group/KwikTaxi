package com.example.kwiktaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.models.DriverTaxiInfoResponse;

import java.util.List;

public class RankDestinationSimpleAdapter extends RecyclerView.Adapter<RankDestinationSimpleAdapter.VH> {
    private final List<DriverTaxiInfoResponse.RankDestination> items;

    public RankDestinationSimpleAdapter(List<DriverTaxiInfoResponse.RankDestination> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        DriverTaxiInfoResponse.RankDestination rd = items.get(position);
        holder.t1.setText(rd.getDestination_name());
        holder.t2.setText("Fare: R" + rd.getFare() + " • " + rd.getDistance_km() + "km • " + rd.getEstimated_duration() + "min");
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView t1, t2;
        VH(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(android.R.id.text1);
            t2 = itemView.findViewById(android.R.id.text2);
        }
    }
}



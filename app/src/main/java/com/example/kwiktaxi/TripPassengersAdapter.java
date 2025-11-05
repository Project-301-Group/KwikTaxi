package com.example.kwiktaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kwiktaxi.models.TripPassengersResponse;

import java.util.List;

public class TripPassengersAdapter extends RecyclerView.Adapter<TripPassengersAdapter.VH> {
    private final List<TripPassengersResponse.Passenger> items;

    public TripPassengersAdapter(List<TripPassengersResponse.Passenger> items) {
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
        TripPassengersResponse.Passenger p = items.get(position);
        holder.t1.setText(p.getFirstname() + " " + p.getLastname());
        holder.t2.setText(p.getPhone() + (p.getAddress() != null ? " • " + p.getAddress() : ""));
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



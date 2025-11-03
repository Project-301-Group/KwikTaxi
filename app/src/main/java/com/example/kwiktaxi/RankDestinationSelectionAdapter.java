package com.example.kwiktaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kwiktaxi.models.RankDestinationResponse;
import java.util.List;

public class RankDestinationSelectionAdapter extends RecyclerView.Adapter<RankDestinationSelectionAdapter.ViewHolder> {

    private final List<RankDestinationResponse> destinationList;
    private final RankSelectionAdapter.OnRankSelectedListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public RankDestinationSelectionAdapter(List<RankDestinationResponse> destinationList, RankSelectionAdapter.OnRankSelectedListener listener) {
        this.destinationList = destinationList;
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
        RankDestinationResponse destination = destinationList.get(position);
        holder.destinationName.setText(destination.getDestinationName());
        
        String location = destination.getCity();
        if (destination.getProvince() != null && !destination.getProvince().isEmpty()) {
            location += (location.isEmpty() ? "" : ", ") + destination.getProvince();
        }
        holder.destinationLocation.setText(location);
        
        boolean isSelected = selectedPosition == position;
        holder.itemView.setSelected(isSelected);
        if (isSelected) {
            holder.itemView.setAlpha(0.8f);
        } else {
            holder.itemView.setAlpha(1.0f);
        }
        
        holder.itemView.setOnClickListener(v -> {
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
            listener.onRankSelected(String.valueOf(destinationList.get(adapterPosition).getId()));
        });
    }

    @Override
    public int getItemCount() {
        return destinationList != null ? destinationList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView destinationName;
        TextView destinationLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationName = itemView.findViewById(R.id.tvRankName);
            destinationLocation = itemView.findViewById(R.id.tvRankLocation);
        }
    }
}


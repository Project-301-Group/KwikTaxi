package com.example.kwiktaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
        RankDestinationResponse destination = destinationList.get(position);
        holder.destinationName.setText(destination.getDestinationName());
    }

    @Override
    public int getItemCount() {
        return destinationList != null ? destinationList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView destinationName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationName = itemView.findViewById(R.id.tvDestinationName);
        }
    }
}

package com.example.kwiktaxi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kwiktaxi.models.RankResponse;
import java.util.List;

public class RankSelectionAdapter extends RecyclerView.Adapter<RankSelectionAdapter.ViewHolder> {

    private final List<RankResponse> rankList;
    private final OnRankSelectedListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public interface OnRankSelectedListener {
        void onRankSelected(String rankId);
    }

    public RankSelectionAdapter(List<RankResponse> rankList, OnRankSelectedListener listener) {
        this.rankList = rankList;
        this.listener = listener;
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
        RankResponse rank = rankList.get(position);
        holder.rankName.setText(rank.getName());

        holder.itemView.setSelected(selectedPosition == position);
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
            listener.onRankSelected(rankList.get(adapterPosition).getId());
        });
    }

    @Override
    public int getItemCount() {
        return rankList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView rankName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rankName = itemView.findViewById(R.id.tvRankName);
        }
    }
}

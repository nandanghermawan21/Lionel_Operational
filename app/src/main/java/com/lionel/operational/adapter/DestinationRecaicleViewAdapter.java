package com.lionel.operational.adapter;

import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lionel.operational.R;
import com.lionel.operational.interfaces.OnItemDestinationClickListener;
import com.lionel.operational.model.DestinationModel;

import java.util.List;

public class DestinationRecaicleViewAdapter extends RecyclerView.Adapter<DestinationRecaicleViewAdapter.ViewHolder> {

    private List<DestinationModel> itemList;
    private List<DestinationModel> filteredList;
    private OnItemDestinationClickListener listener;

    public interface OnItemDestinationClickListener {
        void onItemClick(DestinationModel position);
    }

    public void setOnItemClickListener(OnItemDestinationClickListener listener) {
        this.listener = listener;
    }

    public DestinationRecaicleViewAdapter(List<DestinationModel> itemList) {
        this.itemList = itemList;
        this.filteredList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destination, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DestinationModel item = filteredList.get(position);
        holder.destinationMame.setText(item.getId());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView destinationMame;
        TextView branchId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationMame = itemView.findViewById(R.id.textDestinationName);
            branchId = itemView.findViewById(R.id.textBranchId);
        }
    }

    // Method untuk mengupdate dataList dengan filteredList
    public void filterList(List<DestinationModel> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }
}

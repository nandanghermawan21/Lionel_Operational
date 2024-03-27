package com.lionel.operational.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lionel.operational.R;
import com.lionel.operational.model.ShipmentModel;

import java.util.List;

public class ShipmentReceicleViewAdapter extends RecyclerView.Adapter<ShipmentReceicleViewAdapter.ViewHolder> {
    private List<ShipmentModel> itemList;
    private List<ShipmentModel> filteredList;

    public ShipmentReceicleViewAdapter(List<ShipmentModel> itemList) {
        this.itemList = itemList;
        this.filteredList = itemList;
    }

    @NonNull
    @Override
    public ShipmentReceicleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShipmentReceicleViewAdapter.ViewHolder holder, int position) {
        ShipmentModel item = filteredList.get(position);
        holder.destinationMame.setText(item.getParentCode());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView destinationMame;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationMame = itemView.findViewById(R.id.textSttNumber);
        }
    }
}

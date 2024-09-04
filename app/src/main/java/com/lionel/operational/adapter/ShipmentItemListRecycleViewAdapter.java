package com.lionel.operational.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lionel.operational.R;
import com.lionel.operational.model.DestinationModel;
import com.lionel.operational.model.ShipmentItemList;

import java.util.ArrayDeque;
import java.util.List;

public class ShipmentItemListRecycleViewAdapter extends RecyclerView.Adapter<ShipmentItemListRecycleViewAdapter.ViewHolder> {

    private List<ShipmentItemList> itemList;

    public ShipmentItemListRecycleViewAdapter(List<ShipmentItemList> shipmentItemList) {
        this.itemList = shipmentItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipment_list, parent, false);
        return new ShipmentItemListRecycleViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShipmentItemList item = itemList.get(position);
        holder.itemNo.setText(position + 1 + "");
        holder.itemShipmentNo.setText(item.getBarcode());
        holder.itemStatus.setText("1".equals(item.getAccepted()) ? "Sudah Diterima" : "Belum Diterima");
        //jika accepted = 1 maka warna card jadi hijau dan jika accepted = 0 maka warna card jadi merah
        if ("1".equals(item.getAccepted())) {
            holder.cardView.setCardBackgroundColor(holder.cardView.getResources().getColor(R.color.green_ancent));
        } else {
            holder.cardView.setCardBackgroundColor(holder.cardView.getResources().getColor(R.color.reed_ancent));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView itemNo;
        TextView itemShipmentNo;
        TextView itemStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.itemShipmentList);
            itemNo = itemView.findViewById(R.id.itemNo);
            itemShipmentNo = itemView.findViewById(R.id.itemShipmentNo);
            itemStatus = itemView.findViewById(R.id.itemStatus);
        }
    }
}

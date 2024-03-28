package com.lionel.operational.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lionel.operational.R;
import com.lionel.operational.interfaces.OnItemShipmentClickListener;
import com.lionel.operational.model.ShipmentModel;

import java.util.List;

public class ShipmentReceicleViewAdapter extends RecyclerView.Adapter<ShipmentReceicleViewAdapter.ViewHolder> {
    private List<ShipmentModel> itemList;
    private List<ShipmentModel> filteredList;
    private OnItemShipmentClickListener listener;

    public interface OnItemShipmentClickListener {
        void onItemClickDelete(ShipmentModel item);
    }

    public void setOnItemClickListener(OnItemShipmentClickListener listener) {
        this.listener = listener;
    }

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
        holder.sttNumber.setText(item.getBarcode());
        holder.grossWeight.setText(String.valueOf(item.getGrossWeight()));
        holder.length.setText(String.valueOf(item.getLength()));
        holder.width.setText(String.valueOf(item.getWidth()));
        holder.height.setText(String.valueOf(item.getHeight()));
        holder.buttonDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClickDelete(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sttNumber;
        TextView grossWeight;
        TextView length;
        TextView width;
        TextView height;
        ImageView buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sttNumber = itemView.findViewById(R.id.textSttNumber);
            grossWeight = itemView.findViewById(R.id.valueGW);
            length = itemView.findViewById(R.id.valueLength);
            width = itemView.findViewById(R.id.valueWidth);
            height = itemView.findViewById(R.id.valueHeight);
            buttonDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

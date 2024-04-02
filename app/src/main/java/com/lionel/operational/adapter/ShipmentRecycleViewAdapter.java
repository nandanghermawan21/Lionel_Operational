package com.lionel.operational.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lionel.operational.R;
import com.lionel.operational.model.ShipmentModel;

import java.util.List;

public class ShipmentRecycleViewAdapter extends RecyclerView.Adapter<ShipmentRecycleViewAdapter.ViewHolder> {
    private List<ShipmentModel> itemList;
    private List<ShipmentModel> filteredList;
    private OnItemShipmentClickListener listener;

    public interface OnItemShipmentClickListener {
        void onItemClickDelete(ShipmentModel item);
    }

    public void setOnItemClickListener(OnItemShipmentClickListener listener) {
        this.listener = listener;
    }

    public ShipmentRecycleViewAdapter(List<ShipmentModel> itemList) {
        this.itemList = itemList;
        this.filteredList = itemList;
    }

    @NonNull
    @Override
    public ShipmentRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShipmentRecycleViewAdapter.ViewHolder holder, int position) {
        ShipmentModel item = filteredList.get(position);
        holder.sttNumber.setText(item.getSTTNumber());
        holder.grossWeight.setText(String.valueOf(item.getGrossWeight()));
        holder.length.setText(String.valueOf(item.getLength()));
        holder.width.setText(String.valueOf(item.getWidth()));
        holder.height.setText(String.valueOf(item.getHeight()));
        holder.volume.setText(String.valueOf(item.getVolume()));
        holder.volumeWeight.setText(String.valueOf(item.getVolumeWeight()));
        holder.chargeableWeight.setText(String.valueOf(item.getChargeableWeight()));
        holder.shippingFactor.setText(String.valueOf(item.getShippingFactor()));
        //nyalakan console layout jika barcode console tidak kosong
        if (item.getConsoleBarcode() != null && !item.getConsoleBarcode().isEmpty()) {
            holder.consoleLayout.setVisibility(View.VISIBLE);
            holder.consoleCode.setText(item.getConsoleBarcode());
            holder.destination.setText(item.getDestBranchId());
        } else {
            holder.consoleLayout.setVisibility(View.GONE);
        }

        //nyarakan service layout jika service type tidak kosong
        if (item.getServiceType() != null && !item.getServiceType().isEmpty()) {
            holder.serviceLayout.setVisibility(View.VISIBLE);
            holder.serviceType.setText(item.getServiceType());
            holder.natureOfGoods.setText(item.getNatureOfGoods());
            holder.ahippingMethod.setText(item.getShippingMethod());
        } else {
            holder.serviceLayout.setVisibility(View.GONE);
        }
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
        TextView volume;
        TextView volumeWeight;
        TextView chargeableWeight;
        TextView shippingFactor;
        LinearLayout consoleLayout;
        LinearLayout serviceLayout;
        TextView consoleCode;
        TextView destination;
        TextView serviceType;
        TextView natureOfGoods;
        TextView ahippingMethod;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sttNumber = itemView.findViewById(R.id.textSttNumber);
            grossWeight = itemView.findViewById(R.id.valueGW);
            length = itemView.findViewById(R.id.valueLength);
            width = itemView.findViewById(R.id.valueWidth);
            height = itemView.findViewById(R.id.valueHeight);
            buttonDelete = itemView.findViewById(R.id.btnDelete);
            volume = itemView.findViewById(R.id.valueVolume);
            volumeWeight = itemView.findViewById(R.id.valueVolumeWeight);
            chargeableWeight = itemView.findViewById(R.id.valueChargeableWeight);
            shippingFactor = itemView.findViewById(R.id.valueShippingFactor);
            consoleLayout = itemView.findViewById(R.id.consoleInfoLayout);
            serviceLayout = itemView.findViewById(R.id.ServiceInfoLayout);
            consoleCode = itemView.findViewById(R.id.valueBarcodeConsole);
            destination = itemView.findViewById(R.id.valueDestination);
            serviceType = itemView.findViewById(R.id.valueServiceType);
            natureOfGoods = itemView.findViewById(R.id.valueNatureOfGoods);
            ahippingMethod = itemView.findViewById(R.id.valueSHippingMethod);

        }
    }
}

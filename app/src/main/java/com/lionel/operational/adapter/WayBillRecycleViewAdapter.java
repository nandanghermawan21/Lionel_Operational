package com.lionel.operational.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lionel.operational.R;
import com.lionel.operational.model.WayBillModel;

import java.util.ArrayList;
import java.util.List;

public class WayBillRecycleViewAdapter extends RecyclerView.Adapter<WayBillRecycleViewAdapter.ViewHolder> {
    private List<WayBillModel> itemList;
    private List<WayBillModel> filteredList;
    private List<WayBillModel> selectedList;
    private OnItemOptionClickListener listener;

    public interface OnItemOptionClickListener {
        void onItemCheck(WayBillModel position);
    }

    public void setOnItemClickListener(OnItemOptionClickListener listener) {
        this.listener = listener;
    }

    public WayBillRecycleViewAdapter(List<WayBillModel> itemList) {
        this.itemList = itemList;
        this.filteredList = itemList;
        this.selectedList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waybill, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WayBillModel item = filteredList.get(position);
        holder.code.setText(item.getCode());
        holder.type.setText(item.getWaybillType());
        holder.type.setVisibility(View.GONE);
        holder.date.setText(item.getDate());
        holder.shippingAgent.setText(item.getShippingAgentId());
        holder.liner.setText(item.getLinerId());
        holder.branch.setText(item.getBranchId()+" - "+item.getWaybillType());
        holder.destBranch.setText(item.getDestBranchId());
        holder.ttlPieces.setText(item.getTtlPieces());
        holder.ttlChargeableWeight.setText(item.getTtlChargeableWeight());
        if (selectedList != null) {
            holder.check.setChecked(selectedList.contains(item));
        }
        //add listener to check box
        holder.check.setOnClickListener(v -> {
            if (holder.check.isChecked()) {
                selectedList.add(item);
            } else {
                selectedList.remove(item);
            }
            listener.onItemCheck(item);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView code;
        TextView type;
        TextView date;
        TextView shippingAgent;
        TextView liner;
        TextView branch;
        TextView destBranch;
        TextView ttlPieces;
        TextView ttlChargeableWeight;
        CheckBox check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.textCode);
            type = itemView.findViewById(R.id.textType);
            date = itemView.findViewById(R.id.textDate);
            shippingAgent = itemView.findViewById(R.id.valueShippingAgent);
            liner = itemView.findViewById(R.id.valueLiner);
            branch = itemView.findViewById(R.id.valueBranchId);
            destBranch = itemView.findViewById(R.id.valueDestination);
            ttlPieces = itemView.findViewById(R.id.valueTtlPieces);
            ttlChargeableWeight = itemView.findViewById(R.id.valueTtlChargeableWeight);
            check = itemView.findViewById(R.id.check);
        }
    }

    public void filterList(List<WayBillModel> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }

    public List<WayBillModel> getSelectedList() {
        return selectedList;
    }

    //function select all
    public void selectAll() {
        selectedList.clear();
        selectedList.addAll(filteredList);
        notifyDataSetChanged();
    }

    //function unselect all
    public void unSelectAll() {
        selectedList.clear();
        notifyDataSetChanged();
    }

    //funtion to filter by text
    public void filter(String text) {
        List<WayBillModel> filteredList = new ArrayList<>();
        for (WayBillModel item : itemList) {
            if (item.getCode().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        filterList(filteredList);
    }
}

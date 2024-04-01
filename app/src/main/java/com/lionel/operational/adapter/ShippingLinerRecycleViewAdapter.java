package com.lionel.operational.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lionel.operational.R;
import com.lionel.operational.model.ShippingLinerModel;

import java.util.List;

public class ShippingLinerRecycleViewAdapter extends RecyclerView.Adapter<ShippingLinerRecycleViewAdapter.ViewHolder>  {

    private List<ShippingLinerModel> itemList;
    private List<ShippingLinerModel> filteredList;
    private OnItemOptionClickListener listener;

    public interface OnItemOptionClickListener {
        void onItemClick(ShippingLinerModel position);
    }

    public void setOnItemClickListener(OnItemOptionClickListener listener) {
        this.listener = listener;
    }

    public ShippingLinerRecycleViewAdapter(List<ShippingLinerModel> itemList) {
        this.itemList = itemList;
        this.filteredList = itemList;
    }

    @NonNull
    @Override
    public ShippingLinerRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false);
        return new ShippingLinerRecycleViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShippingLinerRecycleViewAdapter.ViewHolder holder, int position) {
        ShippingLinerModel item = filteredList.get(position);
        holder.name.setText(item.getName());
        holder.desc.setText(item.getMaxColi() + " Coli" + " / " + item.getMaxGw() + " Kg");
        holder.desc.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.optionName);
            desc = itemView.findViewById(R.id.optionDesc);
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filterList(List<ShippingLinerModel> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }
}

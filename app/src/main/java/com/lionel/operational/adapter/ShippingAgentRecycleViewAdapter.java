package com.lionel.operational.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lionel.operational.R;
import com.lionel.operational.model.ShippingAgentModel;
import com.lionel.operational.model.ShippingMethodModel;

import java.util.List;

public class ShippingAgentRecycleViewAdapter extends RecyclerView.Adapter<ShippingAgentRecycleViewAdapter.ViewHolder> {

    private List<ShippingAgentModel> itemList;
    private List<ShippingAgentModel> filteredList;
    private OnItemOptionClickListener listener;

    public interface OnItemOptionClickListener {
        void onItemClick(ShippingAgentModel position);
    }

    public void setOnItemClickListener(OnItemOptionClickListener listener) {
        this.listener = listener;
    }

    public ShippingAgentRecycleViewAdapter(List<ShippingAgentModel> itemList) {
        this.itemList = itemList;
        this.filteredList = itemList;
    }

    @NonNull
    @Override
    public ShippingAgentRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false);
        return new ShippingAgentRecycleViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShippingAgentRecycleViewAdapter.ViewHolder holder, int position) {
        ShippingAgentModel item = filteredList.get(position);
        holder.name.setText(item.getName());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.optionName);
            id = itemView.findViewById(R.id.optionId);
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filterList(List<ShippingAgentModel> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }
}

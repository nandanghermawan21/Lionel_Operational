package com.lionel.operational.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lionel.operational.R;
import com.lionel.operational.model.CityModel;
import com.lionel.operational.model.ShippingAgentModel;

import java.util.List;

public class CityRecycleViewAdapter extends RecyclerView.Adapter<CityRecycleViewAdapter.ViewHolder> {

    private List<CityModel> itemList;
    private List<CityModel> filteredList;
    private OnItemOptionClickListener listener;

    public interface OnItemOptionClickListener {
        void onItemClick(CityModel position);
    }

    public void setOnItemClickListener(OnItemOptionClickListener listener) {
        this.listener = listener;
    }

    public CityRecycleViewAdapter(List<CityModel> itemList) {
        this.itemList = itemList;
        this.filteredList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityModel item = filteredList.get(position);
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
            destinationMame = itemView.findViewById(R.id.optionName);
            branchId = itemView.findViewById(R.id.optionDesc);
        }
    }

    public void filterList(List<CityModel> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }
}

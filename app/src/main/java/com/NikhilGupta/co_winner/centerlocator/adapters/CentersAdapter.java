package com.NikhilGupta.co_winner.centerlocator.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.NikhilGupta.co_winner.R;
import com.NikhilGupta.co_winner.centerlocator.models.CentersItem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CentersAdapter extends RecyclerView.Adapter<CentersAdapter.CentersVH>{
    private final ItemClickListener mClickListener;
    private final ArrayList<CentersItem> centersItems = new ArrayList<>();

    public CentersAdapter(ItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    @NonNull
    @Override
    public CentersVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CentersVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_centers, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CentersVH holder, int position) {
        CentersItem centersItem = centersItems.get(position);
        holder.name.setText(centersItem.getName());

        String location = centersItem.getLocation();
        String district = centersItem.getDistrictName();
        String block = centersItem.getBlockName();
        String state = centersItem.getStateName();

        holder.address.setText(String.format("%s, %s, %s, %s", location, district, block, state));
        holder.pincode.setText(String.format("Pincode: %s", centersItem.getPincode()));
    }

    @Override
    public int getItemCount() {
        return centersItems.size();
    }

    public interface ItemClickListener {
        void onItemClick(View v, int pos);
    }

    public void updateDataList(ArrayList<CentersItem> list) {
        centersItems.clear(); // Always clean old data
        centersItems.addAll(list);
        notifyDataSetChanged();
    }

    public class CentersVH extends RecyclerView.ViewHolder{
        TextView name, address, pincode;

        public CentersVH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            pincode = itemView.findViewById(R.id.pincode);
            itemView.setOnClickListener(view -> {
                mClickListener.onItemClick(itemView,getAdapterPosition());
            });
        }
    }
}

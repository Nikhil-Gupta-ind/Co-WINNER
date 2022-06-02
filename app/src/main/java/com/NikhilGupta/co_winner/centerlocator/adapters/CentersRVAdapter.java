package com.NikhilGupta.co_winner.centerlocator.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.NikhilGupta.co_winner.R;
import com.NikhilGupta.co_winner.centerlocator.models.CenterData;

import java.util.ArrayList;

public class CentersRVAdapter extends RecyclerView.Adapter<CentersRVAdapter.CLViewHolder> {
    private Context context;
    private ArrayList<CenterData> centerDataArrayList;
    final String TAG = "Test";

    public CentersRVAdapter(Context context, ArrayList<CenterData> centerDataArrayList) {
        this.context = context;
        this.centerDataArrayList = centerDataArrayList;
        Log.d(TAG, "CLRecyclerViewAdapter: received centerDataArrayList");
    }

    @NonNull
    @Override
    public CentersRVAdapter.CLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_center_locator, parent, false);
        Log.d(TAG, "onCreateViewHolder: returning created ViewHolder");
        return new CLViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CentersRVAdapter.CLViewHolder holder, int position) {
        CenterData centerData = centerDataArrayList.get(position);
        holder.name.setText(centerData.getName());

        String address = centerData.getAddress();
        if (!centerData.getBlock().equals(centerData.getDistrict())) {
            holder.address.setText(String.format("Address:\n%s,\n%s,\n%s", address, centerData.getBlock(), centerData.getDistrict()));
        } else
            holder.address.setText(String.format("Address:\n%s,\n%s,", address, centerData.getDistrict()));

//        holder.block.setText(centerData.getBlock());
//        holder.district.setText(centerData.getDistrict());

        holder.state.setText(centerData.getState());
        holder.vaccine.setText(centerData.getVaccine());
        holder.from.setText(centerData.getFrom());
        holder.to.setText(centerData.getTo());
        Log.d(TAG, "onBindViewHolder: ViewHolders Populated");
    }

    @Override
    public int getItemCount() {
        return centerDataArrayList.size();
    }

    public class CLViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, block, district, state, vaccine, from, to;

        public CLViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
//            block = itemView.findViewById(R.id.block_name);
//            district = itemView.findViewById(R.id.district);
            state = itemView.findViewById(R.id.state);
            vaccine = itemView.findViewById(R.id.vaccine);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            Log.d(TAG, "CLViewHolder: Layout Binded");
        }
    }
}

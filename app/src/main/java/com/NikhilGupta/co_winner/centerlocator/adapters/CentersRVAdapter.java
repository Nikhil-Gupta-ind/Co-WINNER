package com.NikhilGupta.co_winner.centerlocator.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.NikhilGupta.co_winner.R;
import com.NikhilGupta.co_winner.centerlocator.models.SessionsItem;

import java.util.ArrayList;

public class CentersRVAdapter extends RecyclerView.Adapter<CentersRVAdapter.CLViewHolder> {
    private final ArrayList<SessionsItem> sessionsItems = new ArrayList<>();

    @NonNull
    @Override
    public CentersRVAdapter.CLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_center_locator, parent, false);
        return new CLViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CentersRVAdapter.CLViewHolder holder, int position) {
        SessionsItem sessionsItem = sessionsItems.get(position);
        holder.name.setText(sessionsItem.getName());

        String address = sessionsItem.getAddress();
        String block = sessionsItem.getBlockName();
        String district = sessionsItem.getDistrictName();
        if (!block.equalsIgnoreCase(district)) {
            holder.address.setText(String.format("Address:\n%s,\n%s,\n%s", address, block, district));
        } else
            holder.address.setText(String.format("Address:\n%s,\n%s,", address, district));

        holder.state.setText(sessionsItem.getStateName());
        holder.vaccine.setText(sessionsItem.getVaccine());
        holder.from.setText(sessionsItem.getFrom());
        holder.to.setText(sessionsItem.getTo());
    }

    @Override
    public int getItemCount() {
        return sessionsItems.size();
    }

    public void updateDataList(ArrayList<SessionsItem> list) {
        sessionsItems.clear(); // Always clean old data
        sessionsItems.addAll(list);
        notifyDataSetChanged();
    }

    public static class CLViewHolder extends RecyclerView.ViewHolder {
        TextView name, address, state, vaccine, from, to;

        public CLViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            state = itemView.findViewById(R.id.state);
            vaccine = itemView.findViewById(R.id.vaccine);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
        }
    }
}

package com.NikhilGupta.co_winner;

import android.util.Log;

public class CenterData {
    String name, address, block, district, state, vaccine, from, to;
    final String TAG = "Data Test";
    public CenterData(String name, String address, String block, String district, String state, String vaccine, String from, String to) {
        this.name = name;
        this.address = address;
        this.block = block;
        this.district = district;
        this.state = state;
        this.vaccine = vaccine;
        this.from = from;
        this.to = to;
        Log.d(TAG, "CenterData: another object set");
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBlock() {
        return block;
    }

    public String getDistrict() {
        return district;
    }

    public String getState() {
        return state;
    }

    public String getVaccine() {
        return vaccine;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}

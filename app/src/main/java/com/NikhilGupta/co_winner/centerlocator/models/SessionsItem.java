package com.NikhilGupta.co_winner.centerlocator.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SessionsItem {
    @SerializedName("center_id")
    private int centerId;

    private String name;
    private String address;

    @SerializedName("state_name")
    private String stateName;

    @SerializedName("district_name")
    private String districtName;

    @SerializedName("block_name")
    private String blockName;

    private String pincode;
    private String from;
    private String to;
    private double lat;

    @SerializedName("long")
    private double longitude;

    @SerializedName("fee_type")
    private String feeType;

    @SerializedName("session_id")
    private String sessionId;

    private String date;

    @SerializedName("available_capacity")
    private int availableCapacity;

    @SerializedName("available_capacity_dose1")
    private int availableCapacityDose1;

    @SerializedName("available_capacity_dose2")
    private int availableCapacityDose2;

    private String fee;

    @SerializedName("min_age_limit")
    private int minAgeLimit;

    @SerializedName("max_age_limit")
    private int maxAgeLimit;

    @SerializedName("allow_all_age")
    private boolean allowAllAge;

    private String vaccine;
    private ArrayList<Slots> slots;

    public int getCenterId() {
        return centerId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getStateName() {
        return stateName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getBlockName() {
        return blockName;
    }

    public String getPincode() {
        return pincode;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public double getLat() {
        return lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getFeeType() {
        return feeType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getDate() {
        return date;
    }

    public int getAvailableCapacity() {
        return availableCapacity;
    }

    public int getAvailableCapacityDose1() {
        return availableCapacityDose1;
    }

    public int getAvailableCapacityDose2() {
        return availableCapacityDose2;
    }

    public String getFee() {
        return fee;
    }

    public int getMinAgeLimit() {
        return minAgeLimit;
    }

    public int getMaxAgeLimit() {
        return maxAgeLimit;
    }

    public boolean isAllowAllAge() {
        return allowAllAge;
    }

    public String getVaccine() {
        return vaccine;
    }

    public ArrayList<Slots> getSlots() {
        return slots;
    }
}
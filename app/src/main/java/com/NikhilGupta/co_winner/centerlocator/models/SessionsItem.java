package com.NikhilGupta.co_winner.centerlocator.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SessionsItem{
	private String date;
	private String addressL;
	private int minAgeLimit;
	private String nameL;
	private String fee;
	private String feeType;
	private double jsonMemberLong;

	@SerializedName("district_name")
	private String districtName;

	@SerializedName("block_name")
	private String blockName;
	private String stateName;
	private String from;
	private double lat;
	private String pincode;
	private String address;
	private String districtNameL;
	private String sessionId;
	private String stateNameL;
	private int availableCapacity;
	private String blockNameL;
	private String vaccine;
	private List<Slots> slots;
	private int centerId;
	private String name;
	private String to;
	private int availableCapacityDose2;
	private String walkinInd;
	private int availableCapacityDose1;

	public String getDate(){
		return date;
	}

	public String getAddressL(){
		return addressL;
	}

	public int getMinAgeLimit(){
		return minAgeLimit;
	}

	public String getNameL(){
		return nameL;
	}

	public String getFee(){
		return fee;
	}

	public String getFeeType(){
		return feeType;
	}

	public double getJsonMemberLong(){
		return jsonMemberLong;
	}

	public String getDistrictName(){
		return districtName;
	}

	public String getBlockName(){
		return blockName;
	}

	public String getStateName(){
		return stateName;
	}

	public String getFrom(){
		return from;
	}

	public double getLat(){
		return lat;
	}

	public String getPincode(){
		return pincode;
	}

	public String getAddress(){
		return address;
	}

	public String getDistrictNameL(){
		return districtNameL;
	}

	public String getSessionId(){
		return sessionId;
	}

	public String getStateNameL(){
		return stateNameL;
	}

	public int getAvailableCapacity(){
		return availableCapacity;
	}

	public String getBlockNameL(){
		return blockNameL;
	}

	public String getVaccine(){
		return vaccine;
	}

	public List<Slots> getSlots(){
		return slots;
	}

	public int getCenterId(){
		return centerId;
	}

	public String getName(){
		return name;
	}

	public String getTo(){
		return to;
	}

	public int getAvailableCapacityDose2(){
		return availableCapacityDose2;
	}

	public String getWalkinInd(){
		return walkinInd;
	}

	public int getAvailableCapacityDose1(){
		return availableCapacityDose1;
	}
}
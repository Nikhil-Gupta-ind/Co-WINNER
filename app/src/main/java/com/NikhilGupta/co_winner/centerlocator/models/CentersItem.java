package com.NikhilGupta.co_winner.centerlocator.models;

import com.google.gson.annotations.SerializedName;

public class CentersItem{

	@SerializedName("center_id")
	private int centerId;
	private String name;

	@SerializedName("district_name")
	private String districtName;

	@SerializedName("state_name")
	private String stateName;

	private String location;
	private String pincode;

	@SerializedName("block_name")
	private String blockName;

	private String lat;

	@SerializedName("long")
	private String longitude;


	public String getDistrictName(){
		return districtName;
	}

	public String getPincode(){
		return pincode;
	}

	public String getBlockName(){
		return blockName;
	}

	public int getCenterId(){
		return centerId;
	}

	public String getStateName(){
		return stateName;
	}

	public String getName(){
		return name;
	}

	public String getLocation(){
		return location;
	}

	public String getLat(){
		return lat;
	}

	public String getLongitude() {
		return longitude;
	}
}

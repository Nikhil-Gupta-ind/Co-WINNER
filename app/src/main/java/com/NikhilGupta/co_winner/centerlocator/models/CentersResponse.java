package com.NikhilGupta.co_winner.centerlocator.models;

import java.util.ArrayList;
import java.util.List;

public class CentersResponse{
	private ArrayList<CentersItem> centers;
	private int ttl;

	public ArrayList<CentersItem> getCenters(){
		return centers;
	}

	public int getTtl(){
		return ttl;
	}
}
package com.blastedstudios.crittercaptors.util;

public class LocationStruct{
	public double lat, lon;
	
	public LocationStruct(double lat, double lon){
		this.lat = lat;
		this.lon = lon;
	}
	
	public LocationStruct add(LocationStruct locationStruct){
		lat += locationStruct.lat;
		lon += locationStruct.lon;
		return this;
	}
	
	public LocationStruct sub(LocationStruct locationStruct){
		lat -= locationStruct.lat;
		lon -= locationStruct.lon;
		return this;
	}
	
	public LocationStruct tmp(){
		return new LocationStruct(lat, lon);
	}
}
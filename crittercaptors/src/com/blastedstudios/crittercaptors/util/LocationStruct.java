package com.blastedstudios.crittercaptors.util;

public class LocationStruct{
	public static final LocationStruct DEFAULT = new LocationStruct(36.878705,-76.260400),
			ZERO = new LocationStruct(0,0);
	
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
	
	public boolean equals(LocationStruct location){
		return lat == location.lat && lon == location.lon;
	}
	
	public String toString(){
		return "Lat: " + lat + " / Lon: " + lon;
	}
}
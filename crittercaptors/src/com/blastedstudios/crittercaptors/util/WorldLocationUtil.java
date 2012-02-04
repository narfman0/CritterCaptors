package com.blastedstudios.crittercaptors.util;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.creature.AffinityCalculator;
import com.blastedstudios.crittercaptors.creature.AffinityEnum;
import com.blastedstudios.crittercaptors.ui.terrain.Terrain;

/**
 * Keep world location up to date. Since floats are too inaccurate, using doubles
 * up to render, and setting initial lat so as to shrink the error when converting
 */
public class WorldLocationUtil {
	private double lat = 0.0, lon = 0.0;
	private final double latInitial, lonInitial;
	private BufferedImage worldLocationLastImage;
	private float timeSinceLastUpdate = TIME_TO_UPDATE;
	private static final float TIME_TO_UPDATE = 60;
	private HashMap<AffinityEnum, Float> currentWorldAffinities;
	
	public WorldLocationUtil(){
		latInitial = Gdx.input.getGPSLatitude();
		lonInitial = Gdx.input.getGPSLongitude();
	}

	public void update(){
		timeSinceLastUpdate += Gdx.graphics.getDeltaTime();
		if(timeSinceLastUpdate > TIME_TO_UPDATE){
			lat = Gdx.input.getGPSLatitude();
			lon = Gdx.input.getGPSLongitude();
			timeSinceLastUpdate = 0;
			try {
				worldLocationLastImage = ImageIO.read(
						new URL("http://ojw.dev.openstreetmap.org/StaticMap/?lat="+
								lat+"&lon="+lon+"&z=18&w=64&h=64&mode=Export&show=1"));
				currentWorldAffinities = AffinityCalculator.getAffinitiesFromTexture(worldLocationLastImage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public double getLatitude(){
		return lat;
	}
	
	public double getLongitude(){
		return lon;
	}

	public float[] getHeightmap(Vector3 location){
		float[] heightMap = new float[(Terrain.DEFAULT_WIDTH + 1) * (Terrain.DEFAULT_WIDTH + 1)];
		for(int x=0; x<Terrain.DEFAULT_WIDTH + 1; x++)
			for(int z=0; z<Terrain.DEFAULT_WIDTH + 1; z++){
				double geoCoordX = x-(Terrain.DEFAULT_WIDTH + 1)/2+location.x;
				double geoCoordZ = z-(Terrain.DEFAULT_WIDTH + 1)/2+location.z;
				double[] latlon = MercatorUtil.toGeoCoord(geoCoordX, geoCoordZ);
				latlon[0] += lonInitial;
				latlon[1] += latInitial;
				heightMap[x*(Terrain.DEFAULT_WIDTH+1)+z] = (float)getAltitude(latlon[0], latlon[1]);
			}
		return heightMap;
	}

	public HashMap<AffinityEnum, Float> getWorldAffinities(){
		return currentWorldAffinities;
	}
	
	/**
	 * Found at http://stackoverflow.com/questions/1995998/android-get-altitude-by-longitude-and-latitude
	 * Note alternative: http://www.earthtools.org/webservices.htm#height
	 * @return altitude in meters
	 */
	public static double getAltitude(Double longitude, Double latitude) {
		double result = Double.NaN;
		String html = HTMLUtil.getHTML("http://gisdata.usgs.gov/"
				+ "xmlwebservices2/elevation_service.asmx/"
				+ "getElevation?X_Value=" + String.valueOf(longitude)
				+ "&Y_Value=" + String.valueOf(latitude)
				+ "&Elevation_Units=METERS&Source_Layer=-1&Elevation_Only=true");
		String tagOpen = "<double>";
		String tagClose = "</double>";
		if (html.indexOf(tagOpen) != -1) {
			int start = html.indexOf(tagOpen) + tagOpen.length();
			int end = html.indexOf(tagClose);
			String value = html.substring(start, end);
			result = Double.parseDouble(value);
		}
		return result;
	}
}

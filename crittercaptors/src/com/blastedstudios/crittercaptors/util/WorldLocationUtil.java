package com.blastedstudios.crittercaptors.util;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
	private double lat = 36.878705, lon = -76.260400;
	public final double latInitial, lonInitial;
	private float timeSinceLastUpdate = TIME_TO_UPDATE;
	private static final float TIME_TO_UPDATE = 60;
	private HashMap<AffinityEnum, Float> currentWorldAffinities = new HashMap<AffinityEnum, Float>();
	private final ExecutorService executerService;
	
	public WorldLocationUtil(){
		latInitial = 36.878705;//Gdx.input.getGPSLatitude();
		lonInitial = -76.260400;//Gdx.input.getGPSLongitude();
		executerService = Executors.newCachedThreadPool();
	}

	public void update(){
		timeSinceLastUpdate += Gdx.graphics.getDeltaTime();
		if(timeSinceLastUpdate > TIME_TO_UPDATE){
			//TODO add back in updates
			//lat = Gdx.input.getGPSLatitude();
			//lon = Gdx.input.getGPSLongitude();
			timeSinceLastUpdate = 0;
			executerService.execute(new AffinitiesThread());
			try {
				executerService.awaitTermination(500, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e1) {
				currentWorldAffinities.clear();
				currentWorldAffinities.put(AffinityEnum.suburban, 1f);
			}
		}
	}
	
	public double getLatitude(){
		return lat;
	}

	public double getLongitude(){
		return lon;
	}

	public double getRelativeLongitude(){
		return lonInitial - lon;
	}

	public double getRelativeLatitude(){
		return latInitial - lat;
	}

	public float[] getHeightmap(Vector3 location){
		float[] heightMap = new float[(Terrain.DEFAULT_WIDTH + 1) * (Terrain.DEFAULT_WIDTH + 1)];
		for(int x=0; x<Terrain.DEFAULT_WIDTH + 1; x++)
			for(int z=0; z<Terrain.DEFAULT_WIDTH + 1; z++){
				double geoCoordX = x-(Terrain.DEFAULT_WIDTH + 1)/2+location.x;
				double geoCoordZ = z-(Terrain.DEFAULT_WIDTH + 1)/2+location.z;
				double[] latlon = MercatorUtil.toGeoCoord(geoCoordX, geoCoordZ);
				latlon[0] += lon;
				latlon[1] += lat;
				executerService.execute(new AltitudeThread(heightMap, x*(Terrain.DEFAULT_WIDTH+1)+z, latlon[0], latlon[1]));
			}
		try {
			Gdx.app.log("WorldLocationUtil:getHeightmap","Changed heightmap time to 5 seconds for debugging, make longer for release");
			//TODO change back to 60 or something else
			executerService.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return heightMap;
	}

	public HashMap<AffinityEnum, Float> getWorldAffinities(){
		return currentWorldAffinities;
	}
	
	/**
	 * Found at http://stackoverflow.com/questions/1995998/android-get-altitude-by-longitude-and-latitude
	 * Note alternative: http://www.earthtools.org/webservices.htm#height
	 * @return altitude in meters, clamped to -10 minimum
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
		return Math.max(-10, result);
	}

	private class AltitudeThread implements Runnable {
		private final float[] heightMap;
		private final int index;
		private final double lat, lon;
		
		public AltitudeThread(final float[] heightMap, final int index, 
				final double lon, final double lat){
			this.heightMap = heightMap;
			this.index = index;
			this.lat = lat;
			this.lon = lon;
		}
		
		public void run() {
			heightMap[index] = (float)getAltitude(lon, lat);
		}
	}

	private class AffinitiesThread implements Runnable {
		public void run() {
			String url = "http://ojw.dev.openstreetmap.org/StaticMap/?lat="+
				lat+"&lon="+lon+"&z=18&w=64&h=64&mode=Export&show=1";
			try {
				BufferedImage worldLocationLastImage = ImageIO.read(new URL(url));
				AffinityCalculator.getAffinitiesFromTexture(worldLocationLastImage, currentWorldAffinities);
			} catch (Exception e) {	}
		}
	}
}

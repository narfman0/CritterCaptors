package com.blastedstudios.crittercaptors.util;

import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.creature.AffinityCalculator;
import com.blastedstudios.crittercaptors.creature.AffinityEnum;
import com.blastedstudios.crittercaptors.ui.terrain.Terrain;
import com.blastedstudios.crittercaptors.util.OptionsUtil.OptionEnum;

/**
 * Keep world location up to date. Since floats are too inaccurate, using doubles
 * up to render, and setting initial lat so as to shrink the error when converting
 */
public class WorldLocationUtil {
	private LocationStruct loc = LocationStruct.DEFAULT.tmp(), lastLoc = LocationStruct.ZERO.tmp();
	public final LocationStruct initialLatLon;
	private float timeSinceLastUpdate = TIME_TO_UPDATE;
	private static final float TIME_TO_UPDATE = 60;
	private HashMap<AffinityEnum, Float> currentWorldAffinities = new HashMap<AffinityEnum, Float>();
	private final ExecutorService executerService;
	private CritterCaptors game;
	
	public WorldLocationUtil(CritterCaptors game){
		this.game = game;
		this.initialLatLon = getInitialLocation();
		executerService = Executors.newCachedThreadPool();
	}

	public void update(){
		timeSinceLastUpdate += Gdx.graphics.getDeltaTime();
		if(timeSinceLastUpdate > TIME_TO_UPDATE){
			if(game.getOptions().getOptionBoolean(OptionEnum.Gps))
				loc = new LocationStruct(Gdx.input.getGPSLatitude(),Gdx.input.getGPSLongitude());
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
	
	public LocationStruct getLocation(){
		return loc;
	}

	public LocationStruct getRelativeLatLon(){
		return initialLatLon.tmp().sub(loc);
	}

	public float[] getHeightmap(Vector3 location){
		float[] heightMap = new float[(Terrain.DEFAULT_WIDTH + 1) * (Terrain.DEFAULT_WIDTH + 1)];
		for(int x=0; x<Terrain.DEFAULT_WIDTH + 1; x++)
			for(int z=0; z<Terrain.DEFAULT_WIDTH + 1; z++){
				double geoCoordX = x-(Terrain.DEFAULT_WIDTH + 1)/2+location.x;
				double geoCoordZ = z-(Terrain.DEFAULT_WIDTH + 1)/2+location.z;
				LocationStruct latlon = MercatorUtil.toGeoCoord(geoCoordX, geoCoordZ).add(loc);
				executerService.execute(new AltitudeThread(heightMap, x*(Terrain.DEFAULT_WIDTH+1)+z, latlon.lon, latlon.lat));
			}
		try {
			executerService.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			Gdx.app.log("WorldLocationUtil:getHeightmap","Heightmap not complete after one minute, consider a better device");
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
		double result = -10;
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
			try {
				URL url = new URL("http://ojw.dev.openstreetmap.org/StaticMap/?lat="+
						loc.lat+"&lon="+loc.lon+"&z=18&w=64&h=64&mode=Export&show=1");
				Color[] colors = RenderUtil.imageFromURL(url);
				if(colors == null){
					//TODO below
					Gdx.app.log("Affinities Thread", "fix imageFromURL to load texs correctly."+
							"libgdx does not work 100% with their Pixmap class which"+
							"would work perfectly for this express reason");
					colors = new Color[]{AffinityCalculator.SUBURBAN_COLOR};
				}
				AffinityCalculator.getAffinitiesFromTexture(colors, currentWorldAffinities);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private LocationStruct getInitialLocation(){
		if(game.getOptions().getOptionBoolean(OptionEnum.Gps)){
			long gpsAcquireTimeout = System.currentTimeMillis();
			while(!isGPSAcquired() && gpsAcquireTimeout + 10*1000 > System.currentTimeMillis())
				try {
					Thread.sleep(1000);
					lastLoc.lat = Gdx.input.getGPSLatitude();
					lastLoc.lon = Gdx.input.getGPSLongitude();
				} catch (InterruptedException e) {}
			return new LocationStruct(Gdx.input.getGPSLatitude(),Gdx.input.getGPSLongitude());
		}else
			return LocationStruct.DEFAULT.tmp();
	}
	
	/**
	 * .01 is gps accuracy... it must not be changing more than that squared (pythag)
	 */
	private boolean isGPSAcquired(){
		if(!lastLoc.equals(LocationStruct.ZERO) && 
				Math.pow(Gdx.input.getGPSLatitude()-lastLoc.lat,2) + 
				Math.pow(Gdx.input.getGPSLongitude()-lastLoc.lon,2) < .01)
			return true;
		return false;
	}
}

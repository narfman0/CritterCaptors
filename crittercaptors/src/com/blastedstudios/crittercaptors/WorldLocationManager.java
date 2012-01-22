package com.blastedstudios.crittercaptors;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.crittercaptors.creature.AffinityCalculator;
import com.blastedstudios.crittercaptors.creature.AffinityEnum;

public class WorldLocationManager {
	private Vector2 worldLocation = new Vector2(36.878705f, -76.260400f);
	private BufferedImage worldLocationLastImage;
	private float timeSinceLastUpdate = TIME_TO_UPDATE;
	private static final float TIME_TO_UPDATE = 60;
	private HashMap<AffinityEnum, Float> currentWorldAffinities;

	public void update(){
		timeSinceLastUpdate += Gdx.graphics.getDeltaTime();
		if(timeSinceLastUpdate > TIME_TO_UPDATE){
			timeSinceLastUpdate = 0;
			try {
				worldLocationLastImage = ImageIO.read(
						new URL("http://ojw.dev.openstreetmap.org/StaticMap/?lat="+
						worldLocation.x+"&lon="+worldLocation.y+"&z=18&w=64&h=64&mode=Export&show=1"));
				currentWorldAffinities = AffinityCalculator.getAffinitiesFromTexture(worldLocationLastImage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Vector2 getLocation(){
		return worldLocation;
	}
	
	public HashMap<AffinityEnum, Float> getWorldAffinities(){
		return currentWorldAffinities;
	}
}

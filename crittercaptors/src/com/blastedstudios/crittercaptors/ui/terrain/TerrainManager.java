package com.blastedstudios.crittercaptors.ui.terrain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.ui.terrain.Terrain;
import com.blastedstudios.crittercaptors.util.WorldLocationUtil;

public class TerrainManager {
	private static final List<Terrain> cachedTerrains = new ArrayList<Terrain>();
	private final List<Terrain> activeTerrains = new ArrayList<Terrain>();
	private final WorldLocationUtil locationManager;
	
	public TerrainManager(WorldLocationUtil locationManager){
		this.locationManager = locationManager;
		add(new Vector3());
	}
	
	public void add(Vector3 location){
		boolean found = false;
		for(Terrain terrain : cachedTerrains)
			if(terrain.location.dst(location) < 10)
				found = true;
		if(!found)
			cachedTerrains.add(new Terrain(locationManager.getHeightmap(location), location, 5, 5));
	}

	public void render(Vector3 playerLocation) {
		//stupid terrain management, fix this sometime. works for now i guess
		activeTerrains.clear();
		for(Terrain terrain : cachedTerrains)
			if(terrain.location.x + Terrain.DEFAULT_WIDTH_TIM2*terrain.scaleX >= playerLocation.x &&
				terrain.location.x - Terrain.DEFAULT_WIDTH_TIM2*terrain.scaleX <= playerLocation.x &&
				terrain.location.z + Terrain.DEFAULT_WIDTH_TIM2*terrain.scaleZ >= playerLocation.z &&
				terrain.location.z - Terrain.DEFAULT_WIDTH_TIM2*terrain.scaleZ <= playerLocation.z )
				activeTerrains.add(terrain);
		
		for(Terrain terrain : activeTerrains)
			terrain.render();
	}
	
	/**
	 * @param x in game coordinates
	 * @param z in game coordinates
	 * @return altitude in meters
	 */
	public float getHeight(float x, float z){
		for(Terrain terrain : activeTerrains)
			if(terrain.location.x + Terrain.DEFAULT_WIDTH_DIV2*terrain.scaleX >= x &&
				terrain.location.x - Terrain.DEFAULT_WIDTH_DIV2*terrain.scaleX <= x &&
				terrain.location.z + Terrain.DEFAULT_WIDTH_DIV2*terrain.scaleZ >= z &&
				terrain.location.z - Terrain.DEFAULT_WIDTH_DIV2*terrain.scaleZ <= z )
				return terrain.getHeight(
						x-terrain.location.x+Terrain.DEFAULT_WIDTH_DIV2*terrain.scaleX, 
						z-terrain.location.z+Terrain.DEFAULT_WIDTH_DIV2*terrain.scaleZ);
		return 0;
	}
	
}

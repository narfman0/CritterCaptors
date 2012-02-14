package com.blastedstudios.crittercaptors.ui.terrain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.util.WorldLocationUtil;

public class TerrainManager {
	private static final List<ITerrain> terrains = new ArrayList<ITerrain>();
	private final WorldLocationUtil locationManager;
	
	public TerrainManager(WorldLocationUtil locationManager){
		this.locationManager = locationManager;
		add(new Vector3());
	}
	
	public void add(Vector3 location){
		boolean found = false;
		for(ITerrain terrain : terrains)
			if(terrain.getLocation().dst(location) < 10)
				found = true;
		if(!found)
			terrains.add(new Terrain(locationManager.getHeightmap(location), location, 5, 5));
	}

	public void render(Vector3 playerLocation) {
		for(ITerrain terrain : terrains)
			terrain.render();
	}
	
	/**
	 * @param x in game coordinates
	 * @param z in game coordinates
	 * @return altitude in meters
	 */
	public float getHeight(float x, float z){
		for(ITerrain terrain : terrains)
			if(terrain.getLocation().x + Terrain.DEFAULT_WIDTH_DIV2*terrain.getScaleX() >= x &&
				terrain.getLocation().x - Terrain.DEFAULT_WIDTH_DIV2*terrain.getScaleX() <= x &&
				terrain.getLocation().z + Terrain.DEFAULT_WIDTH_DIV2*terrain.getScaleZ() >= z &&
				terrain.getLocation().z - Terrain.DEFAULT_WIDTH_DIV2*terrain.getScaleZ() <= z )
				return terrain.getHeight(
						x-terrain.getLocation().x+Terrain.DEFAULT_WIDTH_DIV2*terrain.getScaleX(), 
						z-terrain.getLocation().z+Terrain.DEFAULT_WIDTH_DIV2*terrain.getScaleZ());
		return 0;
	}
	
}

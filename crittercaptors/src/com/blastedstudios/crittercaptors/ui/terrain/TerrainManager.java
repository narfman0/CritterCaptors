package com.blastedstudios.crittercaptors.ui.terrain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.util.WorldLocationUtil;

public class TerrainManager {
	private static final List<ITerrain> terrains = new ArrayList<ITerrain>();
	/**
	 * baseTerrain is the bottom layer of terrain to ensure the players never 
	 * see less black void of terrain-less worlds
	 */
	private final Terrain baseTerrain;
	private final WorldLocationUtil locationManager;
	private final int SCALE = 5;
	
	public TerrainManager(WorldLocationUtil locationManager){
		this.locationManager = locationManager;
		baseTerrain = new Terrain(
				new float[(Terrain.DEFAULT_WIDTH + 1) * (Terrain.DEFAULT_WIDTH + 1)], 
				new Vector3(), SCALE, SCALE);
		add(new Vector3());
	}
	
	public void add(Vector3 location){
		boolean found = false;
		for(ITerrain terrain : terrains)
			if(terrain.getLocation().dst(location) < 10)
				found = true;
		if(!found)
			terrains.add(new Terrain(locationManager.getHeightmap(location), location, SCALE, SCALE));
	}

	public void render(Vector3 playerLocation) {
		int stride = Terrain.DEFAULT_WIDTH*SCALE;
		for(int x=-stride; x<=stride; x+=stride)
			for(int z=-stride; z<=stride; z+=stride){
				baseTerrain.location.x = ((int)(playerLocation.x/5))*5f+x;
				baseTerrain.location.z = ((int)(playerLocation.z/5))*5f+z;
				baseTerrain.render();
			}
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

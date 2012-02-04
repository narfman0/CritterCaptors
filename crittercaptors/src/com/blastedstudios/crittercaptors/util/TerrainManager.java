package com.blastedstudios.crittercaptors.util;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.ui.terrain.Terrain;

public class TerrainManager {
	private final List<Terrain> terrains = new ArrayList<Terrain>();
	
	public TerrainManager(WorldLocationManager locationManager){
		terrains.add(new Terrain(new float[(Terrain.DEFAULT_WIDTH + 1) * (Terrain.DEFAULT_WIDTH + 1)], new Vector3()));
	}

	public void render(Vector3 playerLocation) {
		for(Terrain terrain : terrains)
			terrain.render();
	}
}

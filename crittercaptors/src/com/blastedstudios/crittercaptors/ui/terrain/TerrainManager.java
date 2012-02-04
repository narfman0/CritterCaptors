package com.blastedstudios.crittercaptors.ui.terrain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.ui.terrain.Terrain;
import com.blastedstudios.crittercaptors.util.WorldLocationManager;

public class TerrainManager {
	private final List<Terrain> terrains = new ArrayList<Terrain>();
	
	public TerrainManager(WorldLocationManager locationManager){
		terrains.add(new Terrain(locationManager.getHeightmap(new Vector3()), new Vector3()));
	}

	public void render(Vector3 playerLocation) {
		for(Terrain terrain : terrains)
			terrain.render();
	}
}

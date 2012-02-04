package com.blastedstudios.crittercaptors.ui.terrain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.ui.terrain.Terrain;
import com.blastedstudios.crittercaptors.util.WorldLocationUtil;

public class TerrainManager {
	private static final List<Terrain> terrains = new ArrayList<Terrain>();
	
	public TerrainManager(WorldLocationUtil locationManager){
		boolean found = false;
		for(Terrain terrain : terrains)
			if(terrain.location.dst(new Vector3()) < 10)
				found = true;
		if(!found)
			terrains.add(new Terrain(locationManager.getHeightmap(new Vector3()), new Vector3()));
	}

	public void render(Vector3 playerLocation) {
		for(Terrain terrain : terrains)
			terrain.render();
	}
}

package com.blastedstudios.crittercaptors.ui.terrain;

import com.badlogic.gdx.math.Vector3;

public interface ITerrain {
	public static final int DEFAULT_WIDTH = 16,
			DEFAULT_WIDTH_DIV2 = DEFAULT_WIDTH/2;
	
	public void render ();
	public float getHeight(float x, float z);
	public Vector3 getLocation();
}

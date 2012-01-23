/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.blastedstudios.crittercaptors;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderRegistry;
import com.badlogic.gdx.graphics.g3d.model.Model;
import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.creature.Creature;
import com.blastedstudios.crittercaptors.creature.CreatureManager;
import com.blastedstudios.crittercaptors.ui.UIManager;

public class CritterCaptors implements ApplicationListener {
	public static Random random = new Random();
	public static final float MOVE_SPEED = 10f, TURN_RATE = 100f;
    private Camera camera;
    private Texture skyTexture;
    private final String skyTexturePath = "data/sky/skydome.png";
	private SpriteBatch spriteBatch;
	private BitmapFont font;
	private HashMap<String,Model> modelMap;
	private CreatureManager creatureManager;
	private WorldLocationManager worldLocationManager;
	private UIManager uiManager;

	@Override
	public void create () {
        if (skyTexturePath != null) 
        	skyTexture = new Texture(Gdx.files.internal(skyTexturePath), Format.RGB565, true);
        
		spriteBatch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.getFileHandle("data/fonts/arial-15.fnt", FileType.Internal), 
				Gdx.files.getFileHandle("data/fonts/arial-15.png", FileType.Internal), false);

		creatureManager = new CreatureManager();
		worldLocationManager = new WorldLocationManager();
		
		modelMap = new HashMap<String, Model>();
		modelMap.put("skydome", ModelLoaderRegistry.load(Gdx.files.internal("data/sky/skydome.obj")));
		for(String name : creatureManager.getCreatureTemplateNames())
			modelMap.put(name, ModelLoaderRegistry.load(Gdx.files.internal("data/models/static/" + name.toLowerCase() + ".obj")));
		uiManager = new UIManager();
	}

	@Override
	public void render () {
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		if(uiManager.isActive()){
			uiManager.render();
			return;
		}
		
		update();
		Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
		
		drawSky();
		Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);
		Gdx.gl10.glFrontFace(GL10.GL_CW);
		
		for(Creature creature : creatureManager.getCreatures()){
			Gdx.gl10.glPushMatrix();
			Gdx.gl10.glTranslatef(creature.camera.position.x, creature.camera.position.y, creature.camera.position.z);
			Gdx.gl10.glRotatef((float)Math.toDegrees(Math.atan2(creature.camera.direction.x, creature.camera.direction.z)), 0, 1, 0);
			Gdx.gl10.glScalef(100f, 100f, 100f);
			modelMap.get(creature.getName()).render();
			Gdx.gl10.glPopMatrix();
		}
		
		Gdx.gl.glDisable(GL10.GL_TEXTURE_2D);
		spriteBatch.begin();
		font.drawMultiLine(spriteBatch, "acc x=" + Gdx.input.getAccelerometerX() + 
				"\nacc y=" + Gdx.input.getAccelerometerY() + 
				"\nacc z=" + Gdx.input.getAccelerometerZ() +
				"\nazimuth=" + Gdx.input.getAzimuth() + 
				"\npitch=" + Gdx.input.getPitch() + 
				"\nroll=" + Gdx.input.getRoll(), 4, 256);
		font.drawMultiLine(spriteBatch, /*"gpsIsAvailable=" + Gdx.input.isPeripheralAvailable(Peripheral.GPS) +
				"\nlat=" + Gdx.input.getGPSLatitude() + 
				"\nlon=" + Gdx.input.getGPSLongitude() + 
				"\nalt=" + Gdx.input.getGPSAltitude() +*/
				"\nnumCreatures=" + creatureManager.getCreatures().size() +
				"\ncurrentLocation=" + camera.position.x + "," + camera.position.z + 
				"\ndeltax=" + Gdx.input.getDeltaX() + "\ndeltay=" + Gdx.input.getDeltaY() +
				"\nx=" + Gdx.input.getX() + "\ny=" + Gdx.input.getY(), 164, 256);
		spriteBatch.end();
	}
	
	private void update(){
		worldLocationManager.update();
		creatureManager.update(worldLocationManager.getWorldAffinities(), camera.position);
		processInput();
	}
	
	private void drawSky(){
		Gdx.gl10.glDisable(GL10.GL_DEPTH_TEST);
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glTranslatef(camera.position.x, camera.position.y, camera.position.z);
		Gdx.gl10.glScalef(10f, 10f, 10f);
		skyTexture.bind();
		modelMap.get("skydome").render();
		Gdx.gl10.glPopMatrix();
	}

	private void processInput () {
		Vector3 movement = new Vector3();
		if (Gdx.input.isKeyPressed(Keys.W))
			movement.add(camera.direction.tmp().mul(Gdx.graphics.getDeltaTime()));
		if (Gdx.input.isKeyPressed(Keys.S))
			movement.add(camera.direction.tmp().mul(-Gdx.graphics.getDeltaTime()));
		if (Gdx.input.isKeyPressed(Keys.A))
			camera.rotate(TURN_RATE * Gdx.graphics.getDeltaTime(), 0, 1, 0);
		if (Gdx.input.isKeyPressed(Keys.D))
			camera.rotate(-TURN_RATE * Gdx.graphics.getDeltaTime(), 0, 1, 0);
		camera.position.add(movement.mul(MOVE_SPEED));
        camera.update();
        camera.apply(Gdx.gl10);
	}

	@Override
	public void resize (int width, int height) {
        float aspectRatio = (float) width / (float) height;
        camera = new PerspectiveCamera(67, 2f * aspectRatio, 2f);
        camera.translate(0, 1, 6);
        camera.direction.set(0, 0, -1).nor();
        camera.update();
        camera.apply(Gdx.gl10);
	}

	@Override public void pause () {}
	@Override public void resume () {}
	@Override public void dispose () {}
}

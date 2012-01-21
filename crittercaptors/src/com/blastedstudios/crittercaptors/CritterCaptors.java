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

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderRegistry;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedAnimation;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedModel;
import com.badlogic.gdx.math.Vector2;

public class CritterCaptors implements ApplicationListener {
    private Camera camera;
    private KeyframedModel model;
    private KeyframedAnimation anim;
    private Texture texture = null;
    private final String knightTexturePath = "data/models/knight/knight.jpg",
    					knightModelPath = "data/models/knight/knight.g3d";
	private float animTime = 0;
	private SpriteBatch spriteBatch;
	private BitmapFont font;

	@Override
	public void create () {
		model = ModelLoaderRegistry.loadKeyframedModel(Gdx.files.internal(knightModelPath));
        if (knightTexturePath != null) 
        	texture = new Texture(Gdx.files.internal(knightTexturePath), Format.RGB565, true);
		anim = model.getAnimations()[0];
		spriteBatch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.getFileHandle("data/arial-15.fnt", FileType.Internal), Gdx.files.getFileHandle(
				"data/arial-15.png", FileType.Internal), false);
	}

	@Override
	public void render () {
		animTime += Gdx.graphics.getDeltaTime();
		if (animTime >= anim.totalDuration)
			animTime = 0;
		
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);

		if (texture != null) {
			Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
			texture.bind();
		}
		Gdx.gl10.glFrontFace(GL10.GL_CW);
		
		model.setAnimation(anim.name, animTime, false);
		
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glTranslatef(0, 0, 0);
		model.render();
		Gdx.gl10.glPopMatrix();
		
		if (texture != null) {
			Gdx.gl.glDisable(GL10.GL_TEXTURE_2D);
		}
		
		spriteBatch.begin();
		font.drawMultiLine(spriteBatch, "acc x=" + Gdx.input.getAccelerometerX() + 
				"\nacc y=" + Gdx.input.getAccelerometerY() + 
				"\nacc z=" + Gdx.input.getAccelerometerZ() +
				"\nazimuth=" + Gdx.input.getAzimuth() + 
				"\npitch=" + Gdx.input.getPitch() + 
				"\nroll=" + Gdx.input.getRoll(), 4, 256);
		/*font.drawMultiLine(spriteBatch, "gpsIsAvailable=" + Gdx.input.isPeripheralAvailable(Peripheral.GPS) +
				"\nlat=" + Gdx.input.getGPSLatitude() + 
				"\nlon=" + Gdx.input.getGPSLongitude() + 
				"\nalt=" + Gdx.input.getGPSAltitude() +
				"\ndeltax=" + Gdx.input.getDeltaX() + "\ndeltay=" + Gdx.input.getDeltaY() +
				"\nx=" + Gdx.input.getX() + "\ny=" + Gdx.input.getY(), 164, 256);*/
		spriteBatch.end();
	}

	@Override
	public void resize (int width, int height) {
        float aspectRatio = (float) width / (float) height;
        camera = new PerspectiveCamera(67, 2f * aspectRatio, 2f);
        camera.translate(0, 30, 40);
        camera.direction.set(0, -.4f, -1).nor();
        camera.update();
        camera.apply(Gdx.gl10);
	}

	@Override public void pause () {}
	@Override public void resume () {}
	@Override public void dispose () {}
}

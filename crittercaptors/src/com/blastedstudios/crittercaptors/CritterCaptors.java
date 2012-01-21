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

import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

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
import com.badlogic.gdx.graphics.g3d.model.Model;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedAnimation;
import com.badlogic.gdx.graphics.g3d.model.keyframe.KeyframedModel;
import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.crittercaptors.creature.AffinityCalculator;
import com.blastedstudios.crittercaptors.creature.AffinityEnum;

public class CritterCaptors implements ApplicationListener {
    private Camera camera;
    private KeyframedModel kinghtModel;
    private KeyframedAnimation knightAnim;
    private Texture knightTexture = null;
    private final String knightTexturePath = "data/models/knight/knight.jpg",
    					knightModelPath = "data/models/knight/knight.g3d";
	private float animTime = 0;
	private SpriteBatch spriteBatch;
	private BitmapFont font;
	private HashMap<String,Model> modelMap;

	@Override
	public void create () {
		modelMap = new HashMap<String, Model>();
		kinghtModel = ModelLoaderRegistry.loadKeyframedModel(Gdx.files.internal(knightModelPath));
        if (knightTexturePath != null) 
        	knightTexture = new Texture(Gdx.files.internal(knightTexturePath), Format.RGB565, true);
		knightAnim = kinghtModel.getAnimations()[0];
		spriteBatch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.getFileHandle("data/arial-15.fnt", FileType.Internal), 
				Gdx.files.getFileHandle("data/arial-15.png", FileType.Internal), false);

		try {
			Vector2 location = new Vector2(36.878705f, -76.260400f);
			URL url = new URL("http://ojw.dev.openstreetmap.org/StaticMap/?lat="+
					location.x+"&lon="+location.y+"&z=18&w=64&h=64&mode=Export&show=1");
			BufferedImage bi = ImageIO.read(url);
			HashMap<AffinityEnum, Float> sububrna = AffinityCalculator.getAffinitiesFromTexture(bi);
		} catch (IOException e) {
			fail();
		}
	}

	@Override
	public void render () {
		animTime += Gdx.graphics.getDeltaTime();
		if (animTime >= knightAnim.totalDuration)
			animTime = 0;
		
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);

		if (knightTexture != null) {
			Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
			knightTexture.bind();
		}
		Gdx.gl10.glFrontFace(GL10.GL_CW);
		
		kinghtModel.setAnimation(knightAnim.name, animTime, false);
		
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glTranslatef(0, 0, 0);
		kinghtModel.render();
		Gdx.gl10.glPopMatrix();
		
		if (knightTexture != null) {
			Gdx.gl.glDisable(GL10.GL_TEXTURE_2D);
		}
		
		/*spriteBatch.begin();
		font.drawMultiLine(spriteBatch, "acc x=" + Gdx.input.getAccelerometerX() + 
				"\nacc y=" + Gdx.input.getAccelerometerY() + 
				"\nacc z=" + Gdx.input.getAccelerometerZ() +
				"\nazimuth=" + Gdx.input.getAzimuth() + 
				"\npitch=" + Gdx.input.getPitch() + 
				"\nroll=" + Gdx.input.getRoll(), 4, 256);
		font.drawMultiLine(spriteBatch, "gpsIsAvailable=" + Gdx.input.isPeripheralAvailable(Peripheral.GPS) +
				"\nlat=" + Gdx.input.getGPSLatitude() + 
				"\nlon=" + Gdx.input.getGPSLongitude() + 
				"\nalt=" + Gdx.input.getGPSAltitude() +
				"\ndeltax=" + Gdx.input.getDeltaX() + "\ndeltay=" + Gdx.input.getDeltaY() +
				"\nx=" + Gdx.input.getX() + "\ny=" + Gdx.input.getY(), 164, 256);
		spriteBatch.end();*/
	}

	@Override
	public void resize (int width, int height) {
        float aspectRatio = (float) width / (float) height;
        camera = new PerspectiveCamera(67, 2f * aspectRatio, 2f);
        camera.translate(0, 30, 60);
        camera.direction.set(0, -.4f, -1).nor();
        camera.update();
        camera.apply(Gdx.gl10);
	}

	@Override public void pause () {}
	@Override public void resume () {}
	@Override public void dispose () {}
}

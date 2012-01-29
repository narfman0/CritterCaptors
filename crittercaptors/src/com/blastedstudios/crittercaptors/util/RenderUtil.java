package com.blastedstudios.crittercaptors.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.model.Model;
import com.badlogic.gdx.math.Vector3;

public class RenderUtil {
	public static void drawModel(Model model, Vector3 pos,
			Vector3 direction, Vector3 scale){
		Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);
		Gdx.gl10.glFrontFace(GL10.GL_CW);
		Gdx.gl10.glDisable(GL10.GL_TEXTURE_2D);
		Gdx.gl10.glEnable(GL10.GL_COLOR_MATERIAL);
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glTranslatef(pos.x, pos.y, pos.z);
		Gdx.gl10.glRotatef((float)Math.toDegrees(Math.atan2(direction.x, direction.z)), 0, 1, 0);
		Gdx.gl10.glScalef(scale.x, scale.y, scale.z);
		model.render();
		Gdx.gl10.glPopMatrix();
	}
	
	public static Camera resize(int width, int height){
        float aspectRatio = (float) width / (float) height;
        Camera camera = new PerspectiveCamera(67, 2f * aspectRatio, 2f);
        camera.translate(0, 1, 0);
        camera.direction.set(0, 0, 1);
        camera.update();
        camera.apply(Gdx.gl10);
        return camera;
	}

	public static void drawSky(Model model, Texture skyTexture, Vector3 cameraPosition){
		Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
		Gdx.gl10.glDisable(GL10.GL_DEPTH_TEST);
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glTranslatef(cameraPosition.x, cameraPosition.y, cameraPosition.z);
		Gdx.gl10.glScalef(10f, 10f, 10f);
		skyTexture.bind();
		model.render();
		Gdx.gl10.glPopMatrix();
	}
}

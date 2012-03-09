package com.blastedstudios.crittercaptors.util;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.model.Model;
import com.badlogic.gdx.math.Vector3;

public class RenderUtil {
	public static void drawModel(Model model, Texture texture, Vector3 pos,
			Vector3 direction, Vector3 scale){
		Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);
		Gdx.gl10.glFrontFace(GL10.GL_CW);
		if(texture != null){
			Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
			texture.bind();
		}
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
	
	public static BufferedImage toBufferedImage(Image image) {
	    if (image instanceof BufferedImage) {
	        return (BufferedImage)image;
	    }

	    // This code ensures that all the pixels in the image are loaded
	    image = new ImageIcon(image).getImage();

	    // Create a buffered image with a format that's compatible with the screen
	    BufferedImage bimage = null;
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    try {
	        // Determine the type of transparency of the new buffered image
	        int transparency = Transparency.OPAQUE;

	        // Create the buffered image
	        GraphicsDevice gs = ge.getDefaultScreenDevice();
	        GraphicsConfiguration gc = gs.getDefaultConfiguration();
	        bimage = gc.createCompatibleImage(
	            image.getWidth(null), image.getHeight(null), transparency);
	    } catch (HeadlessException e) {
	        // The system does not have a screen
	    }

	    if (bimage == null) {
	        // Create a buffered image using the default color model
	        int type = BufferedImage.TYPE_INT_RGB;
	        bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
	    }

	    // Copy image to buffered image
	    Graphics g = bimage.createGraphics();

	    // Paint the image onto the buffered image
	    g.drawImage(image, 0, 0, null);
	    g.dispose();

	    return bimage;
	}
}

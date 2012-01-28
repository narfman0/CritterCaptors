package com.blastedstudios.crittercaptors.ui.worldmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.creature.Creature;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;
import com.blastedstudios.crittercaptors.ui.Terrain;
import com.blastedstudios.crittercaptors.ui.battle.BattleScreen;

public class WorldMap extends AbstractScreen {
    private Texture skyTexture;
    private final String skyTexturePath = "data/sky/skydome.png";
	private SpriteBatch spriteBatch;
	private BitmapFont font;
    private Camera camera;
    public static final float MOVE_SPEED = 10f, TURN_RATE = 100f,
		REMOVE_DISTANCE = 1000000f, FIGHT_DISTANCE = 40f;
    private SideMenu sideMenu = null;
    private Terrain terrain;
	
	public WorldMap(CritterCaptors game) {
		super(game);
        if (skyTexturePath != null) 
        	skyTexture = new Texture(Gdx.files.internal(skyTexturePath), Format.RGB565, true);
        
		spriteBatch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.getFileHandle("data/fonts/arial-15.fnt", FileType.Internal), 
				Gdx.files.getFileHandle("data/fonts/arial-15.png", FileType.Internal), false);
		terrain = new Terrain();
	}
	
	@Override public void render (float arg0) {
		game.getWorldLocationManager().update();
		game.getCreatureManager().update(game.getWorldLocationManager().getWorldAffinities(), camera.position);
		for(int i=0; i<game.getCreatureManager().getCreatures().size(); i++){
			float distance = game.getCreatureManager().getCreatures().get(i).camera.position.dst2(camera.position);
			if(distance < FIGHT_DISTANCE){
				game.setScreen(new BattleScreen(game, game.getCreatureManager().getCreatures().get(i)));
				dispose();
			}if(distance > REMOVE_DISTANCE)
				game.getCreatureManager().getCreatures().remove(i--);
		}
		
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		processInput();
		drawSky();
		Gdx.gl10.glEnable(GL10.GL_DEPTH_TEST);
		Gdx.gl10.glFrontFace(GL10.GL_CW);
		Gdx.gl10.glDisable(GL10.GL_TEXTURE_2D);
		Gdx.gl10.glEnable(GL10.GL_COLOR_MATERIAL);
		for(Creature creature : game.getCreatureManager().getCreatures()){
			Gdx.gl10.glPushMatrix();
			Gdx.gl10.glTranslatef(creature.camera.position.x, creature.camera.position.y, creature.camera.position.z);
			Gdx.gl10.glRotatef((float)Math.toDegrees(Math.atan2(creature.camera.direction.x, creature.camera.direction.z)), 0, 1, 0);
			Gdx.gl10.glScalef(100f, 100f, 100f);
			game.getModel(creature.getName()).render();
			Gdx.gl10.glPopMatrix();
		}
		terrain.render();
		
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
				"\nnumCharacterCreatures=" + game.getCharacter().getOwnedCreatures().size() +
				"\nnumCreatures=" + game.getCreatureManager().getCreatures().size() +
				"\ncurrentLocation=" + camera.position.x + "," + camera.position.z + 
				"\ndeltax=" + Gdx.input.getDeltaX() + "\ndeltay=" + Gdx.input.getDeltaY() +
				"\nx=" + Gdx.input.getX() + "\ny=" + Gdx.input.getY(), 164, 256);
		spriteBatch.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
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
		if (Gdx.input.isKeyPressed(Keys.ESCAPE))
			if(sideMenu == null || sideMenu.dispose)
				stage.addActor(sideMenu = new SideMenu(game, skin));
		camera.position.add(movement.mul(MOVE_SPEED));
        camera.update();
        camera.apply(Gdx.gl10);
	}

	@Override
	public void resize (int width, int height) {
        float aspectRatio = (float) width / (float) height;
        camera = new PerspectiveCamera(67, 2f * aspectRatio, 2f);
        camera.translate(0, 1, 0);
        camera.direction.set(0, 0, -1).nor();
        camera.update();
        camera.apply(Gdx.gl10);
	}

	private void drawSky(){
		Gdx.gl10.glEnable(GL10.GL_TEXTURE_2D);
		Gdx.gl10.glDisable(GL10.GL_DEPTH_TEST);
		Gdx.gl10.glPushMatrix();
		Gdx.gl10.glTranslatef(camera.position.x, camera.position.y, camera.position.z);
		Gdx.gl10.glScalef(10f, 10f, 10f);
		skyTexture.bind();
		game.getModel("skydome").render();
		Gdx.gl10.glPopMatrix();
	}
}

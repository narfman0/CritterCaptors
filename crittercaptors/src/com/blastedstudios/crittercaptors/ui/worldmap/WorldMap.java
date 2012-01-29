package com.blastedstudios.crittercaptors.ui.worldmap;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.creature.Creature;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;
import com.blastedstudios.crittercaptors.ui.Terrain;
import com.blastedstudios.crittercaptors.ui.battle.BattleScreen;
import com.blastedstudios.crittercaptors.util.RenderUtil;

public class WorldMap extends AbstractScreen {
	private SpriteBatch spriteBatch;
	private BitmapFont font;
    private Camera camera;
    public static final float MOVE_SPEED = 10f, TURN_RATE = 100f,
		REMOVE_DISTANCE = 1000000f, FIGHT_DISTANCE = 40f;
    private SideMenu sideMenu = null;
    private Terrain terrain;
	
	public WorldMap(CritterCaptors game) {
		super(game);
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
				game.getCreatureManager().getCreatures().clear();
				dispose();
			}if(distance > REMOVE_DISTANCE)
				game.getCreatureManager().getCreatures().remove(i--);
		}
		
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		processInput();
		RenderUtil.drawSky(game.getModel("skydome"), game.getTexture("skydome"), camera.position);
		for(Creature creature : game.getCreatureManager().getCreatures())
			RenderUtil.drawModel(game.getModel(creature.getName()), creature.camera.position, 
					creature.camera.direction, new Vector3(100f,100f,100f));
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

	@Override public void resize (int width, int height) {
        camera = RenderUtil.resize(width, height);
	}
}

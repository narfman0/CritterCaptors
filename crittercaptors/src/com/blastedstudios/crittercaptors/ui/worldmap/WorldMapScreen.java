package com.blastedstudios.crittercaptors.ui.worldmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.character.Base;
import com.blastedstudios.crittercaptors.creature.Creature;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;
import com.blastedstudios.crittercaptors.ui.battle.BattleScreen;
import com.blastedstudios.crittercaptors.ui.terrain.TerrainManager;
import com.blastedstudios.crittercaptors.util.MercatorUtil;
import com.blastedstudios.crittercaptors.util.OptionsUtil.OptionEnum;
import com.blastedstudios.crittercaptors.util.RenderUtil;

public class WorldMapScreen extends AbstractScreen {
    private Camera camera;
    public static final float MOVE_SPEED = 10f, TURN_RATE = 100f,
		REMOVE_DISTANCE = 1000000f, FIGHT_DISTANCE = 150f, 
		ACCELEROMETER_THRESHOLD = (float) (Math.PI/6), SMOOTHING_FACTOR = .85f;
    private TerrainManager terrainManager;
    private TextField debug;
    
    public WorldMapScreen(CritterCaptors game) {
    	this(game, false);
    }
	
	public WorldMapScreen(CritterCaptors game, boolean isNewCharacter) {
		super(game);
		terrainManager = new TerrainManager(game);
		if(isNewCharacter)
			stage.addActor(new NewCharacterWindow(skin));
		stage.addActor(new SideWindow(game, skin, this));
		if(game.getOptions().getOptionBoolean(OptionEnum.Debug)){
			Window debugWindow = new Window(skin);
			debugWindow.width = 400;
			debugWindow.height = 50;
			debug = new TextField("debug text hurrah", skin);
			debug.width = 400;
			debugWindow.addActor(debug);
			stage.addActor(debugWindow);
		}
	}
	
	@Override public void render (float arg0) {
		game.getWorldLocationManager().update();
		game.getCreatureManager().update(game.getWorldLocationManager().getWorldAffinities(), camera.position, game);
		for(int i=0; i<game.getCreatureManager().getCreatures().size(); i++){
			Creature creature = game.getCreatureManager().getCreatures().get(i);
			float distance = creature.camera.position.dst2(camera.position);
			if(distance < FIGHT_DISTANCE){
				game.setScreen(new BattleScreen(game, creature));
				game.getCreatureManager().getCreatures().clear();
				dispose();
			}if(distance > REMOVE_DISTANCE)
				game.getCreatureManager().getCreatures().remove(i--);
		}
		
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		processInput();
		RenderUtil.drawSky(CritterCaptors.getModel("skydome"), CritterCaptors.getTexture("skydome"), camera.position);
		terrainManager.render(camera.position);
		for(Creature creature : game.getCreatureManager().getCreatures()){
			Vector3 position = creature.camera.position.tmp();
			position.y += terrainManager.getHeight(position.x, position.z);
			Texture texture = CritterCaptors.getTexture(creature.getName());
			RenderUtil.drawModel(CritterCaptors.getModel(creature.getName()), texture, 
					position, creature.camera.direction);
		}
		
		//render base after terrain to cache location (need terrain to get height of base)
		for(Base base : game.getCharacter().getBases()){
			if(base.getCachedPosition() == null){
				double[] mercator = MercatorUtil.toPixel(base.loc.tmp().sub(game.getWorldLocationManager().initialLatLon));
				float x = (float)mercator[0], z = (float)mercator[1], y = terrainManager.getHeight(x, z);
				base.setCachedPosition(new Vector3(x, y, z));
			}
			base.render();
		}
		
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
		if(game.getOptions().getOptionBoolean(OptionEnum.Accelerometer) && 
				Math.abs(Gdx.input.getAccelerometerZ()) > ACCELEROMETER_THRESHOLD )
			movement.add(camera.direction.tmp().mul(Gdx.graphics.getDeltaTime()*(Gdx.input.getAccelerometerZ()-ACCELEROMETER_THRESHOLD)));
		if(Gdx.input.isTouched() && game.getOptions().getOptionBoolean(OptionEnum.TouchMovement)){
			if(Gdx.input.getX() != 0){
				float degree = (Gdx.input.getX() - Gdx.graphics.getWidth()/2f) / (Gdx.graphics.getWidth()/-2f);
				camera.rotate(TURN_RATE * Gdx.graphics.getDeltaTime() * degree, 0, 1, 0);
			} if(Gdx.input.getY() != 0){
				float degree = (Gdx.input.getY() - Gdx.graphics.getHeight()/2f) / (Gdx.graphics.getHeight()/2f);
				degree *= game.getOptions().getOptionBoolean(OptionEnum.MovementInvert) ? -1 : 1;
				movement.add(camera.direction.tmp().mul(-Gdx.graphics.getDeltaTime()*degree));
			}
		}
		if(game.getOptions().getOptionBoolean(OptionEnum.Debug))
			debug.setText("Az=" + (int)Gdx.input.getAzimuth() + " loc x=" + (int)camera.position.x + " y=" + (int)camera.position.y + " z=" + (int)camera.position.z + 
					" dir x=" + camera.direction.x + " y=" + camera.direction.y + " z=" + camera.direction.z);
		if(game.getOptions().getOptionBoolean(OptionEnum.Gps)){
			Vector3 compassDirection = new Vector3((float)(Math.cos(Math.toRadians(Gdx.input.getAzimuth()))), camera.direction.y, (float)(Math.sin(Math.toRadians(Gdx.input.getAzimuth()))));
			camera.direction.mul(SMOOTHING_FACTOR);
			camera.direction.add(compassDirection.mul(1-SMOOTHING_FACTOR));
			camera.direction.nor();
			Gdx.app.debug("Compass direction", "bearing=" + Gdx.input.getGPSBearing());
			
			double[] coords = MercatorUtil.toPixel(game.getWorldLocationManager().getRelativeLatLon());
			Vector3 gpsPosition = new Vector3((float) coords[0], camera.position.y, (float) coords[1]); 
			camera.position.mul(SMOOTHING_FACTOR);
			camera.position.add(gpsPosition.mul(1-SMOOTHING_FACTOR));
			Gdx.app.debug("GPS movement", "Azimuth=" + Gdx.input.getAzimuth() + " cam loc is x=" + camera.position.x + " y=" + camera.position.y + " z=" + camera.position.z);
		}else
			camera.position.add(movement.mul(MOVE_SPEED));
		camera.position.y = terrainManager.getHeight(camera.position.x, camera.position.z)+1.9f;
        camera.update();
        camera.apply(Gdx.gl10);
	}

	@Override public void resize (int width, int height) {
        camera = RenderUtil.resize(width, height);
        double[] mercator = MercatorUtil.toPixel(game.getWorldLocationManager().getRelativeLatLon());
        camera.translate((float)mercator[0], 0, (float)mercator[1]);
        camera.update();
	}
	
	public Camera getCamera(){
		return camera;
	}
}

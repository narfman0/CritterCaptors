package com.blastedstudios.crittercaptors.ui.worldmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.character.Base;
import com.blastedstudios.crittercaptors.creature.Creature;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;
import com.blastedstudios.crittercaptors.ui.battle.BattleScreen;
import com.blastedstudios.crittercaptors.ui.terrain.TerrainManager;
import com.blastedstudios.crittercaptors.util.MercatorUtil;
import com.blastedstudios.crittercaptors.util.RenderUtil;
import com.blastedstudios.crittercaptors.util.OptionsUtil.OptionEnum;

public class WorldMapScreen extends AbstractScreen {
    private Camera camera;
    public static final float MOVE_SPEED = 10f, TURN_RATE = 100f,
		REMOVE_DISTANCE = 1000000f, FIGHT_DISTANCE = 150f, 
		ACCELEROMETER_THRESHOLD = (float) (Math.PI/6);
    private SideWindow sideMenu = null;
    private TerrainManager terrainManager;
    
    public WorldMapScreen(CritterCaptors game) {
    	this(game, false);
    }
	
	public WorldMapScreen(CritterCaptors game, boolean isNewCharacter) {
		super(game);
		terrainManager = new TerrainManager(game);
		if(isNewCharacter)
			showNewCharacterWindow();
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
					position, creature.camera.direction, new Vector3(1f,1f,1f));
		}
		
		//render base after terrain to cache location (need terrain to get height of base)
		for(Base base : game.getCharacter().getBases())
			base.render(terrainManager, game.getWorldLocationManager());
		
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
				stage.addActor(sideMenu = new SideWindow(game, skin));
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
		camera.position.add(movement.mul(MOVE_SPEED));
		camera.position.y = terrainManager.getHeight(camera.position.x, camera.position.z)+1.9f;
        camera.update();
        camera.apply(Gdx.gl10);
	}

	@Override public void resize (int width, int height) {
        camera = RenderUtil.resize(width, height);
        double[] mercator = MercatorUtil.toPixel(
        		game.getWorldLocationManager().getRelativeLongitude(),
        		game.getWorldLocationManager().getRelativeLatitude());
        camera.translate((float)mercator[0], 0, (float)mercator[1]);
        camera.update();
	}
	
	private void showNewCharacterWindow(){
		final Window window = new Window("Welcome!", skin);
		final Button button = new TextButton("Ok", skin.getStyle(TextButtonStyle.class), "ok");
		button.setClickListener(new ClickListener() {
			@Override public void click(Actor arg0, float arg1, float arg2) {
				stage.removeActor(window);
			}
		});
		window.add(new Label("You should build a base\nimmediately to take care"+
			"\nof creature management\nand provide a place for\nyour character to rest.\n\n" +
			"Press <esc> and choose\n\"Base\" from the menu", skin));
		window.row();
		window.add(button);
		window.pack();
		window.x = Gdx.graphics.getWidth() / 2 - window.width / 2;
		window.y = Gdx.graphics.getHeight() / 2 - window.height / 2;
		stage.addActor(window);
	}
}

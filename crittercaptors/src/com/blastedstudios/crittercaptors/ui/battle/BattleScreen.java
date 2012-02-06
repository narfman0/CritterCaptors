package com.blastedstudios.crittercaptors.ui.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.util.ExperienceUtil;
import com.blastedstudios.crittercaptors.creature.Creature;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;
import com.blastedstudios.crittercaptors.ui.terrain.Terrain;
import com.blastedstudios.crittercaptors.ui.worldmap.WorldMapScreen;
import com.blastedstudios.crittercaptors.util.RenderUtil;

public class BattleScreen extends AbstractScreen {
    private static final Terrain terrain;
	private Creature enemy, activeCreature;
    private Camera camera;
    private CreatureInfoWindow creatureInfoWindow, enemyInfoWindow;
    static{
    	float[] heightMap = new float[(Terrain.DEFAULT_WIDTH + 1) * (Terrain.DEFAULT_WIDTH + 1)];
		for(int x=0; x<Terrain.DEFAULT_WIDTH; x++){
			heightMap[Terrain.DEFAULT_WIDTH*Terrain.DEFAULT_WIDTH + x] = 5f + (float)Math.sin(x/4);
		}
		terrain = new Terrain(heightMap, new Vector3(-Terrain.DEFAULT_WIDTH/2,0,-Terrain.DEFAULT_WIDTH/2));
    }

	public BattleScreen(CritterCaptors game, Creature enemy) {
		super(game);
		this.enemy = enemy;
		activeCreature = game.getCharacter().getNextActiveCreature();
		if(activeCreature == null)
			game.setScreen(new BlackoutScreen(game));
		creatureInfoWindow = new CreatureInfoWindow(game, skin, activeCreature, 0, (int)stage.height()-200);
		enemyInfoWindow = new CreatureInfoWindow(game, skin, enemy, (int)stage.width()-236, 200);
		stage.addActor(creatureInfoWindow);
		stage.addActor(enemyInfoWindow);
		stage.addActor(new BottomWindow(game, skin, this));
	}
	
	@Override public void render(float arg0){
        camera.update();
        camera.apply(Gdx.gl10);
		
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		RenderUtil.drawSky(CritterCaptors.getModel("skydome"), CritterCaptors.getTexture("skydome"), camera.position);
		terrain.render();
		RenderUtil.drawModel(CritterCaptors.getModel(enemy.getName()), CritterCaptors.getTexture(enemy.getName()),
				new Vector3(-3, 0, 10), new Vector3(-1,0,-1), new Vector3(1,1,1));
		RenderUtil.drawModel(CritterCaptors.getModel(activeCreature.getName()), 
				CritterCaptors.getTexture(activeCreature.getName()), new Vector3(1, 0, 3.5f), new Vector3(1,0,1), new Vector3(1,1,1));

		stage.act(arg0);
		stage.draw();
	}

	public void fight(String name) {
		String enemyAttack = enemy.getActiveAbilities().get(CritterCaptors.random.nextInt(
				enemy.getActiveAbilities().size())).name;
		
		//figure out first attacker from speed
		boolean playerFirst = enemy.getSpeed() <= activeCreature.getSpeed();
		Creature first = playerFirst ? activeCreature : enemy, 
				second = playerFirst ? enemy : activeCreature;
		String firstAttack = playerFirst ? name : enemyAttack, 
				secondAttack = playerFirst ? enemyAttack : name;
		
		if(second.receiveDamage(first.attack(enemy, firstAttack)) || 
				first.receiveDamage(second.attack(activeCreature, secondAttack)) ||
				second.statusUpdate() || first.statusUpdate())
			death();
		
		creatureInfoWindow.update();
		enemyInfoWindow.update();
		stage.addActor(new BottomWindow(game, skin, this));
	}
	
	/**
	 * check who died and act appropriately
	 */
	private void death(){
		if(activeCreature.getHPCurrent() == 0){ 
			if(game.getCharacter().getNextActiveCreature() != null)
				stage.addActor(new CreatureSelectWindow(game, skin, this));
			else
				game.setScreen(new BlackoutScreen(game));
		}else{ 
			//show window indicating victory!
			activeCreature.addExperience(ExperienceUtil.getKillExperience(enemy));
			activeCreature.getEV().add(enemy.getEVYield());
			game.setScreen(new WorldMapScreen(game));
		}
	}

	@Override public void resize (int width, int height) {
        camera = RenderUtil.resize(width, height);
	}

	public void capture() {
		final float catchRoll = CritterCaptors.random.nextFloat(),
		catchRate = 1-enemy.getCatchRate();
		if(catchRoll >= catchRate){
			enemy.setActive(game.getCharacter().getNextEmptyActiveIndex());
			game.getCharacter().getOwnedCreatures().add(enemy);
			game.setScreen(new WorldMapScreen(game));
			return;
		}else
			fight(null);
		final Window failWindow = new Window(skin);
		final Button okButton = new TextButton("Ok", skin.getStyle(TextButtonStyle.class), "ok");
		okButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				actor.getStage().removeActor(failWindow);
			}
		});
		failWindow.add(new TextField("Catch failed!", skin));
		failWindow.row();
		failWindow.add(new TextField("Must roll > " + (int)(100*catchRate), skin));
		failWindow.row();
		failWindow.add(new TextField("You rolled " + (int)(100*catchRoll), skin));
		failWindow.row();
		failWindow.add(okButton);
		failWindow.pack();
		failWindow.x = Gdx.graphics.getWidth() / 2 - failWindow.width / 2;
		failWindow.y = Gdx.graphics.getHeight() / 2 - failWindow.height / 2;
		stage.addActor(failWindow);
		
	}

	public Creature getActiveCreature() {
		return activeCreature;
	}

	public void setActiveCreature(Creature activeCreature) {
		this.activeCreature = activeCreature;
		stage.removeActor(creatureInfoWindow);
		creatureInfoWindow = new CreatureInfoWindow(game, skin, activeCreature, 0, (int)stage.height()-200);
		stage.addActor(creatureInfoWindow);
	}
}

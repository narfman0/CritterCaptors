package com.blastedstudios.crittercaptors.ui.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.creature.Creature;
import com.blastedstudios.crittercaptors.creature.StatusEffectEnum;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;
import com.blastedstudios.crittercaptors.ui.battle.attackwindows.AttackWindowFirst;
import com.blastedstudios.crittercaptors.ui.battle.attackwindows.AttackWindowSecond;
import com.blastedstudios.crittercaptors.ui.battle.attackwindows.StatusWindowFirst;
import com.blastedstudios.crittercaptors.ui.battle.attackwindows.StatusWindowSecond;
import com.blastedstudios.crittercaptors.ui.terrain.ITerrain;
import com.blastedstudios.crittercaptors.ui.terrain.Terrain;
import com.blastedstudios.crittercaptors.ui.worldmap.WorldMapScreen;
import com.blastedstudios.crittercaptors.util.ExperienceUtil;
import com.blastedstudios.crittercaptors.util.RenderUtil;

public class BattleScreen extends AbstractScreen {
    private static final ITerrain terrain;
	private Creature enemy, activeCreature;
    private Camera camera;
    private CreatureInfoWindow creatureInfoWindow, enemyInfoWindow;
    static{
    	float[] heightMap = new float[(Terrain.DEFAULT_WIDTH + 1) * (Terrain.DEFAULT_WIDTH + 1)];
		for(int x=0; x<Terrain.DEFAULT_WIDTH; x++)
			heightMap[Terrain.DEFAULT_WIDTH*Terrain.DEFAULT_WIDTH + x] = 1f - (float)Math.sin(x/4)/5f;		
		terrain = new Terrain(heightMap, new Vector3(), 10);
    }

	public BattleScreen(CritterCaptors game, Creature enemy) {
		super(game);
		this.enemy = enemy;
		activeCreature = game.getCharacter().getNextActiveCreature();
		if(activeCreature == null)
			game.setScreen(new BlackoutScreen(game, this));
		creatureInfoWindow = new CreatureInfoWindow(game, skin, activeCreature, 0, (int)stage.height()-200);
		enemyInfoWindow = new CreatureInfoWindow(game, skin, enemy, (int)stage.width()-236, 200);
		stage.addActor(creatureInfoWindow);
		stage.addActor(enemyInfoWindow);
		stage.addActor(new BottomWindow(game, skin, this));
	}
	
	@Override public void render(float time){
        camera.update();
        camera.apply(Gdx.gl10);
		
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		RenderUtil.drawSky(CritterCaptors.getModel("skydome"), CritterCaptors.getTexture("skydome"), camera.position);
		terrain.render();
		RenderUtil.drawModel(CritterCaptors.getModel(enemy.getName()), CritterCaptors.getTexture(enemy.getName()),
				new Vector3(-3, 0, 10), new Vector3(-1,0,-1), new Vector3(1,1,1));
		RenderUtil.drawModel(CritterCaptors.getModel(activeCreature.getName()), 
				CritterCaptors.getTexture(activeCreature.getName()), new Vector3(1, 0, 3.5f), new Vector3(1,0,1), new Vector3(1,1,1));

		stage.act(time);
		stage.draw();
	}

	/**
	 * @param name ability name
	 * @param dmgModifier the multiplier of damage according to how 
	 * well the user input the gesture
	 */
	public void fight(String name, float dmgModifier) {
		String enemyAttack = enemy.getActiveAbilities().get(CritterCaptors.random.nextInt(
				enemy.getActiveAbilities().size())).name;
		
		//figure out first attacker from speed
		boolean playerFirst = enemy.getSpeed() <= activeCreature.getSpeed();
		Creature first = playerFirst ? activeCreature : enemy, 
				second = playerFirst ? enemy : activeCreature;
		String firstAttack = playerFirst ? name : enemyAttack, 
				secondAttack = playerFirst ? enemyAttack : name;
		int firstAtkDmg = (int)(first.attack(second, firstAttack) * (playerFirst?dmgModifier:1)),
			secondAtkDmg = (int)(second.attack(first, secondAttack) * (!playerFirst?dmgModifier:1));
		AttackStruct attack = new AttackStruct(firstAtkDmg, secondAtkDmg, 
				firstAttack, secondAttack, first, second);
		setNextBattleWindow(new AttackWindowFirst(game, skin, this, attack), attack);
	}
	
	/**
	 * check who died and act appropriately
	 */
	public void death(){
		if(activeCreature.getHPCurrent() == 0){ 
			if(game.getCharacter().getNextActiveCreature() != null)
				stage.addActor(new CreatureSelectWindow(game, skin, this));
			else
				game.setScreen(new BlackoutScreen(game, this));
		}else{ 
			//show window indicating victory!
			int xpAdded = ExperienceUtil.getKillExperience(enemy);
			activeCreature.addExperience(xpAdded);
			activeCreature.getEV().add(enemy.getEVYield());
			stage.addActor(new VictoryWindow(game, skin, this, xpAdded));
		}
	}
	
	public void endBattle(){
		game.setScreen(new WorldMapScreen(game));
		for(Creature creature : game.getCharacter().getOwnedCreatures())
			creature.endBattle();
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
			stage.addActor(new CaptureSuccessWindow(game, skin, this));
			return;
		}
		stage.addActor(new CaptureFailWindow(game, skin, this, 
				(int)(100*catchRate), (int)(100*catchRoll) ));
	}

	public Creature getActiveCreature() {
		return activeCreature;
	}
	
	public Creature getEnemy(){
		return enemy;
	}

	public void setActiveCreature(Creature activeCreature) {
		this.activeCreature.statusUpdate(false);
		this.activeCreature = activeCreature;
		stage.removeActor(creatureInfoWindow);
		creatureInfoWindow = new CreatureInfoWindow(game, skin, activeCreature, 0, (int)stage.height()-200);
		stage.addActor(creatureInfoWindow);
	}
	
	public void updateStatusWindows(){
		creatureInfoWindow.update();
		enemyInfoWindow.update();
	}
	
	/**
	 * Figures out the next attack window that should be shown
	 */
	public void setNextBattleWindow(Window next, AttackStruct attackStruct){
		if(next instanceof AttackWindowFirst)
			if(attackStruct.attackFirst != null)
				stage.addActor(next);
			else
				setNextBattleWindow(new AttackWindowSecond(game, skin, this, attackStruct), attackStruct);
		if(next instanceof AttackWindowSecond)
			if(attackStruct.attackSecond != null)
				stage.addActor(next);
			else
				setNextBattleWindow(new StatusWindowSecond(game, skin, this, attackStruct), attackStruct);
		if(next instanceof StatusWindowFirst)
			if(attackStruct.creatureFirst.getStatus() != StatusEffectEnum.None)
				stage.addActor(next);
			else
				stage.addActor(new BottomWindow(game, skin, this));
		if(next instanceof StatusWindowSecond)
			if(attackStruct.creatureSecond.getStatus() != StatusEffectEnum.None)
				stage.addActor(next);
			else
				setNextBattleWindow(new StatusWindowFirst(game, skin, this, attackStruct), attackStruct);
			
	}
}

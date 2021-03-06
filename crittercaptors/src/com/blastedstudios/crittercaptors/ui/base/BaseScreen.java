package com.blastedstudios.crittercaptors.ui.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.character.BaseUpgradeEnum;
import com.blastedstudios.crittercaptors.creature.Creature;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;
import com.blastedstudios.crittercaptors.ui.vr.VRScreen;
import com.blastedstudios.crittercaptors.ui.worldmap.WorldMapScreen;

public class BaseScreen extends AbstractScreen {
	public BaseScreen(final CritterCaptors game) {
		super(game);
		Window window = new Window("Base", skin);
		final Button cancelButton = new TextButton("Cancel", skin.getStyle(TextButtonStyle.class), "cancel");
		final Button creaturesButton = new TextButton("Creatures", skin.getStyle(TextButtonStyle.class), "creatures");
		final Button healButton = new TextButton("Heal All", skin.getStyle(TextButtonStyle.class), "heal");
		final Button upgradeButton = new TextButton("Upgrades", skin.getStyle(TextButtonStyle.class), "upgrade");
		final Button vrButton = new TextButton("VR Sim", skin.getStyle(TextButtonStyle.class), "vr");
		cancelButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.activeBase = null;
				game.setScreen(new WorldMapScreen(game));
			}
		});
		creaturesButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.setScreen(new CreaturesScreen(game));
			}
		});
		healButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				for(Creature creature : game.getCharacter().getOwnedCreatures())
					creature.heal();
				game.setScreen(new BaseScreen(game));
			}
		});
		upgradeButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.setScreen(new UpgradesScreen(game));
			}
		});
		vrButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.setScreen(new VRScreen(game));
			}
		});
		window.add(creaturesButton);
		window.row();
		window.add(healButton);
		window.row();
		window.add(upgradeButton);
		window.row();
		if(game.activeBase.hasUpgrade(BaseUpgradeEnum.VirtualReality)){
			window.add(vrButton);
			window.row();
		}
		window.add(cancelButton);
		window.pack();
		window.x = Gdx.graphics.getWidth()/2 - window.width/2;
		window.y = Gdx.graphics.getHeight()/2 - window.height/2;
		stage.addActor(window);
	}
}

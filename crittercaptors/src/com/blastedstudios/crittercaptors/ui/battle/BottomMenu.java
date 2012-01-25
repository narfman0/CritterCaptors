package com.blastedstudios.crittercaptors.ui.battle;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.ui.worldmap.WorldMap;

public class BottomMenu extends Window {
	public BottomMenu(final CritterCaptors game, final Skin skin, final BattleScreen battleScreen) {
		super("Actions", skin);
		final Button fightButton = new TextButton("Fight", skin.getStyle(TextButtonStyle.class), "fight");
		final Button runButton = new TextButton("Run", skin.getStyle(TextButtonStyle.class), "run");
		//final Button creaturesButton = new TextButton("Creatures", skin.getStyle(TextButtonStyle.class), "creatures");
		runButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				actor.parent.parent.removeActorRecursive(actor.parent);
				game.setScreen(new WorldMap(game));
			}
		});
		fightButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				actor.parent.addActor(new FightMenu(game, skin, battleScreen));
				actor.parent.parent.removeActorRecursive(actor.parent);
			}
		});
		add(fightButton);
		row();
		//add(creaturesButton);
		//row();
		add(runButton);
		pack();
		x = 8;
		y = 8;
	}
}

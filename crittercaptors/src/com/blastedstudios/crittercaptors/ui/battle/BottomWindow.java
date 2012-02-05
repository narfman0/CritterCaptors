package com.blastedstudios.crittercaptors.ui.battle;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.ui.worldmap.WorldMapScreen;

public class BottomWindow extends Window {
	public BottomWindow(final CritterCaptors game, final Skin skin, final BattleScreen battleScreen) {
		super("Actions", skin);
		final Button fightButton = new TextButton("Fight", skin.getStyle(TextButtonStyle.class), "fight");
		final Button captureButton = new TextButton("Capture", skin.getStyle(TextButtonStyle.class), "capture");
		final Button runButton = new TextButton("Run", skin.getStyle(TextButtonStyle.class), "run");
		final Button creaturesButton = new TextButton("Creatures", skin.getStyle(TextButtonStyle.class), "creatures");
		//bag?
		creaturesButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				actor.getStage().addActor(new CreatureSelectWindow(game, skin, battleScreen));
				actor.getStage().removeActor(actor.parent);
			}
		});
		runButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				actor.getStage().removeActor(actor.parent);
				game.setScreen(new WorldMapScreen(game));
			}
		});
		captureButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				battleScreen.capture();
			}
		});
		fightButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				actor.getStage().addActor(new FightWindow(game, skin, battleScreen));
				actor.getStage().removeActor(actor.parent);
			}
		});
		add(fightButton);
		row();
		add(captureButton);
		row();
		add(creaturesButton);
		row();
		add(runButton);
		pack();
		x = 8;
		y = 8;
	}
}

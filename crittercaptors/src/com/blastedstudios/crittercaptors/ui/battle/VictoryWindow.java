package com.blastedstudios.crittercaptors.ui.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.blastedstudios.crittercaptors.CritterCaptors;

public class VictoryWindow extends Window {
	public VictoryWindow(final CritterCaptors game, final Skin skin, 
			final BattleScreen battleScreen, int xpAdded) {
		super("Victory", skin);
		final Button okButton = new TextButton("Ok", skin.getStyle(TextButtonStyle.class), "ok");
		okButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				battleScreen.endBattle();
			}
		});
		TextButton catchResult = new TextButton(battleScreen.getActiveCreature().getName() + 
				" has defeated\n" + battleScreen.getEnemy().getName() + ", earning\n" + xpAdded +
				" experience.", skin);
		add(catchResult);
		catchResult.touchable = false;
		row();
		add(okButton);
		pack();
		x = Gdx.graphics.getWidth() / 2 - width / 2;
		y = Gdx.graphics.getHeight() / 2 - height / 2;
	}
}

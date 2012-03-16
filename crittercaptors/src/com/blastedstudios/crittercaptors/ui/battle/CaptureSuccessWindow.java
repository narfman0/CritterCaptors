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

public class CaptureSuccessWindow extends Window {
	public CaptureSuccessWindow(final CritterCaptors game, final Skin skin, 
			final BattleScreen battleScreen) {
		super("Success", skin);
		final Button okButton = new TextButton("Ok", skin.getStyle(TextButtonStyle.class), "ok");
		okButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				battleScreen.endBattle();
			}
		});
		TextButton catchResult = new TextButton("Capture success!\nYou now have a level " + 
				battleScreen.getEnemy().getLevel() + "\n" + battleScreen.getEnemy().getName(), skin);
		add(catchResult);
		catchResult.touchable = false;
		row();
		add(okButton);
		pack();
		x = Gdx.graphics.getWidth() / 2 - width / 2;
		y = Gdx.graphics.getHeight() / 2 - height / 2;
	}
}

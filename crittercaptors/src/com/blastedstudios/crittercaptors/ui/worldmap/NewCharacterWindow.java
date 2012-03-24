package com.blastedstudios.crittercaptors.ui.worldmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class NewCharacterWindow extends Window {
	public NewCharacterWindow(Skin skin) {
		super("Welcome!", skin);
		final Button button = new TextButton("Ok", skin.getStyle(TextButtonStyle.class), "ok");
		button.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				stage.removeActor(actor.parent);
			}
		});
		add(new Label("You should build a base\nimmediately to take care"+
			"\nof creature management\nand provide a place for\nyour character to rest.\n\n" +
			"Press <esc> and choose\n\"Base\" from the menu", skin));
		row();
		add(button);
		pack();
		x = Gdx.graphics.getWidth() / 2 - width / 2;
		y = Gdx.graphics.getHeight() / 2 - height / 2;
	}
	
}

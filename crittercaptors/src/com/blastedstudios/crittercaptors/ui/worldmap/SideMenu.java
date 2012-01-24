package com.blastedstudios.crittercaptors.ui.worldmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.ui.mainscreen.MainMenu;

public class SideMenu extends Window{
	public boolean dispose = false;

	public SideMenu(final CritterCaptors game, final Skin skin) {
		super("Menu", skin);
		final Button creaturesButton = new TextButton("Creatures", skin.getStyle(TextButtonStyle.class), "creatures");
		final Button cancelButton = new TextButton("Cancel", skin.getStyle(TextButtonStyle.class), "cancel");
		final Button exitButton = new TextButton("Exit", skin.getStyle(TextButtonStyle.class), "exit");
		cancelButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				dispose = true;
				actor.parent.parent.removeActorRecursive(actor.parent);
			}
		});
		exitButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.getCharacter().save();
				game.setScreen(new MainMenu(game));
			}
		});
		add(creaturesButton);
		row();
		add(cancelButton);
		row();
		add(exitButton);
		pack();
		x = Gdx.graphics.getWidth() - width - 8;
		y = Gdx.graphics.getHeight() - height - 8;
	}

}

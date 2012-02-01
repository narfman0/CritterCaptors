package com.blastedstudios.crittercaptors.ui.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.creature.Creature;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;

public class SellScreen extends AbstractScreen {
	public SellScreen(final CritterCaptors game, final Creature creature) {
		super(game);
		final Button confirmButton = new TextButton("Confirm", skin.getStyle(TextButtonStyle.class), "confirm");
		confirmButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.getCharacter().sell(creature);
				game.setScreen(new CreaturesScreen(game));
			}
		});
		final Button cancelButton = new TextButton("Cancel", skin.getStyle(TextButtonStyle.class), "cancel");
		cancelButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.setScreen(new CreatureScreen(game, creature));
			}
		});
		Window window = new Window("Are you sure?", skin);
		window.add(new Label("You want to sell\n" + creature.getName() + 
				" for $" + creature.getWorth() + "?", skin)).colspan(2);
		window.row();
		window.add(confirmButton);
		window.add(cancelButton);
		window.pack();
		window.x = Gdx.graphics.getWidth()/2 - window.width/2;
		window.y = Gdx.graphics.getHeight()/2 - window.height/2;
		stage.addActor(window);
	}
}

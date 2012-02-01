package com.blastedstudios.crittercaptors.ui.battle;

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
import com.blastedstudios.crittercaptors.ui.worldmap.WorldMapScreen;

public class BlackoutScreen extends AbstractScreen {
	public BlackoutScreen(final CritterCaptors game) {
		super(game);
		final int lostCash = game.getCharacter().blackout();
		final Button okButton = new TextButton("Ok", skin.getStyle(TextButtonStyle.class), "ok");
		okButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				for(Creature creature : game.getCharacter().getOwnedCreatures())
					creature.heal();
				game.setScreen(new WorldMapScreen(game));
			}
		});
		Window window = new Window("Blackout", skin);
		window.add(new Label("You have blacked out\nand lost $" + lostCash + ".\nYou have been healed, but\n" +
			"please take better care\nnext time, as your creatures\n" +
			"are not pleased by this\nbehavior.\nReinserting into world...", skin));
		window.row();
		window.add(okButton);
		window.pack();
		window.x = Gdx.graphics.getWidth()/2 - window.width/2;
		window.y = Gdx.graphics.getHeight()/2 - window.height/2;
		stage.addActor(window);
	}
}

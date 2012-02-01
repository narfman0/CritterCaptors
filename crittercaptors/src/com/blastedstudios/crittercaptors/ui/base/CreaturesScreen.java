package com.blastedstudios.crittercaptors.ui.base;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.creature.Creature;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;

public class CreaturesScreen extends AbstractScreen implements Screen {
	public CreaturesScreen(final CritterCaptors game) {
		super(game);
		Window window = new Window("Active Creatures", skin);
		HashMap<Integer, Creature> creatures = game.getCharacter().getActiveCreatures();
		for(int i=0; i<6; i++){
			final Button button;
			if(creatures.containsKey(i)){
				final Creature creature = creatures.get(i);
				String buttonText = creature.getName() + "    Level: " + creature.getLevel() + "\n";
				buttonText += creature.getHPCurrent() + " / " + creature.getHPMax();
				buttonText += "    Next level: %" + creature.getPercentLevelComplete();
				button = new TextButton(buttonText, skin.getStyle(TextButtonStyle.class), "creature");
				button.setClickListener(new ClickListener() {
					@Override public void click(Actor actor, float arg1, float arg2) {
						game.setScreen(new CreatureScreen(game, creature));
					}
				});
			}
			else
				button = new TextButton("", skin.getStyle(TextButtonStyle.class), "creature");
			button.height(46);
			button.width(180);
			window.add(button);
			if(i%2 == 1)
				window.row();
		}
		final Button cancelButton = new TextButton("Cancel", skin.getStyle(TextButtonStyle.class), "cancel");
		cancelButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.setScreen(new BaseScreen(game));
			}
		});
		window.add(cancelButton).colspan(2);
		window.pack();
		window.x = Gdx.graphics.getWidth() / 2 - window.width / 2;
		window.y = Gdx.graphics.getHeight() / 2 - window.height / 2;
		stage.addActor(window);
	}
}

package com.blastedstudios.crittercaptors.ui.base;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class CreatureActivateScreen extends AbstractScreen implements Screen {
	public CreatureActivateScreen(final CritterCaptors game, final Creature creature) {
		super(game);
		Window window = new Window("Activate", skin);
		final Button cancelButton = new TextButton("Cancel", skin.getStyle(TextButtonStyle.class), "cancel");
		cancelButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.setScreen(new CreaturesScreen(game));
			}
		});
		window.add(new Label("At what position would you like the creature?", skin)).colspan(2);
		window.row();
		HashMap<Integer, Creature> creatures = game.getCharacter().getActiveCreatures();
		for(int i=0; i<6; i++){
			String buttonText = "";
			final Creature currentCreature = creatures.get(i);
			final int activeIndex = i;
			if(currentCreature != null)
				buttonText += currentCreature.getName() + "    Level: " + currentCreature.getLevel() + "\n"+
					currentCreature.getHPCurrent() + " / " + currentCreature.getHPMax()+
					"    Next level: %" + currentCreature.getPercentLevelComplete();
			final Button button = new TextButton(buttonText, skin.getStyle(TextButtonStyle.class), "creature");
			button.setClickListener(new ClickListener() {
				@Override public void click(Actor actor, float arg1, float arg2) {
					creature.setActive(activeIndex);
					if(currentCreature != null)
						currentCreature.setActive(-1);
					game.setScreen(new CreaturesScreen(game));
				}
			});
			button.height(46);
			button.width(180);
			window.add(button);
			if(i%2 == 1)
				window.row();
		}
		window.add(cancelButton).colspan(2);
		window.pack();
		window.x = Gdx.graphics.getWidth()/2 - window.width/2;
		window.y = Gdx.graphics.getHeight()/2 - window.height/2;
		stage.addActor(window);
	}
}

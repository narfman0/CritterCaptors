package com.blastedstudios.crittercaptors.ui.worldmap;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.creature.Creature;

public class CreaturesMenu extends Window {

	public CreaturesMenu(final CritterCaptors game, final Skin skin) {
		super("Creatures", skin);
		HashMap<Integer, Creature> creatures = game.getCharacter().getActiveCreatures();
		for(int i=0; i<6; i++){
			final Button button;
			if(creatures.containsKey(i)){
				Creature creature = creatures.get(i);
				String buttonText = creature.getName() + "    Level: " + creature.getLevel() + "\n";
				buttonText += creature.getHPCurrent() + " / " + creature.getHPMax();
				buttonText += "    Next level: %" + creature.getPercentLevelComplete();
				button = new TextButton(buttonText, skin.getStyle(TextButtonStyle.class), "creature");
				button.setClickListener(new ClickListener() {
					@Override public void click(Actor actor, float arg1, float arg2) {
						actor.getStage().addActor(new CreaturesMenu(game, skin));
					}
				});
			}
			else
				button = new TextButton("", skin.getStyle(TextButtonStyle.class), "creature");
			button.height(46);
			button.width(180);
			add(button);
			if(i%2 == 1)
				row();
		}
		final Button cancelButton = new TextButton("Cancel", skin.getStyle(TextButtonStyle.class), "cancel");
		cancelButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				actor.getStage().removeActor(actor.parent);
			}
		});
		add(cancelButton);
		this.pack();
		x = Gdx.graphics.getWidth() / 2 - width / 2;
		y = Gdx.graphics.getHeight() / 2 - height / 2;
	}
}

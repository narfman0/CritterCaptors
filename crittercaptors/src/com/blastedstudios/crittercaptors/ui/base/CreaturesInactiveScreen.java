package com.blastedstudios.crittercaptors.ui.base;

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

public class CreaturesInactiveScreen extends AbstractScreen implements Screen {
	public CreaturesInactiveScreen(final CritterCaptors game, final int screenIndex) {
		super(game);
		Window window = new Window("Creatures", skin);
		int currentWindowCreatureCount = 0;
		int inactiveCount = 0;
		for(int i = 0; i < game.getCharacter().getOwnedCreatures().size() && currentWindowCreatureCount != 6; i++){
			if(game.getCharacter().getOwnedCreatures().get(i).getActive() == -1){
				if(inactiveCount > screenIndex*6){
					final Creature creature = game.getCharacter().getOwnedCreatures().get(i);
					String buttonText = creature.getName() + "    Level: " + creature.getLevel() + "\n";
					buttonText += creature.getHPCurrent() + " / " + creature.getHPMax();
					buttonText += "    Next level: %" + creature.getPercentLevelComplete();
					final Button button = new TextButton(buttonText, skin.getStyle(TextButtonStyle.class), "creature");
					button.setClickListener(new ClickListener() {
						@Override public void click(Actor actor, float arg1, float arg2) {
							game.setScreen(new CreatureScreen(game, creature));
						}
					});
					button.height(46);
					button.width(180);
					window.add(button);
					if(currentWindowCreatureCount%2 == 1)
						window.row();
				}
				inactiveCount++;
			}
		}
		final Button previousButton = new TextButton("Previous", skin.getStyle(TextButtonStyle.class), "previous");
		previousButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.setScreen(screenIndex == 0 ? new CreaturesScreen(game) : new CreaturesInactiveScreen(game, screenIndex-1));
			}
		});
		final Button cancelButton = new TextButton("Cancel", skin.getStyle(TextButtonStyle.class), "cancel");
		cancelButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.setScreen(new BaseScreen(game));
			}
		});

		window.add(previousButton);
		window.add(cancelButton);		
		if(inactiveCount == 6){
			final Button nextButton = new TextButton("Next", skin.getStyle(TextButtonStyle.class), "next");
			nextButton.setClickListener(new ClickListener() {
				@Override public void click(Actor actor, float arg1, float arg2) {
					game.setScreen(new CreaturesInactiveScreen(game, screenIndex+1));
				}
			});
			window.add(nextButton);
		}
		window.pack();
		window.x = Gdx.graphics.getWidth() / 2 - window.width / 2;
		window.y = Gdx.graphics.getHeight() / 2 - window.height / 2;
		stage.addActor(window);
	}
}

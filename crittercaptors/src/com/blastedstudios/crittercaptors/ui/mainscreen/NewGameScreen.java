package com.blastedstudios.crittercaptors.ui.mainscreen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.blastedstudios.crittercaptors.character.Base;
import com.blastedstudios.crittercaptors.character.Character;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.util.ExperienceUtil;
import com.blastedstudios.crittercaptors.creature.Creature;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;
import com.blastedstudios.crittercaptors.ui.worldmap.WorldMapScreen;

public class NewGameScreen extends AbstractScreen {
	private final TextField newGameNameTextfield;
	private static final int INITIAL_LEVEL = 5, INITIAL_CASH = 1500;
		
	public NewGameScreen(final CritterCaptors game){
		super(game);
		newGameNameTextfield = new TextField("", "Enter name here", skin.getStyle(TextFieldStyle.class), "name");
		final Button newGameButton = new TextButton("Start", skin.getStyle(TextButtonStyle.class), "ok");
		final Button cancelButton = new TextButton("Cancel", skin.getStyle(TextButtonStyle.class), "cancel");
		final List initialCreatureList = new List(new String[]{"Armadillo","Gecko","Penguin"}, skin);
		initialCreatureList.setSelectedIndex(0);
		newGameButton.setClickListener(new ClickListener() {
			@Override public void click(Actor arg0, float arg1, float arg2) {
				ArrayList<Creature> ownedCreatures = new ArrayList<Creature>();
				Creature constructed = game.getCreatureManager().create(initialCreatureList.getSelection());
				constructed.setActive(0);
				constructed.setExperience(ExperienceUtil.getExperience(INITIAL_LEVEL));
				constructed.heal();
				ownedCreatures.add(constructed);
				game.setCharacter(new Character(newGameNameTextfield.getText(), INITIAL_CASH, ownedCreatures, new ArrayList<Base>()));
				game.setScreen(new WorldMapScreen(game, true));
				dispose();
			}
		});
		cancelButton.setClickListener(new ClickListener() {
			@Override public void click(Actor arg0, float arg1, float arg2) {
				game.setScreen(new MainScreen(game));
				dispose();
			}
		});
		Window window = new Window("New Game", skin.getStyle(WindowStyle.class), "window");
		window.row();
		window.add(newGameNameTextfield).colspan(2);
		window.row();
		window.add(initialCreatureList).colspan(2);
		window.row();
		window.add(newGameButton);
		window.add(cancelButton);
		window.pack();
		window.x = Gdx.graphics.getWidth()/2 - window.width/2;
		window.y = Gdx.graphics.getHeight()/2 - window.height/2;
		stage.addActor(window);
	}
}

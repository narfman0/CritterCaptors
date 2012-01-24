package com.blastedstudios.crittercaptors.ui.mainscreen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.blastedstudios.crittercaptors.Character;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;
import com.blastedstudios.crittercaptors.ui.worldmap.WorldMap;

public class NewGameMenu extends AbstractScreen implements ClickListener {
	private static final String NEW_GAME_ENTER_NAME = "ng enter name", 
	NEW_GAME_START = "ng ok", NEW_GAME_CANCEL = "ng cancel";
	private final TextField newGameNameTextfield;
		
	public NewGameMenu(CritterCaptors game){
		super(game);
		newGameNameTextfield = new TextField("", "Enter name here", skin.getStyle(TextFieldStyle.class), NEW_GAME_ENTER_NAME);
		final Button newGameButton = new TextButton("Start", skin.getStyle(TextButtonStyle.class), NEW_GAME_START);
		final Button cancelButton = new TextButton("Cancel", skin.getStyle(TextButtonStyle.class), NEW_GAME_CANCEL);
		newGameButton.setClickListener(this);
		cancelButton.setClickListener(this);
		Window window = new Window("New Game", skin.getStyle(WindowStyle.class), "window");
		window.x = window.y = 0;
		window.width = stage.width();
		window.height = stage.height();
		window.defaults().spaceBottom(10);
		window.row().fill().expandX();
		window.add(newGameNameTextfield).minWidth(100).expandX().fillX().colspan(3);
		window.row();
		window.add(newGameButton).fill(0f, 0f);
		window.add(cancelButton).fill(0f, 0f);
		window.pack();
		stage.addActor(window);
	}

	@Override public void click(Actor actor, float arg1, float arg2) {
		if(actor.name.equals(NEW_GAME_START)){
			game.setCharacter(Character.load(game.getCreatureManager(), newGameNameTextfield.getMessageText()));
			game.setScreen(new WorldMap(game));
			dispose();
		}else if(actor.name.equals(NEW_GAME_CANCEL)){
			game.setScreen(new MainMenu(game));
			dispose();
		}
	}
}

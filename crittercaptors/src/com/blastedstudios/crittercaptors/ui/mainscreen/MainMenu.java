package com.blastedstudios.crittercaptors.ui.mainscreen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;

public class MainMenu extends AbstractScreen implements ClickListener {
	private static final String MAIN_NEW_GAME_TEXT = "new",
	MAIN_LOAD_GAME_TEXT = "load";
	
	public MainMenu(CritterCaptors game){
		super(game);
		final Button newGameButton = new TextButton("New", skin.getStyle(TextButtonStyle.class), MAIN_NEW_GAME_TEXT);
		final Button loadGameButton = new TextButton("Load", skin.getStyle(TextButtonStyle.class), MAIN_LOAD_GAME_TEXT);
		newGameButton.setClickListener(this);
		loadGameButton.setClickListener(this);
		Window window = new Window("Critter Captors", skin.getStyle(WindowStyle.class), "window");
		window.x = window.y = 0;
		window.width = stage.width();
		window.height = stage.height();
		window.defaults().spaceBottom(10);
		window.row().fill().expandX();
		window.add(newGameButton).fill(0f, 0f);
		window.row();
		window.add(loadGameButton).fill(0f, 0f);
		window.pack();
		stage.addActor(window);
	}

	@Override public void click(Actor actor, float arg1, float arg2) {
		if(actor.name.equals(MAIN_NEW_GAME_TEXT)){
			game.setScreen(new NewGameMenu(game));
			dispose();
		}else if(actor.name.equals(MAIN_LOAD_GAME_TEXT)){
			game.setScreen(new LoadMenu(game));
			dispose();
		}
	}
}

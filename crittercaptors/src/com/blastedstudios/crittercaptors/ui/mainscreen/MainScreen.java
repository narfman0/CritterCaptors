package com.blastedstudios.crittercaptors.ui.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;

public class MainScreen extends AbstractScreen {
	public MainScreen(final CritterCaptors game){
		super(game);
		final Button newGameButton = new TextButton("New Game", skin.getStyle(TextButtonStyle.class), "new");
		final Button loadGameButton = new TextButton("Load", skin.getStyle(TextButtonStyle.class), "load");
		final Button optionsButton = new TextButton("Options", skin.getStyle(TextButtonStyle.class), "options");
		final Button exitButton = new TextButton("Exit", skin.getStyle(TextButtonStyle.class), "exit");
		newGameButton.setClickListener(new ClickListener() {
			@Override public void click(Actor arg0, float arg1, float arg2) {
				game.setScreen(new NewGameScreen(game));
			}
		});
		loadGameButton.setClickListener(new ClickListener() {
			@Override public void click(Actor arg0, float arg1, float arg2) {
				game.setScreen(new LoadScreen(game));
			}
		});
		optionsButton.setClickListener(new ClickListener() {
			@Override public void click(Actor arg0, float arg1, float arg2) {
				game.setScreen(new OptionsScreen(game));
			}
		});
		exitButton.setClickListener(new ClickListener() {
			@Override public void click(Actor arg0, float arg1, float arg2) {
				Gdx.app.exit();
			}
		});
		Window window = new Window("Critter Captors", skin.getStyle(WindowStyle.class), "window");
		window.add(newGameButton);
		window.row();
		window.add(loadGameButton);
		window.row();
		window.add(optionsButton);
		window.row();
		window.add(exitButton);
		window.pack();
		window.x = Gdx.graphics.getWidth()/2 - window.width/2;
		window.y = Gdx.graphics.getHeight()/2 - window.height/2;
		stage.addActor(window);
	}
}

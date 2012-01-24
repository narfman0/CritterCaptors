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

public class MainMenu extends AbstractScreen {
	public MainMenu(final CritterCaptors game){
		super(game);
		final Button newGameButton = new TextButton("New", skin.getStyle(TextButtonStyle.class), "new");
		final Button loadGameButton = new TextButton("Load", skin.getStyle(TextButtonStyle.class), "load");
		newGameButton.setClickListener(new ClickListener() {
			@Override public void click(Actor arg0, float arg1, float arg2) {
				game.setScreen(new NewGameMenu(game));
				dispose();
			}
		});
		loadGameButton.setClickListener(new ClickListener() {
			@Override public void click(Actor arg0, float arg1, float arg2) {
				game.setScreen(new LoadMenu(game));
				dispose();
			}
		});
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
}

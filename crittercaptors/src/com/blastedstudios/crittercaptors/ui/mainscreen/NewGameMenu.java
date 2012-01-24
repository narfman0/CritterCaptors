package com.blastedstudios.crittercaptors.ui.mainscreen;

import com.badlogic.gdx.Gdx;
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

public class NewGameMenu extends AbstractScreen {
	private final TextField newGameNameTextfield;
		
	public NewGameMenu(final CritterCaptors game){
		super(game);
		newGameNameTextfield = new TextField("", "Enter name here", skin.getStyle(TextFieldStyle.class), "name");
		final Button newGameButton = new TextButton("Start", skin.getStyle(TextButtonStyle.class), "ok");
		final Button cancelButton = new TextButton("Cancel", skin.getStyle(TextButtonStyle.class), "cancel");
		newGameButton.setClickListener(new ClickListener() {
			@Override public void click(Actor arg0, float arg1, float arg2) {
				game.setCharacter(Character.load(game.getCreatureManager(), newGameNameTextfield.getMessageText()));
				game.setScreen(new WorldMap(game));
				dispose();
			}
		});
		cancelButton.setClickListener(new ClickListener() {
			@Override public void click(Actor arg0, float arg1, float arg2) {
				game.setScreen(new MainMenu(game));
				dispose();
			}
		});
		Window window = new Window("New Game", skin.getStyle(WindowStyle.class), "window");
		window.row();
		window.add(newGameNameTextfield).minWidth(100).expandX().fillX().colspan(3);
		window.row();
		window.add(newGameButton);
		window.add(cancelButton);
		window.pack();
		window.x = Gdx.graphics.getWidth()/2 - window.width/2;
		window.y = Gdx.graphics.getHeight()/2 - window.height/2;
		stage.addActor(window);
	}
}

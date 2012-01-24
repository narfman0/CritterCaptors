package com.blastedstudios.crittercaptors.ui.mainscreen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.blastedstudios.crittercaptors.Character;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;
import com.blastedstudios.crittercaptors.ui.worldmap.WorldMap;

public class LoadMenu extends AbstractScreen implements ClickListener {
	private static final String LOAD_GAME_CANCEL = "load cancel",
		LOAD_GAME_OK = "load ok", LOAD_CHARACTER_LIST = "load list";
	private final List savedCharacterList;

	public LoadMenu(CritterCaptors game){
		super(game);
		savedCharacterList = new List(Character.getSavedCharactersNames(), skin.getStyle(ListStyle.class), LOAD_CHARACTER_LIST);
		final Button okButton = new TextButton("Load", skin.getStyle(TextButtonStyle.class), LOAD_GAME_OK);
		final Button cancelButton = new TextButton("Cancel", skin.getStyle(TextButtonStyle.class), LOAD_GAME_CANCEL);
		okButton.setClickListener(this);
		cancelButton.setClickListener(this);
		final ScrollPane scrollPane = new ScrollPane(savedCharacterList, skin.getStyle(ScrollPaneStyle.class), "scroll");
		Window window = new Window("Load", skin.getStyle(WindowStyle.class), "window");
		window.x = window.y = 0;
		window.width = stage.width();
		window.height = stage.height();
		window.defaults().spaceBottom(10);
		window.row().fill().expandX();
		window.add(scrollPane).minWidth(100).expandX().fillX().colspan(3);
		window.row();
		window.add(okButton).fill(0f, 0f);
		window.row();
		window.add(cancelButton).fill(0f, 0f);
		window.pack();
		stage.addActor(window);
	}

	@Override public void click(Actor actor, float arg1, float arg2) {
		if(actor.name.equals(LOAD_GAME_CANCEL)){
			game.setScreen(new MainMenu(game));
			dispose();
		}else if(actor.name.equals(LOAD_GAME_OK)){
			game.setCharacter(Character.load(game.getCreatureManager(), savedCharacterList.getSelection()));
			game.setScreen(new WorldMap(game));
			dispose();
		}
	}
}

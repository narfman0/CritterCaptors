package com.blastedstudios.crittercaptors.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;

public class UIManager implements ClickListener {
	private static final String MAIN_NEW_GAME_TEXT = "main ng",
		MAIN_LOAD_GAME_TEXT = "main load",
		NEW_GAME_ENTER_NAME = "ng enter name", NEW_GAME_START = "ng ok", 
		NEW_GAME_CANCEL = "ng cancel", LOAD_GAME_CANCEL = "load cancel",
		LOAD_GAME_OK = "load ok", LOAD_CHARACTER_LIST = "load list";
	private final TextField newGameNameTextfield;
	private final List savedCharacterList;
	private Stage stage;
	private Skin skin;
	public String selectedCharacter;

	public UIManager(){
		skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"), Gdx.files.internal("data/ui/uiskin.png"));
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		newGameNameTextfield = new TextField("", "Enter name here", skin.getStyle(TextFieldStyle.class), NEW_GAME_ENTER_NAME);
		savedCharacterList = new List(com.blastedstudios.crittercaptors.Character.getSavedCharactersNames(), skin.getStyle(ListStyle.class), LOAD_CHARACTER_LIST);
		Gdx.input.setInputProcessor(stage);
		stage.addActor(createMainMenu());
	}
	
	private Window createMainMenu(){
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
		return window;
	}
	
	private Window createNewGameMenu(){
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
		return window;
	}

	private Window createLoadMenu(){
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
		return window;
	}
	
	public void render(){
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override public void click(Actor actor, float arg1, float arg2) {
		if(actor.name.equals(MAIN_NEW_GAME_TEXT)){
			stage.removeActor(actor.parent);
			createNewGameMenu();
			stage.addActor(createNewGameMenu());
		}else if(actor.name.equals(MAIN_LOAD_GAME_TEXT)){
			stage.removeActor(actor.parent);
			stage.addActor(createLoadMenu());
		}else if(actor.name.equals(NEW_GAME_START)){
			stage.removeActor(actor.parent);
			selectedCharacter = newGameNameTextfield.getMessageText();
		}else if(actor.name.equals(NEW_GAME_CANCEL)){
			stage.removeActor(actor.parent);
			stage.addActor(createMainMenu());
		}else if(actor.name.equals(LOAD_GAME_CANCEL)){
			stage.removeActor(actor.parent);
			stage.addActor(createMainMenu());
		}else if(actor.name.equals(LOAD_GAME_OK)){
			stage.removeActor(actor.parent);
			selectedCharacter = savedCharacterList.getSelection();
		}
	}
	
	public boolean isActive(){
		return stage.getActors().size() > 0;
	}
}

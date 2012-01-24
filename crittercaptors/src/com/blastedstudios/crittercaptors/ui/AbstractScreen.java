package com.blastedstudios.crittercaptors.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.crittercaptors.CritterCaptors;

public abstract class AbstractScreen implements Screen {
	protected Stage stage;
	protected Skin skin;
	protected CritterCaptors game;
	
	public AbstractScreen(CritterCaptors game){
		this.game = game;
		skin = new Skin(Gdx.files.internal("data/ui/uiskin.json"), Gdx.files.internal("data/ui/uiskin.png"));
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		Gdx.input.setInputProcessor(stage);
	}

	@Override public void render(float arg0) {
		Gdx.gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}
	
	@Override public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}
	@Override public void dispose() {}
	@Override public void hide() {}
	@Override public void pause() {}
	@Override public void resume() {}
	@Override public void show() {}
}

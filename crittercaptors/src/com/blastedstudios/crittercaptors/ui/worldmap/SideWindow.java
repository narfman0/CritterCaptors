package com.blastedstudios.crittercaptors.ui.worldmap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.character.Base;
import com.blastedstudios.crittercaptors.ui.base.BaseScreen;
import com.blastedstudios.crittercaptors.ui.mainscreen.MainScreen;

public class SideWindow extends Window{
	private final WorldMapScreen worldMapScreen;
	
	public SideWindow(final CritterCaptors game, final Skin skin, final WorldMapScreen worldMapScreen) {
		super("Menu", skin);
		this.worldMapScreen = worldMapScreen;
		final Button creaturesButton = new TextButton("Creatures", skin.getStyle(TextButtonStyle.class), "creatures");
		final Button baseButton = new TextButton("Base", skin.getStyle(TextButtonStyle.class), "base");
		final Button exitButton = new TextButton("Exit", skin.getStyle(TextButtonStyle.class), "exit");
		creaturesButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				actor.getStage().addActor(new CreaturesWindow(game, skin));
			}
		});
		baseButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				baseButtonHit(game, skin);
			}
		});
		exitButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.getCharacter().save();
				game.setScreen(new MainScreen(game));
			}
		});
		add(creaturesButton);
		row();
		add(baseButton);
		row();
		add(exitButton);
		pack();
		x = Gdx.graphics.getWidth() - width - 8;
		y = Gdx.graphics.getHeight() - height - 8;
	}

	protected void baseButtonHit(CritterCaptors game, Skin skin) {
		Vector3 pos = worldMapScreen.getCamera().position;
		for(Base characterBase : game.getCharacter().getBases())
			if(characterBase.getCachedPosition() != null && 
				characterBase.getCachedPosition().dst2(pos) < Base.BASE_DISTANCE)
				game.activeBase = characterBase;
		if(game.activeBase != null)
			game.setScreen(new BaseScreen(game));
		else
			if(game.getCharacter().getCash() < Base.BASE_COST)
				stage.addActor(new BuildBaseBrokeWindow(game, skin));
			else
				stage.addActor(new BuildBaseWindow(game, skin, worldMapScreen));
	}
}

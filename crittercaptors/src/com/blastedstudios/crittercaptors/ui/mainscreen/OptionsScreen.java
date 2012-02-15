package com.blastedstudios.crittercaptors.ui.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;
import com.blastedstudios.crittercaptors.util.OptionsUtil;

public class OptionsScreen extends AbstractScreen implements Screen {
	public OptionsScreen(final CritterCaptors game) {
		super(game);
		final TextButton enableGPSButton = new TextButton(getEnableGPSText(), skin.getStyle(TextButtonStyle.class), "enableGPS");
		enableGPSButton.setClickListener(new ClickListener() {
			@Override public void click(Actor arg0, float arg1, float arg2) {
				game.getOptions().saveOption(OptionsUtil.USE_GPS, !game.getOptions().getOptionBoolean(OptionsUtil.USE_GPS));
				enableGPSButton.setText(getEnableGPSText());
			}
		});
		final Button okButton = new TextButton("Ok", skin.getStyle(TextButtonStyle.class), "ok");
		okButton.setClickListener(new ClickListener() {
			@Override public void click(Actor arg0, float arg1, float arg2) {
				game.setScreen(new MainScreen(game));
			}
		});
		Window window = new Window("Options", skin.getStyle(WindowStyle.class), "options");
		window.row();
		window.add(enableGPSButton);
		window.row();
		window.add(okButton);
		window.row();
		window.x = Gdx.graphics.getWidth()/2 - window.width/2;
		window.y = Gdx.graphics.getHeight()/2 - window.height/2;
		stage.addActor(window);
	}
	
	private String getEnableGPSText(){
		return game.getOptions().getOptionBoolean(OptionsUtil.USE_GPS) ? "Disable GPS" : "Enable GPS";
	}
}

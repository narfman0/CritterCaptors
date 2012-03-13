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
import com.blastedstudios.crittercaptors.util.OptionsUtil.OptionEnum;

public class OptionsScreen extends AbstractScreen implements Screen {
	public OptionsScreen(final CritterCaptors game) {
		super(game);
		Window window = new Window("Options", skin.getStyle(WindowStyle.class), "options");
		for(final OptionEnum option : OptionEnum.values()){
			final TextButton button = new TextButton(getButtonText(option), skin.getStyle(TextButtonStyle.class), "enable" + option.name());
			button.setClickListener(new ClickListener() {
				@Override public void click(Actor arg0, float arg1, float arg2) {
					game.getOptions().saveOption(option, !game.getOptions().getOptionBoolean(option));
					button.setText(getButtonText(option));
				}
			});
			window.add(button);
			window.row();
		}
		final Button okButton = new TextButton("Ok", skin.getStyle(TextButtonStyle.class), "ok");
		okButton.setClickListener(new ClickListener() {
			@Override public void click(Actor arg0, float arg1, float arg2) {
				game.setScreen(new MainScreen(game));
			}
		});
		window.add(okButton);
		window.pack();
		window.x = Gdx.graphics.getWidth()/2 - window.width/2;
		window.y = Gdx.graphics.getHeight()/2 - window.height/2;
		stage.addActor(window);
	}

	private String getButtonText(OptionEnum option){
		String text = option.name();
		for(int i=0; i<text.length(); i++)
			if(i>0 && Character.isUpperCase(text.charAt(i)))
				text = text.substring(0, i++) + " " + text.substring((i++-1));
		return game.getOptions().getOptionBoolean(option) ? "Disable " + text : "Enable " + text;
	}
}

package com.blastedstudios.crittercaptors.ui.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.character.Base;
import com.blastedstudios.crittercaptors.character.BaseUpgradeEnum;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;

public class UpgradesScreen extends AbstractScreen implements Screen {
	public UpgradesScreen(final CritterCaptors game) {
		super(game);
		final Button cancelButton = new TextButton("Cancel", skin.getStyle(TextButtonStyle.class), "cancel");
		cancelButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.setScreen(new BaseScreen(game));
			}
		});
		final TextButton retardantEnableButton = new TextButton(getRetardantEnabledText(), skin.getStyle(TextButtonStyle.class), "cancel");
		retardantEnableButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				if(!game.activeBase.hasUpgrade(BaseUpgradeEnum.MonsterRetardant)){
					game.activeBase.upgrade(BaseUpgradeEnum.MonsterRetardant);
					game.getCharacter().addCash(-Base.RETARDANT_COST);
				}else
					game.activeBase.setRetardantEnabled(!game.activeBase.isRetardantEnabled());
				retardantEnableButton.setText(getRetardantEnabledText());
			}
		});
		Window window = new Window("Upgrades", skin);
		window.add(new Label("Monster retardant: This upgrade\nmakes monsters" +
				" levels scale\nwith distance from the\nnearest retardent " +
				"enabled base", skin));
		window.add(retardantEnableButton);
		window.row();
		window.add(cancelButton).colspan(2);
		window.pack();
		window.x = Gdx.graphics.getWidth()/2 - window.width/2;
		window.y = Gdx.graphics.getHeight()/2 - window.height/2;
		stage.addActor(window);
	}

	private String getRetardantEnabledText(){
		if(game.activeBase.isRetardantEnabled())
			return "Disable";
		else if (!game.activeBase.hasUpgrade(BaseUpgradeEnum.MonsterRetardant))
			return "Buy $" + Base.RETARDANT_COST;
		return "Enable";
	}
}

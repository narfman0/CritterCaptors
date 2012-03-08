package com.blastedstudios.crittercaptors.ui.battle.attackwindows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.ui.battle.AttackStruct;
import com.blastedstudios.crittercaptors.ui.battle.BattleScreen;

public class StatusWindowSecond extends Window {
	public StatusWindowSecond(final CritterCaptors game, final Skin skin, 
			final BattleScreen battleScreen, final AttackStruct attackStruct) {
		super("", skin);
		boolean dead = attackStruct.creatureSecond.statusUpdate(true);
		String text = attackStruct.creatureSecond.getName() + " has status: " + 
			attackStruct.creatureSecond.getStatus(); 
		if(dead)
			battleScreen.death();
		battleScreen.updateStatusWindows();
		final Button button = new TextButton(text, skin.getStyle(TextButtonStyle.class), "accept");
		button.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				actor.getStage().addActor(new StatusWindowFirst(game, skin, battleScreen, attackStruct));
				actor.getStage().removeActor(actor.parent);
			}
		});
		add(button);
		pack();
		x = Gdx.graphics.getWidth() / 2 - width / 2;
		y = 8;
	}
}

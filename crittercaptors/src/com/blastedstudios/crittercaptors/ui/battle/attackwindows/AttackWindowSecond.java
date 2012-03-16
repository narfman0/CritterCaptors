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

public class AttackWindowSecond extends Window {
	public AttackWindowSecond(final CritterCaptors game, final Skin skin, 
			final BattleScreen battleScreen, final AttackStruct attackStruct) {
		super("", skin);
		String text = attackStruct.creatureSecond.getName() + " uses ability\n" +
		attackStruct.attackSecond + "\ndoing " + attackStruct.attackDamageSecond+
		" dmg";
		if(attackStruct.creatureFirst.receiveDamage(attackStruct.attackDamageSecond))
			battleScreen.death();
		battleScreen.updateStatusWindows();
		final Button button = new TextButton(text, skin.getStyle(TextButtonStyle.class), "atk");
		button.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				battleScreen.setNextBattleWindow(new StatusWindowSecond(game, skin, battleScreen, attackStruct), attackStruct);
				actor.getStage().removeActor(actor.parent);
			}
		});
		add(button);
		pack();
		x = Gdx.graphics.getWidth() / 2 - width / 2;
		y = 8;
	}
}

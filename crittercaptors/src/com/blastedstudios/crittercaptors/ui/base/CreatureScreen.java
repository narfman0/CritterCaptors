package com.blastedstudios.crittercaptors.ui.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.creature.AffinityEnum;
import com.blastedstudios.crittercaptors.creature.Creature;
import com.blastedstudios.crittercaptors.ui.AbstractScreen;

public class CreatureScreen extends AbstractScreen {
	public CreatureScreen(final CritterCaptors game, final Creature creature) {
		super(game);
		Window window = new Window("Creature", skin);
		final Button cancelButton = new TextButton("Cancel", skin.getStyle(TextButtonStyle.class), "cancel");
		final Button sellButton = new TextButton("Sell", skin.getStyle(TextButtonStyle.class), "sell");
		//TODO store (if active)/make active feature here
		sellButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.setScreen(new SellScreen(game, creature));
			}
		});
		cancelButton.setClickListener(new ClickListener() {
			@Override public void click(Actor actor, float arg1, float arg2) {
				game.setScreen(new CreaturesScreen(game));
			}
		});
		window.add(new Label(creature.getName(), skin));
		window.add(new Label("Level: " + creature.getLevel(), skin));
		window.row();
		window.add(new Label("HP: " +  creature.getHPCurrent() + " / " + creature.getHPMax(), skin));
		window.add(new Label("XP to level: " + creature.getPercentLevelComplete(), skin));
		window.row();
		window.add(new Label("Attack: " +  creature.getAttack(), skin));
		window.add(new Label("Defense: " +  creature.getDefense(), skin));
		window.row();
		window.add(new Label("Sp Attack: " +  creature.getSpecialAttack(), skin));
		window.add(new Label("Sp Defense: " +  creature.getSpecialDefense(), skin));
		window.row();
		String affinities = "";
		for(AffinityEnum affinity : creature.getAffinities())
			affinities += affinity.name() + ", ";
		window.add(new Label("Affinities: " + affinities.substring(0,affinities.lastIndexOf(",")), skin)).colspan(2);
		window.row();
		window.add(new Label("IV: " + creature.getIV(), skin)).colspan(2);
		window.row();
		window.add(new Label("EV: " + creature.getEV(), skin)).colspan(2);
		window.row();
		window.add(sellButton);
		window.add(cancelButton);
		window.pack();
		window.x = Gdx.graphics.getWidth()/2 - window.width/2;
		window.y = Gdx.graphics.getHeight()/2 - window.height/2;
		stage.addActor(window);
	}

}

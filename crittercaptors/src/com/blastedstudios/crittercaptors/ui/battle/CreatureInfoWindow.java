package com.blastedstudios.crittercaptors.ui.battle;

import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.creature.Creature;

public class CreatureInfoWindow extends Window {
	private final Creature creature;
	private final Label levelLabel, hpRatio, statusLabel;
	private final Slider hpSlider;
	private HPTimer timer;
	
	public CreatureInfoWindow(final CritterCaptors game, final Skin skin, 
			final Creature creature, final int x, final int y) {
		super(skin);
		this.touchable = false;
		this.creature = creature;
		levelLabel = new Label(creature.getLevel()+"", skin);
		statusLabel = new Label(creature.getStatus().name(), skin);
		hpRatio = new Label(creature.getHPCurrent() + "/" + creature.getHPMax(), skin);
		hpSlider = new Slider(0, creature.getHPMax(), 1, skin);
		hpSlider.setValue(creature.getHPCurrent());
		hpSlider.touchable = false;
		add(new Label(creature.getName(), skin));
		add(new Label(" Lvl: ",skin));
		add(levelLabel);
		row();
		add(new Label("HP: ",skin));
		add(hpSlider).colspan(3);
		add(hpRatio);
		row();
		add(new Label(" Status: ",skin));
		add(statusLabel);
		pack();
		this.x = x;
		this.y = y;
	}
	
	public void update(){
		levelLabel.setText(creature.getLevel()+"");
		statusLabel.setText(creature.getStatus().name());
		hpSlider.touchable = true;
		if(timer != null)
			timer.cancel();
		new Timer().scheduleAtFixedRate(timer = new HPTimer(), 100, 10); 
	}
	
	private class HPTimer extends TimerTask{
		float iterationModifier;
		final int difference;
		
		public HPTimer(){
			iterationModifier = 1f;
			difference = (int)hpSlider.getValue() - creature.getHPCurrent();
		}
		
		@Override public void run() {
			int current = creature.getHPCurrent() + (int)(iterationModifier*difference);
			hpRatio.setText(current + "/" + creature.getHPMax());
			hpSlider.setValue(current);
			iterationModifier -= .01f;
			if(iterationModifier <= 0){
				hpRatio.setText(creature.getHPCurrent() + "/" + creature.getHPMax());
				hpSlider.setValue(creature.getHPCurrent());
				hpSlider.touchable = false;
				cancel();
			}
		}
	}
}

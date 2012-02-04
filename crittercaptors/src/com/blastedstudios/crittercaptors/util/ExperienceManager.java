package com.blastedstudios.crittercaptors.util;

import com.blastedstudios.crittercaptors.creature.Creature;

public class ExperienceManager {
	public static final int MAX_LEVEL = 100;
	private static int[] levelToExperience = new int[MAX_LEVEL];
	static{
		for(int i=0; i<MAX_LEVEL; i++)
			levelToExperience[i] = getExperience(i);
	}
	
	public static int getExperience(int level){
		assert(level > 0 && level <= MAX_LEVEL);
		if(level == 1)
			return 0;
		return (int) ((6./5. * Math.pow(level, 3)) - (15*Math.pow(level, 2)) + (MAX_LEVEL*level) - 140);
	}
	
	public static int getLevel(int experience){
		for(int level = 0; level<levelToExperience.length; level++)
			if(levelToExperience[level] > experience || level == MAX_LEVEL)
				return level-1;
		return -1;
	}

	/**
	 * @param enemy that is killed
	 * @return the base experience a creature should get if they kill enemy
	 */
	public static int getKillExperience(Creature enemy) {
		int level = enemy.getLevel();
		if(level <= 5)
			return (int)(8.8 + (level * 2));
		else
			return level * 30;
	}
}

package com.blastedstudios.crittercaptors;

public class ExperienceManager {
	private static int[] levelToExperience = new int[100];
	static{
		for(int i=0; i<100; i++)
			levelToExperience[i] = getExperience(i);
	}
	
	public static int getExperience(int level){
		if(level == 0 || level == 1)
			return 0;
		return (int) ((6./5. * Math.pow(level, 3)) - (15*Math.pow(level, 2)) + (100*level) - 140);
	}
	
	public static int getLevel(int experience){
		for(int level = 0; level<levelToExperience.length; level++)
			if(levelToExperience[level] > experience || level == 100)
				return level-1;
		return -1;
	}
}

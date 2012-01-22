package com.blastedstudios.crittercaptors;

import java.util.HashMap;

import com.blastedstudios.crittercaptors.creature.AffinityEnum;

public class Ability {
	public static HashMap<String,Ability> abilities;
	static{
		abilities.put("Kick", new Ability(20, AffinityEnum.physical));
		abilities.put("Swipe", new Ability(30, AffinityEnum.physical));
		abilities.put("Bareknuckle", new Ability(30, AffinityEnum.urban));
		abilities.put("Cheap Shot", new Ability(20, AffinityEnum.urban));
		abilities.put("Head Butt", new Ability(20, AffinityEnum.urban));
		abilities.put("Wink", new Ability(20, AffinityEnum.education));
		abilities.put("Slap", new Ability(20, AffinityEnum.education));
		abilities.put("Chop", new Ability(30, AffinityEnum.education));
		abilities.put("Flood", new Ability(30, AffinityEnum.coastal));
		abilities.put("Tail Slap", new Ability(25, AffinityEnum.coastal));
		abilities.put("Tsunami", new Ability(50, AffinityEnum.ocean));
		abilities.put("Dance", new Ability(10, AffinityEnum.park));
		abilities.put("Tactical Strike", new Ability(45, AffinityEnum.restricted));
		abilities.put("Dive", new Ability(25, AffinityEnum.restricted));
		abilities.put("Spill", new Ability(20, AffinityEnum.road));
		abilities.put("Slay", new Ability(30, AffinityEnum.suburban));
		abilities.put("Drop", new Ability(25, AffinityEnum.suburban));
	}
	
	public int power;
	public AffinityEnum affinity;
	
	public Ability(int power, AffinityEnum affinity){
		this.power = power;
		this.affinity = affinity;
	}
}

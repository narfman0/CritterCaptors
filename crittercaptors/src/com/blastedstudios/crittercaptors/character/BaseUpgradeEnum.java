package com.blastedstudios.crittercaptors.character;

public enum BaseUpgradeEnum {
	/**
	 * Makes monster level scale with distance to base. Initial base has 
	 * a retardant enabled by default. A value of 0 indicates there is a 
	 * retardant but it is disabled, and a value of 1 indicates it is available
	 * and active. 
	 */
	MonsterRetardant,
	/**
	 * The virtual reality simulator lets the user fight already conquered
	 * creatures in a safe environment. The simulator gives a number of 
	 * creatures to fight, along with creature level. The upgrade level of the
	 * VR simulator is what level one may simulate creatures. 0 signifies the
	 * upgrade has not been unlocked for the base yet.
	 */
	VirtualReality
}

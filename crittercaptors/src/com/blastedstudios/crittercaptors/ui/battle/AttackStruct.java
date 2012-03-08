package com.blastedstudios.crittercaptors.ui.battle;

import com.blastedstudios.crittercaptors.creature.Creature;

public class AttackStruct {
	public final int attackDamageFirst, attackDamageSecond;
	public final String attackFirst, attackSecond;
	public final Creature creatureFirst, creatureSecond;
	
	public AttackStruct(int attackDamageFirst, int attackDamageSecond,
			String attackFirst, String attackSecond, Creature creatureFirst,
			Creature creatureSecond){
		this.attackDamageFirst = attackDamageFirst;
		this.attackDamageSecond = attackDamageSecond;
		this.attackFirst = attackFirst;
		this.attackSecond = attackSecond;
		this.creatureFirst = creatureFirst;
		this.creatureSecond = creatureSecond;
	}
}

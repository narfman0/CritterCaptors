package com.blastedstudios.crittercaptors.creature;

import org.w3c.dom.Element;

public class Stats {
	public int hpMax, attack, defense, specialAttack, specialDefense, speed;
	public Stats(){}
	public Stats(int hpMax, int attack, int defense, int specialAttack, 
			int specialDefense, int speed){
		this.hpMax = hpMax;
		this.attack = attack;
		this.defense = defense;
		this.specialAttack = specialAttack;
		this.specialDefense = specialDefense;
		this.speed = speed;
	}
	public static Stats fromXML(Element ele){
		return new Stats(
			Integer.parseInt(ele.getAttribute("hpMax")), 
			Integer.parseInt(ele.getAttribute("attack")), 
			Integer.parseInt(ele.getAttribute("defense")), 
			Integer.parseInt(ele.getAttribute("specialAttack")), 
			Integer.parseInt(ele.getAttribute("specialDefense")),
			Integer.parseInt(ele.getAttribute("speed")));
	}
}
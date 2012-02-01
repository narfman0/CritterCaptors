package com.blastedstudios.crittercaptors.creature;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Stats {
	private static final int MAX_EV_SINGLE = 252;
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
	
	public Element asXML(Document doc, String elementName) {
		Element statsElement = doc.createElement(elementName);
		statsElement.setAttribute("hpMax", Integer.toString(hpMax));
		statsElement.setAttribute("attack", Integer.toString(attack));
		statsElement.setAttribute("defense", Integer.toString(defense));
		statsElement.setAttribute("specialAttack", Integer.toString(specialAttack));
		statsElement.setAttribute("specialDefense", Integer.toString(specialDefense));
		statsElement.setAttribute("speed", Integer.toString(speed));
		return statsElement;
	}
	
	/**
	 * When enemy is killed, add ev stats
	 */
	public void add(Stats evYield){
		hpMax = Math.min(MAX_EV_SINGLE, hpMax + evYield.hpMax);
		attack = Math.min(MAX_EV_SINGLE, attack + evYield.attack);
		defense = Math.min(MAX_EV_SINGLE, defense + evYield.defense);
		specialAttack = Math.min(MAX_EV_SINGLE, specialAttack + evYield.specialAttack);
		specialDefense = Math.min(MAX_EV_SINGLE, specialDefense + evYield.specialDefense);
		speed = Math.min(MAX_EV_SINGLE, speed + evYield.speed);
	}
	
	@Override public String toString(){
		return "HP: " + hpMax + "  Attack: " + attack + "  Defense: " + defense +
				"\nSp Attack: " + specialAttack + "  Sp Defense: " + specialDefense +
				"  Speed: " + speed;
	}
	
	public int sum(){
		return attack + defense + specialAttack + specialDefense + speed + hpMax;
	}
}
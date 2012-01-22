package com.blastedstudios.crittercaptors.creature;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.blastedstudios.crittercaptors.Ability;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.ExperienceManager;
import com.blastedstudios.crittercaptors.util.XMLUtil;

/**
 * Defines creature. Has base stats, rest are calculated
 */
public class Creature {
	private String name;
	private List<AffinityEnum> affinities;
	private List<Ability> abilities;
	private int hpMax, attack, defense, specialAttack, specialDefense, speed,
		experience, hpCurrent;
	
	public Creature(String name, int hpMax, int attack, int defense, int specialAttack, 
			int specialDefense,	int speed, int experience, List<AffinityEnum> affinities, 
			List<Ability> abilities){
		this.name = name;
		this.hpMax = hpMax;
		this.hpCurrent = hpMax;
		this.attack = attack;
		this.defense = defense;
		this.specialAttack = specialAttack;
		this.specialDefense = specialDefense;
		this.speed = speed;
		this.experience = experience;
		this.affinities = affinities;
		this.abilities = abilities;
	}

	public int attackPhysical(Creature enemy, Ability ability){
		return (int) (((((( (ExperienceManager.getLevel(experience) * 2f / 5f) + 2f) * 
				getAttack() * ability.power / enemy.getDefense())) / 50f) * 
                /*CH ×*/ getR() / 100f) * AffinityDamage.getDamageMultiplier(affinities, enemy.affinities));
	}
	
	public int attackSpecial(Creature enemy, Ability ability){
		return (int) ((((((((ExperienceManager.getLevel(experience) * 2f / 5f) + 2f) * 
				ability.power * getSpecialAttack() / 50f) / enemy.getSpecialDefense())) / 2f) * 
                /*CH ×*/ getR() / 100f) * AffinityDamage.getDamageMultiplier(ability.affinity, enemy.affinities));
	}
	
	private static int getR(){
		return 100 - CritterCaptors.random.nextInt(15) ;
	}
	
	public int getAttack(){
		return attack + ExperienceManager.getLevel(experience) / 50;
	}
	
	public int getSpecialAttack(){
		return specialAttack + ExperienceManager.getLevel(experience) / 50;
	}

	public int getDefense(){
		return defense + ExperienceManager.getLevel(experience) / 50;
	}
	
	public int getSpecialDefense(){
		return specialDefense + ExperienceManager.getLevel(experience) / 50;
	}
	
	public Creature clone(){
		return new Creature(name, hpMax, attack, defense, specialAttack, 
				specialDefense, speed, experience, affinities, abilities);
	}
	
	public Creature fromXML(Element ele){
		List<AffinityEnum> affinities = new ArrayList<AffinityEnum>();
		for(Element affinityEle : XMLUtil.iterableElementList(ele.getElementsByTagName("affinity")))
			affinities.add(AffinityEnum.valueOf(affinityEle.getNodeValue()));
		List<Ability> abilities = new ArrayList<Ability>();
		for(Element abilityEle : XMLUtil.iterableElementList(ele.getElementsByTagName("ability")))
			abilities.add(Ability.abilities.get(abilityEle.getNodeValue()));
		return new Creature(ele.getAttribute("name"), Integer.parseInt(ele.getAttribute("hpMax")), 
				Integer.parseInt(ele.getAttribute("attack")), Integer.parseInt(ele.getAttribute("defense")), 
				Integer.parseInt(ele.getAttribute("specialAttack")), Integer.parseInt(ele.getAttribute("specialDefense")),
				Integer.parseInt(ele.getAttribute("speed")), Integer.parseInt(ele.getAttribute("experience")), 
				affinities, abilities);
	}
}

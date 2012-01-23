package com.blastedstudios.crittercaptors.creature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.ExperienceManager;
import com.blastedstudios.crittercaptors.util.XMLUtil;

/**
 * Defines creature. Has base stats, rest are calculated
 */
public class Creature {
	private String name;
	private List<AffinityEnum> affinities;
	private HashMap<Ability,Integer> abilities;
	private int hpMax, attack, defense, specialAttack, specialDefense, speed,
		experience, hpCurrent;
	public Camera camera = new PerspectiveCamera(67, 1.33f, 2f);
	public float timeSinceDirectionChange = 0;
	
	public Creature(String name, int hpMax, int attack, int defense, int specialAttack, 
			int specialDefense,	int speed, int experience, List<AffinityEnum> affinities, 
			HashMap<Ability,Integer> abilities){
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
	
	public List<AffinityEnum> getAffinities(){
		return affinities;
	}

	public String getName() {
		return name;
	}
	
	public void setExperience(int experience){
		this.experience = experience;
	}
	
	public Creature clone(){
		return new Creature(name, hpMax, attack, defense, specialAttack, 
				specialDefense, speed, experience, affinities, abilities);
	}
	
	public Element asXML(Document doc){
		Element node = doc.createElement("creature");
		node.setAttribute("name", name);
		node.setAttribute("experience", Integer.toString(experience));
		return node;
	}
	
	public static Creature fromXML(Element ele){
		List<AffinityEnum> affinities = new ArrayList<AffinityEnum>();
		for(Element affinityEle : XMLUtil.iterableElementList(ele.getElementsByTagName("affinity")))
			affinities.add(AffinityEnum.valueOf(affinityEle.getFirstChild().getNodeValue()));
		HashMap<Ability,Integer> abilities = new HashMap<Ability,Integer>();
		for(Element abilityEle : XMLUtil.iterableElementList(ele.getElementsByTagName("ability")))
			abilities.put(Ability.abilities.get(abilityEle.getAttribute("name")), Integer.parseInt(abilityEle.getAttribute("level")));
		return new Creature(ele.getAttribute("name"), Integer.parseInt(ele.getAttribute("hpMax")), 
				Integer.parseInt(ele.getAttribute("attack")), Integer.parseInt(ele.getAttribute("defense")), 
				Integer.parseInt(ele.getAttribute("specialAttack")), Integer.parseInt(ele.getAttribute("specialDefense")),
				Integer.parseInt(ele.getAttribute("speed")), Integer.parseInt(ele.getAttribute("experience")), 
				affinities, abilities);
	}
}

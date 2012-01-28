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
	private List<Ability> activeAbilities;
	private int active;
	
	public Creature(String name, int hpMax, int attack, int defense, int specialAttack, 
			int specialDefense,	int speed, int experience, List<AffinityEnum> affinities, 
			HashMap<Ability,Integer> abilities, int active){
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
		this.active = active;
		activeAbilities = new ArrayList<Ability>();
		for(Ability ability : abilities.keySet())
			if(abilities.get(ability) <= ExperienceManager.getLevel(experience) && activeAbilities.size() <= 4)
				activeAbilities.add(ability);
	}

	private int attackPhysical(Creature enemy, Ability ability){
		return (int) (((((( (ExperienceManager.getLevel(experience) * 2f / 5f) + 2f) * 
				getAttack() * ability.power / enemy.getDefense())) / 50f) * 
                /*CH ×*/ getR() / 100f) * AffinityDamage.getDamageMultiplier(affinities, enemy.affinities));
	}
	
	private int attackSpecial(Creature enemy, Ability ability){
		return (int) ((((((((ExperienceManager.getLevel(experience) * 2f / 5f) + 2f) * 
				ability.power * getSpecialAttack() / 50f) / enemy.getSpecialDefense())) / 2f) * 
                /*CH ×*/ getR() / 100f) * AffinityDamage.getDamageMultiplier(ability.affinity, enemy.affinities));
	}
	
	public int attack(Creature enemy, String abilityName){
		for(Ability activeAbility : activeAbilities)
			if(activeAbility.name.equals(abilityName))
				if(activeAbility.affinity == AffinityEnum.physical)
					return attackPhysical(enemy, activeAbility);
				else
					return attackSpecial(enemy, activeAbility);
		return 0;
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
	
	public int getHPMax(){
		return hpMax + ExperienceManager.getLevel(experience) / 50;
	}
	public int getHPCurrent(){
		return Math.max(0, hpCurrent + ExperienceManager.getLevel(experience) / 50);
	}
	
	/**
	 * @param damage received by creature
	 * @return if the creature is dead
	 */
	public boolean receiveDamage(int damage){
		hpCurrent -= damage;
		return getHPCurrent() <= 0;
	}
	
	public List<AffinityEnum> getAffinities(){
		return affinities;
	}

	public String getName() {
		return name;
	}
	
	public int getLevel(){
		return ExperienceManager.getLevel(experience);
	}
	
	public float getPercentLevelComplete(){
		int currentLevelXP = ExperienceManager.getExperience(getLevel()+1) - 
			ExperienceManager.getExperience(getLevel());
		int amountCompleted = experience - ExperienceManager.getExperience(getLevel());
		return (float)amountCompleted / (float)currentLevelXP;
	}
	
	public int getExperience(){
		return experience;
	}
	
	public void setExperience(int experience){
		this.experience = experience;
	}
	
	public void addExperience(int experience){
		this.experience += experience;
	}
	
	public Creature clone(){
		return new Creature(name, hpMax, attack, defense, specialAttack, 
				specialDefense, speed, experience, affinities, abilities, active);
	}
	
	public List<Ability> getActiveAbilities(){
		return activeAbilities;
	}
	
	public Element asXML(Document doc){
		Element node = doc.createElement("creature");
		node.setAttribute("name", name);
		node.setAttribute("experience", Integer.toString(experience));
		if(active != -1)
			node.setAttribute("active", Integer.toString(active));
		return node;
	}
	
	public static Creature fromXML(Element ele){
		List<AffinityEnum> affinities = new ArrayList<AffinityEnum>();
		for(Element affinityEle : XMLUtil.iterableElementList(ele.getElementsByTagName("affinity")))
			affinities.add(AffinityEnum.valueOf(affinityEle.getFirstChild().getNodeValue()));
		int active = -1;
		HashMap<Ability,Integer> abilities = new HashMap<Ability,Integer>();
		for(Element abilityEle : XMLUtil.iterableElementList(ele.getElementsByTagName("ability")))
			abilities.put(Ability.abilities.get(abilityEle.getAttribute("name")), Integer.parseInt(abilityEle.getAttribute("level")));
		return new Creature(ele.getAttribute("name"), Integer.parseInt(ele.getAttribute("hpMax")), 
				Integer.parseInt(ele.getAttribute("attack")), Integer.parseInt(ele.getAttribute("defense")), 
				Integer.parseInt(ele.getAttribute("specialAttack")), Integer.parseInt(ele.getAttribute("specialDefense")),
				Integer.parseInt(ele.getAttribute("speed")), Integer.parseInt(ele.getAttribute("experience")), 
				affinities, abilities, active);
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getActive() {
		return active;
	}
}

package com.blastedstudios.crittercaptors.creature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.util.ExperienceUtil;
import com.blastedstudios.crittercaptors.util.XMLUtil;

/**
 * Defines creature. Has base stats, rest are calculated
 */
public class Creature {
	private static final int BASE_HAPPINESS = 70;
	private String name;
	private List<AffinityEnum> affinities;
	private HashMap<Ability,Integer> abilities;
	private int active, experience, hpCurrent, happiness;
	private Stats baseStats, ivStats, evStats, evYield, battleStats;
	public Camera camera = new PerspectiveCamera(67, 1.33f, 2f);
	public float timeSinceDirectionChange = 0;
	private float catchRate = 0f;
	private List<Ability> activeAbilities;
	private StatusEffectEnum status = StatusEffectEnum.None;
	
	public Creature(String name, Stats baseStats, Stats ivStats, Stats evStats, 
			Stats evYield, int experience, List<AffinityEnum> affinities, 
			HashMap<Ability,Integer> abilities, int active, float catchRate,
			int happiness){
		this.name = name;
		this.baseStats = baseStats;
		this.ivStats = ivStats;
		this.evStats = evStats;
		this.evYield = evYield;
		this.experience = experience;
		this.affinities = affinities;
		this.abilities = abilities;
		this.active = active;
		this.catchRate = catchRate;
		this.hpCurrent = getHPMax();
		this.happiness = happiness;
		battleStats = new Stats();
		activeAbilities = new ArrayList<Ability>();
		for(Ability ability : abilities.keySet())
			if(abilities.get(ability) <= ExperienceUtil.getLevel(experience) && activeAbilities.size() <= 4)
				activeAbilities.add(ability);
	}

	private int attackPhysical(Creature enemy, Ability ability){
		float dmgMultiplier = AffinityDamage.getDamageMultiplier(affinities, enemy.affinities);
		int attack = status == StatusEffectEnum.Burn ? getAttack() / 2 : getAttack();
		return (int) ((((((( (ExperienceUtil.getLevel(experience) * 2f / 5f) + 2f) * 
				attack * ability.power / enemy.getDefense())) / 50f) + 2) * 
                /*CH ×*/ getR() / 100f) * dmgMultiplier);
	}
	
	private int attackSpecial(Creature enemy, Ability ability){
		float dmgMultiplier = AffinityDamage.getDamageMultiplier(affinities, enemy.affinities);
		if(ability.affinity == AffinityEnum.suburban/*fire?*/ && enemy.getStatus() == StatusEffectEnum.Freeze)
			enemy.setStatus(StatusEffectEnum.None);
		return (int) ((((((((ExperienceUtil.getLevel(experience) * 2f / 5f) + 2f) * 
				ability.power * getSpecialAttack() / 50f) / enemy.getSpecialDefense())) / 2f) * 
                /*CH ×*/ getR() / 100f) * dmgMultiplier);
	}
	
	public int attack(Creature enemy, String abilityName){
		Ability ability = null;
		for(Ability activeAbility : activeAbilities)
			if(activeAbility.name.equals(abilityName))
				ability = activeAbility;
		
		//do nothing statuses
		if(status == StatusEffectEnum.Confusion && CritterCaptors.random.nextInt(2) == 0){
			receiveDamage(getAbilityDamage(this, ability));
			return 0;
		}
		if( ability == null || ability.hitRate <= CritterCaptors.random.nextFloat() ||
			(status == StatusEffectEnum.Paralyze && CritterCaptors.random.nextInt(4) == 0) ||
			status == StatusEffectEnum.Sleep)
			return 0;
		
		if(ability.status != StatusEffectEnum.None)
			enemy.status = ability.status;
		if(ability.buff != null)
			(ability.buffSelf ? battleStats : enemy.battleStats).add(ability.buff);
		return getAbilityDamage(enemy, ability);
	}
	
	private int getAbilityDamage(Creature enemy, Ability ability){
		if(ability == null)
			return 0;
		if(ability.affinity == AffinityEnum.physical)
			return attackPhysical(enemy, ability);
		else
			return attackSpecial(enemy, ability);
	}
	
	private static int getR(){
		return 100 - CritterCaptors.random.nextInt(15) ;
	}
	
	public int getAttack(){
		return ( ((ivStats.attack + (2 * baseStats.attack) + 
				(evStats.attack / 4)) * getLevel()) / 100) + 5 + battleStats.attack;
	}
	
	public int getSpecialAttack(){
		return ( ((ivStats.specialAttack + (2 * baseStats.specialAttack) + 
				(evStats.specialAttack / 4)) * getLevel()) / 100) + 5 + battleStats.specialAttack;
	}

	public int getDefense(){
		return ( ((ivStats.defense + (2 * baseStats.defense) + 
				(evStats.defense / 4)) * getLevel()) / 100) + 5+ + battleStats.defense;
	}
	
	public int getSpecialDefense(){
		return ( ((ivStats.specialDefense + (2 * baseStats.specialDefense) + 
				(evStats.specialDefense / 4)) * getLevel()) / 100) + 5 + battleStats.specialDefense;
	}
	
	public int getHPMax(){
		return (((ivStats.hpMax + (2 * baseStats.hpMax) + (evStats.hpMax / 4) + 100) * getLevel()) / 100) + 10;
	}
	
	public int getHPCurrent(){
		return hpCurrent;
	}

	public int getSpeed() {
		int speed = ( ((ivStats.speed + (2 * baseStats.speed) + (evStats.speed / 4))
				* getLevel()) / 100) + 5 +  + battleStats.speed;
		return status == StatusEffectEnum.Paralyze ? speed/4 : speed;
	}
	
	public float getCatchRate(){
		return (((3*getHPMax() - 2*getHPCurrent())*catchRate/* *ball bonus*/) / (3*getHPMax()))/* *status bonus*/;
	}
	
	public void addHappiness(int happiness){
		this.happiness += happiness;
	}

	public void setHappiness(int happiness) {
		this.happiness = happiness;
	}
	
	/**
	 * @param damage received by creature
	 * @return if the creature is dead
	 */
	public boolean receiveDamage(int damage){
		hpCurrent = Math.max(0, hpCurrent - damage);
		return getHPCurrent() <= 0;
	}
	
	public List<AffinityEnum> getAffinities(){
		return affinities;
	}

	public String getName() {
		return name;
	}
	
	public int getLevel(){
		return ExperienceUtil.getLevel(experience);
	}
	
	public void setIV(Stats iv){
		ivStats = iv;
	}
	
	public void setEV(Stats ev){
		evStats = ev;
	}
	
	public int getPercentLevelComplete(){
		int currentLevelXP = ExperienceUtil.getExperience(getLevel()+1) - 
			ExperienceUtil.getExperience(getLevel());
		int amountCompleted = experience - ExperienceUtil.getExperience(getLevel());
		return amountCompleted * 100 / currentLevelXP;
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

	public void heal(){
		this.hpCurrent = getHPMax();
	}
	
	public Creature clone(){
		Random rand = CritterCaptors.random;
		Stats ivStats = new Stats(rand.nextInt(31), rand.nextInt(31),
				rand.nextInt(31), rand.nextInt(31), rand.nextInt(31),
				rand.nextInt(31));
		Creature creature = new Creature(name, baseStats, ivStats, evStats,
				evYield, experience, affinities, abilities, active, catchRate,
				happiness);
		return creature;
	}
	
	public List<Ability> getActiveAbilities(){
		return activeAbilities;
	}
	
	public Element asXML(Document doc){
		Element node = doc.createElement("creature");
		node.setAttribute("name", name);
		node.setAttribute("experience", Integer.toString(experience));
		node.setAttribute("happiness", Integer.toString(happiness));
		node.setAttribute("status", status.name());
		if(active != -1)
			node.setAttribute("active", Integer.toString(active));
		return node;
	}
	
	public static Creature fromXML(Element ele){
		List<AffinityEnum> affinities = new ArrayList<AffinityEnum>();
		for(Element affinityEle : XMLUtil.iterableElementList(ele.getElementsByTagName("affinity")))
			affinities.add(AffinityEnum.valueOf(affinityEle.getFirstChild().getNodeValue()));
		int active = -1;
		float catchRate = Float.parseFloat(ele.getAttribute("catchRate"));
		HashMap<Ability,Integer> abilities = new HashMap<Ability,Integer>();
		for(Element abilityEle : XMLUtil.iterableElementList(ele.getElementsByTagName("ability")))
			abilities.put(Ability.abilities.get(abilityEle.getAttribute("name")), 
					Integer.parseInt(abilityEle.getAttribute("level")));
		Stats baseStats = new Stats(), ivStats = new Stats(), evStats = new Stats();
		if(ele.getElementsByTagName("baseStats").getLength() > 0)
			baseStats = Stats.fromXML((Element)ele.getElementsByTagName("baseStats").item(0));
		if(ele.getElementsByTagName("ivStats").getLength() > 0)
			ivStats = Stats.fromXML((Element)ele.getElementsByTagName("ivStats").item(0));
		if(ele.getElementsByTagName("evStats").getLength() > 0)
			evStats = Stats.fromXML((Element)ele.getElementsByTagName("evStats").item(0));
		Stats evYield = Stats.fromXML((Element)ele.getElementsByTagName("evYield").item(0));
		int happiness = BASE_HAPPINESS;
		if(ele.hasAttribute("happiness"))
			happiness = Integer.parseInt(ele.getAttribute("happiness"));
		return new Creature(ele.getAttribute("name"), baseStats, ivStats, 
				evStats, evYield, Integer.parseInt(ele.getAttribute("experience")), 
				affinities, abilities, active, catchRate, happiness);
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getActive() {
		return active;
	}

	public Stats getIV() {
		return ivStats;
	}

	public Stats getEV() {
		return evStats;
	}
	
	public Stats getEVYield() {
		return evYield;
	}
	
	public int getWorth(){
		return (int)((3*getLevel() + evStats.sum() + ivStats.sum()) / getCatchRate()) / 100;
	}

	public StatusEffectEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEffectEnum status) {
		this.status = status;
	}
	
	/**
	 * perform status updates/damages
	 * @return if creature dies as a result of status updates (burn etc)
	 */
	public boolean statusUpdate(boolean isInCombat){
		switch(status){
		case Sleep:
			if(CritterCaptors.random.nextInt(7) == 0)
				status = StatusEffectEnum.None;
			break;
		case Burn:
		case Poison:
			receiveDamage(getHPMax()/8);
			break;
		case Freeze:
			if(CritterCaptors.random.nextInt(10)==0)
				status = StatusEffectEnum.None;
			break;
		case Confusion:
			if(!isInCombat)
				status = StatusEffectEnum.None;
			break;
		}
		return getHPCurrent() <= 0;
	}
	
	public void endBattle(){
		battleStats = new Stats();
		statusUpdate(false);
	}
}

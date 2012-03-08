package com.blastedstudios.crittercaptors.creature;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.blastedstudios.crittercaptors.creature.AffinityEnum;
import com.blastedstudios.crittercaptors.util.XMLUtil;

public class Ability {
	final public static HashMap<String,Ability> abilities;
	static{
		abilities = new HashMap<String, Ability>();
		Document abilityDoc = XMLUtil.parse("data/abilities.xml");
		for(Element abilityElement : XMLUtil.iterableElementList(abilityDoc.getElementsByTagName("ability"))){
			Ability ability = fromXML(abilityElement);
			abilities.put(ability.name, ability);
		}
	}
	
	final public int power;
	final public AffinityEnum affinity;
	final public String name, gestureName;
	final public StatusEffectEnum status;
	final public float hitRate;
	final public Stats buff;
	final public boolean buffSelf;
	
	public Ability(String name, int power, AffinityEnum affinity, float hitRate,
			StatusEffectEnum status, Stats buff, boolean buffSelf, 
			String gestureName){
		this.name = name;
		this.power = power;
		this.affinity = affinity;
		this.hitRate = hitRate;
		this.status = status;
		this.buff = buff;
		this.buffSelf = buffSelf;
		this.gestureName = gestureName;
	}
	
	public static Ability fromXML(Element abilityElement){
		StatusEffectEnum status =StatusEffectEnum.None;
		if(abilityElement.hasAttribute("status"))
			status = StatusEffectEnum.valueOf(abilityElement.getAttribute("status"));
		Stats buff = null;
		if(abilityElement.getElementsByTagName("buff").getLength() > 0)
			buff = Stats.fromXML((Element) abilityElement.getElementsByTagName("buff").item(0));
		boolean buffSelf = false;
		if(abilityElement.hasAttribute("buffSelf"))
			buffSelf = Boolean.parseBoolean(abilityElement.getAttribute("buffSelf"));
		float hitRate = 1f;
		if(abilityElement.hasAttribute("hitRate"))
			hitRate = Float.parseFloat(abilityElement.getAttribute("hitRate"));
		return new Ability(abilityElement.getAttribute("name"), 
				Integer.parseInt(abilityElement.getAttribute("power")),
				AffinityEnum.valueOf(abilityElement.getAttribute("affinity")), 
				hitRate, status, buff, buffSelf, abilityElement.getAttribute("gestureName"));
	}
}

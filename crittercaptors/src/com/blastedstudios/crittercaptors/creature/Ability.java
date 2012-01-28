package com.blastedstudios.crittercaptors.creature;

import java.util.HashMap;

import org.w3c.dom.Element;

import com.blastedstudios.crittercaptors.creature.AffinityEnum;
import com.blastedstudios.crittercaptors.util.XMLUtil;

public class Ability {
	final public static HashMap<String,Ability> abilities;
	static{
		abilities = new HashMap<String, Ability>();
		for(Element abilityElement : XMLUtil.iterableElementList(XMLUtil.parse("data/abilities.xml").getElementsByTagName("ability"))){
			Ability ability = fromXML(abilityElement);
			abilities.put(ability.name, ability);
		}
	}
	
	final public int power;
	final public AffinityEnum affinity;
	final public String name;
	
	public Ability(String name, int power, AffinityEnum affinity){
		this.name = name;
		this.power = power;
		this.affinity = affinity;
	}
	
	public static Ability fromXML(Element abilityElement){
		return new Ability(abilityElement.getAttribute("name"), 
				Integer.parseInt(abilityElement.getAttribute("power")),
				AffinityEnum.valueOf(abilityElement.getAttribute("affinity")));
	}
}

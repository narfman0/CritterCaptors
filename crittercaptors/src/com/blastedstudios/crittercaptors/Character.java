package com.blastedstudios.crittercaptors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.blastedstudios.crittercaptors.creature.Creature;
import com.blastedstudios.crittercaptors.creature.CreatureManager;
import com.blastedstudios.crittercaptors.creature.Stats;
import com.blastedstudios.crittercaptors.util.XMLUtil;

public class Character {
	private static final String SAVE_PATH = "data/saves.xml";
	private String name;
	private int cash = 0;
	private List<Creature> ownedCreatures;
	
	public Character(String name, int cash, ArrayList<Creature> ownedCreatures){
		this.name = name;
		this.cash = cash;
		this.ownedCreatures = ownedCreatures;
	}
	
	public List<Creature> getOwnedCreatures(){
		return ownedCreatures;
	}

	public HashMap<Integer,Creature> getActiveCreatures(){
		HashMap<Integer,Creature> creatures = new HashMap<Integer,Creature>();
		for(Creature creature : ownedCreatures)
			if(creature.getActive() != -1)
				creatures.put(creature.getActive(), creature);
		return creatures;
	}
	
	public String getName(){
		return name;
	}
	
	public int getCash(){
		return cash;
	}

	public void setCash(int cash){
		this.cash = cash;
	}
	
	public Creature getActiveCreature(){
		return ownedCreatures.get(0);
	}
	
	public int getNextEmptyActiveIndex(){
		for(int i=0; i<6; i++){
			boolean used = false;
			for(int creatureIndex = 0; creatureIndex<ownedCreatures.size(); creatureIndex++)
				if(ownedCreatures.get(creatureIndex).getActive() == i)
					used = true;
			if(!used)
				return i;
		}
		return -1;
	}
	
	public static String[] getSavedCharactersNames(){
		Document saveFile = XMLUtil.parse(SAVE_PATH);
		NodeList saveNodes = saveFile.getDocumentElement().getElementsByTagName("save");
		String[] names = new String[saveNodes.getLength()];
		int i=0;
		for(Element saveElement : XMLUtil.iterableElementList(saveNodes))
			names[i++] = saveElement.getAttribute("name");
		return names;
	}
	
	public static Character load(CreatureManager creatureManager, String name){
		ArrayList<Creature> ownedCreatures = new ArrayList<Creature>();
		int cash = 0;
		Document saveFile = XMLUtil.parse(SAVE_PATH);
		for(Element saveElement : XMLUtil.iterableElementList(saveFile.getDocumentElement().getElementsByTagName("save")))
			if(saveElement.getAttribute("name").equals(name)){
				cash = Integer.parseInt(saveElement.getAttribute("cash"));
				for(Element creatureElement : XMLUtil.iterableElementList(saveElement.getElementsByTagName("creature"))){
					Creature creature = creatureManager.create(creatureElement.getAttribute("name"));
					creature.setExperience(Integer.parseInt(creatureElement.getAttribute("experience")));
					creature.setIV(Stats.fromXML((Element)creatureElement.getElementsByTagName("ivStats").item(0)));
					creature.setEV(Stats.fromXML((Element)creatureElement.getElementsByTagName("evStats").item(0)));
					if(creatureElement.hasAttribute("active"))
						creature.setActive(Integer.parseInt(creatureElement.getAttribute("active")));
					ownedCreatures.add(creature);
				}
				return new Character(name, cash, ownedCreatures);
			}
		return null;
	}
	
	public void save(){
		Document saveFile = XMLUtil.parse(SAVE_PATH);
		for(Element saveElement : XMLUtil.iterableElementList(saveFile.getDocumentElement().getElementsByTagName("save")))
			if(saveElement.getAttribute("name").equals(name))
				saveFile.getDocumentElement().removeChild(saveElement);
		Element saveElement = saveFile.createElement("save");
		saveElement.setAttribute("name", name);
		saveElement.setAttribute("cash", Integer.toString(cash));
		for(Creature owned : ownedCreatures){
			Node element = saveElement.appendChild(owned.asXML(saveFile));
			element.appendChild(owned.getIV().asXML(saveFile, "ivStats"));
			element.appendChild(owned.getEV().asXML(saveFile, "evStats"));
		}
		saveFile.getDocumentElement().appendChild(saveElement);
		XMLUtil.writeToFile(saveFile, "data/saves.xml");
	}
}

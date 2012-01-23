package com.blastedstudios.crittercaptors;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.blastedstudios.crittercaptors.creature.Creature;
import com.blastedstudios.crittercaptors.creature.CreatureManager;
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
					Creature creature = creatureManager.create(name);
					creature.setExperience(Integer.parseInt(creatureElement.getAttribute("experience")));
					ownedCreatures.add(creature);
				}
			}
		return new Character(name, cash, ownedCreatures);
	}
	
	public void save(){
		Document saveFile = XMLUtil.parse(SAVE_PATH);
		for(Element saveElement : XMLUtil.iterableElementList(saveFile.getDocumentElement().getElementsByTagName("save")))
			if(saveElement.getAttribute("name").equals(name))
				saveFile.removeChild(saveElement);
		Element saveElement = saveFile.createElement("save");
		saveElement.setAttribute("name", name);
		saveElement.setAttribute("cash", Integer.toString(cash));
		for(Creature owned : ownedCreatures)
			saveElement.appendChild(owned.asXML(saveFile));
		XMLUtil.writeToFile(saveFile, "data/saves.xml");
	}
}

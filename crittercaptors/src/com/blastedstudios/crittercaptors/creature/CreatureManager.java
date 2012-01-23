package com.blastedstudios.crittercaptors.creature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.util.XMLUtil;

public class CreatureManager {
	private List<Creature> creatures;
	private HashMap<AffinityEnum,List<Creature>> creatureTemplates;
	private float timeSinceLastSpawn = 0;
	private static final float TIME_BETWEEN_SPAWNS = 4,
		TIME_BETWEEN_CREATURE_CHANGE_DIRECTION = 10;
	
	public CreatureManager(){
		creatures = new ArrayList<Creature>();
		creatureTemplates = new HashMap<AffinityEnum,List<Creature>>();
		for(AffinityEnum affinity : AffinityEnum.values())
			creatureTemplates.put(affinity, new ArrayList<Creature>());
		for(Element creatureElement : XMLUtil.iterableElementList(
				XMLUtil.parse("data/creatures.xml").getElementsByTagName("creature"))){
			Creature creature = Creature.fromXML(creatureElement);
			for(AffinityEnum creatureAffinity : creature.getAffinities())
				creatureTemplates.get(creatureAffinity).add(creature);
		}
	}

	public void update(HashMap<AffinityEnum, Float> worldAffinities, Vector3 location) {
		//check if we are going to spawn a creature
		timeSinceLastSpawn += Gdx.graphics.getDeltaTime();
		if(timeSinceLastSpawn > TIME_BETWEEN_SPAWNS){
			timeSinceLastSpawn = 0;
			Creature creature = spawn(worldAffinities); 
			float angle = CritterCaptors.random.nextFloat() * 360f;
			creature.camera.position.x = location.x + (float)Math.cos(angle) * 100f;
			creature.camera.position.z = location.z + (float)Math.sin(angle) * 100f;
			creatures.add(creature);
		}
		//move creatures
		for(Creature creature : creatures){
			creature.timeSinceDirectionChange += Gdx.graphics.getDeltaTime();
			if(creature.timeSinceDirectionChange > TIME_BETWEEN_CREATURE_CHANGE_DIRECTION)
				creature.timeSinceDirectionChange = 0;
			else if(creature.timeSinceDirectionChange < TIME_BETWEEN_CREATURE_CHANGE_DIRECTION / 10)
				creature.camera.rotate(CritterCaptors.TURN_RATE * Gdx.graphics.getDeltaTime(), 0, 1, 0);
			else if(creature.timeSinceDirectionChange < TIME_BETWEEN_CREATURE_CHANGE_DIRECTION / 1.5)
				creature.camera.position.add(creature.camera.direction.tmp().mul(Gdx.graphics.getDeltaTime()));
			creature.camera.update();
		}
	}

	private Creature spawn(HashMap<AffinityEnum, Float> worldAffinities) {
		for(AffinityEnum affinity : AffinityEnum.values())
			if(worldAffinities.containsKey(affinity) && worldAffinities.get(affinity) > .4f){
				List<Creature> creatures = creatureTemplates.get(affinity);
				int number = CritterCaptors.random.nextInt(creatures.size());
				return creatures.get(number).clone();
			}
		return null;
	}

	public List<Creature> getCreatures() {
		return creatures;
	}
	
	public List<String> getCreatureTemplateNames() {
		List<String> names = new ArrayList<String>();
		for(AffinityEnum affinity : AffinityEnum.values())
			for(Creature template: creatureTemplates.get(affinity))
				if(!names.contains(template.getName()))
					names.add(template.getName());
		return names;
	}
}

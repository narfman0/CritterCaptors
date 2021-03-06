package com.blastedstudios.crittercaptors.creature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.util.ExperienceUtil;
import com.blastedstudios.crittercaptors.ui.worldmap.WorldMapScreen;
import com.blastedstudios.crittercaptors.util.XMLUtil;

public class CreatureManager {
	private final List<Creature> creatures;
	private final HashMap<AffinityEnum,List<Creature>> creatureTemplates;
	private float timeSinceLastSpawn = 0;
	private static final float TIME_BETWEEN_SPAWNS = 4,
		TIME_BETWEEN_CREATURE_CHANGE_DIRECTION = 10,
		SPAWN_DISTANCE_FROM_PLAYER = 20f;//100f;
	
	public CreatureManager(){
		creatures = new ArrayList<Creature>();
		creatureTemplates = new HashMap<AffinityEnum,List<Creature>>();
		for(AffinityEnum affinity : AffinityEnum.values())
			creatureTemplates.put(affinity, new ArrayList<Creature>());
		Document creatureDoc = XMLUtil.parse("data/creatures.xml", FileType.Internal);
		for(Element creatureElement : XMLUtil.iterableElementList(creatureDoc.getElementsByTagName("creature"))){
			Creature creature = Creature.fromXML(creatureElement);
			for(AffinityEnum creatureAffinity : creature.getAffinities())
				creatureTemplates.get(creatureAffinity).add(creature);
		}
	}

	public void update(HashMap<AffinityEnum, Float> worldAffinities, Vector3 location,
			CritterCaptors game) {
		//check if we are going to spawn a creature
		timeSinceLastSpawn += Gdx.graphics.getDeltaTime();
		if(timeSinceLastSpawn > TIME_BETWEEN_SPAWNS){
			timeSinceLastSpawn = 0;
			Creature creature = spawn(worldAffinities); 
			if(creature != null){
				float angle = CritterCaptors.random.nextFloat() * 360f;
				creature.camera.position.x = location.x + (float)Math.cos(angle) * SPAWN_DISTANCE_FROM_PLAYER;
				creature.camera.position.z = location.z + (float)Math.sin(angle) * SPAWN_DISTANCE_FROM_PLAYER;
				float distance = game.getCharacter().getClosestRetardantDistance(creature.camera.position);
				int creatureLevel = getCreatureLevelFromDistance(distance);
				creature.setExperience(ExperienceUtil.getExperience(creatureLevel));
				creature.heal();
				creatures.add(creature);
			}
		}
		//move creatures
		for(Creature creature : creatures){
			creature.timeSinceDirectionChange += Gdx.graphics.getDeltaTime();
			if(creature.timeSinceDirectionChange > TIME_BETWEEN_CREATURE_CHANGE_DIRECTION)
				creature.timeSinceDirectionChange = 0;
			else if(creature.timeSinceDirectionChange < TIME_BETWEEN_CREATURE_CHANGE_DIRECTION / 10)
				creature.camera.rotate(WorldMapScreen.TURN_RATE * Gdx.graphics.getDeltaTime(), 0, 1, 0);
			else if(creature.timeSinceDirectionChange < TIME_BETWEEN_CREATURE_CHANGE_DIRECTION / 1.5)
				creature.camera.position.add(creature.camera.direction.tmp().mul(Gdx.graphics.getDeltaTime()));
			creature.camera.update();
		}
	}

	/**
	 * Calculate a new spawned creatures level based on distance to a retardant
	 * Courtesy wolfram alpha and a little randomless based on distance
	 * @param distance (note: not distance squared)
	 * @return level according to how far from nearest retardant exponentially
	 */
	private static int getCreatureLevelFromDistance(float distance) {
		int randomMod = CritterCaptors.random.nextInt((int)Math.sqrt(distance)),
			level = (int)(16.65f*Math.pow(Math.E, .000383f*distance))-12;
		return Math.max(1, level + randomMod);
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
	
	public Creature create(String name){
		for(AffinityEnum affinity : AffinityEnum.values())
			for(Creature template: creatureTemplates.get(affinity))
				if(template.getName().equals(name))
					return template.clone();
		return null;
	}
	
	@Test
	public void testGetCreatureLevelFromDistance(){
		int[] levelSet = new int[1000];
		for(int i=1; i<levelSet.length+1; i++)
		 levelSet[i-1] = getCreatureLevelFromDistance(i*5);
		assert(levelSet[0] >= 1);
	}
}

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.blastedstudios.crittercaptors;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderRegistry;
import com.badlogic.gdx.graphics.g3d.model.Model;
import com.blastedstudios.crittercaptors.creature.CreatureManager;
import com.blastedstudios.crittercaptors.ui.mainscreen.MainMenu;

public class CritterCaptors extends Game {
	public static Random random = new Random();
	private HashMap<String,Model> modelMap;
	private CreatureManager creatureManager;
	private WorldLocationManager worldLocationManager;
	private Character character;

	@Override public void create () {
		creatureManager = new CreatureManager();
		worldLocationManager = new WorldLocationManager();
		modelMap = new HashMap<String, Model>();
		modelMap.put("skydome", ModelLoaderRegistry.load(Gdx.files.internal("data/sky/skydome.obj")));
		for(String name : creatureManager.getCreatureTemplateNames())
			modelMap.put(name, ModelLoaderRegistry.load(Gdx.files.internal("data/models/static/" + name.toLowerCase() + ".obj")));
		setScreen(new MainMenu(this));
	}
	
	public Model getModel(String model){
		return modelMap.get(model);
	}
	
	public CreatureManager getCreatureManager(){
		return creatureManager;
	}
	
	public Character getCharacter(){
		return character;
	}
	
	public void setCharacter(Character character){
		this.character = character;
	}
	
	public WorldLocationManager getWorldLocationManager(){
		return worldLocationManager;
	}
}

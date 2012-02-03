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
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.loaders.ModelLoaderRegistry;
import com.badlogic.gdx.graphics.g3d.model.Model;
import com.blastedstudios.crittercaptors.character.Base;
import com.blastedstudios.crittercaptors.character.Character;
import com.blastedstudios.crittercaptors.creature.CreatureManager;
import com.blastedstudios.crittercaptors.ui.mainscreen.MainScreen;
import com.blastedstudios.crittercaptors.util.WorldLocationManager;

public class CritterCaptors extends Game {
	public static Random random = new Random();
	private static HashMap<String,Model> modelMap;
	private static HashMap<String,Texture> textureMap;
	private CreatureManager creatureManager;
	private WorldLocationManager worldLocationManager;
	private Character character;

	@Override public void create () {
		//com.badlogic.gdx.graphics.g3d.model.still.StillModel model = 
		//	ModelLoaderRegistry.loadStillModel(Gdx.files.internal("data/models/base.g3dt"));
		//com.badlogic.gdx.graphics.g3d.loaders.g3d.chunks.G3dExporter.export(model, Gdx.files.absolute("data/models/base.g3d"));
		
		creatureManager = new CreatureManager();
		worldLocationManager = new WorldLocationManager();
		textureMap = new HashMap<String, Texture>();
		textureMap.put("skydome", new Texture(Gdx.files.internal("data/sky/skydome.png"), Format.RGB565, true));
		textureMap.put("base", new Texture(Gdx.files.internal("data/textures/base.png"), Format.RGB565, true));
		modelMap = new HashMap<String, Model>();
		modelMap.put("skydome", ModelLoaderRegistry.load(Gdx.files.internal("data/sky/skydome.obj")));
		modelMap.put("base", ModelLoaderRegistry.load(Gdx.files.internal("data/models/base.g3d")));
		for(String name : creatureManager.getCreatureTemplateNames()){
			try{
				modelMap.put(name, ModelLoaderRegistry.load(Gdx.files.internal("data/models/static/" + name.toLowerCase() + ".obj")));
			}catch(Exception e){
				modelMap.put(name, ModelLoaderRegistry.load(Gdx.files.internal("data/models/" + name.toLowerCase() + ".g3d")));
			}
		}
		setScreen(new MainScreen(this));
	}
	
	public static Model getModel(String model){
		return modelMap.get(model);
	}
	
	public static Texture getTexture(String texture){
		return textureMap.get(texture);
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

	public void addBase() {
		character.getBases().add(new Base(worldLocationManager.getLatitude(), worldLocationManager.getLongitude()));
		character.addCash(-Base.BASE_COST);
	}
}

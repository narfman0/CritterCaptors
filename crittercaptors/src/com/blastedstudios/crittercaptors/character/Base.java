package com.blastedstudios.crittercaptors.character;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.ui.terrain.TerrainManager;
import com.blastedstudios.crittercaptors.util.MercatorUtil;
import com.blastedstudios.crittercaptors.util.RenderUtil;
import com.blastedstudios.crittercaptors.util.WorldLocationUtil;
import com.blastedstudios.crittercaptors.util.XMLUtil;

public class Base {
	public static final float BASE_DISTANCE = 100f;
	public static final int BASE_COST = 1500, RETARDANT_COST = 1000,
			VR_SIMULATOR_COST = 2000, VR_SIMULATOR_UPGRADE = 400;
	private static final String BASE_NAME = "base";
	private final double lat, lon;
	private Vector3 cachedPosition;//used to skip mercator proj every frame
	private final HashMap<BaseUpgradeEnum, Integer> upgrades;
	
	public Base(double lat, double lon, HashMap<BaseUpgradeEnum, Integer> upgrades){
		this.lat = lat;
		this.lon = lon;
		this.upgrades = upgrades;
	}
	
	public Vector3 getCachedPosition(){
		return cachedPosition;
	}
	
	public void render(TerrainManager terrainManager, WorldLocationUtil worldLocationUtil){
		if(cachedPosition == null){
			double[] mercator = MercatorUtil.toPixel(worldLocationUtil.lonInitial - lon, 
					worldLocationUtil.latInitial - lat);
			float x = (float)mercator[0], z = (float)mercator[1], y = terrainManager.getHeight(x, z);
			cachedPosition = new Vector3(x, y, z);
		}
		RenderUtil.drawModel(CritterCaptors.getModel(BASE_NAME), CritterCaptors.getTexture(BASE_NAME),
				cachedPosition, new Vector3(), new Vector3(1,1,1));
	}

	public static Base fromXML(Element baseElement) {
		HashMap<BaseUpgradeEnum, Integer> upgrades = new HashMap<BaseUpgradeEnum, Integer>();
		for(Element upgradeElement : XMLUtil.iterableElementList(baseElement.getElementsByTagName("upgrade"))){
			BaseUpgradeEnum key = BaseUpgradeEnum.valueOf(upgradeElement.getAttribute("name"));
			upgrades.put(key, Integer.parseInt(upgradeElement.getAttribute("value")));
		}
		return new Base(Double.parseDouble(baseElement.getAttribute("lat")), 
				Double.parseDouble(baseElement.getAttribute("lon")), upgrades);
	}
	
	public Element asXML(Document doc){
		Element baseEle = doc.createElement("base");
		baseEle.setAttribute("lat", Double.toString(lat));
		baseEle.setAttribute("lon", Double.toString(lon));
		for(BaseUpgradeEnum key : upgrades.keySet()){
			Element upgradeElement = doc.createElement("upgrade");
			upgradeElement.setAttribute("name", key.name());
			upgradeElement.setAttribute("value", upgrades.get(key).toString());
			baseEle.appendChild(upgradeElement);
		}
		return baseEle;
	}

	public void upgrade(BaseUpgradeEnum upgrade) {
		upgrades.put(upgrade, upgrades.containsKey(upgrade) ? upgrades.get(upgrade)+1 : 0);
	}

	public boolean hasUpgrade(BaseUpgradeEnum upgrade) {
		return upgrades.containsKey(upgrade);
	}

	public Integer getUpgrade(BaseUpgradeEnum upgrade) {
		return upgrades.get(upgrade);
	}

	public void setRetardantEnabled(boolean enabled){
		upgrades.put(BaseUpgradeEnum.MonsterRetardant, enabled ? 1 : 0);
	}

	public boolean isRetardantEnabled(){
		return hasUpgrade(BaseUpgradeEnum.MonsterRetardant) && 
			getUpgrade(BaseUpgradeEnum.MonsterRetardant) == 1;
	}

	public int getVRLevel(){
		return getUpgrade(BaseUpgradeEnum.VirtualReality);
	}
}
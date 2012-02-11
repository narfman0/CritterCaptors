package com.blastedstudios.crittercaptors.character;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.ui.terrain.TerrainManager;
import com.blastedstudios.crittercaptors.util.MercatorUtil;
import com.blastedstudios.crittercaptors.util.RenderUtil;
import com.blastedstudios.crittercaptors.util.WorldLocationUtil;

public class Base {
	public static final float BASE_DISTANCE = 100f;
	public static final int BASE_COST = 1500;
	private static final String BASE_NAME = "base";
	private final double lat, lon;
	private Vector3 cachedPosition;//used to skip mercator proj every frame
	
	public Base(double lat, double lon){
		this.lat = lat;
		this.lon = lon;
	}
	
	public Vector3 getCachedPosition(){
		return cachedPosition;
	}
	
	public void render(TerrainManager terrainManager, WorldLocationUtil worldLocationUtil){
		if(cachedPosition == null){
			double[] mercator = MercatorUtil.toPixel(worldLocationUtil.lonInitial - lon, worldLocationUtil.latInitial - lat);
			float x = (float)mercator[0], z = (float)mercator[1], y = terrainManager.getHeight(x, z);
			cachedPosition = new Vector3(x, y, z);
		}
		RenderUtil.drawModel(CritterCaptors.getModel(BASE_NAME), CritterCaptors.getTexture(BASE_NAME),
				cachedPosition, new Vector3(), new Vector3(1,1,1));
	}

	public static Base fromXML(Element baseElement) {
		return new Base(Double.parseDouble(baseElement.getAttribute("lat")), 
				Double.parseDouble(baseElement.getAttribute("lon")));
	}
	
	public Element asXML(Document doc){
		Element baseEle = doc.createElement("base");
		baseEle.setAttribute("lat", Double.toString(lat));
		baseEle.setAttribute("lon", Double.toString(lon));
		return baseEle;
	}
}

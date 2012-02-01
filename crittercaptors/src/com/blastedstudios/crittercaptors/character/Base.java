package com.blastedstudios.crittercaptors.character;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.badlogic.gdx.math.Vector3;
import com.blastedstudios.crittercaptors.CritterCaptors;
import com.blastedstudios.crittercaptors.ui.terrain.ITerrain;
import com.blastedstudios.crittercaptors.util.MercatorUtil;
import com.blastedstudios.crittercaptors.util.RenderUtil;

public class Base {
	public static final float BASE_DISTANCE = 1000f;
	public static final int BASE_COST = 1500;
	private final double lat, lon;
	private Vector3 cachedPosition;//used to skip mercator proj every frame
	
	public Base(double lat, double lon){
		this.lat = lat;
		this.lon = lon;
	}
	
	public Vector3 getCachedPosition(){
		return cachedPosition;
	}
	
	public void render(ITerrain terrain){
		if(cachedPosition == null){
			double[] mercator = MercatorUtil.toPixel(lon, lat);
			float x = (float)mercator[0], z = (float)mercator[1];
			//TODO use initial lat/lon
			cachedPosition = new Vector3(x, terrain.getHeight(x,z), z);
		}
		RenderUtil.drawModel(CritterCaptors.getModel("base"), cachedPosition, new Vector3(), new Vector3(1,1,1));
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

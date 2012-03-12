package com.blastedstudios.crittercaptors.creature;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;

public class AffinityCalculator {
	public static final Color SUBURBAN_COLOR = new Color(220,220,220, 0),
			OCEAN_COLOR = new Color(181,208,208, 0);
	
	/**
	 * @param affinities 
	 * @return hashmap containing percentages of affinities from the @param image.
	 * One may use this to derive the actual affinity, for instance, .85 suburban will
	 * be defined as suburban area and will spawn only creatures from that affinity.
	 */
	public static void getAffinitiesFromTexture(Color[] image, 
			HashMap<AffinityEnum, Float> affinities){
		affinities.clear();
		int imgWidth = (int) Math.sqrt(image.length);
		float pixelCount = imgWidth * imgWidth;
		HashMap<Color,Float> map = getColorMap(image, imgWidth);
		if(map.containsKey(SUBURBAN_COLOR))
			affinities.put(AffinityEnum.suburban, map.get(SUBURBAN_COLOR)/pixelCount);
		if(map.containsKey(OCEAN_COLOR))
			affinities.put(AffinityEnum.ocean, map.get(OCEAN_COLOR)/pixelCount);
	}
	
	/**
	 * @return HashMap of color -> how many pixels were in @param image
	 */
	private static HashMap<Color,Float> getColorMap(Color[] image, int imgWidth){
		HashMap<Color,Float> map = new HashMap<Color, Float>();
		for(int y=0; y<imgWidth; y++)
			for(int x=0; x<imgWidth; x++){
				Color color = image[x + y*imgWidth];
				if(map.containsKey(color))
					map.put(color, map.get(color)+1f);
				else
					map.put(color, 1f);
			}
		return map;
	}
}

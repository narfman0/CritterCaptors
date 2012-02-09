package com.blastedstudios.crittercaptors.creature;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class AffinityCalculator {
	private static final Color SUBURBAN_COLOR = new Color(220,220,220),
			OCEAN_COLOR = new Color(181,208,208);
	
	/**
	 * @param affinities 
	 * @return hashmap containing percentages of affinities from the @param image.
	 * One may use this to derive the actual affinity, for instance, .85 suburban will
	 * be defined as suburban area and will spawn only creatures from that affinity.
	 */
	public static void getAffinitiesFromTexture(BufferedImage image, 
			HashMap<AffinityEnum, Float> affinities){
		affinities.clear();
		float pixelCount = image.getWidth() * image.getHeight();
		HashMap<Color,Float> map = getColorMap(image);
		if(map.containsKey(SUBURBAN_COLOR))
			affinities.put(AffinityEnum.suburban, map.get(SUBURBAN_COLOR)/pixelCount);
		if(map.containsKey(OCEAN_COLOR))
			affinities.put(AffinityEnum.ocean, map.get(OCEAN_COLOR)/pixelCount);
	}
	
	/**
	 * @return HashMap of color -> how many pixels were in @param image
	 */
	private static HashMap<Color,Float> getColorMap(BufferedImage image){
		HashMap<Color,Float> map = new HashMap<Color, Float>();
		for(int x=0; x<image.getWidth(); x++)
			for(int y=0; y<image.getHeight(); y++){
				Color color = new Color(image.getRGB(x, y));
				if(map.containsKey(color))
					map.put(color, map.get(color)+1f);
				else
					map.put(color, 1f);
			}
		return map;
	}
}

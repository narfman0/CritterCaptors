package com.blastedstudios.crittercaptors.creature;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;

public class AffinityCalculator {
	private static final Color SUBURBAN_COLOR = new Color(220,220,220);
	
	public static HashMap<AffinityEnum,Float> getAffinitiesFromTexture(BufferedImage image){
		float pixelCount = image.getWidth() * image.getHeight();
		HashMap<Color,Float> map = getColorMap(image);
		HashMap<AffinityEnum,Float> affinities = new HashMap<AffinityEnum,Float>();
		if(map.containsKey(SUBURBAN_COLOR))
			affinities.put(AffinityEnum.suburban, map.get(SUBURBAN_COLOR)/pixelCount);
		return affinities;
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

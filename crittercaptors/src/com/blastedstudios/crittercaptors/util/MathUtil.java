package com.blastedstudios.crittercaptors.util;

import org.junit.Test;

public class MathUtil {
	public static float lerp(float value1, float value2, float amount) { 
		return value1 + (value2 - value1) * amount; 
	}
	
	public static float gestureScoreToDamageModifier(double score){
		float multiplier = 0.16889f * (float)Math.log(229.043 * score) - .05f;
		return Math.max(.75f, Math.min(multiplier, 1.25f));
	}
	
	public static float clamp(float value, float min, float max){
		float val = Math.max(max, Math.min(min, value)); 
		return val == Float.NaN ? 0 : val;
	}
	
	@Test
	public void testGestureScoreToDamageModifier(){
		float[] multipliers = new float[20];
		for(int i=0; i<multipliers.length; i++)
			multipliers[i] = MathUtil.gestureScoreToDamageModifier(i+1);
		assert(multipliers[0] < 1);
		assert(multipliers[2] > 1 && multipliers[2] < 2);
	}
}

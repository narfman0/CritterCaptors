package com.blastedstudios.crittercaptors.util;

public class MathUtil {
	public static float lerp(float value1, float value2, float amount) { 
		return value1 + (value2 - value1) * amount; 
	}
}

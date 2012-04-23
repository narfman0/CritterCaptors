package com.blastedstudios.crittercaptors.creature;

public enum AffinityEnum {
	physical("grass"), education("grass"), suburban("gravel"), urban("gravel"),
	rural("grass"), coastal("grass"), ocean("grass"), restricted("grass"),
	park("grass"), road("grass");
	
	public final String texture;
	
	private AffinityEnum(String texture){
		this.texture = texture;
	}
}

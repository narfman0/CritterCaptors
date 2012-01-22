package com.blastedstudios.crittercaptors.creature;

import java.util.List;

public class AffinityDamage {
	static float[][] damageTable = new float[][]{
	//attacker ↓ defender→	physical	education,	suburban,	urban,	rural,	coastal,ocean,	restricted,	park,	road
			/*physical*/{	1,			1,			1,			1,		1,		1,		1,		1,			1,		1},
			/*education*/{	1,			1.5f,		1,			1,		2,		1,		1,		2,			.5f,	.5f},
			/*suburban*/{	1,			1,			1.5f,		1,		1,		1,		.5f,	2,			1,		1},
			/*urban*/{		1,			1,			2,			1.5f,	2,		1,		1,		.5f,		.5f,	1},
			/*rural*/{		1,			.5f,		2,			.5f,	1.5f,	1,		1,		2,			1,		1},
			/*coastal*/{	1,			1,			1,			2,		1,		1.5f,	1,		1,			1,		1},
			/*ocean*/{		1,			1,			.5f,		1,		1,		1,		1.5f,	2,			1,		1},
			/*restricted*/{	1,			2,			.5f,		2,		.5f,	1,		.5f,	1.5f,		1,		2},
			/*park*/{		1,			2,			1,			2,		1,		1,		.5f,	1,			1.5f,	.5f},
			/*road*/{		1,			.5f,		1,			1,		1,		1,		1,		1,			2,		1.5f}	
	};

	public static float getDamageMultiplier(AffinityEnum attacker, List<AffinityEnum> defender){
		float multiplier = 1f;
		for(AffinityEnum defenderEnum : defender)
			multiplier *= damageTable[attacker.ordinal()][defenderEnum.ordinal()];
		return multiplier;
	}

	public static float getDamageMultiplier(List<AffinityEnum> attacker, List<AffinityEnum> defender) {
		float multiplier = 1f;
		for(AffinityEnum attackerEnum : attacker)
			multiplier *= getDamageMultiplier(attackerEnum, defender);
		return multiplier;
	}
}

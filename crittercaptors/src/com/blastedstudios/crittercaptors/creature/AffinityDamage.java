package com.blastedstudios.crittercaptors.creature;

import java.util.List;

public class AffinityDamage {
	float[][] damageTable = new float[][]{
	//attacker ↓ defender→	education,	suburban,	urban,	rural,	coastal,ocean,	restricted,	park,	road
			/*education*/{	1.5f,		1,			1,		2,		1,		1,		2,			.5f,	.5f},
			/*suburban*/{	1,			1.5f,		1,		1,		1,		.5f,	2,			1,		1},
			/*urban*/{		1,			2,			1.5f,	2,		1,		1,		.5f,		.5f,	1},
			/*rural*/{		.5f,		2,			.5f,	1.5f,	1,		1,		2,			1,		1},
			/*coastal*/{	1,			1,			2,		1,		1.5f,	1,		1,			1,		1},
			/*ocean*/{		1,			.5f,		1,		1,		1,		1.5f,	2,			1,		1},
			/*restricted*/{	2,			.5f,		2,		.5f,	1,		.5f,	1.5f,		1,		2},
			/*park*/{		2,			1,			2,		1,		1,		.5f,	1,			1.5f,	.5f},
			/*road*/{		.5f,		1,			1,		1,		1,		1,		1,			2,		1.5f}	
	};

	public float getDamageMultiplier(List<AffinityEnum> attacker, List<AffinityEnum> defender){
		float multiplier = 1f;
		for(AffinityEnum attackerEnum : attacker)
			for(AffinityEnum defenderEnum : defender)
				multiplier *= damageTable[attackerEnum.ordinal()][defenderEnum.ordinal()];
		return multiplier;
	}
}

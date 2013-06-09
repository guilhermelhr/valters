package com.sedentarios.valters.maps;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.sedentarios.valters.characters.ValterChar;
import com.sedentarios.valters.objects.ObjectCoin;
import com.sedentarios.valters.objects.ObjectGround;

public class PlatformerMap extends ValtersMap {

	public PlatformerMap() {
		super(64, 6000);
	}

	@Override
	public void create() {
		assetManager.load("assets/Sounds/smw_coin.wav", Sound.class);
		assetManager.load("assets/Anim/moeda/moeda.txt" ,TextureAtlas.class);
	}

	@Override
	public void createObjects() {

		addObject(new ObjectGround(62, 6000));

		for(int i = 0; i < 10; i++){
			addObject(new ObjectCoin(500 + i * 200, 260));
		}

		ValterChar valter;
		addObject(valter = new ValterChar(64, 120, 60, 720, 0.8f){
			@Override
			public Rectangle getCollider(){
				return new Rectangle(position.x, position.y, 128, 290);
			}
		});
		valter.setAllowYMovement(false);
		valter.setGravity(-15f);
	}
}

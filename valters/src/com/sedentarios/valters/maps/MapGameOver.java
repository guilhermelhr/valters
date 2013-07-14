package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;

public class MapGameOver extends ValtersMap {

	private Class<? extends ValtersMap> nextMap;
	private Sound sound;
	
	public MapGameOver(Class<? extends ValtersMap> nextMap) {
		super(0, 0);
		this.nextMap = nextMap;
	}

	@Override
	public void create() {
		assetManager.load("assets/sounds/smw_game_over.mp3", Sound.class);
	}
	
	@Override
	public void createObjects(){
		sound = assetManager.get("assets/sounds/smw_game_over.mp3", Sound.class);
		sound.play(ValtersOptions.SOUND_LEVEL);
	}
	
	private float exposure = 0f;
	@Override
	protected void preMapRender(OrthographicCamera camera, SpriteBatch batch){
		exposure += Gdx.graphics.getDeltaTime();
		ValtersGame.setCamPosition(0, 0);
		if(nextMap.getSimpleName().equals("MapPrisao")){
			ValtersGame.font.draw(batch, "BUSTED", -ValtersGame.font.getBounds("BUSTED").width / 2, 0);
		}else{
			ValtersGame.font.draw(batch, "GAME OVER", -ValtersGame.font.getBounds("GAME OVER").width / 2, 0);
		}
		
		if(exposure > 5f){
			ValtersGame.changeMap(nextMap);
		}
	}

}

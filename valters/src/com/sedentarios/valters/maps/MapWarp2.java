package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;

public class MapWarp2 extends ValtersMap{

	private Array<Texture> frames;
	
	private float fps = 1f/24f;
	
	private float frameExposure = 0f;
	private byte currFrame = 0;
	
	private Sound portalSound;
	
	private boolean finished = false;
	
	public MapWarp2(int leftCap, int rightCap) {
		super(leftCap, rightCap);
	}

	public MapWarp2(){
		super(0, 0);
	}
	
	@Override
	public void create() {
		assetManager.load("assets/sounds/portal2.wav", Sound.class);
		for(int i = 1; i <= 30; i++){
			assetManager.load("assets/render2/00" + i + ".png", Texture.class);
		}
	}
	
	@Override
	public void createObjects(){
		portalSound = assetManager.get("assets/sounds/portal2.wav", Sound.class);
		
		frames = new Array<Texture>();
		for(int i = 1; i <= 30; i++){
			frames.add(assetManager.get("assets/render2/00" + i + ".png", Texture.class));
		}
		
		portalSound.play(ValtersOptions.SOUND_LEVEL);
	}
	
	
	@Override
	public void render(OrthographicCamera camera){
		if(finished) return;
		frameExposure += Gdx.graphics.getDeltaTime();
		if(frameExposure >= fps){
			currFrame++;
			frameExposure = 0f;
			if(currFrame > frames.size - 1){
				finished = true;
				ValtersGame.changeMap(MapCreditos.class);
			}
		}
		super.render(camera);
		ValtersGame.setCamPosition(0, 0);
	}
	
	@Override
	public void inBatchRender(OrthographicCamera camera, SpriteBatch batch) {
		super.inBatchRender(camera, batch);
		if(!finished)
			batch.draw(frames.get(currFrame), -Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2,
									  Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

}

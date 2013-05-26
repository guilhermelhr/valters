package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MapWarp extends ValtersMap{
	
	private Texture[] frames;
	
	private float fps = 1f/24f;
	
	private float frameExposure = 0f;
	private int currFrame = 0;
	
	private Sound portal;
	
	private boolean finished = false;
	
	@Override
	public void create() {
		frames = new Texture[61];
		for(int i = 0; i < frames.length; i++){
			frames[i] = new Texture("data/render/0" + (175 + i) + ".png");
		}
		portal = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/Portal2.wav"));
		portal.play();
	}
	
	public void dispose() {
		for(int i = 0; i < frames.length; i++){
			frames[i].dispose();
		}
	}
	
	@Override
	public void render(OrthographicCamera camera){
		super.render(camera);
		if(!finished){
			camera.position.x = 0;
			camera.position.y = 0;
			frameExposure += Gdx.graphics.getDeltaTime();
			if(frameExposure >= fps){
				if(currFrame < frames.length - 1){
					currFrame++;
					frameExposure = 0f;
				}else{
					dispose();
					finished = true;
				}
			}
		}
	}
	
	@Override
	public void inBatchRender(SpriteBatch batch) {
		if(!finished)
			batch.draw(frames[currFrame], -Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2,
									  Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

}

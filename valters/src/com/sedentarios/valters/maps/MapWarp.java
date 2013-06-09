package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sedentarios.valters.ValtersGame;

public class MapWarp extends ValtersMap{

	private Texture[] frames;
	
	private float fps = 1f/24f;
	
	private float frameExposure = 0f;
	private byte currFrame = 0;
	
	private Sound portalSound;
	private Sound portaSound;
	
	private boolean finished = false;
	
	private byte stage = 0;
	
	public MapWarp(int leftCap, int rightCap) {
		super(leftCap, rightCap);
	}

	public MapWarp(){
		super(0, 0);
	}
	
	@Override
	public void create() {
		portaSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/porta.wav"));
	}
	
	@Override
	public void createObjects(){
		
	}
	
	private byte spritesLoaded = 0;
	private boolean portalLoaded = false;
	private void loadPortal(){
		if(spritesLoaded == 0){
			frames = new Texture[61];
			portalSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/Portal2.wav"));
		}
		if(spritesLoaded >= frames.length){
			portalLoaded = true;
		}else{
			frames[spritesLoaded] = new Texture("assets/render/0" + (175 + spritesLoaded) + ".png");
			spritesLoaded++;
		}
	}
	
	public void dispose() {
		super.dispose();
		for (Texture frame : frames) {
			frame.dispose();
		}
		portalSound.dispose();
		portaSound.dispose();
	}
	
	private boolean abrePortaPlayed = false;
	@Override
	public void render(OrthographicCamera camera){
		super.render(camera);
		
		if(!finished){
			if(stage == 0){
				if(!portalLoaded) loadPortal();
				if(!abrePortaPlayed){
					abrePortaPlayed = true;
					portaSound.play();
				}else if(getRuntime() >= 3f && portalLoaded){
					stage++;
					portalSound.play();
				}
			}else{
				camera.position.x = 0;
				camera.position.y = 0;
				frameExposure += Gdx.graphics.getDeltaTime();
				if(frameExposure >= fps){
					if(currFrame < frames.length - 1){
						currFrame++;
						frameExposure = 0f;
					}else{
						if(stage == 1){
							Gdx.gl.glClearColor(0, 0, 0, 1);
							Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
							stage++;
						}else{
							finished = true;
							ValtersGame.changeMap(MapRuaNY.class);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void inBatchRender(OrthographicCamera camera, SpriteBatch batch) {
		if(!finished && stage == 1)
			batch.draw(frames[currFrame], -Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2,
									  Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

}

package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.ValtersTexts;
import com.sedentarios.valters.objects.TextBalloon;

public class MapWarp extends ValtersMap{

	private Texture[] frames;
	
	private float fps = 1f/24f;
	
	private float frameExposure = 0f;
	private byte currFrame = 0;
	
	private Sound portalSound;
	private Sound portaSound;
	
	private boolean finished = false;
	
	private byte stage = -2;
	
	public MapWarp(int leftCap, int rightCap) {
		super(leftCap, rightCap);
	}

	public MapWarp(){
		super(0, 0);
	}
	
	@Override
	public void create() {
		portaSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/porta.wav"));
	}
	
	@Override
	public void createObjects(){
		
	}
	
	private byte spritesLoaded = 0;
	private boolean portalLoaded = false;
	private void loadPortal(){
		if(spritesLoaded == 0){
			frames = new Texture[61];
			portalSound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/portal2.wav"));
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
		if(frames != null){
			for (Texture frame : frames) {
				if(frame != null) frame.dispose();
			}
		}
		if(portalSound != null) portalSound.dispose();
		if(portaSound != null) portaSound.dispose();
	}
	
	private boolean abrePortaPlayed = false;
	private TextBalloon balloon;
	@Override
	public void render(OrthographicCamera camera){
		super.render(camera);
		ValtersGame.setCamPosition(0, 0);
		
		if(!finished){
			if(stage == -2){
				addObject(balloon = new TextBalloon(null, ValtersTexts.get("Valter2"), 0, 0));
				balloon.setPosition(0, -400);
				stage++;
			}else if(stage == -1){
				if(balloon.isWaitingRemoval()){
					stage++;
				}
			}else if(stage == 0){
				if(!portalLoaded) loadPortal();
				if(!abrePortaPlayed){
					abrePortaPlayed = true;
					portaSound.play(ValtersOptions.SOUND_LEVEL);
				}else if(getRuntime() >= 3f && portalLoaded){
					stage++;
					portalSound.play(ValtersOptions.SOUND_LEVEL);
				}
			}else{
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
		super.inBatchRender(camera, batch);
		if(!finished && stage == 1)
			batch.draw(frames[currFrame], -Gdx.graphics.getWidth() / 2, -Gdx.graphics.getHeight() / 2,
									  Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

}

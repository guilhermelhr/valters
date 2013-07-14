package com.sedentarios.valters.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sedentarios.valters.objects.ValtersObject;

public class PoliceChar extends ValtersObject {

	public static final int IDLE_L = 0;
	public static final int IDLE_R = 1;
	public static final int RUNNING = 2;
	public static final int MOVING_R = 3;
	public static final int MOVING_L = 4;
	private int state = IDLE_L;
	public void setState(int state) {
		this.state = state;
	}

	private TextureRegion idle;
	private Texture shadow;
	private Animation run;
	private Animation walk;
	private float scale;
	
	public PoliceChar(float x, float y, byte layer,float scale) {
		super("Police", x, y, layer, false, false);
		this.scale = scale;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/anim/fbi_parado.png", Texture.class);
		assetManager.load("assets/anim/fbi_correr/correr.txt", TextureAtlas.class);
		assetManager.load("assets/anim/fbi_andar/andar.txt", TextureAtlas.class);
	}
	
	@Override
	public void create(){
		shadow = new Texture("assets/anim/sombra.png");
		idle = new TextureRegion(getMap().assetManager.get("assets/anim/fbi_parado.png", Texture.class));
		idle.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		TextureAtlas atlas = getMap().assetManager.get("assets/anim/fbi_correr/correr.txt", TextureAtlas.class);
		for(Texture t : atlas.getTextures()){
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		run = new Animation(1f/20f, atlas.getRegions());
		
		atlas = getMap().assetManager.get("assets/anim/fbi_andar/andar.txt", TextureAtlas.class);
		for(Texture t : atlas.getTextures()){
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		walk = new Animation(1f/10f, atlas.getRegions());
	}
	
	private Vector2 moveReq = new Vector2();
	private Vector2 runReq = new Vector2();
	
	@Override
	public boolean move(float x, float y){
		moveReq.set(x, y);
		return true;
	}
	
	@Override
	public boolean run(float x, float y){
		runReq.set(x, y);
		return true;
	}

	private float stateTime = 0f;
	@Override
	public void render(SpriteBatch batch) {
		if(runReq.len2() == 0 && moveReq.len2() == 0){
			if(state != IDLE_L && state != IDLE_R){
				if(state == RUNNING || state == MOVING_L){
					state = IDLE_L;
				}else{
					state = IDLE_R;
				}
			}
		}else{
			if(moveReq.len2() > 0) {
				state = moveReq.x - getPosition().x > 0?MOVING_R:MOVING_L;
				super.move(moveReq.x - getPosition().x, moveReq.y - getPosition().y);
				moveReq.set(0, 0);
			} 
			
			if(runReq.len2() > 0) {
				state = RUNNING;
				super.move(runReq.x - getPosition().x, runReq.y - getPosition().y);
				runReq.set(0, 0);
			}
		}
		
		batch.draw(shadow, position.x, position.y - 15, shadow.getWidth() * scale * 0.95f, shadow.getHeight() * scale * 0.95f);
		
		TextureRegion sprite = null;
		switch(state){
			case IDLE_L:
			case IDLE_R:
				sprite = idle;
				stateTime = 0f;
				break;
			case MOVING_L:
			case MOVING_R:
				stateTime += Gdx.graphics.getDeltaTime();
				sprite = walk.getKeyFrame(stateTime, true);
				break;
			case RUNNING:
				stateTime += Gdx.graphics.getDeltaTime();
				sprite = run.getKeyFrame(stateTime, true);
				break;
		}
		if(sprite != null){
			batch.draw(sprite, getPosition().x - (state == MOVING_R?180:0) - (state == IDLE_L?-168:-135) - (state == IDLE_R?128:0), 
					getPosition().y, -sprite.getRegionWidth() * scale * (state == MOVING_R || state == IDLE_R?-1:1), sprite.getRegionHeight() * scale);
		}
	}

}

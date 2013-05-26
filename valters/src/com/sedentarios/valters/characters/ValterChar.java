package com.sedentarios.valters.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.objects.ValtersObject;

public class ValterChar extends ValtersObject{

	Texture shadow;
	TextureAtlas walkAtlas;
	TextureAtlas runAtlas;
	Texture idleTexture;
	Animation walkAnim;
	Animation runAnim;
	Array<TextureRegion> walkTextures;
	Array<TextureRegion> runTextures;
	float animTime = 0f;
	
	final int IDLE_R = 0;
	final int IDLE_L = 1;
	final int WALKING_R = 2;
	final int WALKING_L = 3;
	final int RUNNING_R = 4;
	final int RUNNING_L = 5;
	int state = 0;
	
	Vector2 direction;
	Vector2 speed;
	
	public ValterChar(float x, float y) {
		super("valter", x, y);
	}
	
	public void create() {
		shadow = new Texture("data/Anim/sombra.png");
		
		idleTexture = new Texture("data/Anim/parado.png");
		idleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		walkAtlas = new TextureAtlas("data/Anim/andar/andar_pack.txt");		
		walkTextures = new Array<TextureRegion>();
		
		runAtlas = new TextureAtlas("data/Anim/correr/correr_pack.txt");		
		runTextures = new Array<TextureRegion>();
		
		for(int i = 6; i <= 25; i++) {
			TextureRegion walkRegion = walkAtlas.findRegion(i >= 10?("andar00" + i):("andar000" + i));
			walkRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			walkTextures.add(walkRegion);
		}
		
		for(int i = 12; i <= 28; i++){
			TextureRegion runRegion = runAtlas.findRegion("correr00" + i);
			runRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			runTextures.add(runRegion);
		}
		
		walkAnim = new Animation(0.06f, walkTextures, Animation.LOOP);
		runAnim = new Animation(0.06f, runTextures, Animation.LOOP);
		
		direction = new Vector2(Vector2.Zero);
		speed = new Vector2(200f, 100f);
	}
	
	
	public void render(SpriteBatch batch) {		
		direction.set(Vector2.Zero);
		if(Gdx.input.isKeyPressed(Keys.A) || ValtersGame.controller.getAxis(1) == -1f) {
			direction.add(-1 * Gdx.graphics.getDeltaTime(), 0);
		}
		if(Gdx.input.isKeyPressed(Keys.D) || ValtersGame.controller.getAxis(1) == 1f){
			direction.add(1 * Gdx.graphics.getDeltaTime(), 0);
		}
		
		if(Gdx.input.isKeyPressed(Keys.W) || ValtersGame.controller.getAxis(0) == -1f) {
			direction.add(0, 1 * Gdx.graphics.getDeltaTime());
		}
		if(Gdx.input.isKeyPressed(Keys.S) || ValtersGame.controller.getAxis(0) == 1f) {
			direction.add(0, -1 * Gdx.graphics.getDeltaTime());
		}
		
		boolean run = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || ValtersGame.controller.getButton(1);
		
		position.add(direction.x * speed.x * (run?2f:1), direction.y * speed.y);
		if(position.x < ValtersGame.map.getLeftCap() || position.x > ValtersGame.map.getRightCap()) {
			state = position.x <= ValtersGame.map.getLeftCap()?IDLE_L:IDLE_R;
			position.x = position.x <= ValtersGame.map.getLeftCap()? ValtersGame.map.getLeftCap() : ValtersGame.map.getRightCap();
			direction.set(Vector2.Zero);
		}
		
		if(position.y < 10 || position.y > 140) {
			position.y = position.y < 15?10:140;
		}
		
		if(direction.len() == 0 && !(state == IDLE_R || state == IDLE_L)){
			state = ((state == WALKING_L) || (state == RUNNING_L)? IDLE_L:IDLE_R);
		}else if(direction.len() > 0){
			if(direction.y != 0 && direction.x == 0) {
				if((state == IDLE_L || state == IDLE_R)) {
					if(!run) {
						state = (state == IDLE_L)?WALKING_L:WALKING_R;
					}else {
						state = (state == IDLE_L)?RUNNING_L:RUNNING_R;
					}
				}
			}else if(direction.x > 0) {
				state = run? RUNNING_R : WALKING_R;
			}else {
				state = run? RUNNING_L : WALKING_L;
			}
		}
		
		batch.draw(shadow, position.x - (shadow.getWidth() / 2), position.y - 18);
		
		if(state != IDLE_L && state != IDLE_R) {
			animTime += Gdx.graphics.getDeltaTime();
		}else {			
			if(state == IDLE_R) {
				batch.draw(idleTexture, (int) (position.x - (idleTexture.getWidth() / 2) + 24), position.y - 8);
			}else {
				batch.draw(idleTexture, (int) (position.x + (idleTexture.getWidth() / 2) - 24), position.y - 8, -idleTexture.getWidth(), idleTexture.getHeight());
			}
		}
		
		if(state == WALKING_R) {
			TextureRegion tr = walkAnim.getKeyFrame(animTime, true);
			batch.draw(tr, (int) (position.x - (tr.getRegionWidth() / 2)), position.y);
		}else if(state == WALKING_L) {
			TextureRegion tr = walkAnim.getKeyFrame(animTime, true);
			batch.draw(tr, (int) (position.x + (tr.getRegionWidth() / 2)), position.y, -tr.getRegionWidth(), tr.getRegionHeight());
		}else if(state == RUNNING_R) {
			TextureRegion tr = runAnim.getKeyFrame(animTime, true);
			batch.draw(tr, (int) (position.x - (tr.getRegionWidth() / 2)), position.y);
		}else if(state == RUNNING_L) {
			TextureRegion tr = runAnim.getKeyFrame(animTime, true);
			batch.draw(tr, (int) (position.x + (tr.getRegionWidth() / 2)), position.y, -tr.getRegionWidth(), tr.getRegionHeight());
		}
	}
	
	public void dispose() {
		idleTexture.dispose();
		for(TextureRegion tr : runTextures) {
			tr.getTexture().dispose();
		}
		for(TextureRegion tr : walkTextures) {
			tr.getTexture().dispose();
		}
	}

}

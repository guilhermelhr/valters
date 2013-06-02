package com.sedentarios.valters.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sedentarios.valters.ValtersGame;

public class ObjectCar extends ValtersObject {

	private TextureRegion[] frames;
	private TextureAtlas carAtlas;
	private float fps = 1f/24f;
	private float exposure = 0f;
	private byte currFrame = 0;
	
	private float speed = 600f;
	
	private Sound horn;
	private Sound car;
	
	public ObjectCar() {
		super("car", 1350 + 64, 10, (byte) 2);
	}

	@Override
	public void create() {
		carAtlas = new TextureAtlas("assets/Anim/carro/carro.txt");
	
		frames = new TextureRegion[24];
		
		for(int i = 1; i <= 24; i++) {
			TextureRegion carFrame = carAtlas.findRegion(i >= 10?("carro00" + i):("carro000" + i));
			carFrame.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			frames[i-1] = carFrame;
		}
		
		horn = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/busina.mp3"));
		car = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/car_driving_by.wav"));
	}

	@Override
	public void dispose() {
		for(int i = 0; i < frames.length; i++){
			frames[i] = null;
		}
		carAtlas.dispose();
	}

	private boolean playedHorn = false;
	private boolean playedCarSound = false;
	//private byte passes = 0;
	private float[] color = {1f, 1f, 1f};
	@Override
	public void render(SpriteBatch batch) {
		if(position.x <= -frames[currFrame].getRegionWidth()){
			//passes++;
			position.x = 9000;
			//if(passes > 2){
			//	requestDeath();
			//}
			playedCarSound = false;
			float rand = 0.5f + (float) Math.random() * 0.5f;
			color[0] = rand;
			color[1] = rand;
			color[2] = rand;
			System.out.println(color[0]);
		}
		
		
		if(timeLeftToPlayer() <= 1.5f && !playedCarSound){
			car.play(0.8f);
			playedCarSound = true;
		}
		
		if(ValtersGame.valter.getCenteredPosition().dst(position) <= 350 && !playedHorn){
			horn.play(1f);
			playedHorn = true;
		}
		
		float delta = Gdx.graphics.getDeltaTime();
		position.x += -speed * delta;		
		exposure += delta;
		if(exposure >= fps){
			exposure = 0;
			currFrame++;
			if(currFrame > frames.length - 1){
				currFrame = 0;
			}
		}
		
		batch.setColor(color[0], color[1], color[2], 1f);
		batch.draw(frames[currFrame], getPosition().x, getPosition().y);
		batch.setColor(Color.WHITE);
	}
	
	private float timeLeftToPlayer(){
		float dist = ValtersGame.valter.getCenteredPosition().dst(position);
		return dist / speed;
	}

}

package com.sedentarios.valters.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class ObjectTelevision extends ValtersObject{

	private Animation screen;
	private int type = -1;
	private float scale;
	
	public ObjectTelevision(float x, float y, float scale, int type){
		this(x, y, scale);
		this.type = type;
	}
	
	public ObjectTelevision(float x, float y, float scale) {
		super("Television", x, y, (byte) 0);
		this.scale = scale;
	}

	public static void loadResources(AssetManager assetManager){
		for(int i = 1; i <= 99; i++){
			assetManager.load("assets/objs/tv/00" + i + ".png", Texture.class);
		}
		
		for(int i = 280; i <= 380; i++){
			assetManager.load("assets/objs/tv2/0" + i + ".png", Texture.class);
		}
		for(int i = 0; i <= 130; i++){
			assetManager.load("assets/objs/tv_valter/" + i + ".png", Texture.class);
		}
	}
	
	@Override
	public void create(){
		screen = null;
		if(type == -1) type = (MathUtils.random() > 0.5f?0:1);
		Array<TextureRegion> frames = new Array<TextureRegion>();
		if(type == 0) {
			for(int i = 1; i <= 99; i++){
				frames.add(new TextureRegion(getMap().assetManager.get("assets/objs/tv/00" + i + ".png", Texture.class)));
			}
		} else if(type == 1) {
			for(int i = 280; i <= 380; i++){
				frames.add(new TextureRegion(getMap().assetManager.get("assets/objs/tv2/0" + i + ".png", Texture.class)));
			}
		} else {
			for(int i = 0; i <= 130; i++){
				frames.add(new TextureRegion(getMap().assetManager.get("assets/objs/tv_valter/" + i + ".png", Texture.class)));
			}
			
		}
		screen = new Animation(1f/20f, frames);
		if(type >= 2){
			screen.setPlayMode(Animation.LOOP);
		}else{
			screen.setPlayMode(Animation.LOOP_PINGPONG);
		}
		super.create();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion frame = screen.getKeyFrame(getMap().getRuntime(), true);
		batch.draw(frame, getPosition().x, getPosition().y, frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
	}

}

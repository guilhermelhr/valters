package com.sedentarios.valters.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.maps.MapEsgoto;
import com.sedentarios.valters.maps.MapGameOver;

public class ObjectBueiro extends ValtersObject {

	private float scale;
	private Array<AtlasRegion> frames;
	private int currFrame;
	private static final float fps = 1f/10f;
	private float exposure = 0f;
	
	public ObjectBueiro(float x, float y, float scale, int startFrame) {
		super("Bueiro", x, y, (byte) 0, true, true);
		this.scale = scale;
		this.currFrame = startFrame;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/anim/bueiro/bueiro.txt", TextureAtlas.class);
	}
	
	@Override
	public void create(){
		frames = getMap().assetManager.get("assets/anim/bueiro/bueiro.txt", TextureAtlas.class).getRegions();
		if(frames.size == 7){
			AtlasRegion[] ar = new AtlasRegion[]{frames.get(frames.size - 1), frames.get(frames.size - 2)};
			frames.addAll(ar);
			frames.addAll(ar);
		}
		super.create();
	}
	
	@Override
	public void onCollision(CollisionComponent other) {
		if(currFrame > 1){
			if(other.getOwner().getName().equals("valter")){
				ValtersGame.changeMap2(new MapGameOver(MapEsgoto.class));
			}
		}
	}

	private boolean pingpong = false;
	@Override
	public void render(SpriteBatch batch) {
		exposure += Gdx.graphics.getDeltaTime();
		if(exposure >= (currFrame==0?fps*15:fps)){
			exposure = 0f;
			if(pingpong)currFrame++;
			if(!pingpong)currFrame--;
			if(currFrame < 0){
				pingpong = !pingpong;
				currFrame = 0;
			}
			if(currFrame > frames.size - 1){
				pingpong = !pingpong;
				currFrame = frames.size - 1;
			}
		}
		AtlasRegion reg = frames.get(currFrame);
		batch.draw(reg, getPosition().x, getPosition().y, reg.getRegionWidth() * scale, reg.getRegionHeight() * scale);
	}
	
	@Override
	public Rectangle getCollider(){
		return new Rectangle(getPosition().x + frames.get(currFrame).getRegionWidth() * scale * 0.25f, getPosition().y, frames.get(currFrame).getRegionWidth() * scale * 0.5f, 
															   frames.get(currFrame).getRegionHeight() * scale);
	}

}

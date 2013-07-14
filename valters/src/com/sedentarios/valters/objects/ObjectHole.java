package com.sedentarios.valters.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.maps.MapGameOver;

public class ObjectHole extends ValtersObject {

	private Texture sprite;
	private float scale;
	
	public ObjectHole(float x, float y, float scale) {
		super("Hole", x, y, (byte) 0, true, true);
		this.scale = scale;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/objs/buraco.png", Texture.class);
	}
	
	@Override
	public void create(){
		sprite = getMap().assetManager.get("assets/objs/buraco.png", Texture.class);
		super.create();
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(sprite, getPosition().x, getPosition().y, sprite.getWidth() * scale, sprite.getHeight() * scale);
	}
	
	@Override
	public void onCollision(CollisionComponent other){
		if(other.getOwner().getName().equals("valter")){
			ValtersGame.changeMap2(new MapGameOver(getMap().getClass()));
		}
	}
	
	@Override
	public Rectangle getCollider(){
		return new Rectangle(getPosition().x, getPosition().y, sprite.getWidth() * scale, sprite.getHeight() * scale);
	}
}

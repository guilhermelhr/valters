package com.sedentarios.valters.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class ObjectCerca extends ValtersObject {

	private Texture sprite;
	private float scale;
	
	public ObjectCerca(float x, float y, float scale) {
		super("Cerca", x, y, (byte) 2, true, false);
		this.scale = scale;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/objs/cerca.png", Texture.class);
	}

	@Override
	public void create(){
		sprite = getMap().assetManager.get("assets/objs/cerca.png", Texture.class);
		super.create();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(sprite, getPosition().x, getPosition().y, sprite.getWidth() * scale, sprite.getHeight() * scale * 1.25f);
	}
	
	@Override
	public Rectangle getCollider(){
		return new Rectangle(position.x, position.y, sprite.getWidth(), sprite.getHeight() * scale * 1.25f);
	}

}

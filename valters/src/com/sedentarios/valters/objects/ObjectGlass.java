package com.sedentarios.valters.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ObjectGlass extends ValtersObject{

	private Texture glass;
	private float scale;
	
	public ObjectGlass(float x, float y, float scale) {
		super("Glass", x, y, (byte) 0);
		this.scale = scale;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/texturas/loja_tv_vidro.png", Texture.class);
	}
	
	@Override
	public void create(){
		glass = getMap().assetManager.get("assets/texturas/loja_tv_vidro.png", Texture.class);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(glass, getPosition().x, getPosition().y, glass.getWidth() * scale, glass.getHeight() * scale);		
	}
	
}

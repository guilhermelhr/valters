package com.sedentarios.valters.objects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.sedentarios.valters.ControllerWrapper;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.characters.ValterChar;
import com.sedentarios.valters.maps.MapGameOver;
import com.sedentarios.valters.maps.MapPrisao;

public class ObjectRock extends ValtersObject {

	private Texture sprite;
	private float scale;
	private boolean collided = false;
	
	public ObjectRock(float x, float y, float scale) {
		super("Rock", x, y, (byte) 1, true, true);
		this.scale = scale;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/objs/pedra.png", Texture.class);
	}
	
	@Override
	public void create(){
		sprite = getMap().assetManager.get("assets/objs/pedra.png", Texture.class);
		sprite.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		super.create();
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(sprite, getPosition().x, getPosition().y, sprite.getWidth() * scale, sprite.getHeight() * scale);		
	}
	
	@Override
	public void postObjectsRender(SpriteBatch batch){
		if(collided){
			ValterChar valter = (ValterChar) ValtersGame.valter;
			if(!valter.hasFlag("falling")){
				valter.goLeft = false;
				valter.setState(ValterChar.FALLING);
				valter.addFlag("falling");
				Tween.mark().delay(2f).setCallback(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						ControllerWrapper.enableInput("right");
						ValtersGame.changeMap2(new MapGameOver(MapPrisao.class));
					}
				}).start(ValtersGame.tweenManager);
			}
		}
	}
	
	@Override
	public void onCollision(CollisionComponent other){
		if(other.getOwner().getName().equals("valter")){
			collided = true;
		}
	}
	
	@Override
	public Rectangle getCollider(){
		return new Rectangle(getPosition().x, getPosition().y, sprite.getWidth() * scale, sprite.getHeight() * scale);
	}

}

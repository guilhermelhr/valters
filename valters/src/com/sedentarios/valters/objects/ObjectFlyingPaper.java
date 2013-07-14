package com.sedentarios.valters.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.maps.MapRuaNY2;

public class ObjectFlyingPaper extends ValtersObject {

	private Texture sprite;
	private Sound pickupSound;
	private float scale;
	private boolean picked = false;
	
	public ObjectFlyingPaper(float x, float y, float scale) {
		super("FlyingPaper", x, y, (byte) 1, true, false);
		this.scale = scale;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/objs/aviao.png", Texture.class);
		assetManager.load("assets/sounds/pickup.wav", Sound.class);
	}
	
	@Override
	public void create(){
		sprite = getMap().assetManager.get("assets/objs/aviao.png", Texture.class);
		pickupSound = getMap().assetManager.get("assets/sounds/pickup.wav", Sound.class);
		super.create();
		getCollisionComponent().ignore.add("Mendigo");
	}

	private boolean spawnedTelevisions = false;
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(sprite, getPosition().x, getPosition().y, sprite.getWidth() * scale, sprite.getHeight() * scale);
		if(picked && !spawnedTelevisions){
			MapRuaNY2.finished = true;
			spawnedTelevisions = true;
			requestDeath();
			for(ValtersObject o : getMap().getObjects("Television")){
				o.requestDeath();
			}
			for(int x = 0; x < 5; x++){
				for(int y = 0; y < Math.ceil(MathUtils.random() * 3); y++){
					getMap().addObject(new ObjectTelevision(x * 310 * 0.5f + 64, y * 200 * 0.5f + 52, 0.5f, 2));
				}
			}
		}
	}
	
	
	@Override
	public void onCollision(CollisionComponent other){
		if(picked) return;
		
		if(other.getOwner().getName().equals("valter")){
			picked = true;
			pickupSound.play(ValtersOptions.SOUND_LEVEL);
		}
	}
	
	@Override
	public Rectangle getCollider(){
		return new Rectangle(getPosition().x, getPosition().y, sprite.getWidth() * scale, sprite.getHeight() * scale);
	}

}

package com.sedentarios.valters.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;

public class ObjectCoin extends ValtersObject{

	private boolean collected = false;
	private Animation animation;
	private Array<TextureRegion> frames;
	private Array<TextureAtlas.AtlasRegion> atlas;
	private float scale = 1f;
	private Sound pickupSound;

	public ObjectCoin(float x, float y) {
		super("Coin", x, y, (byte) 1, true, true);
	}

	public ObjectCoin(float x, float y, float scale){
		this(x, y);
		this.scale = scale;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/sounds/smw_coin.wav", Sound.class);
		assetManager.load("assets/anim/moeda/moeda.txt", TextureAtlas.class);
		
	}

	@Override
	public void create(){
		pickupSound = getMap().assetManager.get("assets/sounds/smw_coin.wav", Sound.class);

		atlas = getMap().assetManager.get("assets/anim/moeda/moeda.txt", TextureAtlas.class).getRegions();
		frames = new Array<TextureRegion>();
		for(int i = 0; i < atlas.size; i++) {
			TextureRegion region = atlas.get(i);
			region.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			frames.add(region);
		}

		animation = new Animation(1f/12f, frames, Animation.LOOP);

		frame = animation.getKeyFrame(ValtersGame.map.getRuntime());
		super.create();
	}

	@Override
	public void onCollision(CollisionComponent other){
		super.onCollision(other);
		if(other.getOwner().getName().equalsIgnoreCase("valter") && !collected){
			pickupSound.play(ValtersOptions.SOUND_LEVEL);
			collected = true;
			requestDeath();
		}
	}


	TextureRegion frame;
	@Override
	public void render(SpriteBatch batch) {
		if(!collected){
			frame = animation.getKeyFrame(ValtersGame.map.getRuntime());
			batch.draw(frame, getPosition().x, getPosition().y,
					frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
		}
	}

	@Override
	public Rectangle getCollider(){
		return new Rectangle(position.x, position.y, frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
	}
}

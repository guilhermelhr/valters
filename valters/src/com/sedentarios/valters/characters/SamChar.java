package com.sedentarios.valters.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersTexts;
import com.sedentarios.valters.maps.MapWarp2;
import com.sedentarios.valters.objects.TextBalloon;
import com.sedentarios.valters.objects.ValtersObject;


public class SamChar extends ValtersObject {

	public static final int IDLE = 0;
	public static final int WALKING = 1;
	public static final int DIE = 2;
	public int state = IDLE;
	
	private TextureRegion idle;
	private Animation die;
	private float scale;
	
	public SamChar(float x, float y, float scale) {
		super("Sam", x, y, (byte) 1, true, false);
		this.scale = scale;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/anim/parado_sam.png", Texture.class);
		assetManager.load("assets/anim/lutar_morrer_sam/morrer.txt", TextureAtlas.class);
	}
	
	@Override
	public void create(){
		idle = new TextureRegion(getMap().assetManager.get("assets/anim/parado_sam.png", Texture.class));
		idle.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		die = new Animation(1f/24f, getMap().assetManager.get("assets/anim/lutar_morrer_sam/morrer.txt", 
																					TextureAtlas.class).getRegions());
		
		super.create();
	}

	private Vector2 offset = new Vector2();
	private float stateTime = 0f; 
	@Override
	public void render(SpriteBatch batch) {
		TextureRegion frame = null;
		offset.set(0, 0);
		boolean h_flip = false;
		
		if(state == IDLE){
			frame = idle;
			offset.x = 330;
			h_flip = true;
		}else if(state == DIE){
			stateTime += Gdx.graphics.getDeltaTime();
			frame = die.getKeyFrame(stateTime);
			offset.x = 330;
			h_flip = true;
		}
		
		batch.draw(frame, getPosition().x + offset.x, getPosition().y + offset.y, 
					frame.getRegionWidth() * scale * (h_flip?-1:1), frame.getRegionHeight() * scale);
	}
	
	private boolean dead = false;
	@Override
	public void onDamage(ValtersObject other) {
		if(!dead && other.getName().equals("valter")){
			dead = true;
			state = DIE;
			getMap().addObject(new TextBalloon(ValtersGame.valter, ValtersTexts.get("Valter17"), 0, -450, true){
				@Override
				public void onFinish() {
					getMap().addObject(new TextBalloon(ValtersGame.valter, ValtersTexts.get("Sam4"), -64, 32, true){
						@Override
						public void onFinish() {
							ValtersGame.changeMap(MapWarp2.class);
						}
					});
				}
			});
		}
	};
	
	@Override
	public Rectangle getCollider(){
		return new Rectangle(getPosition().x, getPosition().y, 150 * scale, 200 * scale);
	}

}

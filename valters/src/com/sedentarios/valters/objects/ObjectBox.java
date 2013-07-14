package com.sedentarios.valters.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.characters.ValterCharPlatformer;

public class ObjectBox extends ValtersObject {
	
	private Texture sprite;
	private TextureRegion damaged;
	private Animation breaking;
	private float scale;
	private int type;
	private boolean spawnWhiskey;
	
	private final int REGULAR = 0;
	private final int WHISKEY = 1;
	private final int REGULAR_DAMAGED = 2;
	private final int WHISKEY_DAMAGED = 3;
	private final int BREAKING = 4;
	
	public ObjectBox(float x, float y, byte layer, float scale, int type) {
		super("Box", x, y, layer, true, false);
		this.scale = scale;
		addFlag("groundy");
		setGravity(-15);
		this.type = type;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/objs/caixa.png", Texture.class);
		assetManager.load("assets/objs/caixa2.png", Texture.class);
		assetManager.load("assets/objs/caixa2_quebrada.png", Texture.class);
		assetManager.load("assets/anim/caixa/caixa.txt", TextureAtlas.class);
	}
	
	@Override
	public void create(){
		switch(type){
			case REGULAR:
				sprite = getMap().assetManager.get("assets/objs/caixa.png", Texture.class);
				spawnWhiskey = false;
				break;
			case WHISKEY:
				sprite = getMap().assetManager.get("assets/objs/caixa2.png", Texture.class);
				spawnWhiskey = true;
				break;
		}
		Array<AtlasRegion> regions = getMap().assetManager.get("assets/anim/caixa/caixa.txt", TextureAtlas.class).getRegions();
		breaking = new Animation(1f/19f, regions);
		damaged = regions.get(0);
		if(type == WHISKEY){
			damaged = new TextureRegion(getMap().assetManager.get("assets/objs/caixa2_quebrada.png", Texture.class));
		}
		
		super.create();
	}
	
	@Override
	public void onDamage(ValtersObject other){
		if(other.getName().equals("valter")){
			if(type == REGULAR_DAMAGED || type == WHISKEY_DAMAGED){
				type = BREAKING;
				stateTime = 0f;
			} else {
				if(type == REGULAR){
					type = REGULAR_DAMAGED;
				}else if(type == WHISKEY){
					type = WHISKEY_DAMAGED;
				}
			}
		}
	}

	private float stateTime = 0f;
	@Override
	public void render(SpriteBatch batch) {
		if(type != BREAKING){
			if(type == REGULAR_DAMAGED || type == WHISKEY_DAMAGED){
				batch.draw(damaged, getPosition().x, getPosition().y, sprite.getWidth() * scale, sprite.getHeight() * scale);
			} else {
				batch.draw(sprite, getPosition().x, getPosition().y, sprite.getWidth() * scale, sprite.getHeight() * scale);
			}
		}else{
			stateTime += Gdx.graphics.getDeltaTime();
			batch.draw(breaking.getKeyFrame(stateTime), getPosition().x, 
					getPosition().y, sprite.getWidth() * scale, sprite.getHeight() * scale);
			if(breaking.isAnimationFinished(stateTime)){
				if(spawnWhiskey){
					Array<ObjectWhiskey> throwables = new Array<ObjectWhiskey>();
					for(int x = 0; x < 2; x++){
						for(int y = 0; y < 5; y++){
							ObjectWhiskey w = new ObjectWhiskey(getPosition().x + x * 70 + x * 1, getPosition().y + y * 22, 1f);
							throwables.add(w);
							w.setGravity(-15);
							w.setBreakable(false);
							getMap().addObject(w);
						}
					}
					throwables.reverse();
					((ValterCharPlatformer)ValtersGame.valter).throwables = throwables;
				}
				requestDeath();
			}
		}
	}
	
	@Override
	public Rectangle getCollider(){
		return new Rectangle(position.x, position.y, sprite.getWidth() * scale, sprite.getHeight() * scale);
	}

}

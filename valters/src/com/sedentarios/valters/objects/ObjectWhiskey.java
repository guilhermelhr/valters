package com.sedentarios.valters.objects;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Sine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.CollisionManager;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.ValtersTexts;
import com.sedentarios.valters.characters.ValterCharPlatformer;

public class ObjectWhiskey extends ValtersObject {

	private TextureRegion sprite;
	private Vector2 origin;
	private float scale = 1f;
	private float width, height;
	private Animation breaking;
	private boolean collided = false;
	private boolean breakable = true;
	public boolean placed = false;
	
	public ObjectWhiskey(float x, float y, float scale) {
		super("Whiskey", x, y, (byte) 2, true, false);
		this.scale = scale;
		origin = new Vector2();
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/sounds/vidroquebrando.wav", Sound.class);
		assetManager.load("assets/objs/whiskey.png", Texture.class);
		for(int i = 1; i <= 7; i++){
			assetManager.load("assets/anim/garrafa/quebrar000" + i + ".png", Texture.class);
		}
	}
	
	public float getHeight(){
		return height;
	}
	
	public void setOrigin(float x, float y){
		origin.set(x, y);
	}
	
	public void setScale(float scale){
		this.scale = scale;
	}
	
	public void setSize(float width, float height){
		this.width = width;
		this.height = height;
	}
	
	public void setRotation(float rotation){
		if(collided) return;
		this.rotation = rotation;
		float s = (float) Math.sin(rotation);
		float c = (float) Math.cos(rotation);
		float wn = height * Math.abs(s) + width * Math.abs(c);
		float hh = height * Math.abs(c) + width * Math.abs(s);
		Rectangle rect = getCollisionComponent().getRect();
		rect.set(getPosition().x - origin.x, getPosition().y - origin.y, wn, hh);
	}
	
	public void returnToAngle(float angle){
		Tween.to(this, ObjectAccessor.ROTATION, 1f).target(angle).ease(Sine.OUT).start(ValtersGame.tweenManager);
	}

	@Override
	public void create(){
		Texture sprite = getMap().assetManager.get("assets/objs/whiskey.png", Texture.class);
		sprite.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		this.sprite = new TextureRegion(sprite);
		
		Array<TextureRegion> breaking = new Array<TextureRegion>();
		for(int i = 1; i <= 7; i++){
			breaking.add(new TextureRegion(
					getMap().assetManager.get("assets/anim/garrafa/quebrar000" + i + ".png", Texture.class)));
		}
		
		this.breaking = new Animation(1f/19f, breaking);
		
		width = sprite.getWidth();
		height = sprite.getHeight();
		super.create();
		getCollisionComponent().ignore.add("valter");
	}
	
	public void setBreakable(boolean breakable){
		this.breakable = breakable;
	}
	
	private float breakingTime = 0f;
	@Override
	public void render(SpriteBatch batch) {
		if(collided){
			breakingTime += Gdx.graphics.getDeltaTime();
			batch.draw(breaking.getKeyFrame(breakingTime), getPosition().x, getPosition().y - 16, width * scale, height * scale);
			if(breaking.isAnimationFinished(breakingTime)){
				requestDeath();
			}
		}else{
			batch.draw(sprite, getPosition().x, getPosition().y, origin.x, origin.y, width, height, scale, scale, rotation);
		}
	}
	
	
	public static boolean tvRespawn = false;
	@Override
	public void onCollision(CollisionComponent other){
		if(other.getOwner().getName().equals("Whiskey")){
			ObjectWhiskey w = (ObjectWhiskey) other.getOwner();
			if(breakable && !w.placed){
				startBreak(other.getOwner());
			}
		}else{
			if((breakable || other.getOwner().getName().equals("Box")) && !other.getOwner().getName().equals("Colchao")){
				startBreak(other.getOwner());
			}else if(!placed && other.getOwner().getName().equals("Colchao")){
				placed = true;
				final ValtersObject mendigo = getMap().getObject("Mendigo");
				ValterCharPlatformer.placedBottle = true;
				
				getMap().addObject(new TextBalloon(ValtersGame.valter, ValtersTexts.get("Valter10"), -60, -16){
					@Override
					public void onFinish(){
						ValtersGame.valter.frozen = true;
						getMap().addObject(new TextBalloon(mendigo, ValtersTexts.get("Mendigo4"), -50, -460, true){
							@Override
							public void onFinish(){
								if(ValtersGame.map.getObjects("FlyingPaper").size == 0){
									ObjectFlyingPaper fp = new ObjectFlyingPaper(mendigo.getPosition().x - 128, mendigo.getPosition().y + 400, 0.4f);
									fp.setGravity(-15);
									Vector2 force = new Vector2(-1, 1);
									force.scl(500f);
									fp.applyForce(force);
									ValtersGame.map.addObject(fp);
									
									ValtersGame.setCamDisplacement(0, 0);
									ValtersGame.valter.frozen = false;
								}
							}
						});
					}
				});	
			}
		}
	}
	
	private void startBreak(ValtersObject object){
		if(collided) return;
		if(object.getName().equals("valter")) return;
		if(!collided) getMap().assetManager.get("assets/sounds/vidroquebrando.wav", Sound.class).play(ValtersOptions.SOUND_LEVEL);
		collided = true;
		CollisionManager.unregisterComponent(getCollisionComponent());
	}
	
	@Override
	public void onDamage(ValtersObject other){
		startBreak(other);
	}
	
	@Override
	public Rectangle getCollider(){
		return new Rectangle(getPosition().x, getPosition().y, width * scale, height * scale);
	}

}

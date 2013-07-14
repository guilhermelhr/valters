package com.sedentarios.valters.objects;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Sine;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.characters.ValterChar;
import com.sedentarios.valters.characters.ValterCharPlatformer;

public class ObjectColchao extends ValtersObject {

	private Texture sprite;
	private float scale;
	
	public ObjectColchao(float x, float y, float scale) {
		super("Colchao", x, y, (byte) 1, true, false);
		this.scale = scale;
		addFlag("groundy");
	}

	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/objs/colchao.png", Texture.class);
	}
	
	@Override
	public void create(){
		sprite = getMap().assetManager.get("assets/objs/colchao.png", Texture.class);
		super.create();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(sprite, getPosition().x, getPosition().y, sprite.getWidth() * scale, sprite.getHeight() * scale);
	}
	
	private int relativePos = 0;
	@Override
	public void onDamage(ValtersObject other){
		if(other.getName().equals("valter")){
			if(((ValterCharPlatformer)other).attack == ValterCharPlatformer.KICKING){
				ValterChar valter = (ValterChar) other;
				if(valter.isFacingLeft() && relativePos == -1) return;
				if(!valter.isFacingLeft() && relativePos == 1) return;
				relativePos += valter.isFacingLeft()?-1:1;
				Tween.to(this, ObjectAccessor.POSITION_X, 1f).target(getPosition().x + (valter.isFacingLeft()?-670:670)).ease(Sine.OUT).start(ValtersGame.tweenManager);
			}
		}
	}

	@Override
	public Rectangle getCollider(){
		return new Rectangle(getPosition().x, getPosition().y, sprite.getWidth() * scale, sprite.getHeight() * scale);
	}
}

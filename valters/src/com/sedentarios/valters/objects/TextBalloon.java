package com.sedentarios.valters.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.sedentarios.valters.ControllerWrapper;
import com.sedentarios.valters.ValtersGame;

public class TextBalloon extends ValtersObject {

	private float width = -1, height = -1;
	private Texture sprite;
	private ValtersObject object;
	private String text;
	private float index = 1;
	private float yDisplacement;
	private float xDisplacement;
	private boolean invertBalloon = false;
	
	public TextBalloon(ValtersObject object, String text, float yDisplacement, float xDisplacement, boolean invertBallon) {
		this(object, text, yDisplacement, xDisplacement);
		this.invertBalloon = invertBallon;
	}
	
	public TextBalloon(ValtersObject object, String text, float yDisplacement, float xDisplacement) {
		super("TextBalloon", 0, 0, (byte) 2, false, false);
		this.object = object;
		this.text = text;
		this.yDisplacement = yDisplacement;
		this.xDisplacement = xDisplacement;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/objs/balao.png", Texture.class);
	}
	
	@Override
	public void create(){
		if(object != null) object.frozen = true;
		sprite = getMap().assetManager.get("assets/objs/balao.png", Texture.class);
		if(width == -1 && height == -1){
			width = sprite.getWidth();
			height = sprite.getHeight();
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		if(index < text.length()) index += Gdx.graphics.getDeltaTime() * (MathUtils.random() * 20 + 5);
		
		if(object != null) this.setPosition(object.getPosition());
	}
	
	@Override
	public void postObjectsRender(SpriteBatch batch){
		batch.draw(sprite, getPosition().x + 128 + xDisplacement + (invertBalloon?sprite.getWidth():0), 
				getPosition().y + 368 + yDisplacement, width * (invertBalloon?-1:1), height);
		
		ValtersGame.font18.drawMultiLine(batch, text.subSequence(0, (int) Math.min(Math.floor(index), text.length())), 
				getPosition().x + 128 + 8 + xDisplacement, getPosition().y + 368 + height - 8 + yDisplacement);
		
		if(index >= text.length() / 2 && (ControllerWrapper.isInputActive("action", false))){
			if(object != null){
				object.frozen = false;
			}
			requestDeath();
		}
	}
	
	@Override
	public void dispose(){
		onFinish();
		super.dispose();
	}
	
	public void onFinish(){
		
	}

}

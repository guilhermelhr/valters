package com.sedentarios.valters.characters;

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
import com.sedentarios.valters.ValtersTexts;
import com.sedentarios.valters.objects.TextBalloon;
import com.sedentarios.valters.objects.ValtersObject;

public class MendigoChar extends ValtersObject{

	private Animation idle;
	private AtlasRegion sprite;
	private float scale;
	private Texture sombra;
	
	public MendigoChar(float x, float y, float scale) {
		super("Mendigo", x, y, (byte) 0, true, false);
		this.scale = scale;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/anim/mendigo/mendigo.txt", TextureAtlas.class);
		assetManager.load("assets/anim/sombra.png", Texture.class);
	}

	@Override
	public void create(){
		Array<AtlasRegion> regions = getMap().assetManager.get("assets/anim/mendigo/mendigo.txt", TextureAtlas.class).getRegions();
		idle = new Animation(1f/16f, regions);
		sprite = regions.get(14);
		sombra = getMap().assetManager.get("assets/anim/sombra.png", Texture.class);
		super.create();
	}
	
	private boolean animating = false;
	private float stateTime = 0f;
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(sombra, getPosition().x - 58, getPosition().y - 13, sombra.getWidth() * 0.6f, sombra.getHeight() * 0.6f);
		
		stateTime += Gdx.graphics.getDeltaTime();
		if(animating){
			TextureRegion frame = idle.getKeyFrame(stateTime);
			batch.draw(frame, getPosition().x + frame.getRegionWidth() * scale * 0.5f, getPosition().y, -frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
			if(idle.isAnimationFinished(stateTime)){
				animating = false;
			}
		}else{
			if(stateTime >= 10f){
				animating = true;
				stateTime = 0f;
			}
			batch.draw(sprite, getPosition().x + sprite.getRegionWidth() * scale * 0.5f, getPosition().y, -sprite.getRegionWidth() * scale, sprite.getRegionHeight() * scale);
		}
		
		if(ValtersGame.valter != null){
			final ValtersObject mendigo = this;
			if(!hasFlag("talked_with_valter") && ValtersGame.valter.getPosition().dst2(getPosition()) <= 76383f){
				addFlag("talked_with_valter");
				getMap().addObject(new TextBalloon(ValtersGame.valter, ValtersTexts.get("Valter6"), -60, -16){
					@Override
					public void onFinish(){
						ValtersGame.valter.frozen = true;
						getMap().addObject(new TextBalloon(mendigo, ValtersTexts.get("Mendigo1"), -55, -70){
							@Override
							public void onFinish(){
								ValtersGame.valter.frozen = true;
								getMap().addObject(new TextBalloon(ValtersGame.valter, ValtersTexts.get("Valter7"), -60, -16){
									@Override
									public void onFinish(){
										ValtersGame.valter.frozen = true;
										getMap().addObject(new TextBalloon(mendigo, ValtersTexts.get("Mendigo2"), -55, -70){
											@Override
											public void onFinish(){
												ValtersGame.valter.frozen = true;
												getMap().addObject(new TextBalloon(ValtersGame.valter, ValtersTexts.get("Valter8"), -60, -16){
													@Override
													public void onFinish(){
														ValtersGame.valter.frozen = true;
														getMap().addObject(new TextBalloon(mendigo, ValtersTexts.get("Mendigo3"), -55, -70){
															@Override
															public void onFinish(){
																ValtersGame.valter.frozen = false;
															}
														});
													}
												});
											}
										});
									}
								});								
							}
						});
					}
				});
			}
		}
	}
	
	@Override
	public Rectangle getCollider(){
		return new Rectangle(getPosition().x - sprite.getRegionWidth() / 2, getPosition().y, sprite.getRegionWidth() * scale, sprite.getRegionHeight() * scale);
	}

}

package com.sedentarios.valters.objects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Sine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.sedentarios.valters.ControllerWrapper;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.ValtersTexts;
import com.sedentarios.valters.maps.MapRuaNY2;
import com.sedentarios.valters.ui.UIScene;

public class ObjectStockMachine extends ValtersObject{

	private Texture sign;
	private Texture sprite;
	private Sound cashSound;
	private Sound printerSound;
	private Animation cert;
	private Texture saida;
	private float scale = 0.5f;
	private UIScene ui;
	private boolean ended = false;
	private boolean map3 = false;
	
	public void setMap3(boolean map3){
		this.map3 = map3;
	}

	public ObjectStockMachine(float x, float y, byte layer) {
		super("StockMachine", x, y, layer, true, false);
		finishedGivingMoney = false;
		sprite = getMap().assetManager.get("assets/objs/stock_machine.png");
		sign = getMap().assetManager.get("assets/objs/placa2.png");
		saida = getMap().assetManager.get("assets/anim/certificado/saida.png");
		TextureAtlas certAtlas = getMap().assetManager.get("assets/anim/certificado/certificado.txt");
		cert = new Animation(1f/14f, certAtlas.getRegions());
		ui = getMap().getUIScene("money");
		cashSound = getMap().assetManager.get("assets/sounds/cash_register.wav", Sound.class);
		printerSound = getMap().assetManager.get("assets/sounds/printer.mp3", Sound.class);		
	}

	private float renderScreen = 0f;
	private float exposure = -1f;
	private boolean changingMap = false;
	private boolean gaveMoney = false;
	public static boolean finishedGivingMoney = false;
	@Override
	public void render(SpriteBatch batch) {
		if(!map3){
			if(!changingMap && ended && 
					ValtersGame.valter.getPosition().x >= getMap().getRightCap() + ValtersOptions.SCREEN_WIDTH / 2 - 90){
				ValtersGame.changeMap(MapRuaNY2.class);
				changingMap = true;
			}
		}
		
		batch.draw(sign, getPosition().x - 6, getPosition().y + sprite.getHeight() * scale, sign.getWidth() * scale, sign.getHeight() * scale);
		batch.draw(sprite, getPosition().x, getPosition().y, sprite.getWidth() * scale, sprite.getHeight() * scale);
		
		if(ValtersGame.valter != null){
			float dist = ValtersGame.valter.getPosition().dst2(getPosition());

			if(!map3){
				if(dist <= 5895){
					ui.setActive(true);
					int money = Integer.parseInt(ValtersGame.valter.getAttribute("reais"));
					if(exposure < 0 && ControllerWrapper.isInputActive("action")){
						if(money > 0 && renderScreen <= 0f){
							renderScreen = 0.5f;
							cashSound.play();
							ValtersGame.valter.setAttribute("reais", 
									String.valueOf(Integer.parseInt(ValtersGame.valter.getAttribute("reais")) - 1));
						}
					}
					if(money == 0 && exposure < 0f && !ended){
						exposure = 0f;
						printerSound.play();
					}
				}else{
					ui.setActive(false);
				}
			}else{
				if(!gaveMoney && dist <= 5895){
					ui.setActive(true);
					if(ControllerWrapper.isInputActive("action")){
						getMap().addObject(new TextBalloon(ValtersGame.valter, ValtersTexts.get("Valter15"), -64, -380, true){
							@Override
							public void onFinish(){
								for(int i = 0; i < 100; i++){
									final int seq = i;
									Tween.mark().delay(0.1f * i).setCallback(new TweenCallback() {
										@Override
										public void onEvent(int type, BaseTween<?> source) {
											final ObjectCoin coin = new ObjectCoin(getPosition().x + 28, getPosition().y + 100);
											getMap().addObject(coin);
											Tween.to(coin, ObjectAccessor.POSITION_XY, 0.8f).target(ValtersGame.valter.getPosition().x + 32, 
													ValtersGame.valter.getPosition().y + 128).ease(Sine.IN).setCallback(new TweenCallback() {
														@Override
														public void onEvent(int type, BaseTween<?> source) {
															coin.onCollision(ValtersGame.valter.getCollisionComponent());
															ValtersGame.valter.setAttribute("dollars", 
																	String.valueOf(Integer.parseInt(ValtersGame.valter.getAttribute("dollars")) + 100000));
															if(seq == 99){
																ValtersGame.map.assetManager.get("assets/sounds/smw_1-up.wav", Sound.class).play(1.5f * ValtersOptions.SOUND_LEVEL);
																getMap().addObject(new TextBalloon(ValtersGame.valter,
																		ValtersTexts.get("Valter11"), -64, -380, true){
																	@Override
																	public void onFinish(){
																		finishedGivingMoney = true;
																	}
																});
															}
														}
													}).start(ValtersGame.tweenManager);
										}
									}).start(ValtersGame.tweenManager);
								}
							}
						});
						gaveMoney = true;
					}
				}
			}
		}
		
		if(!map3){
			if(renderScreen > 0){
				renderScreen -= Gdx.graphics.getDeltaTime();
				ValtersGame.font.draw(batch, "+$", getPosition().x + 28, getPosition().y + 233);
			}
		}
		//ended = false;
		
		if(!map3 && exposure >= 0f){
			TextureRegion frame = cert.getKeyFrame(exposure);
			batch.draw(frame, getPosition().x + 88 - frame.getRegionWidth() * scale * 0.8f
						, getPosition().y + 168, frame.getRegionWidth() * scale * 0.8f, frame.getRegionHeight() * scale * 0.8f);
			if(exposure >= 2f && ControllerWrapper.isInputActive("action") && !ended){
				getMap().assetManager.get("assets/sounds/pickup.wav", Sound.class).play();
				exposure = -1f;
				ended = true;
				getMap().addObject(new ObjectCertificado(ValtersGame.valter.getPosition().x, 400));
				ValtersGame.valter.frozen = true;
				Tween.mark().delay(2).setCallback(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						getMap().addObject(new TextBalloon(ValtersGame.valter, ValtersTexts.get("Valter5"), -64, -380, true));
					}
				}).start(ValtersGame.tweenManager);
			}else{
				exposure += Gdx.graphics.getDeltaTime();
			}
		}else{
			batch.draw(saida, getPosition().x + 88 - saida.getWidth() * scale * 0.8f, 
					getPosition().y + 168, saida.getWidth() * scale * 0.8f, saida.getHeight() * scale);
		}				
	}

	@Override
	public Rectangle getCollider(){
		return new Rectangle(getPosition().x, getPosition().y + 50, sprite.getWidth() * scale,
																	sprite.getHeight() * scale * 0.8f);
	}
}

package com.sedentarios.valters.maps;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.sedentarios.valters.ControllerWrapper;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.ValtersTexts;
import com.sedentarios.valters.characters.PoliceChar;
import com.sedentarios.valters.characters.ValterChar;
import com.sedentarios.valters.objects.ObjectAccessor;
import com.sedentarios.valters.objects.ObjectCar;
import com.sedentarios.valters.objects.ObjectCertificado;
import com.sedentarios.valters.objects.ObjectCoin;
import com.sedentarios.valters.objects.ObjectHole;
import com.sedentarios.valters.objects.ObjectRock;
import com.sedentarios.valters.objects.ObjectStockMachine;
import com.sedentarios.valters.objects.TextBalloon;
import com.sedentarios.valters.ui.UISceneMoney;

public class MapRuaNY3 extends ValtersMap {

	private Texture background;
	private Sound backgroundMusic;
	//private Sound crowdNoise;
	private Texture[] noise;
	
	public MapRuaNY3() {
		super(64, 5638);
	}

	@Override
	public void create() {
		assetManager.load("assets/maps/ruany.tmx", TiledMap.class);
		assetManager.load("assets/maps/fundo-parallax_1.png", Texture.class);
		assetManager.load("assets/objs/stock_machine.png", Texture.class);
		assetManager.load("assets/objs/placa2.png", Texture.class);
		assetManager.load("assets/sounds/musica_quarta_fase.wav", Sound.class);
		assetManager.load("assets/sounds/cash_register.wav", Sound.class);
		assetManager.load("assets/sounds/printer.mp3", Sound.class);
		assetManager.load("assets/anim/certificado/saida.png", Texture.class);
		assetManager.load("assets/anim/certificado/certificado.txt", TextureAtlas.class);
		assetManager.load("assets/sounds/pickup.wav", Sound.class);
		assetManager.load("assets/texturas/jornal.png", Texture.class);
		assetManager.load("assets/objs/jornal.png", Texture.class);
		assetManager.load("assets/sounds/smw_1-up.wav", Sound.class);
		UISceneMoney.loadAssets(assetManager);
		ObjectCertificado.loadResources(assetManager);
		ObjectCoin.loadResources(assetManager);
		PoliceChar.loadResources(assetManager);
		ObjectHole.loadResources(assetManager);
		ObjectRock.loadResources(assetManager);
		ValterChar.loadResources(assetManager);
		
		noise = new Texture[3];
		for(int i = 0; i < noise.length; i++){
			assetManager.load("assets/ruido/ruido" + (i + 1) + ".png", Texture.class);
		}
	}

	@Override
	public void createObjects(){
		ControllerWrapper.enableAllInputs();
		
		ValtersGame.setCamDisplacement(0, 0);
		valterInPlace = false;
		removeUIScene("money");
		uiScenes.add(new UISceneMoney("money"));
		getUIScene("money").setActive(false);
		
		map = assetManager.get("assets/maps/ruany.tmx", TiledMap.class);
		background = assetManager.get("assets/maps/fundo-parallax_1.png", Texture.class);

		if(backgroundMusic != null) backgroundMusic.stop();
		backgroundMusic = assetManager.get("assets/sounds/musica_quarta_fase.wav", Sound.class);
		backgroundMusic.loop(0.4f * ValtersOptions.MUSIC_LEVEL);

		noise = new Texture[3];
		for(int i = 0; i < noise.length; i++){
			noise[i] = assetManager.get("assets/ruido/ruido" + (i + 1) + ".png", Texture.class);
		}

		applyShader();
		ObjectStockMachine sm = new ObjectStockMachine(5936, 280, (byte) 0);
		sm.setMap3(true);
		addObject(sm);
		addObject(new ObjectCar());
		
		ValterChar v = new ValterChar(6088, 250, 200, 320, 0.8f);
		addObject(ValtersGame.valter = v);
		v.setState(ValterChar.IDLE_L);
		
		ValtersGame.valter.setAttribute("reais", "0");
		((UISceneMoney)getUIScene("money")).setObject(ValtersGame.valter);
		//x 465 4400
		for(int x = 0; x < 9; x++){
			ObjectHole hole = new ObjectHole(x * 540 + 465, (float) (MathUtils.random() * 105 + 195), 1f);
			addObject(hole);
		}
		
		addObject(new ObjectRock(301, 300, 1f));
		addObject(new ObjectRock(301, 250, 1.3f));
		addObject(new ObjectRock(301, 200, 1.2f));
		//y 238 280
	}
	
	private float fps = 1f/19f;
	private byte currFrame = 0;
	private float exposure = 0f;
	private boolean valterInPlace = false;
	@Override
	public void render(OrthographicCamera camera){
		super.render(camera);
		exposure += Gdx.graphics.getDeltaTime();
		if(exposure >= fps){
			exposure = 0f;
			currFrame++;
			if(currFrame > noise.length - 1){
				currFrame = 0;
			}
		}
		
		if(!ObjectStockMachine.finishedGivingMoney){
			ValtersGame.valter.getPosition().x = Math.max(5595, ValtersGame.valter.getPosition().x);
		}else if(!valterInPlace){
			if(ValtersGame.valter.getPosition().x <= 5595f){
				valterInPlace = true;
				((ValterChar)ValtersGame.valter).setState(ValterChar.IDLE_R);
				ValtersGame.valter.frozen = true;
				final PoliceChar p = new PoliceChar(6300, 250, (byte) 1, 0.8f);
				addObject(p);
				Tween.to(p, ObjectAccessor.RUN, 1.5f).target(5847f, 250f).ease(Linear.INOUT).setCallback(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						ValtersGame.valter.frozen = true;
						addObject(new TextBalloon(p, ValtersTexts.get("Police1"), -64, -380, true){
							@Override
							public void onFinish(){
								ValtersGame.valter.frozen = true;
								addObject(new TextBalloon(ValtersGame.valter, ValtersTexts.get("Valter12"), -64, -380, true){
									@Override
									public void onFinish(){
										ValtersGame.valter.frozen = true;
										addObject(new TextBalloon(p, ValtersTexts.get("Police2"), -64, -380, true){
											@Override
											public void onFinish(){
												ValtersGame.valter.frozen = true;
												addObject(new TextBalloon(ValtersGame.valter, ValtersTexts.get("Valter13"), -64, -380, true){
													@Override
													public void onFinish(){
														ValtersGame.valter.frozen = true;
														addObject(new TextBalloon(p, ValtersTexts.get("Police3"), -64, -380, true){
															@Override
															public void onFinish(){
																ValtersGame.valter.frozen = true;
																addObject(new TextBalloon(ValtersGame.valter, ValtersTexts.get("Valter14"), -64, -380, true){
																	@Override
																	public void onFinish(){
																		Tween.to(p, ObjectAccessor.RUN, 15f).target(328, 250f).ease(Linear.INOUT).start(ValtersGame.tweenManager);
																		((ValterChar)ValtersGame.valter).goLeft = true;
																		ControllerWrapper.disableInput("right");
																		ValtersGame.valter.getCollisionComponent().getRect().height = 32;
																		ValtersGame.setCamDisplacement(-150, 0);
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
				}).start(ValtersGame.tweenManager);				
			}
		}
		
		if(ValtersGame.valter.getPosition().x <= 730){
			ValtersGame.setCamDisplacement(0, 0);
		}
		
		//if(valterInPlace) getObject("Police").move(-0.05f, 0);
		//System.out.println(getObject("Police").getPosition());
	}
	
	@Override
	protected void postObjectRender(OrthographicCamera camera, SpriteBatch batch){
		batch.draw(noise[currFrame], camera.position.x - Gdx.graphics.getWidth() / 2,
				camera.position.y - Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	@Override
	protected void preMapRender(OrthographicCamera camera, SpriteBatch batch){
		float parallaxX = (camera.position.x - Gdx.graphics.getWidth() / 2) * 0.9f;
		
		batch.draw(background, parallaxX, 820 - background.getHeight());
	}
	
	private void applyShader(){
		if(super.batch != null){
			super.batch.dispose();
			super.batch = null;
		}
		try {
			final String VERTEX = Gdx.files.internal("assets/shaders/bw.vert").readString();
			final String FRAGMENT = Gdx.files.internal("assets/shaders/bw.frag").readString();
						
			ShaderProgram program = new ShaderProgram(VERTEX, FRAGMENT);
			
			if (program.getLog().length()!=0 || !program.isCompiled()){
				System.err.println(program.getLog());
			}

			batch = new SpriteBatch(1000, program);
			batch.setShader(program);
			
			renderer = new OrthogonalTiledMapRenderer(map, 1.5f);
			renderer.getSpriteBatch().setShader(program);
		} catch (Exception e) { 
			System.err.println(e.getMessage());
			System.err.println("Could not load shaders, using regular SpriteBatch!");
			batch = new SpriteBatch();
			renderer = new OrthogonalTiledMapRenderer(map, 1.5f);
		}
	}

}

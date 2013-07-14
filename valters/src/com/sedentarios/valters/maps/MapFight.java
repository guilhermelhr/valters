package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.ValtersTexts;
import com.sedentarios.valters.characters.SamChar;
import com.sedentarios.valters.characters.ValterFight;
import com.sedentarios.valters.objects.TextBalloon;

public class MapFight extends ValtersMap {

	private Texture[] noise;
	private Texture mesa;
	private Texture lifeBar;
	private Texture money;
	private boolean loopingBg = false;
	
	public MapFight() {
		super(64, 576);
	}

	@Override
	public void create() {
		noise = new Texture[3];
		for(int i = 0; i < noise.length; i++){
			assetManager.load("assets/ruido/ruido" + (i + 1) + ".png", Texture.class);
		}
		assetManager.load("assets/maps/tio_sam.tmx", TiledMap.class);
		assetManager.load("assets/objs/mesa_sam.png", Texture.class);
		assetManager.load("assets/objs/life_bar.png", Texture.class);
		assetManager.load("assets/objs/sacos_dinheiro.png", Texture.class);
		assetManager.load("assets/sounds/ct_boss.mp3", Sound.class);
		ValterFight.loadResources(assetManager);
		SamChar.loadResources(assetManager);
	}

	@Override
	public void createObjects() {
		lifeBar = assetManager.get("assets/objs/life_bar.png", Texture.class);
		money = assetManager.get("assets/objs/sacos_dinheiro.png", Texture.class);		
		mesa = assetManager.get("assets/objs/mesa_sam.png");
		
		noise = new Texture[3];
		for(int i = 0; i < noise.length; i++){
			noise[i] = assetManager.get("assets/ruido/ruido" + (i + 1) + ".png", Texture.class);
		}
		
		map = assetManager.get("assets/maps/tio_sam.tmx", TiledMap.class);
		
		applyShader();
		
		final ValterFight vf = new ValterFight(64, 32, 0.5f);
		vf.frozen = true;
		addObject(ValtersGame.valter = vf);
		final SamChar sam = new SamChar(900, 32, 1.1f);
		addObject(sam);
		addObject(new TextBalloon(sam, ValtersTexts.get("Sam1"), 0, -450, true){
			@Override
			public void onFinish() {
				vf.frozen = true;
				addObject(new TextBalloon(vf, ValtersTexts.get("Valter16"), 0, 32, false){
					@Override
					public void onFinish() {
						vf.frozen = true;
						addObject(new TextBalloon(sam, ValtersTexts.get("Sam2"), 0, -450, true){
							@Override
							public void onFinish() {
								vf.frozen = true;
								addObject(new TextBalloon(sam, ValtersTexts.get("Sam3"), 0, -450, true){
									@Override
									public void onFinish() {
										vf.frozen = false;
										if(!loopingBg){
											loopingBg = true;
											assetManager.get("assets/sounds/ct_boss.mp3", Sound.class).loop(ValtersOptions.MUSIC_LEVEL);
										}
									}
								});		
							}
						});		
					}
				});		
			}
		});		
	}
	
	@Override
	protected void inBatchRender(com.badlogic.gdx.graphics.OrthographicCamera camera, SpriteBatch batch) {
		batch.draw(mesa, 500, 60, mesa.getWidth() * 0.5f, mesa.getHeight() * 0.5f);
		batch.draw(money, 1000, 60, money.getWidth() * 0.43f, money.getHeight() * 0.43f);
	}
	
	private float fps = 1f/19f;
	private byte currFrame = 0;
	private float exposure = 0f;
	@Override
	protected void postObjectRender(com.badlogic.gdx.graphics.OrthographicCamera camera, SpriteBatch batch) {
		batch.draw(lifeBar, 35, ValtersOptions.SCREEN_HEIGHT - 118, lifeBar.getWidth() * 0.36f, lifeBar.getHeight() * 0.36f);
		
		exposure += Gdx.graphics.getDeltaTime();
		if(exposure >= fps){
			exposure = 0f;
			currFrame++;
			if(currFrame > noise.length - 1){
				currFrame = 0;
			}
		}
		
		batch.draw(noise[currFrame], camera.position.x - Gdx.graphics.getWidth() / 2,
				camera.position.y - Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
			
			renderer = new OrthogonalTiledMapRenderer(map, 1.2f);
			renderer.getSpriteBatch().setShader(program);
		} catch (Exception e) { 
			System.err.println(e.getMessage());
			System.err.println("Could not load shaders, using regular SpriteBatch!");
			batch = new SpriteBatch();
			renderer = new OrthogonalTiledMapRenderer(map, 1.5f);
		}
	}

}

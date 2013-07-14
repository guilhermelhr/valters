package com.sedentarios.valters.maps;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.ValtersTexts;
import com.sedentarios.valters.characters.PoliceChar;
import com.sedentarios.valters.characters.ValterChar;
import com.sedentarios.valters.characters.ValterCharPrisao;
import com.sedentarios.valters.objects.ObjectAccessor;
import com.sedentarios.valters.objects.ObjectCrack;
import com.sedentarios.valters.objects.TextBalloon;

public class MapPrisao extends ValtersMap {

	private Sound backgroundMusic;
	private Texture medidor;
	private TextureRegion ponteiro;
	private float rotation;
	private PoliceChar pol;
	private ValterCharPrisao vp;
	
	private Texture travesseiro;
	private Texture panela;
	private Texture colher;
	
	private Texture[] noise;
	
	public MapPrisao() {
		super(0, 175);
	}
	
	@Override
	public void create() {
		assetManager.load("assets/maps/cela.tmx", TiledMap.class);
		assetManager.load("assets/sounds/musica_prisao.mp3", Sound.class);
		assetManager.load("assets/objs/medidor.png", Texture.class);
		assetManager.load("assets/objs/ponteiro.png", Texture.class);
		assetManager.load("assets/objs/travesseiro.png", Texture.class);
		assetManager.load("assets/objs/panela.png", Texture.class);
		assetManager.load("assets/objs/colher.png", Texture.class);
		ValterCharPrisao.loadResources(assetManager);
		PoliceChar.loadResources(assetManager);
		ObjectCrack.loadResources(assetManager);
		
		noise = new Texture[3];
		for(int i = 0; i < noise.length; i++){
			assetManager.load("assets/ruido/ruido" + (i + 1) + ".png", Texture.class);
		}
	}
	
	@Override
	public void createObjects(){
		rotation = 70f;
		
		noise = new Texture[3];
		for(int i = 0; i < noise.length; i++){
			noise[i] = assetManager.get("assets/ruido/ruido" + (i + 1) + ".png", Texture.class);
		}

		map = assetManager.get("assets/maps/cela.tmx", TiledMap.class);
		if(backgroundMusic == null){
			backgroundMusic = assetManager.get("assets/sounds/musica_prisao.mp3", Sound.class);
			backgroundMusic.loop(0.4f * ValtersOptions.MUSIC_LEVEL);
		}

		travesseiro = assetManager.get("assets/objs/travesseiro.png", Texture.class);
		panela = assetManager.get("assets/objs/panela.png", Texture.class);
		colher = assetManager.get("assets/objs/colher.png", Texture.class);
		
		medidor = assetManager.get("assets/objs/medidor.png", Texture.class);
		medidor.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Texture ponteiro = assetManager.get("assets/objs/ponteiro.png", Texture.class);
		
		this.ponteiro = new TextureRegion(ponteiro);
		
		applyShader();
		
		vp = new ValterCharPrisao(100, 32, 0.3f);
		ValtersGame.valter = vp;		
		vp.setState(ValterChar.IDLE_R);
		addObject(vp);
		
		pol = new PoliceChar(-200, 32, (byte) -1, 0.7f);
		movePolice(pol);
		addObject(pol);
		
		addObject(new ObjectCrack(710, 11, 1f));
		
		ValtersGame.xDisplacement = -68;
		ValtersGame.yDisplacement = 290;		
		ValtersGame.setZoom(0.8f);
	}
	
	private Tween lastTween = null;
	private final void movePolice(final PoliceChar pol){
		if(pol == null || pol.isWaitingRemoval()) return;
		lastTween = Tween.to(pol, ObjectAccessor.MOVE, 10f).target(660f, 32f).ease(Linear.INOUT).setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				if(pol == null || pol.isWaitingRemoval()) return;
				lastTween = Tween.to(pol, ObjectAccessor.MOVE, 10f).target(-200f, 32f).ease(Linear.INOUT).setCallback(new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						if(pol == null || pol.isWaitingRemoval()) return;
						movePolice(pol);						
					}
				}).start(ValtersGame.tweenManager);				
			}
		}).start(ValtersGame.tweenManager);
	}
	
	private boolean lost = false;
	
	@Override
	protected void inBatchRender(OrthographicCamera camera, SpriteBatch batch) {
		if(vp.arma != ValterCharPrisao.COLHER) batch.draw(colher, 530, 32, colher.getWidth() * 0.3f, colher.getHeight() * 0.3f);
		if(vp.arma != ValterCharPrisao.PANELA) batch.draw(panela, 30, 32, panela.getWidth() * 0.5f, panela.getHeight() * 0.5f);
		if(vp.arma != ValterCharPrisao.TRAVESSEIRO) batch.draw(travesseiro, 620, 32, travesseiro.getWidth() * 0.3f, travesseiro.getHeight() * 0.3f);
		
		rotation -= Gdx.graphics.getDeltaTime() * -5f;
		rotation = Math.min(90, rotation);
		float dist = Math.min(0.07f, (1f/pol.getPosition().dst(373, 32)) * 2);
		float prox = Math.min(0.07f,(1f/ValtersGame.valter.getPosition().dst(751, 32)));
		float result = Math.max(-90,Math.min(90, rotation - (dist * 800f) - (1000f * prox)));
		//System.out.println(dist);
		batch.draw(medidor, 230, 380);
		batch.draw(ponteiro, 277, 385, ponteiro.getRegionWidth() * 0.5f, 3, ponteiro.getRegionWidth(), 
							ponteiro.getRegionHeight(), 1, 1, result);
		if(!lost && result <= -90){
			lost = true;
			lastTween.free();
			ValtersGame.valter.frozen = true;
			Tween.to(pol, ObjectAccessor.MOVE, 1f).target(126, 32).setCallback(new TweenCallback() {
				@Override
				public void onEvent(int type, BaseTween<?> source) {
					pol.setState(PoliceChar.IDLE_R);
					addObject(new TextBalloon(pol, ValtersTexts.get("Police4"), -64, -32){
						@Override
						public void onFinish(){
							ValtersGame.changeMap2(new MapGameOver(MapPrisao.class));
						}
					});
				}
			}).start(ValtersGame.tweenManager);
		}
	}
	
	private float fps = 1f/19f;
	private byte currFrame = 0;
	private float exposure = 0f;
	@Override
	protected void postObjectRender(OrthographicCamera camera, SpriteBatch batch) {
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
	};
	
	private void applyShader(){
		if(super.batch != null){
			super.batch.dispose();
			super.batch = null;
		}
		try {
			final String VERTEX = Gdx.files.internal("assets/shaders/bw.vert").readString();
			final String FRAGMENT = Gdx.files.internal("assets/shaders/bw.frag").readString();
						
			ShaderProgram program = new ShaderProgram(VERTEX, FRAGMENT);
			
			if (program.getLog().length() != 0 || !program.isCompiled()){
				System.err.println(program.getLog());
			}

			batch = new SpriteBatch(1000, program);
			batch.setShader(program);
			
			renderer = new OrthogonalTiledMapRenderer(map, 1f);
			renderer.getSpriteBatch().setShader(program);
		} catch (Exception e) { 
			System.err.println(e.getMessage());
			System.err.println("Could not load shaders, using regular SpriteBatch!");
			batch = new SpriteBatch();
			renderer = new OrthogonalTiledMapRenderer(map, 1.5f);
		}
	}

	public void wallHit(float dmg) {
		rotation -= dmg * 5f;
	}
	
}

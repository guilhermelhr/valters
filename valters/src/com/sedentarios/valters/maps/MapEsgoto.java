package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.characters.ValterCharEsgoto;
import com.sedentarios.valters.objects.ObjectBueiro;
import com.sedentarios.valters.objects.ObjectCorda;
import com.sedentarios.valters.objects.ValtersObject;

public class MapEsgoto extends ValtersMap {

	private Texture background;
	private Texture entrada;
	private Texture[] noise;
	private boolean playingBg = false;
	
	public MapEsgoto() {
		super(64, 3500);
	}

	@Override
	public void create() {
		assetManager.load("assets/maps/esgoto.tmx", TiledMap.class);
		assetManager.load("assets/texturas/fundo_esgoto.png", Texture.class);
		assetManager.load("assets/objs/esgoto_entrada.png", Texture.class);
		assetManager.load("assets/sounds/agua_jorrando.mp3", Sound.class);
		ObjectBueiro.loadResources(assetManager);
		ValterCharEsgoto.loadResources(assetManager);
		ObjectCorda.loadResources(assetManager);
		
		noise = new Texture[3];
		for(int i = 0; i < noise.length; i++){
			assetManager.load("assets/ruido/ruido" + (i + 1) + ".png", Texture.class);
		}
	}

	@Override
	public void createObjects() {
		map = assetManager.get("assets/maps/esgoto.tmx", TiledMap.class);
		background = assetManager.get("assets/texturas/fundo_esgoto.png", Texture.class);
		entrada = assetManager.get("assets/objs/esgoto_entrada.png", Texture.class);
		background.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		if(!playingBg){
			playingBg = true;
			assetManager.get("assets/sounds/agua_jorrando.mp3", Sound.class).loop(0.65f * ValtersOptions.SOUND_LEVEL);
		}

		noise = new Texture[3];
		for(int i = 0; i < noise.length; i++){
			noise[i] = assetManager.get("assets/ruido/ruido" + (i + 1) + ".png", Texture.class);
		}
		
		applyShader();
		
		ValterCharEsgoto v = new ValterCharEsgoto(64, 360, 0.75f);
		
		addObject(ValtersGame.valter = v);
		
		addObject(new ObjectBueiro(1520, 230, 0.4f, 0));
		addObject(new ObjectBueiro(1840, 230, 0.4f, 6));
		addObject(new ObjectCorda(2450, 700, 0.5f));
		
		createGround();
		
		ValtersGame.setCamDisplacement(0, 64f);
	}
	
	@Override
	protected void preMapRender(OrthographicCamera camera, SpriteBatch batch) {		
		float parallaxX = (camera.position.x - Gdx.graphics.getWidth() / 2) * 0.7f;
		batch.draw(background, parallaxX, ValtersGame.getCamDisplacement().y, background.getWidth(), ValtersOptions.SCREEN_HEIGHT);
	}
	
	@Override
	protected void inBatchRender(OrthographicCamera camera, SpriteBatch batch) {
		batch.draw(entrada, -16, 350, entrada.getWidth() * 0.8f, entrada.getHeight() * 0.9f);
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
	}
	
	private void createGround(){
		addObject(new ValtersObject("Ground", 0, 0, (byte) 0, true, false){
			@Override
			public void render(SpriteBatch batch) {}
			@Override
			public Rectangle getCollider(){
				addFlag("groundy");
				return new Rectangle(getPosition().x, getPosition().y, 383, 352);
			}
		});
		addObject(new ValtersObject("Ground", 897, 0, (byte) 0, true, false){
			@Override
			public void render(SpriteBatch batch) {}
			@Override
			public Rectangle getCollider(){
				addFlag("groundy");
				return new Rectangle(getPosition().x, getPosition().y, 544, 289);
			}
		});
		addObject(new ValtersObject("Ground", 1600, 0, (byte) 0, true, false){
			@Override
			public void render(SpriteBatch batch) {}
			@Override
			public Rectangle getCollider(){
				addFlag("groundy");
				return new Rectangle(getPosition().x, getPosition().y, 160, 288);
			}
		});
		addObject(new ValtersObject("Ground", 1920, 0, (byte) 0, true, false){
			@Override
			public void render(SpriteBatch batch) {}
			@Override
			public Rectangle getCollider(){
				addFlag("groundy");
				return new Rectangle(getPosition().x, getPosition().y, 160, 288);
			}
		});
		addObject(new ValtersObject("Ground", 2240, 0, (byte) 0, true, false){
			@Override
			public void render(SpriteBatch batch) {}
			@Override
			public Rectangle getCollider(){
				addFlag("groundy");
				return new Rectangle(getPosition().x, getPosition().y, 160, 288);
			}
		});
		addObject(new ValtersObject("Ground", 3648, 0, (byte) 0, true, false){
			@Override
			public void render(SpriteBatch batch) {}
			@Override
			public Rectangle getCollider(){
				addFlag("groundy");
				return new Rectangle(getPosition().x, getPosition().y, 544, 352);
			}
		});
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
			
			renderer = new OrthogonalTiledMapRenderer(map, 1f);
			renderer.getSpriteBatch().setShader(program);
		} catch (Exception e) { 
			System.err.println(e.getMessage());
			System.err.println("Could not load shaders, using regular SpriteBatch!");
			batch = new SpriteBatch();
			renderer = new OrthogonalTiledMapRenderer(map, 1f);
		}
	}

}

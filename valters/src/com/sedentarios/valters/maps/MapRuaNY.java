package com.sedentarios.valters.maps;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Bounce;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.characters.ValterChar;
import com.sedentarios.valters.objects.*;

public class MapRuaNY extends ValtersMap{
	private Texture background;
	private Sound backgroundMusic;
	//private Sound crowdNoise;
	private Texture[] noise;

	public MapRuaNY(){
		super(64,5638);
	}
	
	public MapRuaNY(int leftCap, int rightCap) {
		super(leftCap, rightCap);
	}

	@Override
	public void create() {

		assetManager.load("assets/Maps/RuaNY.tmx", TiledMap.class);
		assetManager.load("assets/Maps/fundo-parallax_1.png", Texture.class);
		assetManager.load("assets/Music/jazz_bg.wav", Sound.class);
		
		noise = new Texture[3];
		for(int i = 0; i < noise.length; i++){
			assetManager.load("assets/Ruido/ruido" + (i + 1) + ".png", Texture.class);
		}
		//crowdNoise = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/"));
	}
	
	@Override
	public void createObjects(){
		map = assetManager.get("assets/Maps/RuaNY.tmx", TiledMap.class);
		background = assetManager.get("assets/Maps/fundo-parallax_1.png", Texture.class);

		backgroundMusic = assetManager.get("assets/Music/jazz_bg.wav", Sound.class);
		backgroundMusic.loop(0.4f);

		noise = new Texture[3];
		for(int i = 0; i < noise.length; i++){
			noise[i] = assetManager.get("assets/Ruido/ruido" + (i + 1) + ".png", Texture.class);
		}

		applyShader();

		addObject(new ObjectCar());
		//addObject(new ValterChar(88, 250, 200, 280, 0.8f));
		final ValtersObject fallingValter;
		addObject(fallingValter = new ObjectFallingValter(20, 720));
		fallingValter.setEnabled(false);
		ValtersGame.valter = fallingValter;
		addObject(new ObjectJornal(900, 230));

		final int mapHash = this.hashCode();
		Tween.mark().delay(1f).setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				fallingValter.setEnabled(true);
				Timeline.createSequence().push(Tween.to(fallingValter, ObjectAccessor.POSITION_XY, 3.5f)
						.target(0, 250).ease(Bounce.OUT)).push(Tween.mark().delay(0.3f).setCallback(
					new TweenCallback() {			
						@Override
						public void onEvent(int type, BaseTween<?> source) {
							if(mapHash == ValtersGame.map.hashCode()){
								removeObject(fallingValter);
								addObject(ValtersGame.valter = new ValterChar(325, 250, 200, 280, 0.8f));
							}
						}
					}
				)).start(ValtersGame.tweenManager);
			}
		}).start(ValtersGame.tweenManager);
	}
	
	private float fps = 1f/19f;
	private byte currFrame = 0;
	private float exposure = 0f;
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

	
	@Override
	public void inBatchRender(OrthographicCamera camera, SpriteBatch batch) {
				
	}
	
	private void applyShader(){
		if(super.batch != null){
			super.batch.dispose();
			super.batch = null;
		}
		try {
			final String VERTEX = Gdx.files.internal("assets/Shaders/bw.vert").readString();
			final String FRAGMENT = Gdx.files.internal("assets/Shaders/bw.frag").readString();
						
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

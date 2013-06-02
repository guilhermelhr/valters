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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.characters.ValterChar;
import com.sedentarios.valters.objects.ObjectAccessor;
import com.sedentarios.valters.objects.ObjectCar;
import com.sedentarios.valters.objects.ObjectFallingValter;
import com.sedentarios.valters.objects.ObjectJornal;
import com.sedentarios.valters.objects.ValtersObject;

public class MapRuaNY extends ValtersMap{
	private Texture background;
	public static BitmapFont font = new BitmapFont();
	private Sound backgroundMusic;
	//private Sound crowdNoise;

	public MapRuaNY(){
		super(64,5638);
	}
	
	public MapRuaNY(int leftCap, int rightCap) {
		super(leftCap, rightCap);
	}

	@Override
	public void create() {
		font.setScale(5f);
		
		map = new TmxMapLoader().load("assets/Maps/RuaNY.tmx");
		
		applyShader();
		
		background = new Texture("assets/Maps/fundo-parallax_1.png");
		backgroundMusic = Gdx.audio.newSound(Gdx.files.internal("assets/Music/jazz_bg.wav"));
		backgroundMusic.loop(0.3f);
		//crowdNoise = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/"));
	}
	
	@Override
	public void createObjects(){
		addObject(new ObjectCar());
		//addObject(new ValterChar(88, 250, 200, 280, 0.8f));
		final ValtersObject fallingValter;
		addObject(fallingValter = new ObjectFallingValter(20, 720));
		fallingValter.setEnabled(false);
		ValtersGame.valter = fallingValter;
		addObject(new ObjectJornal(900, 230));
		
		Tween.mark().delay(1f).setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				fallingValter.setEnabled(true);
				Timeline.createSequence().push(Tween.to(fallingValter, ObjectAccessor.POSITION_XY, 3.5f)
						.target(0, 250).ease(Bounce.OUT)).push(Tween.mark().delay(0.3f).setCallback(
					new TweenCallback() {			
						@Override
						public void onEvent(int type, BaseTween<?> source) {
							removeObject(fallingValter);
							addObject(ValtersGame.valter = new ValterChar(380, 250, 200, 280, 0.8f));
						}
					}
				)).start(ValtersGame.tweenManager);
			}
		}).start(ValtersGame.tweenManager);
	}
	
	@Override
	public void dispose(){
		super.dispose();
		background.dispose();
	}
	
	@Override
	public void render(OrthographicCamera camera){
		super.render(camera);
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

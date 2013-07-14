package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.ValtersTexts;
import com.sedentarios.valters.characters.MendigoChar;
import com.sedentarios.valters.characters.ValterCharPlatformer;
import com.sedentarios.valters.objects.ObjectBox;
import com.sedentarios.valters.objects.ObjectCerca;
import com.sedentarios.valters.objects.ObjectColchao;
import com.sedentarios.valters.objects.ObjectFlyingPaper;
import com.sedentarios.valters.objects.ObjectGlass;
import com.sedentarios.valters.objects.ObjectGround;
import com.sedentarios.valters.objects.ObjectTelevision;
import com.sedentarios.valters.objects.ObjectWhiskey;
import com.sedentarios.valters.objects.TextBalloon;

public class MapRuaNY2 extends PlatformerMap {

	private Texture[] noise;
	private Sound background;
	public static boolean finished = false;
	
	@Override
	public void create() {
		//assetManager.load("assets/sounds/smw_coin.wav", Sound.class);
		//assetManager.load("assets/anim/moeda/moeda.txt" ,TextureAtlas.class);
		assetManager.load("assets/sounds/musica_terceira_fase.mp3", Sound.class);
		assetManager.load("assets/texturas/loja_tv_vidro.png", Texture.class);
		assetManager.load("assets/maps/ruany2.tmx", TiledMap.class);
		ObjectWhiskey.loadResources(assetManager);
		ObjectCerca.loadResources(assetManager);
		ObjectBox.loadResources(assetManager);
		ObjectColchao.loadResources(assetManager);
		ValterCharPlatformer.loadResources(assetManager);
		MendigoChar.loadResources(assetManager);
		ObjectTelevision.loadResources(assetManager);
		ObjectGlass.loadResources(assetManager);
		ObjectFlyingPaper.loadResources(assetManager);
		
		noise = new Texture[3];
		for(int i = 0; i < noise.length; i++){
			assetManager.load("assets/ruido/ruido" + (i + 1) + ".png", Texture.class);
		}
	}
	

	@Override
	public void createObjects() {
		MapRuaNY2.finished = false;
		ValterCharPlatformer.placedBottle = false;		
		ObjectWhiskey.tvRespawn = false;
		
		if(background == null){
			background = assetManager.get("assets/sounds/musica_terceira_fase.mp3", Sound.class);
			background.loop(ValtersOptions.MUSIC_LEVEL);
		}		
		
		ValtersGame.setZoom(1f);
		ValtersGame.yDisplacement = 360;
		map = assetManager.get("assets/maps/ruany2.tmx", TiledMap.class);
		//renderer = new OrthogonalTiledMapRenderer(map, 1.35f);
		applyShader();
		addObject(new ObjectGlass(128, 50, 1.38f));
		addObject(new ObjectGround(-5, 6000));
		addObject(new ObjectCerca(3154, 0, 1));
		addObject(new ObjectBox(3050, 195, (byte) 1, 0.5f, 0));
		addObject(new ObjectBox(2885, 20, (byte) 1, 0.5f, 0));
		addObject(new ObjectBox(2989, 20, (byte) 1, 0.8f, 1));
		addObject(new ObjectColchao(3950, 20, 0.75f));
		addObject(new MendigoChar(3700, 25, 0.82f));

		/*for(int i = 0; i < 10; i++){
			addObject(new ObjectCoin(500 + i * 200, 260));
		}*/

		addObject(ValtersGame.valter = new ValterCharPlatformer(64, 15, 0.8f));
		
		noise = new Texture[3];
		for(int i = 0; i < noise.length; i++){
			noise[i] = assetManager.get("assets/ruido/ruido" + (i + 1) + ".png", Texture.class);
		}
		
		for(int x = 0; x < 5; x++){
			for(int y = 0; y < Math.ceil(MathUtils.random() * 3); y++){
				addObject(new ObjectTelevision(x * 310 * 0.5f + 64, y * 200 * 0.5f + 52, 0.5f));
			}
		}
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
		
		if(finished){
			if(ValtersGame.valter.getPosition().x <= 843 && !ValtersGame.valter.hasFlag("realized_television")){
				ValtersGame.valter.addFlag("realized_television");
				addObject(new TextBalloon(ValtersGame.valter, ValtersTexts.get("Valter9"), -64, -20, false));
			}
			if(ValtersGame.valter.getPosition().x <= 65){
				ValtersGame.changeMap(MapRuaNY3.class);
			}
		}
	}
	
	@Override
	protected void postObjectRender(OrthographicCamera camera, SpriteBatch batch){
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
			
			renderer = new OrthogonalTiledMapRenderer(map, 1.35f);
			renderer.getSpriteBatch().setShader(program);
		} catch (Exception e) { 
			System.err.println(e.getMessage());
			System.err.println("Could not load shaders, using regular SpriteBatch!");
			batch = new SpriteBatch();
			renderer = new OrthogonalTiledMapRenderer(map, 1.35f);
		}
	}
}

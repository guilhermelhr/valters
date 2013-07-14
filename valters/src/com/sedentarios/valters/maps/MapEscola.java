package com.sedentarios.valters.maps;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.sedentarios.valters.ControllerWrapper;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.ValtersTexts;
import com.sedentarios.valters.characters.ValterChar;
import com.sedentarios.valters.objects.ObjectMesa;
import com.sedentarios.valters.objects.TextBalloon;
import com.sedentarios.valters.objects.ValtersObject;

public class MapEscola extends ValtersMap{
	Texture etimPlate;
	Texture terceiroPlate;
	Texture lixo;
	Texture sombra;
	ValtersObject valter;
	private Sound background;
	
	public MapEscola(){
		super(64, 4000);
	}
	
	public MapEscola(int leftCap, int rightCap) {
		super(leftCap, rightCap);
	}
	
	@Override
	public void create() {
		assetManager.load("assets/objs/placa.png", Texture.class);
		assetManager.load("assets/objs/3_placa.png", Texture.class);
		assetManager.load("assets/objs/lixo.png", Texture.class);
		assetManager.load("assets/anim/sombra.png", Texture.class);
		assetManager.load("assets/maps/escola.tmx", TiledMap.class);
		assetManager.load("assets/sounds/musica_escola.mp3", Sound.class);
	}
	
	@Override
	public void createObjects(){
		if(background == null){
			background = assetManager.get("assets/sounds/musica_escola.mp3", Sound.class);
			background.loop(ValtersOptions.MUSIC_LEVEL);
		}
		
		etimPlate = assetManager.get("assets/objs/placa.png", Texture.class);
		terceiroPlate = assetManager.get("assets/objs/3_placa.png", Texture.class);
		lixo = assetManager.get("assets/objs/lixo.png", Texture.class);
		sombra = assetManager.get("assets/anim/sombra.png", Texture.class);

		//map = new TmxMapLoader().load("assets/maps/escola.tmx");
		map = assetManager.get("assets/maps/escola.tmx", TiledMap.class);

		renderer = new OrthogonalTiledMapRenderer(map, 2.1f);

		addObject(valter = new ValterChar(88f, 20f, 0.9f));
		addObject(new ObjectMesa(770f, 160f));
		
		addObject(new TextBalloon(valter, ValtersTexts.get("Valter1"), 0, 0));
		
		ValtersGame.valter = valter;
	}
	
	@Override
	public void render(OrthographicCamera camera){
		super.render(camera);
		if(valter.getPosition().x > 2540 && valter.getPosition().x < 2750){
			if(ControllerWrapper.isInputActive("action")){
				ValtersGame.changeMap(MapWarp.class);
			}
		}
	}
	
	@Override
	public void inBatchRender(OrthographicCamera camera, SpriteBatch batch) {
		batch.draw(etimPlate, 4440, 390);
		batch.draw(terceiroPlate, 2620, 400);
		batch.draw(sombra, 471, 170, sombra.getWidth() * 0.5f, sombra.getHeight() * 0.5f);
		batch.draw(sombra, 2151, 170, sombra.getWidth() * 0.5f, sombra.getHeight() * 0.5f);
		batch.draw(lixo, 2150, 180, lixo.getWidth() * 1.3f, lixo.getHeight() * 1.3f);
		batch.draw(lixo, 470, 180, lixo.getWidth() * 1.3f, lixo.getHeight() * 1.3f);
	}
	
}
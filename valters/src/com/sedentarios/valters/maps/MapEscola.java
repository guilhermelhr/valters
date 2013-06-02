package com.sedentarios.valters.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.sedentarios.valters.ControllerWrapper;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.characters.ValterChar;
import com.sedentarios.valters.objects.ObjectMesa;
import com.sedentarios.valters.objects.ValtersObject;

public class MapEscola extends ValtersMap{
	Texture etimPlate;
	Texture lixo;
	Texture sombra;
	ValtersObject valter;
	
	public MapEscola(){
		super(64, 4000);
	}
	
	public MapEscola(int leftCap, int rightCap) {
		super(leftCap, rightCap);
	}
	
	@Override
	public void create() {		
		etimPlate = new Texture("assets/Objs/placa.png");
		lixo = new Texture("assets/Objs/lixo.png");
		sombra = new Texture("assets/Anim/sombra.png");
		
		map = new TmxMapLoader().load("assets/Maps/escola.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 2.1f);
	}
	
	@Override
	public void createObjects(){
		addObject(valter = new ValterChar(88f, 20f, 0.9f));
		addObject(new ObjectMesa(770f, 160f));
		
		ValtersGame.valter = valter;
	}
	
	public void dispose() {
		super.dispose();
		etimPlate.dispose();
	}
	
	@Override
	public void render(OrthographicCamera camera){
		super.render(camera);
		if(valter.getPosition().x > 2556 && valter.getPosition().x < 2773){
			if(ControllerWrapper.isInputActive("action")){
				dispose();
				ValtersGame.changeMap(new MapWarp(0,0));
			}
		}
	}
	
	@Override
	public void inBatchRender(OrthographicCamera camera, SpriteBatch batch) {
		batch.draw(etimPlate, 4440, 390);
		batch.draw(sombra, 471, 170, sombra.getWidth() * 0.5f, sombra.getHeight() * 0.5f);
		batch.draw(sombra, 2151, 170, sombra.getWidth() * 0.5f, sombra.getHeight() * 0.5f);
		batch.draw(lixo, 2150, 180, lixo.getWidth() * 1.3f, lixo.getHeight() * 1.3f);
		batch.draw(lixo, 470, 180, lixo.getWidth() * 1.3f, lixo.getHeight() * 1.3f);
	}
	
}
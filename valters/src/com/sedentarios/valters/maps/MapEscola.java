package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.characters.ValterChar;
import com.sedentarios.valters.objects.ObjectMesa;
import com.sedentarios.valters.objects.ValtersObject;

public class MapEscola extends ValtersMap{	
	Texture etimPlate;
	ValtersObject valter;
	
	@Override
	public void create() {
		map = new TmxMapLoader().load("data/Maps/escola.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 2.1f);
		
		etimPlate = new Texture("data/Objs/placa.png");
		
		addObject(new ValterChar(88f, 20f));
		addObject(new ObjectMesa(1120f, 30f));
		
		leftCap = 64;
		rightCap = 4000;
		valter = this.getObject("valter");
	}
	
	public void dispose() {
		map.dispose();
		batch.dispose();
		etimPlate.dispose();
		renderer.dispose();
	}
	
	@Override
	public void render(OrthographicCamera camera){
		super.render(camera);
		if(valter.getPosition().x > 2556 && valter.getPosition().x < 2773){
			if(Gdx.input.isKeyPressed(Keys.E) || ValtersGame.controller.getButton(0)){
				ValtersGame.clearStage();
				ValtersGame.map = new MapWarp();
				ValtersGame.map.create();
			}
		}
	}
	
	@Override
	public void inBatchRender(SpriteBatch batch) {
		batch.draw(etimPlate, 4440, 390);
	}
	
}
package com.sedentarios.valters;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sedentarios.valters.maps.MapEscola;
import com.sedentarios.valters.maps.ValtersMap;
import com.sedentarios.valters.objects.ValtersObject;

public class ValtersGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	public static ValtersMap map;
	public static ValtersObject valter;
	public static Controller controller;
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		batch = new SpriteBatch();
		
		map = new MapEscola();
		map.create();
		
		if(Controllers.getControllers().size > 0){
			controller = Controllers.getControllers().first();
		}
	}
	
	public static void clearStage(){
		map.dispose();
		map = null;
		valter.dispose();
		valter = null;
	}

	@Override
	public void dispose() {
		if(batch != null) batch.dispose();
		if(map != null) map.dispose();
	}
	
	boolean showMousePos = false;
	
	@Override
	public void render() {
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(map == null){
			return;
		}
		
		if(Gdx.input.isKeyPressed(Keys.M)){
			showMousePos = !showMousePos;
		}
		
		map.render(camera);
		
		if(valter != null) {
			camera.position.set(Math.max(ValtersGame.map.getLeftCap() + Gdx.graphics.getWidth() / 2 - 60, Math.min(ValtersGame.map.getRightCap(),(int) valter.getPosition().x)), 360, 0);
		}else {
			if(map != null){
				valter = map.getObject("valter");
			}else{
				return;
			}
		}
		
		camera.update();
		
		map.postUpdate();
		
		if(showMousePos){
			System.out.println((valter.getPosition().x) + " " + (valter.getPosition().y));
		}
	}

	@Override
	public void resize(int width, int height) {
		//camera.setToOrtho(false, width, height);
		//camera.zoom = 1f;
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}
}

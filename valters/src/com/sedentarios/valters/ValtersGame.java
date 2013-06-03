package com.sedentarios.valters;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sedentarios.valters.maps.MapRuaNY;
import com.sedentarios.valters.maps.ValtersMap;
import com.sedentarios.valters.objects.ObjectAccessor;
import com.sedentarios.valters.objects.ValtersObject;

public class ValtersGame implements ApplicationListener {
	private OrthographicCamera camera;
	public static ValtersMap map;
	public static ValtersObject valter;
	public static Controller controller;
	public static TweenManager tweenManager;
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		
		tweenManager = new TweenManager();
		Tween.registerAccessor(ValtersObject.class, new ObjectAccessor());
		
		if(Controllers.getControllers().size > 0){
			controller = Controllers.getControllers().first();
		}
		
		ControllerWrapper.bindAxisToInput(-1, "up");
		ControllerWrapper.bindAxisToInput(1, "down");
		ControllerWrapper.bindAxisToInput(2, "right");
		ControllerWrapper.bindAxisToInput(-2, "left");
		
		ControllerWrapper.bindButtonToInput(0, "action");
		ControllerWrapper.bindButtonToInput(1, "run");
		
		ControllerWrapper.bindKeyToInput(Keys.W, "up");
		ControllerWrapper.bindKeyToInput(Keys.S, "down");
		ControllerWrapper.bindKeyToInput(Keys.D, "right");
		ControllerWrapper.bindKeyToInput(Keys.A, "left");
		
		ControllerWrapper.bindKeyToInput(Keys.E, "action");
		ControllerWrapper.bindKeyToInput(Keys.SHIFT_LEFT, "run");
		
		//changeMap(new MapEscola());
		changeMap(new MapRuaNY());
	}
	
	public static void clearStage(){	
		if(map != null && !map.disposed){
			map.dispose();
			map = null;
		}
		if(valter != null){
			valter.dispose();
			valter = null;
		}
	}
	
	public static void changeMap(ValtersMap map){
		if(map != null){
			clearStage();
			ValtersGame.map = map;
			map.create();
			map.createObjects();
			System.out.println("Map changed to " + map.getClass().getSimpleName());
		}else{
			System.err.println("Game tried to load null map");
		}
	}

	@Override
	public void dispose() {
		if(map != null && !map.disposed) map.dispose();
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
			camera.position.set(Math.max(ValtersGame.map.getLeftCap() + Gdx.graphics.getWidth() / 2 - 60, Math.min(ValtersGame.map.getRightCap(),(int) valter.getPosition().x + 50)), 360, 0);
		}else {
			if(map != null){
				valter = map.getObject("valter");
			}else{
				return;
			}
		}
		
		camera.update();
		
		map.postUpdate();
		
		tweenManager.update(Gdx.graphics.getDeltaTime());
		
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

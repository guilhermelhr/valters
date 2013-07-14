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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.sedentarios.valters.maps.MapMenu;
import com.sedentarios.valters.maps.ValtersMap;
import com.sedentarios.valters.objects.ObjectAccessor;
import com.sedentarios.valters.objects.ValtersObject;

public class ValtersGame implements ApplicationListener {
	private static OrthographicCamera camera;
	public static ValtersMap map;
	public static ValtersObject valter;
	public static Controller controller;
	public static TweenManager tweenManager;
	public static BitmapFont font;
	public static BitmapFont font18;
	public static int yDisplacement = 360;
	public static int xDisplacement = 0;	
	
	@Override
	public void create() {
		ValtersTexts.load();
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);

		tweenManager = new TweenManager();
		Tween.registerAccessor(ValtersObject.class, new ObjectAccessor());

		font = new BitmapFont(Gdx.files.internal("assets/fontes/helvetica.fnt"),
							Gdx.files.internal("assets/fontes/helvetica.png"), false);
		
		font18 = new BitmapFont(Gdx.files.internal("assets/fontes/helvetica_18.fnt"),
				Gdx.files.internal("assets/fontes/helvetica_18.png"), false);

		if(Controllers.getControllers().size > 0){
			controller = Controllers.getControllers().first();
		}
		
		ControllerWrapper.bindAxisToInput(-1, "up");
		ControllerWrapper.bindAxisToInput(1, "down");
		ControllerWrapper.bindAxisToInput(2, "right");
		ControllerWrapper.bindAxisToInput(-2, "left");

		ControllerWrapper.bindButtonToInput(1, "action");
		ControllerWrapper.bindButtonToInput(7, "run");
		ControllerWrapper.bindButtonToInput(0, "jump");
		
		ControllerWrapper.bindButtonToInput(2, "punch");
		ControllerWrapper.bindButtonToInput(3, "kick");

		ControllerWrapper.bindButtonToInput(4, "exit");

		ControllerWrapper.bindKeyToInput(Keys.W, "up");
		ControllerWrapper.bindKeyToInput(Keys.S, "down");
		ControllerWrapper.bindKeyToInput(Keys.D, "right");
		ControllerWrapper.bindKeyToInput(Keys.A, "left");

		ControllerWrapper.bindKeyToInput(Keys.SPACE, "jump");

		ControllerWrapper.bindKeyToInput(Keys.ESCAPE, "exit");
		ControllerWrapper.setInputDelay("exit", 1f);
		
		ControllerWrapper.bindKeyToInput(Keys.E, "action");
		ControllerWrapper.setInputDelay("action", 0.1f);
		ControllerWrapper.bindKeyToInput(Keys.ENTER, "enter");
		ControllerWrapper.setInputDelay("enter", 0.25f);
		ControllerWrapper.bindKeyToInput(Keys.SHIFT_LEFT, "run");
		
		ControllerWrapper.bindKeyToInput(Keys.L, "punch");
		ControllerWrapper.bindKeyToInput(Keys.K, "kick");
		
		//changeMap(MapCreditos.class);
		changeMap(MapMenu.class);
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
	
	private static ValtersMap nextMap;
	public static void changeMap(Class<? extends ValtersMap> c){
		try {
			nextMap = c.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static void changeMap2(ValtersMap map){
		nextMap = map;
	}
	
	private static void changeMap(ValtersMap map){
		if(map != null){
			yDisplacement = 360;
			xDisplacement = 0;	
			setCamDisplacement(0, 0);
			ValtersGame.setZoom(1f);
			
			clearStage();
			ValtersGame.map = map;
			map.create();
			cameraOnValter = false;
			System.out.println("Map changed to " + map.getClass().getSimpleName());
		}else{
			System.err.println("Game tried to load null map");
		}
		nextMap = null;
	}

	@Override
	public void dispose() {
		if(map != null && !map.disposed) map.dispose();
	}
	
	public static void setZoom(float zoom){
		camera.zoom = zoom;
	}
	
	public static void setCamDisplacement(float x, float y){
		camDisp.set(x, y, 0);
	}
	
	public static Vector3 getCamDisplacement(){
		return camDisp;
	}
	
	public static void setCamPosition(float x, float y){
		targetCamPos.set(x, y, 0);
		camPos.set(x, y, 0);
	}
	
	public static Vector3 getCamPosition(){
		return camPos;
	}

	boolean showMousePos = false;
	static Vector3 camDisp = new Vector3();
	static Vector3 targetCamPos = new Vector3();
	static Vector3 camPos = new Vector3();
	static boolean cameraOnValter = false;
	@Override
	public void render() {
		ControllerWrapper.update();

		if(map == null && nextMap == null){
			return;
		}else if(nextMap != null){
			changeMap(nextMap);
			return;
		}else if(!map.assetManager.update()){
			map.renderLoading();
			return;
		}else if(!map.loaded){
			map.createObjects();
			map.loaded = true;
			System.out.println("Map loaded");
		}

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isKeyPressed(Keys.M)){
			showMousePos = !showMousePos;
		}

		map.render(camera);

		if(valter != null) {			
			targetCamPos.set(Math.max(ValtersGame.map.getLeftCap() +
					ValtersOptions.SCREEN_WIDTH / 2 - 60, Math.min(ValtersGame.map.getRightCap(),
					(int) valter.getPosition().x + 50)) + xDisplacement, yDisplacement, 0);
			targetCamPos.add(camDisp);
			
			if(!cameraOnValter){
				camPos.set(targetCamPos);
				cameraOnValter = true;
			}
		}else {
			valter = map.getObject("valter");
		}

		map.postUpdate();

		tweenManager.update(Gdx.graphics.getDeltaTime());
		
		camPos.lerp(targetCamPos, 0.1f);
		camera.position.set((int) camPos.x, (int) camPos.y, 0);
		
		camera.update();

		if(showMousePos && valter != null){
			System.out.println((valter.getPosition().x) + " " + (valter.getPosition().y));
		}

		if(ControllerWrapper.isInputActive("exit")){
			if(map != null && !map.disposed){
				map.onEscape();
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		if(!ValtersOptions.LIMITE_HORIZONTAL){
			camera.setToOrtho(false, width, ValtersOptions.SCREEN_HEIGHT);
			ValtersOptions.SCREEN_WIDTH = width;
		}else{
			camera.setToOrtho(false, ValtersOptions.SCREEN_BASE_WIDTH, ValtersOptions.SCREEN_HEIGHT);
		}
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}
}

package com.sedentarios.valters;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sedentarios.valters.maps.MapEscola;
import com.sedentarios.valters.objects.ValtersObject;

public class ValtersGame implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private MapEscola map;
	ValtersObject valter;
	
	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		batch = new SpriteBatch();
		
		map = new MapEscola();
		map.create();
	}

	@Override
	public void dispose() {
		batch.dispose();
		map.dispose();
	}
	
	@Override
	public void render() {
		System.out.println(Gdx.input.getX() + camera.position.x - 1280 / 2);
		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		map.render(camera);
		
		if(valter != null) {
			camera.position.set(Math.max(640f, Math.min(2048,(int) valter.getPosition().x)), 190, 0);
		}else {
			valter = map.getObject("valter");
		}
		
		camera.update();
		
		map.postUpdate();
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
		camera.zoom = 380f/height;
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}

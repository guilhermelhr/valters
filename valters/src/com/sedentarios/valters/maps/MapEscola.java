package com.sedentarios.valters.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.sedentarios.valters.objects.ObjectMesa;

public class MapEscola {
	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	Texture etimPlate;
	SpriteBatch batch;
	
	ObjectMesa mesa;
	
	public void create() {
		map = new TmxMapLoader().load("data/Maps/escola.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1.2f);
		etimPlate = new Texture("data/Objs/placa.png");
		batch = new SpriteBatch();
		mesa = new ObjectMesa();
		mesa.create();
	}
	
	public void dispose() {
		map.dispose();
		mesa.dispose();
		batch.dispose();
		etimPlate.dispose();
		renderer.dispose();
	}
	
	public void render(OrthographicCamera camera) {
		renderer.setView(camera);
		renderer.render();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(etimPlate, 2522, 213);
		mesa.render(batch);
		batch.end();
	}

}

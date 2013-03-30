package com.sedentarios.valters.maps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.sedentarios.valters.characters.ValterChar;
import com.sedentarios.valters.objects.ObjectMesa;

public class MapEscola extends ValtersMap{	
	Texture etimPlate;
	
	public void create() {
		map = new TmxMapLoader().load("data/Maps/escola.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1.2f);
		
		etimPlate = new Texture("data/Objs/placa.png");
		
		addObject(new ValterChar(88f, 20f));
		addObject(new ObjectMesa(1120f, 30f));
		
	}
	
	public void dispose() {
		map.dispose();
		batch.dispose();
		etimPlate.dispose();
		renderer.dispose();
	}
	
	@Override
	public void inBatchRender(SpriteBatch batch) {
		batch.draw(etimPlate, 2522, 213);
	}

}

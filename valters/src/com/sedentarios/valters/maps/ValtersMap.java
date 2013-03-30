package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.objects.ValtersObject;

public class ValtersMap {
	
	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	SpriteBatch batch;
	
	private Array<ValtersObject> objects;
	private Array<ValtersObject> toBeRemoved;
	
	public ValtersMap() {
		objects = new Array<ValtersObject>();
		toBeRemoved = new Array<ValtersObject>();
		batch = new SpriteBatch();
	}
	
	public void addObject(ValtersObject object) {
		objects.add(object);
		object.create();
	}
	
	public void removeObject(ValtersObject object) {
		if(!toBeRemoved.contains(object, false)) {
			toBeRemoved.add(object);
		}
	}
	
	public ValtersObject getObject(String name) {
		for(ValtersObject object : objects) {
			if(object.getName().equals(name)) {
				return object;
			}
		}
		return null;
	}
		
	private ValtersObject[] calculateDepth(ValtersObject[] depthBuffer) {
		quickSort(depthBuffer, 0, depthBuffer.length - 1);
		
		return depthBuffer;
	}
	
	public void render(OrthographicCamera camera) {
		renderer.setView(camera);
		renderer.render();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		inBatchRender(batch);
		for(ValtersObject object : calculateDepth(objects.toArray(ValtersObject.class))) {
			object.render(batch);
		}
		batch.end();
	}
	
	protected void inBatchRender(SpriteBatch batch) {}
	
	public void postUpdate() {
		for(ValtersObject object : toBeRemoved) {
			object.dispose();
			objects.removeValue(object, false);
			Gdx.app.log("Object Processor", "Object " + object.hashCode() + " was removed");
		}
		toBeRemoved.clear();
	}
	
	public void dispose() {
		toBeRemoved.clear();
		for(ValtersObject object : objects) {
			object.dispose();
		}
		objects.clear();
		toBeRemoved = null;
		objects = null;
		
		map.dispose();
		renderer.dispose();
		batch.dispose();
	}
	
	public static void quickSort(ValtersObject[] v, int inicio, int fim){
        if(inicio<fim){
            int pivo=inicio, i=fim;
            ValtersObject vPivo=v[pivo];
            while(pivo<i){
                if(vPivo.getPosition().y < v[i].getPosition().y){
                    v[pivo]=v[i];
                    pivo=i;
                    i=inicio+1;
                    while(pivo>i){
                        if(vPivo.getPosition().y > v[i].getPosition().y){
                            v[pivo]=v[i];
                            pivo=i;
                            i=fim;
                            break;
                         }else
                            i++;
                    }
                } else 
                    i--;
           }
           v[pivo]=vPivo;
           quickSort(v, inicio, pivo-1);
           quickSort(v, pivo+1, fim);
        }
    }
}

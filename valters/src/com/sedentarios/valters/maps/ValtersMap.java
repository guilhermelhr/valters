package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.objects.ValtersObject;

public abstract class ValtersMap {
	
	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	protected SpriteBatch batch;
	
	private Array<Array<ValtersObject>> layers;
	private Array<ValtersObject> toBeRemoved;
	
	protected int leftCap, rightCap;
	
	public boolean disposed = false;
	
	public static final byte LAYERS = 3; 
	
	private float runtime = 0f;
	
	private static boolean debugCollision = false;
	
	public ValtersMap(int leftCap, int rightCap) {
		layers = new Array<Array<ValtersObject>>();
		for(int i = 0; i < LAYERS; i++){
			layers.add(new Array<ValtersObject>());
		}
		toBeRemoved = new Array<ValtersObject>();
		batch = new SpriteBatch();
		this.leftCap = leftCap;
		this.rightCap = rightCap;
	}
	
	public abstract void create();
	public abstract void createObjects();
	
	public void addObject(ValtersObject object) {
		layers.get(object.getLayer()).add(object);
		object.create();
	}
	
	public void removeObject(ValtersObject object) {
		if(!toBeRemoved.contains(object, false)) {
			toBeRemoved.add(object);
		}
	}
	
	public ValtersObject getObject(String name) {
		for(Array<ValtersObject> objects : layers){
			for(ValtersObject object : objects) {
				if(object.getName().equals(name)) {
					return object;
				}
			}
		}
		return null;
	}
		
	private ValtersObject[] calculateDepth(Array<ValtersObject> objects) {
		ValtersObject[] depthBuffer = new ValtersObject[objects.size];
		
		for(int i = 0; i < objects.size; i++) {
			depthBuffer[i] = objects.get(i);
		}
		
		quickSort(depthBuffer, 0, depthBuffer.length - 1);
		
		return depthBuffer;
	}
	
	public void render(OrthographicCamera camera) {
		runtime += Gdx.graphics.getDeltaTime();
		batch.begin();
		preMapRender(camera, batch);
		batch.end();
		if(renderer != null){
			renderer.setView(camera);
			renderer.render();
		}
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		inBatchRender(camera, batch);
		for(Array<ValtersObject> layer : layers){
			for(ValtersObject object : calculateDepth(layer)) {
				object.internalRender(batch);
				if(object.isWaitingRemoval()){
					removeObject(object);
				}
			}
		}
		postObjectRender(camera, batch);		
		batch.end();
		
		if(debugCollision){
			for(Array<ValtersObject> layer : layers){
				for(ValtersObject obj : layer){
					if(obj.getCollisionComponent() != null){
						shapeRenderer.begin(ShapeType.Line);
						shapeRenderer.rect(obj.getCollisionComponent().getRect().x + Gdx.graphics.getWidth() / 2 - camera.position.x,
								obj.getCollisionComponent().getRect().y,
								obj.getCollisionComponent().getRect().width, obj.getCollisionComponent().getRect().height);
						shapeRenderer.end();
					}
				}
			}
		}
	}
	ShapeRenderer shapeRenderer = new ShapeRenderer();
	
	protected void postObjectRender(OrthographicCamera camera, SpriteBatch batch){}
	
	protected void preMapRender(OrthographicCamera camera, SpriteBatch batch){}
	
	protected void inBatchRender(OrthographicCamera camera, SpriteBatch batch) {}
	
	public void postUpdate() {		
		for(ValtersObject object : toBeRemoved) {
			object.dispose();
			for(Array<ValtersObject> layer : layers){
				layer.removeValue(object, false);
			}
			Gdx.app.log("Object Processor", "Object " + object.getName() + 
					"[" + object.hashCode() + "@layer" + object.getLayer() + "] was removed");
		}
		toBeRemoved.clear();
		
		if(Gdx.input.isKeyPressed(Keys.R)){
			clearObjects();
			ValtersGame.valter = null;
			createObjects();
			System.out.println("Objects recreated");
		}
	}
	
	public int getLeftCap() {
		return leftCap;
	}

	public int getRightCap() {
		return rightCap;
	}
	
	public float getRuntime(){
		return runtime;
	}
	
	private void clearObjects() {
		toBeRemoved.clear();
		for(Array<ValtersObject> layer : layers){
			for(ValtersObject object : layer) {
				object.dispose();
			}
		}
		for(Array<ValtersObject> layer : layers){
			layer.clear();		
		}
	}

	public void dispose() {
		clearObjects();
		toBeRemoved = null;
		layers.clear();
		layers = null;
		
		if(map != null) map.dispose();
		if(renderer != null) renderer.dispose();
		if(batch != null) batch.dispose();
		
		disposed = true;
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

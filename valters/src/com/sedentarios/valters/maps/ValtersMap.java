package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.CollisionManager;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.objects.TextBalloon;
import com.sedentarios.valters.objects.ValtersObject;
import com.sedentarios.valters.ui.UIScene;

public abstract class ValtersMap {
	
	TiledMap map;
	OrthogonalTiledMapRenderer renderer;
	protected SpriteBatch batch;
	
	private Array<Array<ValtersObject>> layers;
	private Array<ValtersObject> behindMap;
	private Array<ValtersObject> toBeRemoved;
	public AssetManager assetManager;
	
	protected int leftCap, rightCap;
	
	public boolean disposed = false;
	public boolean loaded = false;
	
	public static final byte LAYERS = 3; 
	
	private float runtime = 0f;
	
	private static boolean debugCollision = false;

	protected Array<UIScene> uiScenes;
	
	public ValtersMap(int leftCap, int rightCap) {
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		
		TextBalloon.loadResources(assetManager);
		
		uiScenes = new Array<UIScene>();

		layers = new Array<Array<ValtersObject>>();
		for(int i = 0; i < LAYERS; i++){
			layers.add(new Array<ValtersObject>());
		}
		behindMap = new Array<ValtersObject>();
		toBeRemoved = new Array<ValtersObject>();
		batch = new SpriteBatch();

		this.leftCap = leftCap;
		this.rightCap = rightCap;
	}

	/** For loadable assets **/
	public abstract void create();
	/** Other assets/objects **/
	public abstract void createObjects();
	
	public void addObject(ValtersObject object) {
		if(object.getLayer() >= 0){
			layers.get(object.getLayer()).add(object);
		}else{
			behindMap.add(object);
		}
		
		object.create();		
	}
	
	public void removeObject(ValtersObject object) {
		if(toBeRemoved != null && !toBeRemoved.contains(object, false)) {
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
		
		for(ValtersObject object : behindMap) {
			if(object.getName().equals(name)) {
				return object;
			}
		}
		
		return null;
	}
	
	public Array<ValtersObject> getObjects(String name){
		Array<ValtersObject> subjects = new Array<ValtersObject>(); 
		for(Array<ValtersObject> objects : layers){
			for(ValtersObject object : objects) {
				if(object.getName().equals(name)) {
					subjects.add(object);
				}
			}
		}
		
		for(ValtersObject object : behindMap) {
			if(object.getName().equals(name)) {
				subjects.add(object);
			}
		}
		return subjects;
	}
		
	private ValtersObject[] calculateDepth(Array<ValtersObject> objects) {
		ValtersObject[] depthBuffer = new ValtersObject[objects.size];
		
		for(int i = 0; i < objects.size; i++) {
			depthBuffer[i] = objects.get(i);
		}
		
		quickSort(depthBuffer, 0, depthBuffer.length - 1);
		
		return depthBuffer;
	}

	public void renderLoading(){
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin();
		ValtersGame.font.draw(batch, String.valueOf((int) (assetManager.getProgress() * 100)) + "%", Gdx.graphics.getWidth() / 2 - 32, 200);
		batch.end();
		//System.out.println(assetManager.getProgress());
	}
	
	public void render(OrthographicCamera camera) {
		runtime += Gdx.graphics.getDeltaTime();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(ValtersObject object : behindMap) {
			object.internalRender(batch);
			if(object.isWaitingRemoval()){
				removeObject(object);
			}
		}
		preMapRender(camera, batch);
		batch.end();
		if(renderer != null){
			renderer.setView(camera);
			renderer.render();
		}
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
		
		for(ValtersObject object : behindMap) {
			object.postObjectsRender(batch);
		}
		
		for(Array<ValtersObject> layer : layers){
			for(ValtersObject object : calculateDepth(layer)) {
				object.postObjectsRender(batch);
			}
		}
		postObjectRender(camera, batch);
		batch.end();
		
		/* collision debug */
		if(debugCollision && layers != null){
			for(Array<ValtersObject> layer : layers){
				for(ValtersObject obj : layer){
					if(obj.getCollisionComponent() != null){
						shapeRenderer.begin(ShapeType.Line);
						Rectangle rect = obj.getCollisionComponent().getRect();
						shapeRenderer.rect(rect.x + ValtersOptions.SCREEN_WIDTH / 2 - ValtersGame.getCamPosition().x,
								rect.y - ValtersGame.getCamDisplacement().y,
								rect.width, rect.height);
						shapeRenderer.end();
					}
				}
			}
			for(ValtersObject obj : behindMap){
				if(obj.getCollisionComponent() != null){
					shapeRenderer.begin(ShapeType.Line);
					Rectangle rect = obj.getCollisionComponent().getRect();
					shapeRenderer.rect(rect.x + ValtersOptions.SCREEN_WIDTH / 2 - ValtersGame.getCamPosition().x,
							rect.y - ValtersGame.getCamDisplacement().y,
							rect.width, rect.height);
					shapeRenderer.end();
				}
			}
		}
		
		camera.position.x = ValtersOptions.SCREEN_WIDTH / 2;
		camera.position.y = ValtersOptions.SCREEN_HEIGHT / 2;
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		batch.begin();
		for(UIScene ui : uiScenes){
			if(ui != null && ui.isActive()){
				ui.render(batch);
			}
		}
		batch.end();
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
			behindMap.removeValue(object, false);
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
	
	public UIScene getUIScene(String name){
		for(UIScene ui : uiScenes){
			if(ui.getName().equals(name)){
				return ui;
			}
		}
		
		return null;
	}
	
	public void removeUIScene(String name){
		UIScene delete = null;
		for(UIScene ui : uiScenes){
			if(ui.getName().equals(name)){
				delete = ui;
				break;
			}
		}
		
		if(delete != null && uiScenes.removeValue(delete, false)){
			System.out.println(String.format("UI Processor: UI %s[%d] was removed", delete.getName(), delete.hashCode()));
		}
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
		behindMap.clear();
	}

	public void dispose() {
		for(UIScene ui : uiScenes){
			if(ui != null) ui.dispose();
		}
		uiScenes.clear();
		
		clearObjects();
		toBeRemoved = null;
		layers.clear();
		layers = null;
		CollisionManager.clearComponents();
		
		if(map != null) map.dispose();
		if(renderer != null) renderer.dispose();
		if(batch != null) batch.dispose();

        try{
	        assetManager.clear();
        }catch(Exception ex){
        	ex.printStackTrace();
	        assetManager.dispose();
	        assetManager.clear();
        }

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

	public void onEscape() {
		ValtersGame.changeMap(MapMenu.class);
	}
}

package com.sedentarios.valters.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sedentarios.valters.ControllerWrapper;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.maps.MapEsgoto;
import com.sedentarios.valters.maps.MapFight;
import com.sedentarios.valters.maps.MapGameOver;
import com.sedentarios.valters.objects.CollisionComponent;

public class ValterCharEsgoto extends ValterChar{

	private Animation corda;
	private boolean onCorda = false;
	private boolean finishedCorda = false;
	
	public ValterCharEsgoto(float x, float y, float scale) {
		super(x, y, -256, 720, scale);
		setAllowYMovement(false);
		setGravity(-15);
		fixedShadow = true;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/anim/cadeia_parado.png", Texture.class);
		for(int i = 5; i <= 15; i++){
			assetManager.load("assets/anim/cadeia_andar/cadeia_andar00" + i + ".png", Texture.class);
		}
		
		assetManager.load("assets/anim/corda/corda.txt", TextureAtlas.class);
	}
	
	@Override
	public void create(){
		corda = new Animation(1f/19f, getMap().assetManager.get("assets/anim/corda/corda.txt", TextureAtlas.class).getRegions());
		corda.setPlayMode(Animation.LOOP_PINGPONG);
		super.create();
	}
	
	@Override
	public Rectangle getCollider(){
		return new Rectangle(position.x, position.y, 128 * scale, 365 * scale);
	}
	
	@Override
	public void update(){
		if(position.y <= -256){
			ValtersGame.changeMap2(new MapGameOver(MapEsgoto.class));
		}
		
		if(position.x >= 3995){
			ValtersGame.changeMap(MapFight.class);
		}
		super.update();
	}
	
	private float stateTime = 0f;
	private boolean inputReleased = true;
	public static boolean placedBottle = false;
	@Override
	public void render(SpriteBatch batch){
		inputReleased = inputReleased || !(ControllerWrapper.isInputActive("punch") || ControllerWrapper.isInputActive("kick"));
		if(onCorda){
			stateTime += Gdx.graphics.getDeltaTime();
			TextureRegion frame = corda.getKeyFrame(stateTime);
			batch.draw(frame, getPosition().x, getPosition().y, frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
			
			if(ControllerWrapper.isInputActive("right")){
				position.x += Gdx.graphics.getDeltaTime() * 80f;
				if(position.x >= 3422){
					onCorda = false;
					finishedCorda = true;
					setGravity(-15);
					applyForce(new Vector2(100, 0));
				}
			}
		}else{
			super.render(batch);
		}
	}
	
	@Override
	public void postObjectsRender(SpriteBatch batch){
		super.postObjectsRender(batch);
	}
	
	@Override
	public void renderShadow(SpriteBatch batch){
		return;
	}
	
	@Override
	public void onCollision(CollisionComponent other){
		if(!finishedCorda && !onCorda && other.getOwner().getName().equals("Corda")){
			onCorda = true;
			stateTime = 0f;
			setGravity(0f);
			position.y = 450f;
		}
		super.onCollision(other);
	}
	
}

package com.sedentarios.valters.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.CollisionManager;
import com.sedentarios.valters.ControllerWrapper;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.objects.CollisionComponent;
import com.sedentarios.valters.objects.ObjectWhiskey;

public class ValterCharPlatformer extends ValterChar {
	
	private boolean throwing = false;
	public static final int NONE = -1;
	public static final int PUNCHING = 0;
	public static final int KICKING = 1;
	public int attack = NONE;
	
	public Array<ObjectWhiskey> throwables;
	
	private TextureRegion arm;
	private Texture noArmValter;
	private float rotation = 0.65f;
	private ObjectWhiskey whiskey;
	
	private Animation punching;
	private Animation kicking;

	public ValterCharPlatformer(float x, float y, float scale) {
		super(x, y, -5, 720, scale);
		setAllowYMovement(false);
		setGravity(-15);
		fixedShadow = true;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/anim/valter_braco.png", Texture.class);
		assetManager.load("assets/anim/valter_sem_braco.png", Texture.class);
		assetManager.load("assets/anim/lutar_chute/chute.txt", TextureAtlas.class);
		assetManager.load("assets/anim/lutar_soco/soco.txt", TextureAtlas.class);
	}
	
	@Override
	public void create(){
		super.create();
		ValtersGame.setCamDisplacement(0, 0);
		arm = new TextureRegion(getMap().assetManager.get("assets/anim/valter_braco.png", Texture.class));
		arm.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		noArmValter = getMap().assetManager.get("assets/anim/valter_sem_braco.png");
		noArmValter.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		kicking = new Animation(1f/24f, 
				getMap().assetManager.get("assets/anim/lutar_chute/chute.txt", TextureAtlas.class).getRegions());		
		punching = new Animation(1f/24f,
				getMap().assetManager.get("assets/anim/lutar_soco/soco.txt", TextureAtlas.class).getRegions());
		
		getCollisionComponent().ignore.add("Mendigo");
	}
	
	@Override
	public Rectangle getCollider(){
		return new Rectangle(position.x, position.y, 128 * scale, 365 * scale);
	}
	
	@Override
	public void update(){
		if(ControllerWrapper.isInputActive("action") && throwables != null){
			getBottle();
		}
		//if(throwing){
			super.update();
		//}
	}
	
	private void getBottle(){
		if(throwables.size > 0){
			throwing = true;
			throwables.pop().requestDeath();
			getMap().addObject(whiskey = new ObjectWhiskey(getPosition().x + 40, getPosition().y + 84, 1));
		}
	}
	
	private float angularSpeed = 0.75f;
	private float resultingSpeed = 0f;
	private boolean throwStarted = false;
	private boolean returning = false;
	private float throwStartedAt = rotation;
	private float stateTime = 0f;
	private boolean inputReleased = true;
	public static boolean placedBottle = false;
	@Override
	public void render(SpriteBatch batch){
		inputReleased = inputReleased || !(ControllerWrapper.isInputActive("punch") || ControllerWrapper.isInputActive("kick"));
		if(throwing){
			ValtersGame.setCamDisplacement(400, 0);
			if(!throwStarted){				
				if((throwables.size == 0 || placedBottle)){
					if(whiskey != null){
						whiskey.requestDeath();
					}
					throwing = false;
					whiskey = null;
					throwables = null;
					returning = false;
					throwStarted = false;
					return;
				}
				
				
				if(whiskey == null){
					getBottle();
				}else{
					if(ControllerWrapper.isInputActive("up")){
						rotation += angularSpeed * Gdx.graphics.getDeltaTime();
					}else if(ControllerWrapper.isInputActive("down")){
						rotation -= angularSpeed * Gdx.graphics.getDeltaTime();
					}
					
					if(ControllerWrapper.isInputActive("action")){
						throwStarted = true;
						throwStartedAt = 0.55f;
					}
					
					rotation = Math.min(1, rotation);
					rotation = Math.max(0, rotation);
				}
			}else{
				if(!returning && !ControllerWrapper.isInputActive("action") && rotation < 1.10f){
					angularSpeed += 10f * Gdx.graphics.getDeltaTime();
					resultingSpeed += 2500f * Gdx.graphics.getDeltaTime();
					rotation += angularSpeed * Gdx.graphics.getDeltaTime();
				}else{
					if(whiskey != null){
						whiskey.setGravity(-15);
						Vector2 force = new Vector2(1f, 1f);
						force.setAngle(-110 + -(-110 - 90) * rotation);
						force.scl(resultingSpeed);
						whiskey.applyForce(force);
						whiskey.returnToAngle(0);
						whiskey = null;
						resultingSpeed = 0f;
					}
					returning = true;
					angularSpeed = 0.75f;
				}
				if(returning){
					rotation -= angularSpeed * Gdx.graphics.getDeltaTime();
					if(rotation <= throwStartedAt){
						returning = throwStarted = false;
						rotation = throwStartedAt;
					}
				}
			}
			
			if(whiskey != null){
				whiskey.setRotation(-110 + -(-110 - 90) * rotation);
				whiskey.setOrigin(10, arm.getRegionHeight() * scale - whiskey.getHeight() / 2);
				whiskey.setPosition(getPosition().x + 40, getPosition().y + 84);
			}
			super.renderShadow(batch);
			batch.draw(noArmValter, position.x + 16, position.y - 8, noArmValter.getWidth() * scale, noArmValter.getHeight() * scale);
		} else if(attack == PUNCHING){
			stateTime += Gdx.graphics.getDeltaTime();
			TextureRegion frame = punching.getKeyFrame(stateTime);
			super.renderShadow(batch);
			if(state == IDLE_L || state == RUNNING_L || state == WALKING_L){
				batch.draw(frame, getPosition().x + 110, getPosition().y, -frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
			}else{
				batch.draw(frame, getPosition().x, getPosition().y, frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
			}
			if(punching.isAnimationFinished(stateTime)){
				attack = NONE;
			}
		} else if(attack == KICKING){
			stateTime += Gdx.graphics.getDeltaTime();
			TextureRegion frame = kicking.getKeyFrame(stateTime);
			super.renderShadow(batch);
			if(state == IDLE_L || state == RUNNING_L || state == WALKING_L){
				batch.draw(frame, getPosition().x + 130, getPosition().y, -frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
			}else{
				batch.draw(frame, getPosition().x, getPosition().y, frame.getRegionWidth() * scale, frame.getRegionHeight() * scale);
			}
			if(kicking.isAnimationFinished(stateTime)){
				attack = NONE;
			}
		} else {
			super.render(batch);
			if(attack == NONE && inputReleased){
				boolean attackReq = false;
				if(ControllerWrapper.isInputActive("kick")){
					attack = KICKING;
					stateTime = 0f;
					inputReleased = false;
					attackReq = true;
				}
				if(ControllerWrapper.isInputActive("punch")){
					attack = PUNCHING;
					stateTime = 0f;
					inputReleased = false;
					attackReq = true;
				}
				
				if(attackReq){
					boolean facingLeft = (state == IDLE_L || state == RUNNING_L || state == WALKING_L);
					getCollisionComponent().getRect().x += 60 * (facingLeft? -1: 1);
					CollisionComponent targ = CollisionManager.isColliding(getCollisionComponent(), false, "Ground");
					getCollisionComponent().getRect().x -= 60 * (facingLeft? -1: 1);
					if(targ != null){
						targ.getOwner().onDamage(this);
					}					
				}
			}
		}
	}
	
	@Override
	public void postObjectsRender(SpriteBatch batch){
		if(throwing){
			batch.draw(arm, getPosition().x + 20, getPosition().y + 59, 
					arm.getRegionWidth() * 0.65f, arm.getRegionHeight() * scale + 16, 
				arm.getRegionWidth(), arm.getRegionHeight(), scale, scale, -110 + -(-110 - 90) * rotation);
		}
	}
	
	@Override
	public void onCollision(CollisionComponent other){
		super.onCollision(other);
		if(other.getOwner().getName().equals("Colchao")){
			enhancedJump = true;
		}
	}

}

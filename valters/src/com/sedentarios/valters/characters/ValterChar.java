package com.sedentarios.valters.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.ControllerWrapper;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.objects.ValtersObject;

public class ValterChar extends ValtersObject{

	Texture shadow;
	TextureAtlas walkAtlas;
	TextureAtlas runAtlas;
	Texture idleTexture;
	Animation walkAnim;
	Animation runAnim;
	Animation falling;
	Array<TextureRegion> walkTextures;
	Array<TextureRegion> runTextures;
	float animTime = 0f;
	private Sound jump;
	private Sound springJump;
	
	public static final int IDLE_R = 0;
	public static final int IDLE_L = 1;
	public static final int WALKING_R = 2;
	public static final int WALKING_L = 3;
	public static final int RUNNING_R = 4;
	public static final int RUNNING_L = 5;
	public static final int FALLING = 6;
	
	protected int state = IDLE_R;
	float scale = 1f; 
	
	int minY = 10, maxY = 140;
	final float originalY;
	protected boolean fixedShadow = false;
	
	public boolean goLeft = false;
	
	Vector2 direction;
	Vector2 speed;
	
	public ValterChar(float x, float y, int minY, int maxY, float scale){
		this(x, y, minY, maxY);
		this.scale = scale;
	}
	
	public ValterChar(float x, float y, int minY, int maxY){
		this(x, y);
		this.minY = minY;
		this.maxY = maxY;
	}
	
	public ValterChar(float x, float y, float scale){
		this(x, y);
		this.scale = scale;
	}
	
	public ValterChar(float x, float y) {
		super("valter", x, y, (byte) 1, true, false);
		setAttribute("reais", "0");
		setAttribute("dollars", "0");
		setAttribute("health", "100");
		originalY = y;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/anim/tropecar/tropecar.txt", TextureAtlas.class);
	}
	
	@Override
	public void create(){
		create(true);
	}
	
	public void create(boolean doMyShit) {
		if(doMyShit){
			try{
				TextureAtlas atlas = getMap().assetManager.get("assets/anim/tropecar/tropecar.txt", TextureAtlas.class);
				falling = new Animation(1f/20f, atlas.getRegions());
			}catch(Exception ex){}
			
			jump = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/smw_jump.wav"));
			springJump = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/smw_spring_jump.wav"));
			
			shadow = new Texture("assets/anim/sombra.png");
			
			idleTexture = new Texture("assets/anim/parado.png");
			idleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			walkAtlas = new TextureAtlas("assets/anim/andar/andar_pack.txt");
	
			walkTextures = new Array<TextureRegion>();
			
			runAtlas = new TextureAtlas("assets/anim/correr/correr_pack.txt");		
			runTextures = new Array<TextureRegion>();
			
			for(int i = 6; i <= 25; i++) {
				TextureRegion walkRegion = walkAtlas.findRegion(i >= 10?("andar00" + i):("andar000" + i));
				walkRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
				walkTextures.add(walkRegion);
			}
			
			for(int i = 10; i <= 30; i++){
				TextureRegion runRegion = runAtlas.findRegion("correr00" + i);
				runRegion.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
				runTextures.add(runRegion);
			}
			
			walkAnim = new Animation(1f/20f, walkTextures, Animation.LOOP);
			runAnim = new Animation(1f/20f, runTextures, Animation.LOOP);
			
			direction = new Vector2(Vector2.Zero);
			speed = new Vector2(200f, 100f);
		}
		
		super.create();
	}

	private Vector2 totalJumpAcceleration = new Vector2(0f, 0f);
	private Vector2 jumpAcceleration = new Vector2(0f, 100f);
	protected boolean enhancedJump = false;
	public void update(){
		if(state == FALLING){
			return;
		}
		
		direction.set(Vector2.Zero);
		if(!frozen){
			if(ControllerWrapper.isInputActive("left") || goLeft) {
				direction.add(-1 * Gdx.graphics.getDeltaTime(), 0);
			}
			if(ControllerWrapper.isInputActive("right")) {
				direction.add(1 * Gdx.graphics.getDeltaTime(), 0);
			}
			
			if(allowYMovement && ControllerWrapper.isInputActive("up")) {
				direction.add(0, 1 * Gdx.graphics.getDeltaTime());
			}
			if(allowYMovement && ControllerWrapper.isInputActive("down")) {
				direction.add(0, -1 * Gdx.graphics.getDeltaTime());
			}
			if(!allowYMovement && minY != maxY && ControllerWrapper.isInputActive("jump")){
				if(totalJumpAcceleration.len2() <= 100f){
					if(enhancedJump){
						springJump.play(ValtersOptions.SOUND_LEVEL);
					}else{
						jump.play(ValtersOptions.SOUND_LEVEL);
					}
				}
				if(totalJumpAcceleration.len() < (enhancedJump?1000f:600f)){
					totalJumpAcceleration.add(jumpAcceleration);
					applyForce(jumpAcceleration);
				}else if(isTouchingGround()){
					totalJumpAcceleration.set(0, 0);
					enhancedJump = false;
				}
			}
		}
		
		boolean run = (runAnim != null) && (ControllerWrapper.isInputActive("run") || goLeft);
		
		if(direction.len() > 0){
			move(direction.x * speed.x * (run?2f:1), direction.y * speed.y);
		}
		
		if(position.x < ValtersGame.map.getLeftCap() || position.x > ValtersGame.map.getRightCap() + Gdx.graphics.getWidth() / 2 - 64) {
			state = position.x <= ValtersGame.map.getLeftCap()?IDLE_L:IDLE_R;
			position.x = position.x <= ValtersGame.map.getLeftCap()? ValtersGame.map.getLeftCap() : ValtersGame.map.getRightCap() + Gdx.graphics.getWidth() / 2 - 64;
			direction.set(Vector2.Zero);
		}
		
		if(position.y < minY || position.y > maxY) {
			position.y = position.y < minY + 5?minY:maxY;
		}
		
		if(direction.len() == 0 && !(state == IDLE_R || state == IDLE_L)){
			state = ((state == WALKING_L) || (state == RUNNING_L)? IDLE_L:IDLE_R);
		}else if(direction.len() > 0){
			if(direction.y != 0 && direction.x == 0) {
				if((state == IDLE_L || state == IDLE_R)) {
					if(!run) {
						state = (state == IDLE_L)?WALKING_L:WALKING_R;
					}else {
						state = (state == IDLE_L)?RUNNING_L:RUNNING_R;
					}
				}
			}else if(direction.x > 0) {
				state = run? RUNNING_R : WALKING_R;
			}else {
				state = run? RUNNING_L : WALKING_L;
			}
		}
	}
	
	public void renderShadow(SpriteBatch batch){
		batch.draw(shadow, position.x - 6, (fixedShadow?originalY:position.y) - 18, shadow.getWidth() * scale * 0.95f, shadow.getHeight() * scale * 0.95f);
	}
	
	public void render(SpriteBatch batch) {
		update();
		
		renderShadow(batch);
		
		if(state != IDLE_L && state != IDLE_R) {
			animTime += Gdx.graphics.getDeltaTime();
		}else {			
			if(state == IDLE_R) {
				batch.draw(idleTexture, position.x + 16, position.y - 8, idleTexture.getWidth() * scale, idleTexture.getHeight() * scale);
			}else {
				batch.draw(idleTexture, position.x + 110, position.y - 8, -idleTexture.getWidth() * scale, idleTexture.getHeight() * scale);
			}
		}
		
		if(state == WALKING_R) {
			TextureRegion tr = walkAnim.getKeyFrame(animTime, true);
			batch.draw(tr, position.x, position.y, tr.getRegionWidth() * scale, tr.getRegionHeight() * scale);
		}else if(state == WALKING_L) {
			TextureRegion tr = walkAnim.getKeyFrame(animTime, true);
			batch.draw(tr, position.x + 130, position.y, -tr.getRegionWidth() * scale, tr.getRegionHeight() * scale);
		}else if(state == RUNNING_R) {
			TextureRegion tr = runAnim.getKeyFrame(animTime, true);
			batch.draw(tr, position.x, position.y, tr.getRegionWidth() * scale, tr.getRegionHeight() * scale);
		}else if(state == RUNNING_L) {
			TextureRegion tr = runAnim.getKeyFrame(animTime, true);
			batch.draw(tr, position.x + 130, position.y, -tr.getRegionWidth() * scale, tr.getRegionHeight() * scale);
		}else if(state == FALLING && falling != null) {
			TextureRegion tr = falling.getKeyFrame(animTime);
			batch.draw(tr, position.x + 130, position.y, -tr.getRegionWidth() * scale, tr.getRegionHeight() * scale);
		}
	}
	
	public void setState(int state){
		this.state = state;
		if(state == FALLING) animTime = 0f;
	}
	
	public boolean isFacingLeft(){
		return (state == IDLE_L || state == RUNNING_L || state == WALKING_L);
	}
	
	@Override
	public Rectangle getCollider(){
		return new Rectangle(position.x, position.y, 167f * 0.8f, 100f * 0.8f);
	}
	
	public void dispose() {
		super.dispose();
		if(runAtlas != null) idleTexture.dispose();
		if(walkAtlas != null) walkAtlas.dispose();
		if(runAtlas != null) runAtlas.dispose();
		if(runTextures != null){
			for(TextureRegion tr : runTextures) {
				tr.getTexture().dispose();
			}
		}
		if(walkTextures != null){
			for(TextureRegion tr : walkTextures) {
				tr.getTexture().dispose();
			}
		}
	}

}

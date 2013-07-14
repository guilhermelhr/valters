package com.sedentarios.valters.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.CollisionManager;
import com.sedentarios.valters.ControllerWrapper;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.objects.CollisionComponent;

public class ValterCharPrisao extends ValterChar {

	private final int NONE = -1;
	private final int PUNCHING = 0;
	public int attack = NONE;
	
	private Animation punching;
	private Animation punchingColher;
	private Animation punchingTravesseiro;
	private Animation punchingPanela;
	
	public static final int MAO = 0;
	public static final int TRAVESSEIRO = 1;
	public static final int PANELA = 2;
	public static final int COLHER = 3;
	public int arma = MAO;
	
	
	public ValterCharPrisao(float x, float y, float scale) {
		super(x, y, (int) y, (int) y, scale);
		setAllowYMovement(false);
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/anim/cadeia_parado.png", Texture.class);
		for(int i = 5; i <= 15; i++){
			assetManager.load("assets/anim/cadeia_andar/cadeia_andar00" + i + ".png", Texture.class);
		}
		//assetManager.load("assets/anim/lutar_chute_cadeia/chute.txt", TextureAtlas.class);
		assetManager.load("assets/anim/lutar_soco_cadeia/soco.txt", TextureAtlas.class);
		assetManager.load("assets/anim/travesseiro/travesseiro.txt", TextureAtlas.class);
		assetManager.load("assets/anim/colher/colher.txt", TextureAtlas.class);
		assetManager.load("assets/anim/panela/panela.txt", TextureAtlas.class);
		assetManager.load("assets/sounds/travesseiro.mp3", Sound.class);
		assetManager.load("assets/sounds/som_metal.mp3", Sound.class);
		assetManager.load("assets/sounds/soco2.wav", Sound.class);
	}
	
	@Override
	public void create(){		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int i = 5; i <= 15; i++){
			Texture frame = getMap().assetManager.get("assets/anim/cadeia_andar/cadeia_andar00" + i + ".png", Texture.class);
			frame.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			frames.add(new TextureRegion(frame));
		}
				
		walkAnim = new Animation(1f/20f, frames, Animation.LOOP_PINGPONG);
		
		direction = new Vector2(Vector2.Zero);
		speed = new Vector2(200f, 100f);
		
		idleTexture = getMap().assetManager.get("assets/anim/cadeia_parado.png", Texture.class);
		idleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Array<AtlasRegion> regions = getMap().assetManager.get("assets/anim/lutar_soco_cadeia/soco.txt", TextureAtlas.class).getRegions();
		for(AtlasRegion r : regions){
			r.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		punching = new Animation(1f/24f, regions);
		
		regions = getMap().assetManager.get("assets/anim/travesseiro/travesseiro.txt", TextureAtlas.class).getRegions();
		for(AtlasRegion r : regions){
			r.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		punchingTravesseiro = new Animation(1f/24f, regions, Animation.LOOP_PINGPONG);
		
		regions = getMap().assetManager.get("assets/anim/colher/colher.txt", TextureAtlas.class).getRegions();
		for(AtlasRegion r : regions){
			r.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		punchingColher= new Animation(1f/24f, regions, Animation.LOOP_PINGPONG);
		
		regions = getMap().assetManager.get("assets/anim/panela/panela.txt", TextureAtlas.class).getRegions();
		for(AtlasRegion r : regions){
			r.getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		punchingPanela = new Animation(1f/24f, regions, Animation.LOOP_PINGPONG);
		
		super.create(false);
	}

	private float stateTime = 0f;
	private boolean inputReleased = true;
	@Override
	public void render(SpriteBatch batch) {
		if(ControllerWrapper.isInputActive("action")){
			if(getPosition().dst2(0f, 32f) <= 500f){
				arma = PANELA;
			}else if (getPosition().dst2(486.2f, 32f) <= 500f){
				arma = COLHER;
			}else if (getPosition().dst2(600f, 32f) <= 500f){
				arma = TRAVESSEIRO;
			}
		}
		
		
		inputReleased = inputReleased || !(ControllerWrapper.isInputActive("punch") || ControllerWrapper.isInputActive("kick"));
		
		if(attack == PUNCHING){
			stateTime += Gdx.graphics.getDeltaTime();
			TextureRegion frame = null;
			switch(arma){
				case MAO: frame = punching.getKeyFrame(stateTime); break;
				case PANELA: frame = punchingPanela.getKeyFrame(stateTime); break;
				case COLHER: frame = punchingColher.getKeyFrame(stateTime); break;
				case TRAVESSEIRO: frame = punchingTravesseiro.getKeyFrame(stateTime); break;
			}
			
			if(state == IDLE_L || state == RUNNING_L || state == WALKING_L){
				batch.draw(frame, getPosition().x + 110, getPosition().y - 7, -frame.getRegionWidth() * scale * 2.275f, frame.getRegionHeight() * scale * 2.275f);
			}else{
				batch.draw(frame, getPosition().x + 19, getPosition().y - 7, frame.getRegionWidth() * scale * 2.275f, frame.getRegionHeight() * scale * 2.275f);
			}
			
			switch(arma){
				case MAO:
					if(punching.isAnimationFinished(stateTime))	attack = NONE;
					break;
				case PANELA:
					if(stateTime >= punchingPanela.animationDuration * 2) attack = NONE;
					break;
				case COLHER:
					if(stateTime >= punchingColher.animationDuration * 2) attack = NONE;
					break;
				case TRAVESSEIRO:
					if(stateTime >= punchingTravesseiro.animationDuration * 2) attack = NONE;
					break;
			}
		} else {
			super.render(batch);
			if(attack == NONE && inputReleased){
				boolean attackReq = false;
				if(!frozen){
					/*if(ControllerWrapper.isInputActive("kick")){
						attack = KICKING;
						stateTime = 0f;
						inputReleased = false;
						attackReq = true;
					}*/
					if(ControllerWrapper.isInputActive("punch")){						
						switch (arma) {
							case MAO:
							case COLHER:
								getMap().assetManager.get("assets/sounds/soco2.wav", Sound.class).play(ValtersOptions.SOUND_LEVEL); 
								break;
							case PANELA:
								getMap().assetManager.get("assets/sounds/som_metal.mp3", Sound.class).play(ValtersOptions.SOUND_LEVEL);
								break;
							case TRAVESSEIRO:
								getMap().assetManager.get("assets/sounds/travesseiro.mp3", Sound.class).play(ValtersOptions.SOUND_LEVEL);
								break;							
						}
						attack = PUNCHING;
						stateTime = 0f;
						inputReleased = false;
						attackReq = true;
					}
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
	public void renderShadow(SpriteBatch batch){
		return;
	}

}

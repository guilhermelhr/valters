package com.sedentarios.valters.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.characters.ValterCharPrisao;
import com.sedentarios.valters.maps.MapEsgoto;
import com.sedentarios.valters.maps.MapPrisao;

public class ObjectCrack extends ValtersObject {

	private Array<AtlasRegion> atlas;
	private float scale;
	private float health = 100f;
	private final float step = health / 11;
	
	public ObjectCrack(float x, float y, float scale) {
		super("Crack", x, y, (byte) 0, true, false);
		this.scale = scale;
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/texturas/rachadura/rachadura.txt", TextureAtlas.class);
	}

	@Override
	public void create(){
		atlas = getMap().assetManager.get("assets/texturas/rachadura/rachadura.txt", TextureAtlas.class).getRegions();
		super.create();
	}
	
	@Override
	public Rectangle getCollider(){
		return new Rectangle(getPosition().x + 180, getPosition().y, atlas.get(0).getRegionWidth() * scale, 
																atlas.get(0).getRegionHeight() * scale);
	}
	
	@Override
	public void onDamage(ValtersObject other){
		if(other.getName().equals("valter")){
			ValterCharPrisao p = (ValterCharPrisao) other;
			float dano = 0f;
			switch(p.arma){
				case ValterCharPrisao.MAO: dano = 2.5f; break;
				case ValterCharPrisao.COLHER: dano = 5f; break;
				case ValterCharPrisao.TRAVESSEIRO: dano = 1f; break;
				case ValterCharPrisao.PANELA: dano = 10f; break;
			}
			this.health -= dano;
			((MapPrisao)getMap()).wallHit(dano);
			if(this.health <= -5f){
				ValtersGame.changeMap(MapEsgoto.class);
			}
		}
	}
	
	private TextureRegion texture = null;
	@Override
	public void render(SpriteBatch batch) {		
		for(int i = 0; i < 11; i++){
			if(health <= (i + 1) * step){
				texture = atlas.get(i);
				break;
			}
		}
		
		if(texture != null){
			batch.draw(texture, getPosition().x, getPosition().y, texture.getRegionWidth() * scale,
															   	  texture.getRegionHeight() * scale);
		}
	}

}

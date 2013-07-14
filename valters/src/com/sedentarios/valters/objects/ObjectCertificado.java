package com.sedentarios.valters.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.sedentarios.valters.ValtersOptions;

public class ObjectCertificado extends ValtersObject{

	private TextureRegion sprite;
	private Vector2 scale;
	private float rotation;
	
	public ObjectCertificado(float x, float y) {
		super("Certificado", x, y, (byte) 2, false, false);		
	}
	
	public static void loadResources(AssetManager assetManager){
		assetManager.load("assets/texturas/certificado.png", Texture.class);
		assetManager.load("assets/sounds/vento.wav", Sound.class);
	}
	
	@Override
	public void create(){
		super.create();
		sprite = new TextureRegion(getMap().assetManager.get("assets/texturas/certificado.png", Texture.class));
		getMap().assetManager.get("assets/sounds/vento.wav", Sound.class).play(1.5f * ValtersOptions.SOUND_LEVEL);
		scale = new Vector2(0.4f, 0.65f);
	}
	
	float runtime = 0f;
	@Override
	public void render(SpriteBatch batch) {
		float delta = Gdx.graphics.getDeltaTime();
		runtime += delta * 1.3f;
		rotation += MathUtils.sin(delta) * (MathUtils.random() + 1) * 50;
		if(rotation > 359) rotation = 0;
		position.x += delta * 200;
		batch.draw(sprite, position.x, position.y + ((float) Math.sin(runtime) * 100), sprite.getRegionWidth() / 2, 0f, (float) sprite.getRegionWidth(), 
				(float) sprite.getRegionHeight(), scale.x, scale.y, rotation);		
	}

}

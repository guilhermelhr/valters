package com.sedentarios.valters.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ObjectFallingValter extends ValtersObject {

	private Texture[] frames;
	private Texture sombra;
	private static final float fps = 1f/18f;
	private byte currFrame = 0;
	private float exposure = 0f;
	
	private final float scale = 0.6f;
	
	public ObjectFallingValter(float x, float y) {
		super("fallingValter", x, y);
	}

	@Override
	public void create() {
		frames = new Texture[41];
		for(int i = 0; i < 41; i++){
			frames[i] = new Texture(String.format("assets/Anim/cair/caindo00%d.png", i + 1));
		}
		sombra = new Texture("assets/Anim/sombra.png");
	}

	@Override
	public void dispose() {
		for(int i = 0; i < 41; i++){
			frames[i].dispose();
			frames[i] = null;
		}
		sombra.dispose();
	}

	@Override
	public void render(SpriteBatch batch) {
		exposure += Gdx.graphics.getDeltaTime();
		if(currFrame != 40){
			if(exposure >= (currFrame == 24?fps * 20:fps)){
				exposure = 0f;
				currFrame++;
			}
		}
		
		/* fórmula mágica do capeta para sincronizar a sombra com a animação do valter caindo */
		float mod = getPosition().y / 250 * 0.65f + (currFrame>=14?(currFrame<=18?(currFrame - 14) * 0.1f:18 * 0.1f):0);
		float umod = currFrame >= 28?(28 - Math.min(currFrame, 32)) * 64:0;
		
		batch.draw(sombra, getPosition().x + 90 - umod + umod * 0.1f, 234, (sombra.getWidth() * 0.8f * mod) 
																	+ umod - umod * 0.12f, sombra.getHeight() * 0.8f);
		batch.draw(frames[currFrame], getPosition().x, getPosition().y,
				frames[currFrame].getWidth() * scale, frames[currFrame].getHeight() * scale);
	}

}

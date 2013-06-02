package com.sedentarios.valters.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ObjectJornal extends ValtersObject {

	private Texture sprite;
	private float scale = 0.35f;
	
	public ObjectJornal(float x, float y) {
		super("jornal", x, y, (byte) 0);
	}

	@Override
	public void create() {
		sprite = new Texture("assets/Objs/jornal.png");
		sprite.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	@Override
	public void dispose() {
		sprite.dispose();
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(sprite, position.x, position.y, sprite.getWidth() * scale, sprite.getHeight() * scale);
	}

}

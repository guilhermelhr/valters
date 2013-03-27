package com.sedentarios.valters.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ObjectMesa {

	Texture texture;
	float scale = 0.73f;
	
	public void create() {
		texture = new Texture("data/Objs/mesa.png");
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
	}
	
	public void dispose() {
		texture.dispose();
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(texture, 1120, 30, texture.getWidth() * scale, texture.getHeight() * scale);
	}

}

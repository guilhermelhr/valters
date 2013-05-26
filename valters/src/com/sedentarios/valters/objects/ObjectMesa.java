package com.sedentarios.valters.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ObjectMesa extends ValtersObject{

	Texture texture;
	float scale = 1.25f;
	
	public ObjectMesa(float x, float y) {
		super("mesa", x, y);
	}
	
	public void create() {
		texture = new Texture("data/Objs/mesa.png");
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
	}
	
	public void dispose() {
		texture.dispose();
	}
	
	public void render(SpriteBatch batch) {
		batch.draw(texture, getPosition().x, getPosition().y, texture.getWidth() * scale, texture.getHeight() * scale);
	}

}

package com.sedentarios.valters.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class UITexture extends UIComponent {
	
	private Rectangle bounds;
	private Texture texture;
	
	public UITexture(String name, float x, float y, Texture texture){
		this(name, x, y, texture.getWidth(), texture.getHeight(), texture);
	}
	
	public UITexture(String name, float x, float y, float width, float height, Texture texture){
		super(name);
		bounds = new Rectangle(x, y, width, height);
		this.texture = texture;
		focusable = false;
	}
	
	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void onFocus() {
		System.out.println("shit happens");
	}

	@Override
	public void onFocusLost() {
		
	}

	@Override
	public void onAction() {
		
	}

	@Override
	public void onActionStopped() {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public void dispose() {
		
	}

}

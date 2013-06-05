package com.sedentarios.valters.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class UIButton extends UIComponent{

	private Texture regular;
	private Texture pressed;
	private Texture hovered;
	private Rectangle bounds;

	public UIButton(float x, float y, float width, float height){
		bounds = new Rectangle(x, y, width, height);
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void onFocus() {

	}

	@Override
	public void onFocusLost() {

	}

	@Override
	public void onActionStopped() {

	}

	ShapeRenderer sr = new ShapeRenderer();
	@Override
	public void render(SpriteBatch batch) {
		Texture toBeRendered = regular;
		switch (getState()){
			case ACTION:
				if(pressed != null) toBeRendered = pressed;
				break;
			case FOCUSED:
				if(pressed != null) toBeRendered = hovered;
		}

		if(toBeRendered != null)
			batch.draw(toBeRendered, getBounds().getX(), getBounds().getY(), getBounds().getWidth(), getBounds().getHeight());

		sr.begin(ShapeRenderer.ShapeType.Line);
		if(getState().equals(UIComponentState.FOCUSED))sr.setColor(Color.BLUE);else sr.setColor(Color.GRAY);
		sr.rect(getBounds().getX(), getBounds().getY(), getBounds().getWidth(), getBounds().getHeight());
		sr.end();
	}

	public void setRegularTexture(Texture regular) {
		this.regular = regular;
	}

	public void setPressedTexture(Texture pressed) {
		this.pressed = pressed;
	}

	public void setHoveredTexture(Texture hovered) {
		this.hovered = hovered;
	}

	public void dispose(){
		if(regular != null) regular.dispose();
		if(pressed != null) pressed.dispose();
		if(hovered != null) hovered.dispose();
	}
}

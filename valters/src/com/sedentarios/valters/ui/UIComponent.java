package com.sedentarios.valters.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class UIComponent {

	private boolean active = true;
	private UIComponentState state = UIComponentState.NOT_FOCUSED;

	public abstract Rectangle getBounds();
	public abstract void onFocus();
	public abstract void onFocusLost();
	public abstract void onAction();
	public abstract void onActionStopped();
	public abstract void render(SpriteBatch batch);

	public UIComponentState getState(){
		return state;
	}

	public void setState(UIComponentState state){
		this.state = state;
	}

	public boolean isActive(){
		return active;
	}

	public abstract void dispose();
}

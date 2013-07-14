package com.sedentarios.valters.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class UIComponent {

	private String name;
	private boolean active = true;
	public boolean focusable = true;
	private UIComponentState state = UIComponentState.NOT_FOCUSED;
	
	public UIComponent(String name){
		this.name = name;
	}

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
	
	public String getName(){
		return name;
	}

	public void sceneLostFocus(){}

	public abstract void dispose();
}

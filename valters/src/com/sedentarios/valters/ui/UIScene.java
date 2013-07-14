package com.sedentarios.valters.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.ControllerWrapper;

public class UIScene {

	private String name;
	public Array<UIComponent> components;
	private boolean active = true;
	private float delayAction = 0f;
	private int focus = 0;
	private boolean interactive = true;
	
	public UIScene(int focus, String name){
		this(name);
		this.focus = focus;
	}

	public UIScene(String name){
		components = new Array<UIComponent>();
		this.name = name;
	}

	public void addComponent(UIComponent component){
		 components.add(component);
	}
	
	public UIComponent getComponent(String name){
		for(UIComponent component : components){
			if(component.getName().equals(name)){
				return component;
			}
		}
		return null;
	}

	public void render(SpriteBatch batch){
		boolean requestAction = interactive && (ControllerWrapper.isInputActive("action") || ControllerWrapper.isInputActive("enter"));
		if(delayAction > 0f){
			delayAction -= Gdx.graphics.getDeltaTime();
		}

		for(UIComponent component : components){
			if(interactive){
				if(!requestAction && component.getState().equals(UIComponentState.ACTION)){
					component.setState(UIComponentState.FOCUSED);
					component.onActionStopped();
				}
	
				if(components.indexOf(component, false) == focus){
					if(component.getState().equals(UIComponentState.NOT_FOCUSED)){
						component.setState(UIComponentState.FOCUSED);
						component.onFocus();
					}else if(requestAction){
						if(!component.getState().equals(UIComponentState.ACTION)){
							component.setState(UIComponentState.ACTION);
							component.onAction();
						}
					}
				}else if(component.getState().equals(UIComponentState.FOCUSED)){
					component.setState(UIComponentState.NOT_FOCUSED);
					component.onFocusLost();
				}
			}

			if(component.isActive()) component.render(batch);
		}
		
		if(interactive){
			if((ControllerWrapper.isInputActive("up") || ControllerWrapper.isInputActive("left")) && delayAction <= 0f){
				do{
					focus--;
					if(focus < 0){
						focus = components.size - 1;
					}
				}while(!components.get(focus).focusable);
	
				delayAction = .2f;
				onFocusChange();
			}else if((ControllerWrapper.isInputActive("down") || ControllerWrapper.isInputActive("right")) && delayAction <= 0f) {
				do{
					focus++;
					if(focus > components.size - 1) {
						focus = 0;
					}
				}while(!components.get(focus).focusable);
	
				delayAction = .2f;
				onFocusChange();
			}
		}
	}
	
	protected void onFocusChange() {

	}
	
	public String getName(){
		return name;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setInteractive(boolean interactive){
		this.interactive = interactive;
	}

	public void dispose() {
		for(UIComponent component : components) {
			component.dispose();
		}

		components.clear();
	}

	public void lostFocus() {
		for(UIComponent component : components) {
			component.sceneLostFocus();
		}
	}
}

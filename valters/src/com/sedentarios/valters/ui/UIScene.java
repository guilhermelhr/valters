package com.sedentarios.valters.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.ControllerWrapper;

public class UIScene {

	public Array<UIComponent> components;
	private boolean active = true;
	private float delayAction = 0f;
	private int focus = 0;


	public UIScene(int focus){
		this();
		this.focus = focus;
	}

	public UIScene(){
		components = new Array<UIComponent>();
	}

	public void addComponent(UIComponent component){
		 components.add(component);
	}

	public void render(SpriteBatch batch){
		boolean requestAction = ControllerWrapper.isInputActive("action");
		if(delayAction > 0f){
			delayAction -= Gdx.graphics.getDeltaTime();
		}

		for(UIComponent component : components){
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

			if(component.isActive()) component.render(batch);
		}

		if((ControllerWrapper.isInputActive("up") || ControllerWrapper.isInputActive("left")) && delayAction <= 0f){
			focus--;
			delayAction = .2f;
			onFocusChange();
		}else if((ControllerWrapper.isInputActive("down") || ControllerWrapper.isInputActive("right")) && delayAction <= 0f){
			focus++;
			delayAction = .2f;
			onFocusChange();
		}

		if(focus > components.size - 1){
			focus = 0;
		}else if(focus < 0){
			focus = components.size - 1;
		}
	}

	protected void onFocusChange(){

	}

	public boolean isActive(){
		return active;
	}

	public void dispose() {
		for(UIComponent component : components){
			component.dispose();
		}

		components.clear();
	}
}

package com.sedentarios.valters.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class CollisionComponent {

	private ValtersObject object;
	private Rectangle rect;
	private boolean active = true;
	private boolean isOnlyTrigger = false;
	public Array<String> ignore;
	
	public CollisionComponent(Rectangle rect){
		this(rect, false);
	}
	
	public CollisionComponent(Rectangle rect, boolean isOnlyTrigger) {
		this.rect = rect;
		this.isOnlyTrigger = isOnlyTrigger;
		ignore = new Array<String>();
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setOwner(ValtersObject object){
		this.object = object;
	}
	
	public ValtersObject getOwner(){
		return object;
	}
	
	public Rectangle getRect(){
		return rect;
	}
	
	public boolean isOnlyTrigger(){
		return isOnlyTrigger;
	}
	
	public boolean collidesWith(CollisionComponent component){
		if(!component.getOwner().equals(object) && object.isEnabled() && rect.overlaps(component.rect)){
			if(!ignore.contains(component.getOwner().getName(), false)){
				return true;
			}
		}
		
		return false;
	}
	
}

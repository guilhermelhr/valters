package com.sedentarios.valters.objects;

import com.badlogic.gdx.math.Rectangle;

public class CollisionComponent {

	private ValtersObject object;
	private Rectangle rect;
	private boolean active = true;
	private boolean isOnlyTrigger = false;
	
	public CollisionComponent(Rectangle rect){
		this(rect, false);
	}
	
	public CollisionComponent(Rectangle rect, boolean isOnlyTrigger) {
		this.rect = rect;
		this.isOnlyTrigger = isOnlyTrigger;
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
			return true;
		}
		
		return false;
	}
	
}

package com.sedentarios.valters.objects;

import com.badlogic.gdx.math.Rectangle;

public class CollisionComponent {

	private ValtersObject object;
	private Rectangle rect;
	private boolean active = true;	
	
	public CollisionComponent(Rectangle rect) {
		this.rect = rect;
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
	
	public boolean collidesWith(CollisionComponent component){
		if(!component.getOwner().equals(object) && object.isEnabled() && rect.overlaps(component.rect)){
			return true;
		}
		
		return false;
	}
	
}

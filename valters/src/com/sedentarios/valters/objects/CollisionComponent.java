package com.sedentarios.valters.objects;

import com.badlogic.gdx.math.Rectangle;
import com.sedentarios.valters.CollisionManager;

public class CollisionComponent {

	private Rectangle rect;
	private boolean active = true;
	private float cooldown = CollisionManager.cooldownTime;	
	
	public CollisionComponent(Rectangle rect) {
		this.rect = rect;
	}
	
	public boolean isActive() {
		return active;
	}
	
}

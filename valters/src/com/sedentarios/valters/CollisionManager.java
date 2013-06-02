package com.sedentarios.valters;

import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.objects.CollisionComponent;

public class CollisionManager {
	
	public static final float cooldownTime = 3f;
	
	private Array<CollisionComponent> globalScope = new Array<CollisionComponent>();
	private Array<CollisionComponent> activeScope = new Array<CollisionComponent>();
	
	public void sortComponents(){
		
	}
		
	
}

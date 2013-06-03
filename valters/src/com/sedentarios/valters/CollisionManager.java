package com.sedentarios.valters;

import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.objects.CollisionComponent;

public class CollisionManager {
	
	private static Array<CollisionComponent> scope = new Array<CollisionComponent>();
	
	public static void registerComponent(CollisionComponent component){
		scope.add(component);
	}
	
	public static boolean isColliding(CollisionComponent component){
		for(CollisionComponent cc : scope){
			if(component.collidesWith(cc)){
				return true;
			}
		}
		
		return false;
	}
	
}

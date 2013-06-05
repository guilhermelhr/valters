package com.sedentarios.valters;

import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.objects.CollisionComponent;

public class CollisionManager {
	
	private static Array<CollisionComponent> scope = new Array<CollisionComponent>();
	
	public static void registerComponent(CollisionComponent component){
		scope.add(component);
	}
	
	public static void unregisterComponent(CollisionComponent component){
		scope.removeValue(component, false);
	}

	public static void clearComponents(){
		scope.clear();
	}
	
	public static boolean isColliding(CollisionComponent component){
		for(CollisionComponent cc : scope){
			if(component.collidesWith(cc)){
				cc.getOwner().onCollision(component);
				component.getOwner().onCollision(cc);
				return !(cc.isOnlyTrigger() || component.isOnlyTrigger());
			}
		}
		
		return false;
	}
	
}

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
		return isColliding(component, true) != null;
	}
	
	public static CollisionComponent isColliding(CollisionComponent component, boolean callback, String...ignore){
		for(CollisionComponent cc : scope){
			if(component.collidesWith(cc)){
				boolean ignored = false;
				for(String name : ignore){
					if(name.equals(cc.getOwner().getName())){
						ignored = true;
						break;
					}
				}
				
				if(ignored) break;				
				if(callback){
					cc.getOwner().onCollision(component);
					component.getOwner().onCollision(cc);
				}
				
				if(!(cc.isOnlyTrigger() || component.isOnlyTrigger())){
					return cc;
				}
			}
		}
		
		return null;
	}
	
}

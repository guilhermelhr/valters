package com.sedentarios.valters.objects;

import com.badlogic.gdx.math.Vector2;
import com.sedentarios.valters.CollisionManager;


public abstract class ValtersRigidBody extends ValtersObject {

	public ValtersRigidBody(String name, float x, float y, byte layer, float restitution, float mass) {
		super(name, x, y, layer, true, false);
		setGravity(-15f);
		addFlag("rigidbody");
		setAttribute("restitution", String.valueOf(restitution));
		setAttribute("mass", String.valueOf(mass));
	}
	
	@Override
	public void onCollision(CollisionComponent other){
		if(other.getOwner().getName().equals("Ground")) return;
		
		ValtersObject a = other.getOwner();
		ValtersObject b = this;
		
		//Vector2 rv = b.getAcceleration().cpy().sub(a.getAcceleration());
		Vector2 normal = b.getPosition().cpy().sub(a.getPosition()).nor();
		//normal.scl(1f);
		
		//float velAlongNormal = rv.dot(normal);
		
		b.position.add((float) Math.ceil(normal.x), 0);
		CollisionComponent col = null;
		if((CollisionManager.isColliding(b.getCollisionComponent(), false, a.getName())) != null){
			b.position.sub(normal.x, 0);
		}
		b.position.add(0, normal.y);
		if((col = CollisionManager.isColliding(b.getCollisionComponent(), false, a.getName())) != null){
			if(col.getOwner().getName().equals("Ground")){
				b.position.sub(0, normal.y - 0.01f);
			}else{
				b.position.sub(0, normal.y);
			}
		}
		
		
	}

}

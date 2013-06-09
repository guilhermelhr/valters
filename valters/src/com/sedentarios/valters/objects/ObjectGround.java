package com.sedentarios.valters.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class ObjectGround extends ValtersObject{

	private Rectangle collider;

	public ObjectGround(float y, float width) {
		super("Ground", 0, y, (byte) 0,true, false);
		addFlag("groundy");
		collider = new Rectangle(0, y, width, 16);
	}

	@Override
	public void render(SpriteBatch batch) {

	}

	@Override
	public Rectangle getCollider() {
		return collider;
	}
}

package com.sedentarios.valters.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class ValtersObject {

	protected String name;
	protected Vector2 position = new Vector2();
	protected boolean enabled = true;
	/** est‡ sendo ignorado, acho que Ž mais simples resolver a profundidade de todos os objetos mesmo **/
	public boolean solveDepth = true;
	
	public ValtersObject(String name, Vector2 position) {
		this.name = name;
		this.position = position;
	}
	public ValtersObject(String name, float x, float y) {
		this.name = name;
		position.set(x, y);
	}
	
	public abstract void create();
	
	public abstract void dispose();
	
	public void internalRender(SpriteBatch batch) {
		if(enabled) {
			render(batch);
		}
	}
	
	public abstract void render(SpriteBatch batch);
	
	public void setPosition(Vector2 position) {
		this.position.set(position);
	}
	
	public void setPosition(float x, float y) {
		position.set(x, y);
	}
	
	public String getName() {
		return name;
	}
	
	public Vector2 getPosition() {
		return position;
	}

}

package com.sedentarios.valters.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sedentarios.valters.CollisionManager;

public abstract class ValtersObject {

	protected String name;
	protected Vector2 position = new Vector2();
	private Vector2 center;
	protected boolean enabled = true;
	/**
	 * est‡ sendo ignorado, acho que Ž mais simples resolver a profundidade de
	 * todos os objetos mesmo
	 **/
	public boolean solveDepth = true;
	protected byte layer = 1;
	private boolean waitingRemoval = false;
	private boolean collidable = false;

	private float width, height;

	private CollisionComponent collision;

	public ValtersObject(String name, Vector2 position) {
		this.name = name;
		this.position = position;
	}

	public ValtersObject(String name, float x, float y) {
		this.name = name;
		position.set(x, y);
	}

	public ValtersObject(String name, float x, float y, byte layer) {
		this(name, x, y);
		this.layer = layer;
	}

	public ValtersObject(String name, float x, float y, byte layer,
			boolean collidable) {
		this(name, x, y, layer);
		this.collidable = collidable;
	}

	public void create() {
		if (collidable){
			collision = new CollisionComponent(getCollider());
			CollisionManager.registerComponent(collision);
			collision.setOwner(this);
		}
	}

	public abstract void dispose();

	public void internalRender(SpriteBatch batch) {
		if (enabled) {
			render(batch);
		}
	}

	public abstract void render(SpriteBatch batch);

	protected boolean move(float x, float y) {
		if (collision != null) {
			collision.getRect().setX(position.x + x);
			collision.getRect().setY(position.y + y);

			if (!CollisionManager.isColliding(collision)) {
				position.add(x, y);
				return true;
			}else{
				
				collision.getRect().setX(position.x);
				collision.getRect().setY(position.y);
			}
		} else {
			position.add(x, y);
			return true;
		}
		return false;
	}

	protected boolean move(Vector2 step) {
		return move(step.x, step.y);
	}

	public void setPosition(Vector2 position) {
		this.position.set(position);
	}

	public void setPosition(float x, float y) {
		position.set(x, y);
	}

	public void setSize(float w, float h) {
		width = w;
		height = h;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public String getName() {
		return name;
	}

	public Vector2 getPosition() {
		return position;
	}

	public Vector2 getCenteredPosition() {
		if (center == null) {
			center = new Vector2();
		}
		center.set(position.x + width / 2, position.y + height / 2);
		return center;
	}

	public byte getLayer() {
		return layer;
	}

	public void requestDeath() {
		enabled = false;
		waitingRemoval = true;
	}

	public boolean isWaitingRemoval() {
		return waitingRemoval;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public CollisionComponent getCollisionComponent() {
		return collision;
	}

	public Rectangle getCollider() {
		assert false;
		return null;
	}

}

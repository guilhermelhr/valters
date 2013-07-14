package com.sedentarios.valters.objects;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sedentarios.valters.CollisionManager;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.maps.ValtersMap;

public abstract class ValtersObject {

	protected String name;
	protected Vector2 position = new Vector2();
	private Vector2 center;
	protected boolean enabled = true;
	protected float rotation = 0f;
	public boolean frozen = false;
	/**
	 * est� sendo ignorado, acho que � mais simples resolver a profundidade de
	 * todos os objetos mesmo
	 **/
	public boolean solveDepth = true;
	protected byte layer = 1;
	private boolean waitingRemoval = false;
	private boolean collidable = false;
	private boolean triggerOnly = false;
	protected boolean allowYMovement = true;
	private Array<String> flags = new Array<String>();
	private HashMap<String, String> attributes = new HashMap<String, String>();

	/** forces **/
	private float gravity = 0f;
	private Vector2 acceleration = new Vector2(0f, 0f);
	private boolean touchingGround = false;

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
			boolean collidable, boolean triggerOnly) {
		this(name, x, y, layer);
		this.collidable = collidable;
		this.triggerOnly = triggerOnly;
	}

	public void create() {
		if (collidable){
			collision = new CollisionComponent(getCollider(), triggerOnly);
			CollisionManager.registerComponent(collision);
			collision.setOwner(this);
		}
	}

	public void dispose(){
		if(collision != null)
			CollisionManager.unregisterComponent(collision);
	}

	public void internalRender(SpriteBatch batch) {
		if (enabled) {
			render(batch);
			//gravity stuff

			if(gravity != 0f) {
				float delta = Gdx.graphics.getDeltaTime();
				acceleration.y += gravity;
				boolean moved = move(acceleration.x * delta, acceleration.y * delta, true);
				if(!moved){
					acceleration.set(0, 0);
				}else{
					touchingGround = false;
				}
			}
		}
	}

	public abstract void render(SpriteBatch batch);
	
	public void postObjectsRender(SpriteBatch batch){}

	public boolean move(float x, float y){
		return move(x, y, false);
	}

	protected boolean move(float x, float y, boolean naturalForce) {
		if(!allowYMovement && !naturalForce) y = 0;
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

	public void applyForce(Vector2 force){
		acceleration.add(force);
	}

	public void onCollision(CollisionComponent other){
		if(other.getOwner().hasFlag("groundy")){
			touchingGround = true;
		}
		//System.out.println(String.format("Collision between %s and %s", getName(), other.getOwner().getName()));
	}
	
	public void onDamage(ValtersObject other){
		
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

	public void setAllowYMovement(boolean allowYMovement){
		this.allowYMovement = allowYMovement;
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
	
	public float getRotation() {
		return rotation;
	}
	
	public void setRotation(float rotation){
		this.rotation = rotation;
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

	public Vector2 getAcceleration(){
		return acceleration;
	}

	public byte getLayer() {
		return layer;
	}
	
	public void setAttribute(String name, String value){
		attributes.put(name, value);
	}
	
	public String getAttribute(String name){
		return attributes.get(name);
	}

	public void addFlag(String flag){
		if(!flags.contains(flag, false)){
			flags.add(flag);
		}
	}

	public boolean isTouchingGround(){
		return touchingGround;
	}

	public boolean hasFlag(String flag){
		return flags.contains(flag, false);
	}

	public void removeFlag(String flag){
		flags.removeValue(flag, false);
	}

	public void requestDeath() {
		enabled = false;
		waitingRemoval = true;
		if(collidable){
			CollisionManager.unregisterComponent(collision);
			collision = null;
		}
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

	public void setGravity(float gravity){
		this.gravity = gravity;
	}

	public ValtersMap getMap(){
		return ValtersGame.map;
	}

	public CollisionComponent getCollisionComponent() {
		return collision;
	}

	public Rectangle getCollider() {
		assert false;
		return null;
	}

	public boolean run(float x, float y) {
		return move(x, y);
	}

}

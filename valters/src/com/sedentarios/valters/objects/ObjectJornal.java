package com.sedentarios.valters.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.sedentarios.valters.ControllerWrapper;
import com.sedentarios.valters.ValtersGame;

public class ObjectJornal extends ValtersObject {

	private Texture sprite;
	private Texture jornal;
	private float scale = 0.35f;
	private Sound pickupSound;
	private boolean picked = false;
	
	public ObjectJornal(float x, float y) {
		super("jornal", x, y, (byte) 0, true, true);
	}

	@Override
	public void create() {
		pickupSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sounds/pickup.wav"));
		jornal = new Texture("assets/Texturas/jornal.png");
		sprite = new Texture("assets/Objs/jornal.png");
		sprite.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);
		super.create();
	}

	@Override
	public void dispose() {
		sprite.dispose();
		jornal.dispose();
		pickupSound.dispose();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if(!picked){
			batch.draw(sprite, position.x, position.y, sprite.getWidth() * scale, sprite.getHeight() * scale);
		}
		
		/*if(getCenteredPosition().dst(ValtersGame.valter.getPosition()) <= 70){
			String msg = String.format("Aperte %s para ler", 
					ControllerWrapper.lastInputFrom.equals(Controller.KEYBOARD)?"E":"A");
			MapRuaNY.font.draw(batch, msg, 690, 650);
			
			if(ControllerWrapper.isInputActive("action")){
				System.out.println("action");
			}
				
		}*/
	}
	
	@Override
	public void postObjectsRender(SpriteBatch batch){
		if(picked) {
			batch.draw(jornal, ValtersGame.camera.position.x - jornal.getWidth() / 2, 0);
			if(ControllerWrapper.isInputActive("action")){
				requestDeath();
			}
		}
	}
	
	@Override
	public Rectangle getCollider(){
		return new Rectangle(position.x, position.y, sprite.getWidth() * scale, sprite.getHeight() * scale);
	}
	
	@Override
	public void onCollision(CollisionComponent other){
		if(!picked && other.getOwner().getName().equals("valter")){
			pickupSound.play();
			//this.requestDeath();
			picked = true;
		}
	}
}

package com.sedentarios.valters.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sedentarios.valters.ControllerWrapper;
import com.sedentarios.valters.ControllerWrapper.Controller;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.maps.MapRuaNY;

public class ObjectJornal extends ValtersObject {

	private Texture sprite;
	private float scale = 0.35f;
	
	public ObjectJornal(float x, float y) {
		super("jornal", x, y, (byte) 0);
	}

	@Override
	public void create() {
		sprite = new Texture("assets/Objs/jornal.png");
		sprite.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);
	}

	@Override
	public void dispose() {
		sprite.dispose();
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(sprite, position.x, position.y, sprite.getWidth() * scale, sprite.getHeight() * scale);
		
		if(getCenteredPosition().dst(ValtersGame.valter.getPosition()) <= 70){
			String msg = String.format("Aperte %s para ler", 
					ControllerWrapper.lastInputFrom.equals(Controller.KEYBOARD)?"E":"A");
			MapRuaNY.font.draw(batch, msg, 690, 650);
			
			if(ControllerWrapper.isInputActive("action")){
				System.out.println("action");
			}
				
		}
	}

}

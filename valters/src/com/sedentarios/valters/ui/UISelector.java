package com.sedentarios.valters.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.sedentarios.valters.ControllerWrapper;
import com.sedentarios.valters.ValtersGame;

public abstract class UISelector extends UIComponent{

	private String[] options;
	private BitmapFont font;
	private Rectangle bounds;
	private int currOption = 0;
	private boolean focused = false;
	private Texture seta;

	public UISelector(String name, float x, float y, BitmapFont font, int selected, String... options){
		super(name);
		if(options.length == 0){ assert false; return; }

		this.font = (font == null?ValtersGame.font:font);
		this.options = options;
		this.currOption = selected;

		String longest = options[0];
		for(String option : options){
			if(option.length() > longest.length()){
				longest = option;
			}
		}

		BitmapFont.TextBounds bounds = this.font.getBounds(longest);
		this.bounds = new Rectangle(x, y, bounds.width, bounds.height);
		seta = ValtersGame.map.assetManager.get("assets/texturas/seta.png", Texture.class);
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void onFocus() {
		focused = true;
		ControllerWrapper.setInputDelay("left", 0.25f);
		ControllerWrapper.setInputDelay("right", 0.25f);
	}

	@Override
	public void onFocusLost() {
		focused = false;
		ControllerWrapper.setInputDelay("left", 0f);
		ControllerWrapper.setInputDelay("right", 0f);
	}

	@Override
	public void onAction() {
		currOption++;
		if(currOption > options.length - 1) currOption = 0;
	}

	@Override
	public void onActionStopped() {

	}

	@Override
	public void render(SpriteBatch batch) {
		font.draw(batch, options[currOption], getBounds().getX(), getBounds().getY());

		if(focused){
			batch.draw(seta, getBounds().x - seta.getWidth(), getBounds().y - 37, -seta.getWidth(), seta.getHeight());
			batch.draw(seta, getBounds().x + bounds.getWidth() * 1.2f, getBounds().y - 37, seta.getWidth(), seta.getHeight());
			if(ControllerWrapper.isInputActive("left")){
				currOption--;
				if(currOption < 0) currOption = options.length - 1;
				onOptionChange(options[currOption]);
			}else if(ControllerWrapper.isInputActive("right")){
				currOption++;
				if(currOption > options.length - 1) currOption = 0;
				onOptionChange(options[currOption]);
			}
		}
	}

	public abstract void onOptionChange(String value);

	@Override
	public void sceneLostFocus(){
		ControllerWrapper.setInputDelay("left", 0f);
		ControllerWrapper.setInputDelay("right", 0f);
	}

	@Override
	public void dispose() {

	}
}

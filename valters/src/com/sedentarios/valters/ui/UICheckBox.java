package com.sedentarios.valters.ui;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;

public abstract class UICheckBox extends UIComponent {

	private Rectangle bounds;
	private Texture inactive;
	private Texture active;
	private Texture inactiveFocus;
	private Texture activeFocus;
	private boolean focused = false;

	private static Sound select;
	private static Sound unselect;

	private boolean selected = false;

	public UICheckBox(String name, float x, float y, float width, float height, boolean selected){
		this(name, x, y, width, height);
		this.selected = selected;
	}

	public UICheckBox(String name, float x, float y, float width, float height){
		super(name);
		bounds = new Rectangle(x, y, width, height);
		loadTextures();
	}

	private void loadTextures(){
		inactive = ValtersGame.map.assetManager.get("assets/texturas/checkbox.png", Texture.class);
		active = ValtersGame.map.assetManager.get("assets/texturas/checkbox2.png", Texture.class);
		inactiveFocus = ValtersGame.map.assetManager.get("assets/texturas/checkbox3.png", Texture.class);
		activeFocus = ValtersGame.map.assetManager.get("assets/texturas/checkbox4.png", Texture.class);
		select = ValtersGame.map.assetManager.get("assets/sounds/menu_select.wav", Sound.class);
		unselect = ValtersGame.map.assetManager.get("assets/sounds/menu_unselect.wav", Sound.class);
	}

	/** Whether the checkbox is selected or not **/
	public boolean isSelected(){
		return selected;
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void onFocus() {
		focused = true;
	}

	@Override
	public void onFocusLost() {
		focused = false;
	}

	@Override
	public void onAction() {
		selected = !selected;
		(selected?select:unselect).play(.9f * ValtersOptions.SOUND_LEVEL);
		onSelect(selected);
	}

	public abstract void onSelect(boolean selected);

	@Override
	public void onActionStopped() {

	}

	@Override
	public void render(SpriteBatch batch) {
		Texture toBeRendered = selected? (focused?activeFocus:active) : (focused?inactiveFocus:inactive);
		if(toBeRendered != null) {
			batch.draw(toBeRendered, getBounds().getX(),  getBounds().getY(),
							getBounds().getWidth(), getBounds().getHeight());
			batch.setColor(Color.WHITE);
		}
	}

	@Override
	public void dispose() {
		inactive = null;
		active = null;
		inactiveFocus = null;
		activeFocus = null;
		select = null;
		unselect = null;
	}
}

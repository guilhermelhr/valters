package com.sedentarios.valters.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.sedentarios.valters.ValtersGame;

public class UILabel extends UIComponent {
	private Rectangle bounds;
	private BitmapFont.TextBounds textBounds;
	private BitmapFont font;
	private String text;
	private Color color;

	public UILabel(String name, String text, float x, float y, Color color){
		this(name, text, x, y);
		this.color = color;
	}
	
	public UILabel(String name, String text, float x, float y){
		this(name, text, x, y, ValtersGame.font);
	}

	public UILabel(String name, String text, float x, float y, BitmapFont font){
		super(name);
		focusable = false;
		this.text = text;
		textBounds = font.getBounds(text);
		bounds = new Rectangle(x, y, textBounds.width, textBounds.height);
		this.font = font;
		color = Color.WHITE;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public void setText(String text){
		this.text = text;
		textBounds = font.getBounds(text);
		bounds.setWidth(textBounds.width);
		bounds.setHeight(textBounds.height);
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void onFocus() {

	}

	@Override
	public void onFocusLost() {

	}

	@Override
	public void onAction() {
   	    System.out.println("shit happens");
	}

	@Override
	public void onActionStopped() {

	}

	@Override
	public void render(SpriteBatch batch) {
		font.setColor(color);
		font.draw(batch, text, getBounds().getX(), getBounds().getY());
	}

	@Override
	public void dispose() {

	}
}

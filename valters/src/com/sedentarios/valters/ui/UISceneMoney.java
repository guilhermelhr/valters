package com.sedentarios.valters.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.objects.ValtersObject;

public class UISceneMoney extends UIScene{
	
	private ValtersObject object;
	private UILabel dollars;
	private UILabel reais;
	
	public UISceneMoney(String name){
		this(name, ValtersOptions.SCREEN_WIDTH - 350, ValtersOptions.SCREEN_HEIGHT - 80);
	}

	public UISceneMoney(String name, float x, float y) {
		super(name);
		setInteractive(false);
		
		Color color = new Color(1f, 1f, 1f, 0.6f);
		
		addComponent(dollars = new UILabel("dollars", "0", x + 200, y + 52, color));
		addComponent(reais = new UILabel("reais", "3", x + 200, y + 52 - 80, color));
		
		addComponent(new UITexture("dollarsTexture", x + 125, y, 64, 64,
				ValtersGame.map.assetManager.get("assets/texturas/us.png", Texture.class)));
		
		addComponent(new UITexture("reaisTexture", x + 125, y - 80, 64, 64, 
				ValtersGame.map.assetManager.get("assets/texturas/rs.png", Texture.class)));
	}
	
	public void setObject(ValtersObject object){
		this.object = object;
	}
	
	@Override
	public void render(SpriteBatch batch){
		if(object != null){
			dollars.setText(object.getAttribute("dollars"));
			reais.setText(object.getAttribute("reais"));
		}
		super.render(batch);
	}
	
	public static void loadAssets(AssetManager manager){
		manager.load("assets/texturas/us.png", Texture.class);
		manager.load("assets/texturas/rs.png", Texture.class);
	}

}

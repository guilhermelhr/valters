package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ui.UIButton;
import com.sedentarios.valters.ui.UIScene;

public class MapMenu extends ValtersMap{

	public MapMenu() {
		super(0, 0);
	}

	@Override
	public void create() {
		uiScene = new UIScene();
		int y = 650;
		uiScene.addComponent(new UIButton(Gdx.graphics.getWidth() / 2 - 50, y-=100, 100, 50) {
			@Override
			public void onAction() {
				System.out.println("PLAY");
				ValtersGame.changeMap(new MapEscola());
			}
		});
		uiScene.addComponent(new UIButton(Gdx.graphics.getWidth() / 2 - 50, y-=100, 100, 50) {
			@Override
			public void onAction() {
				System.out.println("PLAY");
				ValtersGame.changeMap(new MapWarp());
			}
		});
		uiScene.addComponent(new UIButton(Gdx.graphics.getWidth() / 2 - 50, y-=100, 100, 50) {
			@Override
			public void onAction() {
				System.out.println("PLAY");
				ValtersGame.changeMap(new MapRuaNY());
			}
		});
		uiScene.addComponent(new UIButton(Gdx.graphics.getWidth() / 2 - 50, y-=100, 100, 50) {
			@Override
			public void onAction() {
				System.out.println("EXIT");
				Gdx.app.exit();
			}
		});
	}

	@Override
	public void createObjects() {

	}
}

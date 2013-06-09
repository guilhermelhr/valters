package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ui.UIButton;
import com.sedentarios.valters.ui.UIScene;

public class MapDevelopersMenu extends ValtersMap{

	public MapDevelopersMenu() {
		super(0, 0);
	}

	@Override
	public void create() {
		uiScene = new UIScene();
		int y = Gdx.graphics.getHeight() - 70;
		float height = Gdx.graphics.getHeight() / 7f;
		uiScene.addComponent(new UIButton(Gdx.graphics.getWidth() / 2 - 50, y-=height, height, 50, true) {
			@Override
			public void onAction() {
				System.out.println("PLAY Escola");
				ValtersGame.changeMap(MapEscola.class);
			}
		});
		uiScene.addComponent(new UIButton(Gdx.graphics.getWidth() / 2 - 50, y-=height, height, 50, true) {
			@Override
			public void onAction() {
				System.out.println("PLAY Warp");
				ValtersGame.changeMap(MapWarp.class);
			}
		});
		uiScene.addComponent(new UIButton(Gdx.graphics.getWidth() / 2 - 50, y-=height, height, 50, true) {
			@Override
			public void onAction() {
				System.out.println("PLAY NY");
				ValtersGame.changeMap(MapRuaNY.class);
			}
		});
		uiScene.addComponent(new UIButton(Gdx.graphics.getWidth() / 2 - 50, y-=height, height, 50, true) {
			@Override
			public void onAction() {
				System.out.println("PLAY PLATFORMER");
				ValtersGame.changeMap(PlatformerMap.class);
			}
		});
		uiScene.addComponent(new UIButton(Gdx.graphics.getWidth() / 2 - 50, y-=height, height, 50, true) {
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

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
		UIScene uiScene = new UIScene("DevelopersMenu");
		int y = Gdx.graphics.getHeight() + 30;
		float height = Gdx.graphics.getHeight() / 8f;
		uiScene.addComponent(new UIButton("escola", Gdx.graphics.getWidth() / 2 - 50, y-=height, height, 50, true) {
			@Override
			public void onAction() {
				System.out.println("PLAY Escola");
				ValtersGame.changeMap(MapEscola.class);
			}
		});
		uiScene.addComponent(new UIButton("warp", Gdx.graphics.getWidth() / 2 - 50, y-=height, height, 50, true) {
			@Override
			public void onAction() {
				System.out.println("PLAY Warp");
				ValtersGame.changeMap(MapWarp.class);
			}
		});
		uiScene.addComponent(new UIButton("ruany", Gdx.graphics.getWidth() / 2 - 50, y-=height, height, 50, true) {
			@Override
			public void onAction() {
				System.out.println("PLAY NY");
				ValtersGame.changeMap(MapRuaNY.class);
			}
		});
		uiScene.addComponent(new UIButton("ruany2", Gdx.graphics.getWidth() / 2 - 50, y-=height, height, 50, true) {
			@Override
			public void onAction() {
				System.out.println("PLAY NY2");
				ValtersGame.changeMap(MapRuaNY2.class);
			}
		});
		uiScene.addComponent(new UIButton("ruany3", Gdx.graphics.getWidth() / 2 - 50, y-=height, height, 50, true) {
			@Override
			public void onAction() {
				System.out.println("PLAY NY3");
				ValtersGame.changeMap(MapRuaNY3.class);
			}
		});
		uiScene.addComponent(new UIButton("prisao", Gdx.graphics.getWidth() / 2 - 50, y-=height, height, 50, true) {
			@Override
			public void onAction() {
				System.out.println("PLAY PRISAO");
				ValtersGame.changeMap(MapPrisao.class);
			}
		});
		uiScene.addComponent(new UIButton("esgoto", Gdx.graphics.getWidth() / 2 - 50, y-=height, height, 50, true) {
			@Override
			public void onAction() {
				System.out.println("PLAY ESGOTO");
				ValtersGame.changeMap(MapEsgoto.class);
			}
		});
		uiScene.addComponent(new UIButton("fight", Gdx.graphics.getWidth() / 2 - 50, y-=height, height, 50, true) {
			@Override
			public void onAction() {
				System.out.println("PLAY FIGHT");
				ValtersGame.changeMap(MapFight.class);
			}
		});
		uiScenes.add(uiScene);
	}

	@Override
	public void createObjects() {

	}
}

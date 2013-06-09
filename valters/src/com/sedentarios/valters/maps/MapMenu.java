package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ui.UIButton;
import com.sedentarios.valters.ui.UIScene;

public class MapMenu extends ValtersMap{

	private Texture background;

	public MapMenu() {
		super(0, 0);
	}

	@Override
	public void create() {
		assetManager.load("assets/Texturas/menu-fundo.png", Texture.class);
		assetManager.load("assets/Texturas/opcoes-normal.png", Texture.class);
		assetManager.load("assets/Texturas/opcoes2.png", Texture.class);
		assetManager.load("assets/Texturas/play-normal.png", Texture.class);
		assetManager.load("assets/Texturas/play2.png", Texture.class);
		assetManager.load("assets/Texturas/sair-normal.png", Texture.class);
		assetManager.load("assets/Texturas/sair2.png", Texture.class);
		assetManager.load("assets/Music/tema_inicio.wav", Sound.class);
		assetManager.load("assets/Sounds/menu1.wav", Sound.class);
	}

	@Override
	public void createObjects() {
		background = assetManager.get("assets/Texturas/menu-fundo.png", Texture.class);

		uiScene = new UIScene(1){
			@Override
			protected void onFocusChange(){
				assetManager.get("assets/Sounds/menu1.wav", Sound.class).play(.9f);
			}
		};

		UIButton options;
		uiScene.addComponent(options = new UIButton(180, 720 - 580, 168, 63) {
			@Override
			public void onAction() {

			}
		});
		options.setRegularTexture(assetManager.get("assets/Texturas/opcoes-normal.png", Texture.class));
		options.setHoveredTexture(assetManager.get("assets/Texturas/opcoes2.png", Texture.class));
		UIButton play;
		uiScene.addComponent(play = new UIButton(540, 720 - 670, 214, 76) {
			@Override
			public void onAction() {
				ValtersGame.changeMap(MapEscola.class);
			}
		});
		play.setRegularTexture(assetManager.get("assets/Texturas/play-normal.png", Texture.class));
		play.setHoveredTexture(assetManager.get("assets/Texturas/play2.png", Texture.class));
		UIButton exit;
		uiScene.addComponent(exit = new UIButton(885, 720 - 275, 122, 59) {
			@Override
			public void onAction() {
				Gdx.app.exit();
			}
		});
		exit.setRegularTexture(assetManager.get("assets/Texturas/sair-normal.png", Texture.class));
		exit.setHoveredTexture(assetManager.get("assets/Texturas/sair2.png", Texture.class));

		assetManager.get("assets/Music/tema_inicio.wav", Sound.class).loop(0.7f);
	}

	@Override
	protected void postObjectRender(OrthographicCamera camera, SpriteBatch batch) {
		camera.position.x = Gdx.graphics.getWidth() / 2;
		camera.position.y = Gdx.graphics.getHeight() / 2;
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.getX() > 890 && Gdx.input.getX() < 1032 &&
				Gdx.input.getY() > 720 - 468 && Gdx.input.getY() < 720 - 312){
			ValtersGame.changeMap(MapDevelopersMenu.class);
		}
	}
}

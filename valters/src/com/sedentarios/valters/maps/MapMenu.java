package com.sedentarios.valters.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sedentarios.valters.ControllerWrapper;
import com.sedentarios.valters.ValtersGame;
import com.sedentarios.valters.ValtersOptions;
import com.sedentarios.valters.ui.*;

public class MapMenu extends ValtersMap{

	private Texture background;
	private Sound buttonSound;

	private UIScene mainMenu;
	private UIScene optionsMenu;

	public long musicId;
	private Sound music;

	public MapMenu() {
		super(0, 0);
	}

	@Override
	public void create() {
		assetManager.load("assets/texturas/menu-fundo.png", Texture.class);
		assetManager.load("assets/texturas/opcoes-normal.png", Texture.class);
		assetManager.load("assets/texturas/opcoes2.png", Texture.class);
		assetManager.load("assets/texturas/play-normal.png", Texture.class);
		assetManager.load("assets/texturas/play2.png", Texture.class);
		assetManager.load("assets/texturas/sair-normal.png", Texture.class);
		assetManager.load("assets/texturas/sair2.png", Texture.class);
		assetManager.load("assets/texturas/checkbox.png", Texture.class);
		assetManager.load("assets/texturas/checkbox2.png", Texture.class);
		assetManager.load("assets/texturas/checkbox3.png", Texture.class);
		assetManager.load("assets/texturas/checkbox4.png", Texture.class);
		assetManager.load("assets/texturas/seta.png", Texture.class);
		assetManager.load("assets/music/tema_inicio.wav", Sound.class);
		assetManager.load("assets/sounds/menu1.wav", Sound.class);
		assetManager.load("assets/sounds/menu_select.wav", Sound.class);
		assetManager.load("assets/sounds/menu_unselect.wav", Sound.class);
	}

	@Override
	public void createObjects() {
		ControllerWrapper.enableAllInputs();
		
		background  = assetManager.get("assets/texturas/menu-fundo.png", Texture.class);
		buttonSound = assetManager.get("assets/sounds/menu1.wav", Sound.class);

		createMainMenu();
		createOptionsMenu();

		uiScenes.add(mainMenu);
		uiScenes.add(optionsMenu);

		musicId = (music = assetManager.get("assets/music/tema_inicio.wav", Sound.class)).loop(0.7f * ValtersOptions.MUSIC_LEVEL);
	}

	@Override
	protected void postObjectRender(OrthographicCamera camera, SpriteBatch batch) {
		music.setVolume(musicId, 0.7f * ValtersOptions.MUSIC_LEVEL);
		ValtersGame.setCamPosition(ValtersOptions.SCREEN_WIDTH / 2, ValtersOptions.SCREEN_HEIGHT / 2);		
		if(optionsMenu.isActive()) batch.setColor(0.5f,0.5f,0.5f,0.9f);
		batch.draw(background, 0, 0, ValtersOptions.SCREEN_WIDTH, ValtersOptions.SCREEN_HEIGHT);
		if(optionsMenu.isActive()) batch.setColor(Color.WHITE);

		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.getX() > 890 && Gdx.input.getX() < 1032 &&
				Gdx.input.getY() > 720 - 468 && Gdx.input.getY() < 720 - 312){
			ValtersGame.changeMap(MapDevelopersMenu.class);
		}
	}

	private void createOptionsMenu(){
		optionsMenu = new UIScene(0, "OptionsMenu"){
			@Override
			protected void onFocusChange(){
				buttonSound.play(0.9f * ValtersOptions.SOUND_LEVEL);
			}
		};
		optionsMenu.setActive(false);

		int startY = 150;

		optionsMenu.addComponent(new UICheckBox("fullscreen", 700, 720 - (startY += 100), 50, 50, Gdx.graphics.isFullscreen()){
			@Override
			public void onSelect(boolean selected) {
				Gdx.graphics.setDisplayMode(ValtersOptions.SCREEN_WIDTH, ValtersOptions.SCREEN_HEIGHT, selected);
			}
		});

		optionsMenu.addComponent(new UICheckBox("zoom", 700, 720 - (startY += 100), 50, 50, ValtersOptions.LIMITE_HORIZONTAL){
			@Override
			public void onSelect(boolean selected) {
				ValtersOptions.LIMITE_HORIZONTAL = selected;
			}
		});

		optionsMenu.addComponent(new UISelector("music", 700, 720 - (startY += 100) + 48, null,
												(int) (ValtersOptions.MUSIC_LEVEL * 10), "0%", "10%", "20%",
																				"30%", "40%", "50%", "60%",
																				"70%", "80%", "90%", "100%"){
			@Override
			public void onOptionChange(String value) {
				ValtersOptions.MUSIC_LEVEL = Float.parseFloat(value.replace("%","")) / 100f;
			}
		});

		optionsMenu.addComponent(new UISelector("sound", 700, 720 - (startY += 100) + 48, null,
				(int) (ValtersOptions.SOUND_LEVEL * 10), "0%", "10%", "20%",
				"30%", "40%", "50%", "60%",
				"70%", "80%", "90%", "100%"){

			@Override
			public void onOptionChange(String value) {
				ValtersOptions.SOUND_LEVEL = Float.parseFloat(value.replace("%","")) / 100f;
				buttonSound.play(0.9f * ValtersOptions.SOUND_LEVEL);
			}
		});

		startY = 100;
		optionsMenu.addComponent(new UILabel("fullscreenLabel", "Fullscreen", 400, 720 - (startY += 100)));
		optionsMenu.addComponent(new UILabel("zoomLabel", "Zoom", 400, 720 - (startY += 100)));
		optionsMenu.addComponent(new UILabel("musicLabel", "Música", 400, 720 - (startY += 100)));
		optionsMenu.addComponent(new UILabel("soundLabel", "Efeitos", 400, 720 - (startY += 100)));
	}

	private void createMainMenu(){
		mainMenu = new UIScene(1, "MainMenu"){
			@Override
			protected void onFocusChange(){
				buttonSound.play(.9f * ValtersOptions.SOUND_LEVEL);
			}
		};

		UIButton options;
		mainMenu.addComponent(options = new UIButton("options", 180, 720 - 580, 168, 63) {
			@Override
			public void onAction() {
				mainMenu.setActive(false);
				optionsMenu.setActive(true);
			}
		});
		options.setRegularTexture(assetManager.get("assets/texturas/opcoes-normal.png", Texture.class));
		options.setHoveredTexture(assetManager.get("assets/texturas/opcoes2.png", Texture.class));
		UIButton play;
		mainMenu.addComponent(play = new UIButton("play", 540, 720 - 670, 214, 76) {
			@Override
			public void onAction() {
				ValtersGame.changeMap(MapEscola.class);
			}
		});
		play.setRegularTexture(assetManager.get("assets/texturas/play-normal.png", Texture.class));
		play.setHoveredTexture(assetManager.get("assets/texturas/play2.png", Texture.class));
		UIButton exit;
		mainMenu.addComponent(exit = new UIButton("exit", 885, 720 - 275, 122, 59) {
			@Override
			public void onAction() {
				Gdx.app.exit();
			}
		});
		exit.setRegularTexture(assetManager.get("assets/texturas/sair-normal.png", Texture.class));
		exit.setHoveredTexture(assetManager.get("assets/texturas/sair2.png", Texture.class));
	}

	@Override
	public void onEscape(){
		if(optionsMenu.isActive()){
			optionsMenu.lostFocus();
			optionsMenu.setActive(false);
			mainMenu.setActive(true);
		}
	}
}

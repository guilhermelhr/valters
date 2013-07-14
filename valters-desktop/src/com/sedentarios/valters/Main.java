package com.sedentarios.valters;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Valter's Adventure";
		cfg.useGL20 = true;
		cfg.useCPUSynch = true;
		cfg.width = ValtersOptions.SCREEN_WIDTH;
		cfg.height = ValtersOptions.SCREEN_HEIGHT;
		cfg.fullscreen = false;
		cfg.addIcon("assets/texturas/icone.png", Files.FileType.Internal);
		cfg.addIcon("assets/texturas/icone16x.png", Files.FileType.Internal);
		cfg.addIcon("assets/texturas/icone32x.png", Files.FileType.Internal);
				
		new LwjglApplication(new ValtersGame(), cfg);
	}
}

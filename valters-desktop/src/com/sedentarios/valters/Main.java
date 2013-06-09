package com.sedentarios.valters;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Valter's Adventure";
		cfg.useGL20 = true;
		cfg.useCPUSynch = true;
		cfg.height = 720;
		cfg.width = (int) (cfg.height * 16/9f);
		cfg.fullscreen = false;
		
		new LwjglApplication(new ValtersGame(), cfg);
	}
}

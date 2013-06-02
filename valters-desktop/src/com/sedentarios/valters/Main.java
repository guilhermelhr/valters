package com.sedentarios.valters;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Valter's Adventure";
		cfg.useGL20 = true;
		cfg.width = 1280;
		cfg.height = 720;
		cfg.fullscreen = false;
		
		new LwjglApplication(new ValtersGame(), cfg);
	}
}

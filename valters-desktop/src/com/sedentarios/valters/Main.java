package com.sedentarios.valters;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "valters";
		cfg.useGL20 = true;
		cfg.width = 1280;
		cfg.height = 380;
		
		new LwjglApplication(new ValtersGame(), cfg);
	}
}

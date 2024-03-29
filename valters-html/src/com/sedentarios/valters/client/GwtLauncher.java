package com.sedentarios.valters.client;

import com.sedentarios.valters.ValtersGame;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(1280, 380);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new ValtersGame();
	}
}
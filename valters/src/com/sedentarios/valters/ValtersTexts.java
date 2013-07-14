package com.sedentarios.valters;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ValtersTexts {
	private static HashMap<String, String> texts;
	
	public static void load(){
		texts = new HashMap<String, String>();
		
		String[] falas = Gdx.files.internal("assets/falas.txt").readString("utf8").split(System.getProperty("line.separator"));
		for(String f : falas){
			String[] exp = f.split("#", 2);
			texts.put(exp[0], exp[1]);
		}
	}
	
	public static String get(String identifier){
		String text = texts.get(identifier);
		if(text != null){
			return text.replaceAll("(\\\\r)?\\\\n", "\n");
		}else{
			throw new GdxRuntimeException("Text not found: " + identifier);
		}
	}
	
}

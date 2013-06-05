package com.sedentarios.valters;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Map.Entry;

public class ControllerWrapper {
	private static HashMap<String, Integer> buttonToInput = new HashMap<String, Integer>();
	private static HashMap<String, Integer> keyToInput = new HashMap<String, Integer>();
	private static HashMap<String, Integer> axisToInput = new HashMap<String, Integer>();
	
	public static enum Controller{
		KEYBOARD, GAMEPAD
	}
	
	public static Controller lastInputFrom = Controller.KEYBOARD;
	
	public static void bindButtonToInput(int button, String in) {
		buttonToInput.put(in, button);
	}
	
	public static void bindKeyToInput(int key, String in){
		keyToInput.put(in, key);
	}
	
	/** negative axis for negative match, subtract 1 of original axis **/
	public static void bindAxisToInput(int axis, String in){
		axisToInput.put(in, axis);
	}
	
	public static void unbindInput(String in){
		while(buttonToInput.remove(in)!=null);
		while(keyToInput.remove(in)!=null);
		while(axisToInput.remove(in)!=null);
	}
	
	public static boolean isInputActive(String in){		
		if(ValtersGame.controller != null){
			for(Entry<String, Integer> s : axisToInput.entrySet()){
				if(s.getKey().equalsIgnoreCase(in)){
					if(ValtersGame.controller.getAxis(Math.abs(s.getValue()) - 1) == (s.getValue()>0?1f:-1f)){
						lastInputFrom = Controller.GAMEPAD;
						return true;
					}
				}
			}
			
			for(Entry<String, Integer> s : buttonToInput.entrySet()){
				if(s.getKey().equalsIgnoreCase(in)){
					if(ValtersGame.controller != null && ValtersGame.controller.getButton(s.getValue())){
						lastInputFrom = Controller.GAMEPAD;
						return true;
					}
				}
			}
		}
		
		for(Entry<String, Integer> s : keyToInput.entrySet()){
			if(s.getKey().equalsIgnoreCase(in)){
				if(Gdx.input.isKeyPressed(s.getValue())){
					lastInputFrom = Controller.KEYBOARD;
					return true;
				}
			}
		}
		
		return false;
	}
}

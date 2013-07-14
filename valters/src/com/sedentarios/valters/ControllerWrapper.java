package com.sedentarios.valters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map.Entry;

public class ControllerWrapper {
	private static HashMap<String, Integer> buttonToInput = new HashMap<String, Integer>();
	private static HashMap<String, Integer> keyToInput = new HashMap<String, Integer>();
	private static HashMap<String, Integer> axisToInput = new HashMap<String, Integer>();
	private static HashMap<String, Float> inputDelay = new HashMap<String, Float>();
	private static HashMap<String, Float> delay = new HashMap<String, Float>();
	private static Array<String> disabled = new Array<String>();


	public static enum Controller{
		KEYBOARD, GAMEPAD
	}
	
	public static Controller lastInputFrom = Controller.KEYBOARD;
	
	public static void bindButtonToInput(int button, String in) {
		buttonToInput.put(in, button);
		inputRegistered(in);
	}
	
	public static void bindKeyToInput(int key, String in){
		keyToInput.put(in, key);
		inputRegistered(in);
	}
	
	/** negative axis for negative match, subtract 1 of original axis **/
	public static void bindAxisToInput(int axis, String in) {
		axisToInput.put(in, axis);
		inputRegistered(in);
	}

	private static void inputRegistered(String in) {
		if(!inputDelay.containsKey(in)) inputDelay.put(in, 0f);
	}

	private static void inputActivated(String in){
		if(inputDelay.containsKey(in)){
			delay.put(in, inputDelay.get(in));
		}
	}

	public static void setInputDelay(String in, float delay) {
		if(inputDelay.containsKey(in)) inputDelay.remove(in);
		inputDelay.put(in, delay);
	}
	
	public static void unbindInput(String in){
		while(buttonToInput.remove(in)!=null);
		while(keyToInput.remove(in)!=null);
		while(axisToInput.remove(in)!=null);
	}
	
	public static boolean isInputActive(String in){
		return isInputActive(in, true);
	}
	
	public static void disableInput(String in){
		if(!disabled.contains(in, false)){
			disabled.add(in);
		}
	}
	
	public static void enableInput(String in){
		disabled.removeValue(in, false);
	}
	
	public static void enableAllInputs(){
		disabled.clear();
	}
	
	public static boolean isInputActive(String in, boolean checkDelay) {
		if(!disabled.contains(in, false) && (!checkDelay || !delay.containsKey(in))){
			if(ValtersGame.controller != null){
				for(Entry<String, Integer> s : axisToInput.entrySet()){
					if(s.getKey().equalsIgnoreCase(in)){
						if(ValtersGame.controller.getAxis(Math.abs(s.getValue()) - 1) == (s.getValue()>0?1f:-1f)){
							lastInputFrom = Controller.GAMEPAD;
							inputActivated(in);
							return true;
						}
					}
				}

				for(Entry<String, Integer> s : buttonToInput.entrySet()){
					if(s.getKey().equalsIgnoreCase(in)){
						if(ValtersGame.controller != null && ValtersGame.controller.getButton(s.getValue())){
							lastInputFrom = Controller.GAMEPAD;
							inputActivated(in);
							return true;
						}
					}
				}
			}

			for(Entry<String, Integer> s : keyToInput.entrySet()){
				if(s.getKey().equalsIgnoreCase(in)){
					if(Gdx.input.isKeyPressed(s.getValue())){
						lastInputFrom = Controller.KEYBOARD;
						inputActivated(in);
						return true;
					}
				}
			}
		}
		
		return false;
	}

	private static Array<String> removeDelay = new Array<String>();
	public static void update(){
		for(Entry<String, Float> d : delay.entrySet()){
			if(d.getValue() > 0f){
				d.setValue(d.getValue() - Gdx.graphics.getDeltaTime());
			}else{
				removeDelay.add(d.getKey());
			}
		}
		for(String s : removeDelay){
			delay.remove(s);
		}
		removeDelay.clear();
	}
}

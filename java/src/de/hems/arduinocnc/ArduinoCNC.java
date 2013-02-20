package de.hems.arduinocnc;

import processing.serial.Serial;

public class ArduinoCNC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Serial foo = new Serial(new PApplet());
		
		//initialize GUI here
		new ACNCGui();
	}

}

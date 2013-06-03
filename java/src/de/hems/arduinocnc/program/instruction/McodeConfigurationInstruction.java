package de.hems.arduinocnc.program.instruction;

import java.util.ArrayList;

import de.hems.arduino.interfaces.Instruction;

public class McodeConfigurationInstruction implements Instruction {
	protected String rest;
	
	public McodeConfigurationInstruction(String rest) {
		this.rest = rest;
	}
	
	public String toString() {
		String out = this.getClass().getName() + "\t- Value: " + this.rest;
		return out;
	}
	
	public String getRest() {
		return this.rest;
	}
	
	public ArrayList<String> getArduinoStack() {
		return new ArrayList<String>();
	}

}

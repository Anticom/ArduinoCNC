package de.hems.arduinocnc;

import java.util.ArrayList;

public class PlainInstruction /*implements Instruction*/ {
	protected char value;
	//optional params (e.g. for setting the feedrate)
	protected String additionalParam = null;
	protected char output;
	
	public PlainInstruction(char value) {
		this.value				= value;
	}
	
	public PlainInstruction(char value, String additionalParam) {
		this.value				= value;
		this.additionalParam	= additionalParam;
	}
	
	public ArrayList<String> getArduinoStack() {
		ArrayList<String> instructions = new ArrayList<String>();
		
		String instruction = "" + this.value;
		if(this.additionalParam != null) {
			instruction += this.additionalParam;
		}
		
		
		instructions.add(instruction);
		
		return instructions;
	}
}

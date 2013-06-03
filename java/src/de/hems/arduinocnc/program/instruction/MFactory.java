package de.hems.arduinocnc.program.instruction;

import java.util.HashMap;

import de.hems.arduino.interfaces.Instruction;

public class MFactory {
	public static final HashMap<Integer, String> messages;
	static {
		messages = new HashMap<Integer, String>();
		messages.put(1, "foo");
	}
	
	public static Instruction factory(int id) {
		MInstruction instruction = new MInstruction();
		
		return instruction;
	}
}

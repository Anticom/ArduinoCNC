package de.hems.arduinocnc.program.instruction;

public class PlainInstructionTranslator {
	protected char type;
	protected String value;
	
	public PlainInstructionTranslator(char type, String value) {
		this.type	= type;
		this.value	= value;
	}
	
	public char translate() {
		return '0';
	}
	
	public Object getValue() {
		//TODO implement (maybe multiple getValue[DataType] methods make more sense) 
		return new Object();
	}
}

package de.hems.arduinocnc.program;

public class Instructor {
	protected Program program;
	
	public Instructor(Program program) {
		this.program = program;
		
		this.program.dump();
	}
}

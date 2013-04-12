package de.hems.arduinocnc;

public class Instructor {
	protected Program program;
	
	public Instructor(Program program) {
		this.program = program;
		
		this.program.dump();
	}
}

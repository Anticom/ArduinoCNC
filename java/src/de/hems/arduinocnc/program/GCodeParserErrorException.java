package de.hems.arduinocnc.program;

public class GCodeParserErrorException extends Exception {
	public GCodeParserErrorException(String arg0) {
		super(arg0);
	}
	
	public GCodeParserErrorException(Throwable arg0) {
		super(arg0);
	}
	
	public GCodeParserErrorException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}

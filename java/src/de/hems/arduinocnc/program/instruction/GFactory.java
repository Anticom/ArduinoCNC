package de.hems.arduinocnc.program.instruction;

public class GFactory {
	
	
	public static Object factory(int id) {
		switch(id) {
			//movement instructions
			case 0:
			case 1:
			case 2:
			case 3:
				
				break;
			//legacy instructions
			default:
				
				break;
		}
		
		return null;
	}
}

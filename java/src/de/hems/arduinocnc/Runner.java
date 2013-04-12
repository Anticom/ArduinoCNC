package de.hems.arduinocnc;

import java.io.IOException;


public class Runner {
	public static void main(String[] args) throws Exception {
		GCodeParser gcp = new GCodeParser();
		
		try {
			gcp.readFromFile("C:\\Users\\Anticom\\Documents\\Arduino\\ArduinoCNC\\java\\src\\xyzcurve.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		gcp.prepare();
		//PARSING DONE
		
		Instructor instructor = new Instructor(gcp.program);
	}
}
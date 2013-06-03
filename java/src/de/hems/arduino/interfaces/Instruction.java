package de.hems.arduino.interfaces;

import java.util.ArrayList;

/**
 * a single instruction to be performed one after another
 * 
 * instructions do:
 * - move motors
 * - change configuration (tool offset, feed rate, etc)
 * 
 * @author Anticom
 *
 */
public interface Instruction {
	public ArrayList<String> getArduinoStack();
	public ArrayList<String> getIssues();
	public ArrayList<String> getInfos();
	
	public void process();
}
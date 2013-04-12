package de.hems.arduinocnc;

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
	//getter
	public double getX();
	public double getY();
	public double getZ();
	
	public int getRevPerMM_X();
	public int getRevPerMM_Y();
	public int getRevPerMM_Z();
	
	public int getPrecision();
	public ArrayList<String> getArduinoStack();
	
	//setter
	public void setX(double x);
	public void setY(double y);
	public void setZ(double z);
	
	public void setRevPerMM_X(int revPerMM);
	public void setRevPerMM_Y(int revPerMM);
	public void setRevPerMM_Z(int revPerMM);
	
	public void setPrecision(int precision);
	
	public boolean hasArg(char key);
	public double getArg(char key);
	public boolean setArg(char key, double val, boolean overwrite);
	public boolean setArg(char key, double val);
}
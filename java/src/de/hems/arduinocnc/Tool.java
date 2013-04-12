package de.hems.arduinocnc;

import java.util.ArrayList;

public class Tool {
	protected double diameter;
	protected Correction correctionMode = Correction.NONE;
	
	public enum Correction {
		NONE, LEFT, RIGHT
	}
	
	public Tool(double diamieter) {
		this.diameter	= diamieter;
	}
	
	public double getDiameter() {
		return diameter;
	}
	
	public void setDiameter(double diameter) {
		this.diameter	= diameter;
	}
	
	public void setRadius(double radius) {
		this.diameter	= 2 * radius;
	}
	
	public Correction getCorrectionMode() {
		return correctionMode;
	}
	
	public void setCorrectionMode(Correction c) {
		this.correctionMode = c;
	}
	
	//TODO: implementieren!
	public ArrayList<String> calcOffset(double x1, double y1, double z1, double x2, double y2, double z2) {
		ArrayList<String> out = new ArrayList<String>();
		
		return out;
	}
}
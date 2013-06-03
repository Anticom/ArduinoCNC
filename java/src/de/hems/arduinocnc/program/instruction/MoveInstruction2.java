package de.hems.arduinocnc.program.instruction;

import java.util.ArrayList;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class MoveInstruction2 extends AbstractInstruction {
	protected Vector3d movement;
	
	//this will be replaced by config object later on:
	protected int revPerMM_x	= 200;
	protected int revPerMM_y	= 200;
	protected int revPerMM_z	= 200;
	
	public MoveInstruction2(Vector3d movement) {
		this.movement = new Vector3d(movement);
	}
	
	public MoveInstruction2(Point3d movement) {
		this.movement = new Vector3d(movement);
	}
	
	public String toString() {
		return this.getClass().getName() + "\t\t\t- rel x: " + this.movement.x + ", rel y: " + this.movement.y + ", rel z: " + this.movement.z;
	}
	
	public void process() {
		arduinoStack.clear();
		
		String instruction = "M";
		instruction += "X" + Math.round(this.revPerMM_x * this.movement.x);
		instruction += "Y" + Math.round(this.revPerMM_y * this.movement.y);
		instruction += "Z" + Math.round(this.revPerMM_z * this.movement.z);
		
		arduinoStack.add(instruction);
	}
	
	public ArrayList<String> getArduinoStack() {
		return arduinoStack;
	}
}
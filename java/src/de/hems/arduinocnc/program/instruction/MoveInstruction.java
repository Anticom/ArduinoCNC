package de.hems.arduinocnc.program.instruction;

import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.Vector3d;

import de.hems.arduino.interfaces.Instruction;

public class MoveInstruction implements Instruction {
	protected double x;
	protected double y;
	protected double z;
	
	protected int revPerMM_X;
	protected int revPerMM_Y;
	protected int revPerMM_Z;
	
	protected int precision;
	
	protected HashMap<Character, Double> args = new HashMap<Character, Double>();
	
	public MoveInstruction() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public MoveInstruction(double rel_x, double rel_y, double rel_z) {
		this.x = rel_x;
		this.y = rel_y;
		this.z = rel_z;
	}
	
	public MoveInstruction(Vector3d vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}

	public String toString() {
		String out = "";
		
		out = this.getClass().getName() + "\t\t\t- rel x: " + this.x + ", rel y: " + this.y + ", rel z: " + this.z;
		/*
		for(String s: this.getArduinoStack()) {
			if(!out.isEmpty()) {
				out += "\n";
			}
			out += this.getClass().getName() + "\t\t\t- " + s;
		}
		*/
		return out;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}
	
	public int getRevPerMM_X() {
		return this.revPerMM_X;
	}
	
	public void setRevPerMM_X(int revPerMM) {
		this.revPerMM_X = revPerMM;
	}
	
	public int getRevPerMM_Y() {
		return this.revPerMM_Y;
	}
	
	public void setRevPerMM_Y(int revPerMM) {
		this.revPerMM_Y = revPerMM;
	}
	
	public int getRevPerMM_Z() {
		return this.revPerMM_Z;
	}
	
	public void setRevPerMM_Z(int revPerMM) {
		this.revPerMM_Z = revPerMM;
	}
	
	public int getPrecision() {
		return this.precision;
	}
	
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	
	public boolean hasArg(char key) {
		return this.args.containsKey(key);
	}
	
	public double getArg(char key) {
		if(this.args.containsKey(key)) {
			return this.args.get(key);
		} else {
			return 0;
		}
	}
	
	public boolean setArg(char key, double val, boolean overwrite) {
		if(overwrite) {
			this.args.put(key, val);
			return true;
		} else {
			//prüfen!
			if(!this.args.containsKey(key)) {
				this.args.put(key, val);
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean setArg(char key, double val) {
		return this.setArg(key, val, true);
	}

	public ArrayList<String> getArduinoStack() {
		ArrayList<String> instructions = new ArrayList<String>();
		
		//TODO change order of X|Y|Z according to run order that makes sense to avoid steps
		/*
		String instruction = "MX" + Math.round(this.revPerMM_X * this.x) + "Y" + Math.round(this.revPerMM_Y * this.y) + "Z" + Math.round(this.revPerMM_Z * this.z);
		X" + Math.round(this.revPerMM_X * this.x) + "Y" + Math.round(this.revPerMM_Y * this.y) + "Z" + Math.round(this.revPerMM_Z * this.z);
		instructions.add(instruction);
		*/
		
		String instruction = "";
		int x = (int) Math.round(this.revPerMM_X * this.x);
		int y = (int) Math.round(this.revPerMM_Y * this.y);
		int z = (int) Math.round(this.revPerMM_Z * this.z);
		if(x != 0) {
			instruction += "X" + x;
		}
		if(y != 0) {
			instruction += "Y" + y;
		}
		if(z != 0) {
			instruction += "Z" + z;
		}
		
		if(!instruction.isEmpty()) {
			instruction = "M" + instruction;
			instructions.add(instruction);
		}
		
		return instructions;
	}
}
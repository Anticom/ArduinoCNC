package de.hems.arduinocnc;

import java.util.ArrayList;


public class Line {
	protected int lineNo;
	protected ArrayList<Instruction> instructions = new ArrayList<Instruction>();
	
	public Line(int lineNo) {
		this.lineNo = lineNo;
	}
	
	public int getLineNo() {
		return this.lineNo;
	}
	
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	
	public void add(Instruction instruction) {
		this.instructions.add(instruction);
	}
	
	public Instruction pop() {
		return this.instructions.remove((this.instructions.size() - 1));
	}
	
	public ArrayList<Instruction> getInstructions() {
		return this.instructions;
	}
}
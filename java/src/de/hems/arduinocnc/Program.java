package de.hems.arduinocnc;

import java.util.ArrayList;
import java.util.Formatter;

public class Program {
	protected ArrayList<Line> lines = new ArrayList<Line>();
	protected CallStack stack = new CallStack();
	
	//public static Configuration config = new Configuration();
	
	public Program() {
		
	}
	
	public void add(Line line) {
		this.lines.add(line);
	}
	
	public void dump() {
		for(Line line: this.lines) {
			System.out.println("LINE " + line.getLineNo() + ":");
			
			for(Instruction instruction: line.getInstructions()) {
				System.out.println(instruction.toString());
			}
			
			System.out.println("--------------------");
		}
	}
}
package de.hems.arduinocnc.program;

import java.util.ArrayList;

import javax.vecmath.Point3d;

import de.hems.arduino.interfaces.Instruction;

public class Program {
	protected		ArrayList<Line>	lines		= new ArrayList<Line>();	//lines in the program
	protected		CallStack		stack		= new CallStack();			//callstack for subroutines
	
	protected		Point3d			abs_current	= new Point3d();			//current position
	protected		Point3d			abs_min		= new Point3d();			//minimal x,y,z
	protected 		Point3d			abs_max		= new Point3d();			//maximal x,y,z
	
	//public static	Configuration	config		= new Configuration();		//configuration containing mill specific static data
	
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
	
	public void start() {
		ArrayList<String> main = new ArrayList<String>();
		
		//iterate over lines
		for(Line line: this.lines) {
			//iterate over instructions for each line
			for(Instruction instruction: line.getInstructions()) {
				
				
				for(String s: instruction.getArduinoStack()) {
					main.add(s);
				}
			}
		}
		
		for(String s: main) {
			System.out.println(main);
		}
	}
}
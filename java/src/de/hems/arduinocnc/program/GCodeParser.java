package de.hems.arduinocnc.program;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import de.hems.arduinocnc.program.instruction.ConfigurationInstruction;
import de.hems.arduinocnc.program.instruction.GcodeConfigurationInstruction;
import de.hems.arduinocnc.program.instruction.McodeConfigurationInstruction;
import de.hems.arduinocnc.program.instruction.MoveInstruction2;

/**
 * G-Code Parser
 * 
 * @author Anticom
 */
public class GCodeParser {
	//CONFIGURABLE PARAMETERS:
	protected enum Unit {
		MM, INCH
	}
	protected enum Positioning {
		ABSOLUTE, RELATIVE
	}
	protected enum MovementMode {
		LINEAR,
		CIRCULAR_CLOCKWISE,
		CIRCULAR_COUNTER_CLOCKWISE
	}
	
	//runtime config
	protected Unit unit 					= Unit.MM;
	protected Positioning pos				= Positioning.ABSOLUTE;
	protected MovementMode mov				= /*MovementMode.LINEAR;*/ null;
	protected int Feedrate					= 2;
	
	
	//input variables
	protected ArrayList<String> gcode_text	= new ArrayList<String>();
	protected ArrayList<String> tokens		= new ArrayList<String>();
	
	//modes
	protected char current_m				= '0';
	protected char current_n				= '0';
	protected char current_g				= '0';
	
	//current line container
	protected Line current_line				= null;
	
	//global position of tool during program steps
	protected Point3d position_current		= new Point3d();
	protected Point3d position_next			= new Point3d();
	protected Vector3d position_delta		= new Vector3d();
	
	//output - the built program by gcode
	public Program program					= new Program();
	public ArrayList<Issue> issues			= new ArrayList<Issue>();
	
	//DEBUGGING
	protected boolean debug					= true;
	public ArrayList<Point3d> debugPoints	= null;
	
	/**
	 * Constructor
	 */
	public GCodeParser() {
		this.current_line = new Line(-1);
		
		if(this.debug) {
			this.debugPoints = new ArrayList<Point3d>();
			this.debugPoints.add(new Point3d(0,0,0));
		}
	}
	
	/**
	 * Read G-Code from File
	 * 
	 * @author Anticom
	 * @param file
	 * @throws IOException
	 */
	public void readFromFile(File file) throws IOException {
		//empty gcode:
		this.gcode_text.clear();
		
	    BufferedReader reader	= new BufferedReader(new FileReader(file));
	    String line				= null;
	    
	    while((line = reader.readLine()) != null) {
	    	this.gcode_text.add(line.trim());
	    }
	    
	    reader.close();
	}
	
	/**
	 * Read G-Code from String
	 * @param gcode The G-Code to be parsed
	 */
	public void readFromString(String gcode) {
		//empty gcode:
		this.gcode_text.clear();
		
		//convert String to ArrayList
		ArrayList<String> tmp = (ArrayList<String>) Arrays.asList(gcode.split("\\r?\\n"));
		for(String line : tmp) {
			this.gcode_text.add(line.trim());
		}
	}
	
	/**
	 * Public parsing wrapper
	 */
	public void parse() {
		if(!this.gcode_text.isEmpty()) {
			this.tokenize();
			this.doParse();
		} else {
			//TODO throw exception
		}
	}
	
	/**
	 * Tokenize read G-Code
	 * ensure, that all tokens are valid G-Code instructions
	 */
	protected void tokenize() {
		for(String line: this.gcode_text) {
			//current line not empty AND line no comment (obsolete!)
			//TODO check for standard of commenting gcode
			if(line.length() > 0 /*&& line.charAt(0) != ';'*/) {
				StringTokenizer tokenizer = new StringTokenizer(line);
				
				while(tokenizer.hasMoreTokens()) {
					String current_token	= tokenizer.nextToken();
					
					if(this.isToken(current_token)) {
						this.tokens.add(current_token);
					}
				}
			}
		}
	}
	
	/**
	 * Parse tokens
	 */
	protected void doParse() {
		for(int index = 0; index < this.tokens.size(); index++) {
			String token = this.tokens.get(index);
			
			char flag	= token.charAt(0);
			String rest	= token.substring(1); 
			
			switch(flag) {
				case 'N':
					if(this.current_line != null) {
						this.program.add(this.current_line);
					}
					this.current_line = new Line(Integer.parseInt(rest));
					break;
				case 'X':
				case 'Y':
				case 'Z':
					try {
						index = this.completeLinearXYZ(index);
					} catch (GCodeParserErrorException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 'G':
					this.current_line.add(new GcodeConfigurationInstruction(rest));
					break;
				case 'M':
					this.current_line.add(new McodeConfigurationInstruction(rest));
					break;
				default:
					//PlainInstructionTranslator pit = new PlainInstructionTranslator(flag, rest);
					//this.current_line.add(new PlainInstruction(pit.translate(), pit.getValue()));
					
					this.current_line.add(new ConfigurationInstruction(flag + "", rest));
					break;
			}
		}
		
		if(this.current_line.getLineNo() == -1) {
			this.program.add(this.current_line);
		}
	}
	
	/**
	 * Complete linear Milling instruction
	 * @param start_index
	 * @return
	 * @throws GCodeParserErrorException 
	 */
	protected int completeLinearXYZ(int start_index) throws GCodeParserErrorException {
		this.position_current.set(this.position_next);
		
		int index = start_index;
		boolean do_continue = true;
		HashMap<Character, Double> caught = new HashMap<Character, Double>();
		
		while(do_continue && index < this.tokens.size()) {
			String token = this.tokens.get(index);
			
			char flag	= token.charAt(0);
			String rest	= token.substring(1);
			
			switch(flag) {
				case 'X':
				case 'Y':
				case 'Z':
					if(!caught.containsKey(flag)) {
						caught.put(flag, this.parseDouble(rest));
					} else {
						//TODO throw parser error exception
						throw new GCodeParserErrorException("Flag " + flag + " was already set in tuple! (" + caught.toString() + ")");
					}
					break;
				default:
					do_continue = false;
					break;
			}
			
			/*
			if(do_continue) {
				index++;		//<-- this is the right solution. but still needs testing for adoptions
			}
			*/
			
			index++;
			
			//maximal 3 achsen pro 3-Tupel
			if((index - start_index) > 3) {
				do_continue = false;
			}
		}
		
		//nicht vorhandene auffüllen:
		if(caught.containsKey('X')) {
			this.position_next.x = caught.get('X');
		} else {
			this.position_next.x = this.position_current.x;
		}
		
		if(caught.containsKey('Y')) {
			this.position_next.y = caught.get('Y');
		} else {
			this.position_next.y = this.position_current.y;
		}
		
		if(caught.containsKey('Z')) {
			this.position_next.z = caught.get('Z');
		} else {
			this.position_next.z = this.position_current.z;
		}
		
		//debugging:
		if(this.debug) {
			this.debugPoints.add(new Point3d(this.position_next));
		}
		
		this.position_delta.x = this.roundToPrecision(this.position_next.x - this.position_current.x, 3);
		this.position_delta.y = this.roundToPrecision(this.position_next.y - this.position_current.y, 3);
		this.position_delta.z = this.roundToPrecision(this.position_next.z - this.position_current.z, 3);
		//System.out.println("COORD: " + this.position_next.toString());
		//System.out.println("DELTA: " + this.position_delta.toString());
		MoveInstruction2 mi = new MoveInstruction2(this.position_delta);
		this.current_line.add(mi);
		
		//TODO find cleaner solution?! check why index has to be decreased two times
		if(index == this.tokens.size()) {
			return index;
		}
		
		index--;
		index--;	//TODO <-- why is that?
		
		return index;
	}
	
	/**
	 * Auxiliary to determine, wether the current token is a valid token
	 * 
	 * Otherwise it is assumed, that the token is (part of) a comment
	 * and will be ignored
	 * 
	 * @param token
	 * @return
	 */
	protected boolean isToken(String token) {
		if(token.matches("(D-?\\d+)|([GHIJKLMNPQRST]-?\\d+)|(F\\d*\\.?\\d*)|([XYZ]-?\\d*\\.?\\d*)")) {
			return true;
		} else {
			return false;
		}
	}
	
	//getters / setters:
	public ArrayList<String> getGcode_text() {
		return this.gcode_text;
	}
	
	//auxiliaries:
	protected double parseDouble(String value) {
		return Double.parseDouble(value);
	}
	
	protected double roundToPrecision(double value, int precision) {
		return (double)Math.round(value * Math.pow(10, precision)) / Math.pow(10, precision);
	}
	
	protected double inch2mm(double inch) {
		return 0d;
	}
	
	protected void rel2Abs() {
		
	}
	
	protected void abs2Rel() {
		
	}
}

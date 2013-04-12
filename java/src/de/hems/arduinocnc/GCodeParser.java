package de.hems.arduinocnc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * G-Code Parser
 * @author Anticom
 *
 */
public class GCodeParser {
	//CONFIGURABLE PARAMETERS:
	protected enum Unit {
		MM, INCH
	}
	protected enum Positioning {
		ABSOLUTE, RELATIVE
	}
	
	protected Unit unit 		= Unit.MM;
	protected Positioning pos	= Positioning.ABSOLUTE;
	protected int Feedrate		= 2;
	
	
	//input variables
	protected ArrayList<String> gcode_text		= new ArrayList<String>();
	protected ArrayList<String> gcode_parsed	= new ArrayList<String>();
	
	//modes
	protected char current_m					= '0';
	protected char current_n					= '0';
	protected char current_g					= '0';
	
	//current line container
	//protected Line current_line					= new Line(-1);
	protected Line current_line;
	
	//global position of tool during program steps
	protected Point3d position_current			= new Point3d();
	protected Point3d position_next				= new Point3d();
	protected Vector3d position_delta			= new Vector3d();
	
	//output - the built program by gcode
	public Program program						= new Program();
	
	//protected ArrayList<String> issues			= new ArrayList<String>();
	
	public GCodeParser() {
		
	}
	
	/**
	 * 
	 * @param file
	 * @throws IOException
	 * @author Anticom
	 * @todo remove throws declaration and wrap in try-catch-block
	 */
	public void readFromFile(String file) throws IOException {
		//empty gcode:
		this.gcode_text.clear();
		
	    BufferedReader reader	= new BufferedReader( new FileReader (file));
	    String line				= null;
	    
	    while((line = reader.readLine()) != null) {
	    	this.gcode_text.add(line.trim());
	    }
	    
	    reader.close();
	}
	
	public void readFromString(String gcode) {
		//empty gcode:
		this.gcode_text.clear();
		
		//convert String to ArrayList
		ArrayList<String> tmp = (ArrayList<String>) Arrays.asList(gcode.split("\\r?\\n"));
		for(String line : tmp) {
			this.gcode_text.add(line.trim());
		}
	}
	
	public void prepare() {
		if(!this.gcode_text.isEmpty()) {
			this.parse();
			this.buildInstructionChain();
		} else {
			//TODO throw exception
		}
	}
	
	protected void parse() {
		for(String line: this.gcode_text) {
			//current line not empty AND line no comment (obsolete!)
			//TODO check for standard of commenting gcode
			if(line.length() > 0 && line.charAt(0) != ';') {
				StringTokenizer tokenizer = new StringTokenizer(line);
				
				while(tokenizer.hasMoreTokens()) {
					String current_token	= tokenizer.nextToken();
					
					if(this.isToken(current_token)) {
						this.gcode_parsed.add(current_token);
					}
				}
			}
		}
	}
	
	//build instruction chain after parsing the g code
	protected void buildInstructionChain() {
		for(int index = 0; index < this.gcode_parsed.size(); index++) {
			/*
			if(index == 10) {
				System.exit(0);
			}
			*/
			
			String token = this.gcode_parsed.get(index);
			
			char flag	= token.charAt(0);
			String rest	= token.substring(1); 
			
			switch(flag) {
				case 'G':
					//System.out.println("G-code of type: " + rest);
					this.current_line.add(new ConfigurationInstruction(flag + "", rest));
					break;
				case 'M':
					//System.out.println("M-code of type: " + rest);
					this.current_line.add(new ConfigurationInstruction(flag + "", rest));
					break;
				case 'N':
					if(this.current_line != null) {
						this.program.add(this.current_line);
					}
					this.current_line = new Line(Integer.parseInt(rest));
					break;
				case 'X':
				case 'Y':
				case 'Z':
					index = this.completeLinearXYZ(index);
					break;
				default:
					//System.out.println("Argument " + flag + " with value: " + rest);
					this.current_line.add(new ConfigurationInstruction(flag + "", rest));
					break;
			}
		}
	}
	
	protected int completeLinearXYZ(int start_index) {
		this.position_current.set(this.position_next);
		
		int index = start_index;
		boolean do_continue = true;
		HashMap<Character, Double> caught = new HashMap<Character, Double>();
		
		while(do_continue) {
			String token = this.gcode_parsed.get(index);
			
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
						System.err.println("Parser error: Flag " + flag + " was already set in tuple! (" + caught.toString() + ")");
					}
					break;
				default:
					do_continue = false;
					break;
			}
			
			index++;
			//maximal 3 achsen pro 3-Tupel
			if((index - start_index) > 3) {
				do_continue = false;
			}
		}
		
		index--;
		index--;	//TODO <-- why is that?
		
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
		
		this.position_delta.x = this.roundToPrecision(this.position_next.x - this.position_current.x, 3);
		this.position_delta.y = this.roundToPrecision(this.position_next.y - this.position_current.y, 3);
		this.position_delta.z = this.roundToPrecision(this.position_next.z - this.position_current.z, 3);
		//System.out.println("COORD: " + this.position_next.toString());
		//System.out.println("DELTA: " + this.position_delta.toString());
		MoveInstruction mi = new MoveInstruction(this.position_delta);
		mi.setRevPerMM_X(200);
		mi.setRevPerMM_Y(200);
		mi.setRevPerMM_Z(200);
		//System.out.println(mi.getArduinoStack().toString());
		this.current_line.add(mi);
		
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
		/*
		if(token.matches("D-?\\d+")) {
			return true;
		} else if(token.matches("F\\d*\\.?\\d*")) {
			return true;
		} else if(token.matches("G-?\\d+")) {
			return true;
		} else if(token.matches("H-?\\d+")) {
			return true;
		} else if(token.matches("I-?\\d+")) {
			return true;
		} else if(token.matches("J-?\\d+")) {
			return true;
		} else if(token.matches("K-?\\d+")) {
			return true;
		} else if(token.matches("L-?\\d+")) {
			return true;
		} else if(token.matches("M-?\\d+")) {
			return true;
		} else if(token.matches("N-?\\d+")) {
			return true;
		} else if(token.matches("P-?\\d+")) {
			return true;
		} else if(token.matches("Q-?\\d+")) {
			return true;
		} else if(token.matches("R-?\\d+")) {
			return true;
		} else if(token.matches("S-?\\d+")) {
			return true;
		} else if(token.matches("T-?\\d+")) {
			return true;
		} else if(token.matches("X-?\\d*\\.?\\d*")) {
			return true;
		} else if(token.matches("Y-?\\d*\\.?\\d*")) {
			return true;
		} else if(token.matches("Z-?\\d*\\.?\\d*")) {
			return true;
		} else {
			return false;
		}
		*/
	}
	
	protected double parseDouble(String value) {
		return Double.parseDouble(value);
	}
	
	protected double roundToPrecision(double value, int precision) {
		return (double)Math.round(value * Math.pow(10, precision)) / Math.pow(10, precision);
	}
}

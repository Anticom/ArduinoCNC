package de.hems.arduinocnc.program.instruction;

import java.util.ArrayList;

import de.hems.arduino.interfaces.Instruction;

public abstract class AbstractInstruction implements Instruction {
	//n,m,g
	protected String type;
	//01, 02, ...
	protected int id;
	//debugging information
	public Object dbg;
	
	protected	ArrayList<String>	arduinoStack		= new ArrayList<String>();
	protected	ArrayList<String>	issues				= new ArrayList<String>();
	protected	ArrayList<String>	infos				= new ArrayList<String>();
	public		int					pauseMs				= 0;
	public		boolean				pauseTillConfirm	= false; 
	
	//constructor:
	public AbstractInstruction() {
		
	}
	
	public AbstractInstruction(String type, int id) {
		this.type		= type;
		this.id			= id;
	}
	
	public AbstractInstruction(String type, int id, String dbg) {
		this.type		= type;
		this.id			= id;
		this.dbg		= dbg;
	}
	
	public AbstractInstruction(String type, int id, int pauseMs) {
		this.type		= type;
		this.id			= id;
		this.pauseMs	= pauseMs;
	}
	
	public AbstractInstruction(String type, int id, int pauseMs, String dbg) {
		this.type		= type;
		this.id			= id;
		this.pauseMs	= pauseMs;
		this.dbg		= dbg;
	}
	
	//getter / setter:
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	//commonly used methods
	abstract public void process();
	
	public ArrayList<String> getArduinoStack() {
		return arduinoStack;
	}
	
	public ArrayList<String> getIssues() {
		return issues;
	}
	
	public ArrayList<String> getInfos() {
		return infos;
	}
	
	public int getPauseMs() {
		return pauseMs;
	}
}

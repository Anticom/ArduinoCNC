package de.hems.arduinocnc.program;

public class Issue {
	public enum Type {
		INFO,
		WARNING,
		CRITICAL
	}
	
	protected String	message;
	protected Type		type;
	
	public Issue(String message, Type type) {
		this.message	= message;
		this.type		= type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}

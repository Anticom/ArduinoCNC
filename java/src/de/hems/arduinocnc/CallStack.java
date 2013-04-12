package de.hems.arduinocnc;

import java.util.Stack;


public class CallStack {
	protected Stack<Integer> stack = new Stack<Integer>();
	
	public CallStack() {
		
	}
	
	public String toString() {
		String out = "\nCALL STACK DUMP";
		
		for(int i = 0; i < this.stack.size(); i++) {
			out += "\n" + this.stack.get(i);
		}
		
		out += "\n---------------";
		
		return out;
	}
	
	public void add(int line) {
		this.stack.add(line);
	}
	
	public int peek() {
		return this.stack.peek();
	}
	
	public int pop() {
		return this.stack.pop();
	}
	
	public boolean empty() {
		return this.stack.empty();
	}
}
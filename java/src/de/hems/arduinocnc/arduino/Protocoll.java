package de.hems.arduinocnc.arduino;

public class Protocoll {
	/*
	 * TODO: setup mode / manual mode
	 */
	public class Output {
		public static final char reset				= 'Z';
		public static final char ping				= 'I';
		public static final char version			= 'V';
		public static final char milling_start		= 'S';
		public static final char movement_data		= 'M';
		public static final char feed_rate			= 'F';
		public static final char program_pause		= 'P';
		public static final char program_continue	= 'C';
		public static final char milling_stop		= 'T';
		public static final char x					= 'X';
		public static final char y					= 'Y';
		public static final char z					= 'Z';
		
		//may be implemented int future:
		public static final char spindle_on			= 'N';
		public static final char spindle_off		= 'F';
		public static final char spindle_speed		= 'D';
	}
	
	public class Input {
		public static final char was_init			= 'A';
		public static final char pong				= 'P';
		public static final char error				= 'R';
		public static final char send_next			= 'N';
	}
}

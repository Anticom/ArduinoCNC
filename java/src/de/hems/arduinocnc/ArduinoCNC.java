package de.hems.arduinocnc;

//just for testing purposes... will be removed later:

import gnu.io.*;
import processing.core.*;
import processing.serial.Serial;
/*
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
*/

public class ArduinoCNC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//initialize GUI here
		//new ACNCGui();
		
		//TESTING SOME BITS AND PIECES HERE...

		System.out.println(Serial.list());
		/*
		Enumeration foo = CommPortIdentifier.getPortIdentifiers();
		while (foo.hasMoreElements()) {
			System.out.println(foo.nextElement());
		}
		*/
		
	}

}

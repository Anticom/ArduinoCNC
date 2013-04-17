package de.hems.arduinocnc;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class ConfigReader {
	public static void main(String args[]) {
		try {
			Reader r		= new FileReader("C:\\Users\\Anticom\\Documents\\Arduino\\ArduinoCNC\\java\\src\\config.ini");
			Properties p	= new Properties();
			p.load(r);
			p.list(System.out);
			r.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

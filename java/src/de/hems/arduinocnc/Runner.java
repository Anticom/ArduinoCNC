package de.hems.arduinocnc;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import de.hems.arduinocnc.arduino.SerialWrapper;
import de.hems.arduinocnc.program.GCodeParser;
import de.hems.arduinocnc.program.Program;
import de.hems.arduinocnc.util.MessageConsole;

public class Runner {
	protected GCodeParser	gcp	= new GCodeParser();
	protected ConfigReader	cr	= new ConfigReader();
	protected SerialWrapper	sw	= new SerialWrapper();
	protected Program 		p;
	
	public static void main(String[] args) throws Exception {
		new Runner();
	}
	
	public Runner() {
		//change the look and feel to default settings
		try {
		  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		/*
		MainModel		model		= new MainModel();
		MainView		view		= new MainView();
		
		//redirect stdout and stderr to log
		MessageConsole mc = new MessageConsole(view.taLog);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(100);
		
		MainController	controller	= new MainController(model, view);
		*/
		
		gcp = new GCodeParser();
		
		//File file = new File("C:\\Users\\Anticom\\Documents\\Arduino\\ArduinoCNC\\java\\src\\xyzcurve.txt");
		File file = new File("C:\\Users\\Anticom\\Documents\\Arduino\\ArduinoCNC\\java\\src\\air_show_picture.txt");
		
		try {
			gcp.readFromFile(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		view.taGcode.setText("");
		for(String s: gcp.getGcode_text()) {
			view.taGcode.append(s + "\n");
		}
		*/
		
		//parse
		gcp.parse();
		//parsing done
		
		//gcp.program.start();
		
		//Instructor instructor = new Instructor(gcp.program);
		//---------------------------------------------------------------
		
		//double scale = 0.05;
		double scale = 1;
		for(int i = 0; i < gcp.debugPoints.size(); i++) {
			gcp.debugPoints.get(i).scale(scale);
		}
		
		DebugVisualizer3D visual=  new DebugVisualizer3D(gcp.debugPoints);
		int pointSize	= 10;
		boolean aliased	= true;
		boolean showCoordinateSystem = true;
		boolean showSimulation = true;
		//visual.renderPointCloud(gcp.debugPoints, pointSize, aliased, showCoordinateSystem, showSimulation);
		visual.renderLinePath(gcp.debugPoints, pointSize, aliased, showCoordinateSystem, showSimulation);
		
	}
}
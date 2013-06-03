package de.hems.arduinocnc;

import java.awt.Color;

import javax.swing.UIManager;

import de.hems.arduinocnc.util.MessageConsole;

public class MainController {
	protected MainModel	model;
	protected MainView	view;
	
	public MainController(MainModel model, MainView view) {
		this.model	= model;
		this.view	= view;
		
		view.setVisible(true);
	}
}

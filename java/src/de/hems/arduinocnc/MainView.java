package de.hems.arduinocnc;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.vecmath.Point3d;

public class MainView extends JFrame {
	public JTextArea taGcode;
	public JTextArea taIssues;
	public JTextArea taLog;
	
	private JScrollPane spGcode;
	private JScrollPane spIssues;
	private JScrollPane spLog;
	
	public MainView() {
		BorderLayout borderLayout = (BorderLayout) getContentPane().getLayout();
		setTitle("Arduino CNC Server - V 1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		final JFileChooser fc = new JFileChooser();
		final FileNameExtensionFilter fc_filter = new FileNameExtensionFilter("GCode File (*.txt, *.gc)", "txt", "gc");
		fc.addChoosableFileFilter(fc_filter);
		fc.setFileFilter(fc_filter);
		JMenuItem mntmOpenGcode = new JMenuItem("Open G-Code..."/*, createImageIcon("img/folder-horizontal-open.png")*/);
		mntmOpenGcode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showOpenDialog(MainView.this);
				
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                System.out.print("\nchoose: ");
	                System.out.println(file.getAbsolutePath());
	            } else {
	            	//do nothing here :)
	                //System.out.println("aborted");
	            }
			}
		});
		mnFile.add(mntmOpenGcode);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
		
		JMenu menu = new JMenu("?");
		menuBar.add(menu);
		
		JMenuItem mntmHelp = new JMenuItem("Help");
		menu.add(mntmHelp);
		
		JMenuItem mntmAboutAcncs = new JMenuItem("About ACNCS");
		menu.add(mntmAboutAcncs);
		
		/*
		Visualizer3D v3d = new Visualizer3D();
		pVisualisation.add(v3d);
		*/
		
		JPanel pMain = new JPanel();
		getContentPane().add(pMain, BorderLayout.CENTER);
		pMain.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		pMain.add(tabbedPane, BorderLayout.CENTER);
		
		taGcode = new JTextArea();
		taGcode.setEditable(false);
		spGcode = new JScrollPane(taGcode);
		tabbedPane.addTab("G-Code", null, spGcode, null);
		
		taIssues = new JTextArea();
		taIssues.setEditable(false);
		spIssues = new JScrollPane(taIssues);
		tabbedPane.addTab("Issues", null, spIssues, null);
		
		JPanel pVisualisation = new JPanel();
		tabbedPane.addTab("Visualisation", null, pVisualisation, null);
		pVisualisation.setLayout(new BorderLayout(0, 0));
		VisualizerExample st = new VisualizerExample(new ArrayList<Point3d>());
		pVisualisation.add(st);
		
		JPanel pControlPanel = new JPanel();
		tabbedPane.addTab("Control Panel", null, pControlPanel, null);
		
		JPanel pLog = new JPanel();
		pMain.add(pLog, BorderLayout.SOUTH);
		pLog.setLayout(new BorderLayout(0, 0));
		
		JLabel lblLog = new JLabel("Log");
		pLog.add(lblLog, BorderLayout.NORTH);
		
		taLog = new JTextArea();
		taLog.setEditable(false);
		spLog = new JScrollPane(taLog);
		pLog.add(spLog, BorderLayout.CENTER);
		
		JPanel pStatus = new JPanel();
		pStatus.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		getContentPane().add(pStatus, BorderLayout.SOUTH);
		pStatus.setLayout(new BorderLayout(0, 0));
		
		JLabel lblStatus = new JLabel("Ready");
		pStatus.add(lblStatus, BorderLayout.CENTER);
		
		setSize(800, 600);
		setExtendedState( getExtendedState()|JFrame.MAXIMIZED_BOTH );
	}
	
	protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = MainView.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}

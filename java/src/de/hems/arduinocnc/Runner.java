package de.hems.arduinocnc;

import java.io.IOException;
import java.util.ArrayList;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.org.apache.xpath.internal.axes.SelfIteratorNoPredicate;


public class Runner {
	public static void main(String[] args) throws Exception {
		GCodeParser gcp = new GCodeParser();
		
		try {
			//gcp.readFromFile("C:\\Users\\Anticom\\Documents\\Arduino\\ArduinoCNC\\java\\src\\xyzcurve.txt");
			gcp.readFromFile("C:\\Users\\Anticom\\Documents\\Arduino\\ArduinoCNC\\java\\src\\air_show_picture.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//parse
		gcp.prepare();
		//parsing done
		
		//Instructor instructor = new Instructor(gcp.program);
		
		//---------------------------------------------------------------
		double scale = /*0.05*/1;
		for(int i = 0; i < gcp.debugPoints.size(); i++) {
			gcp.debugPoints.get(i).scale(scale);
		}
		
		DebugVisualizer3D visual=  new DebugVisualizer3D(gcp.debugPoints);
		int pointSize	= 10;
		boolean aliased	= true;
		boolean showCoordinateSystem = true;
		boolean showSimulation = true;
		//visual.renderPointCloud(gcp.debugPoints, pointSize, aliased, showCoordinateSystem);
		visual.renderLinePath(gcp.debugPoints, pointSize, aliased, showCoordinateSystem, showSimulation);
		
	}
}
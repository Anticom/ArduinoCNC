package de.hems.arduinocnc;

import java.awt.GraphicsConfiguration;
import java.util.ArrayList;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.PositionPathInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class Visualizer3D extends JPanel {
	protected ArrayList<Point3d> points;

	public Visualizer3D() {
		this.points = new ArrayList<Point3d>();
	}

	public Visualizer3D(ArrayList<Point3d> points) {
		this.points = points;
	}

	public void renderPointCloud(ArrayList<Point3d> points, int pointSize,
			boolean aliased, boolean showCoordinateSystem,
			boolean showSimulation) {
		SimpleUniverse universe = new SimpleUniverse();
		// BranchGroup bg = new BranchGroup();

		// --- ADD POINTS
		PointArray pointArray = new PointArray(points.size(),
				GeometryArray.COORDINATES | GeometryArray.COLOR_3);

		Color3f pointColor = new Color3f(1f, 1f, 1f);
		for (int i = 0; i < points.size(); i++) {
			Point3d p = points.get(i);
			pointArray.setCoordinate(i, p);
			// pointArray.setColor(i, new Color3f(0.5f, 0.5f, 0.5f));
			pointArray.setColor(i, pointColor);
		}

		// create the material for the points
		Appearance pointApp = new Appearance();
		// enlarge the points
		pointApp.setPointAttributes(new PointAttributes(pointSize, aliased));
		// change transparency
		pointApp.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.FASTEST, 0.5f));

		Shape3D pointShape = new Shape3D(pointArray, pointApp);

		BranchGroup bg = new BranchGroup();
		bg.addChild(pointShape);

		universe.addBranchGraph(bg);

		// --- ADD POINTS END

		// --- ADD COORDINATE SYSTEM AND SIMULATION
		if (showCoordinateSystem) {
			universe = this.addCoordinateSystem(universe);
		}
		if (showSimulation) {
			universe = this.addSimulation(universe, points);
		}
		// --- ADD COORDINATE SYSTEM END

		// scene done, now care about displaying and movement
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		Canvas3D c = new Canvas3D(config);

		universe.getViewingPlatform().setNominalViewingTransform();

		ViewingPlatform viewingPlatform = universe.getViewingPlatform();
		// This will move the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		viewingPlatform.setNominalViewingTransform();
		universe.getViewer().getView().setBackClipDistance(100d);

		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);

		// add orbit behavior but disable translate
		OrbitBehavior orbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL
		/* | OrbitBehavior.DISABLE_TRANSLATE */);
		orbit.setSchedulingBounds(bounds);
		viewingPlatform.setViewPlatformBehavior(orbit);
	}

	public void renderLinePath(ArrayList<Point3d> points_in, int pointSize,
			boolean aliased, boolean showCoordinateSystem,
			boolean showSimulation) {
		// translate point array to couples
		ArrayList<Point3d> points = new ArrayList<Point3d>();

		// first point
		points.add(points_in.get(0));
		points.add(points_in.get(1));

		// other points
		for (int i = 1; i < points_in.size(); i++) {
			points.add(points_in.get(i - 1));
			points.add(points_in.get(i));
		}

		// --------------------------------

		SimpleUniverse universe = new SimpleUniverse();
		BranchGroup bg = new BranchGroup();

		// --- ADD POINTS
		LineArray lineArray = new LineArray(points.size(),
				LineArray.COORDINATES);
		// PointArray pointArray = new PointArray(points.size(),
		// GeometryArray.COORDINATES | GeometryArray.COLOR_3);

		for (int i = 0; i < points.size(); i++) {
			Point3d p = points.get(i);
			lineArray.setCoordinate(i, p);
			// lineArray.setColor(i, pointColor);
		}

		// create the material for the path
		final int lineWidth = 2;
		final int linePattern = LineAttributes.PATTERN_SOLID;
		final boolean lineAliased = true;
		Appearance lineApp = new Appearance();
		lineApp.setLineAttributes(new LineAttributes(lineWidth, linePattern,
				lineAliased));

		Shape3D lineShape = new Shape3D(lineArray, lineApp);

		// START END END POINTS
		PointArray pointArray = new PointArray(2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3);
		Color3f colorStart = new Color3f(0f, 1f, 0f);
		Color3f colorEnd = new Color3f(1f, 0f, 0f);

		pointArray.setCoordinate(0, points.get(0));
		pointArray.setColor(0, colorStart);

		pointArray.setCoordinate(1, points.get((points.size() - 1)));
		pointArray.setColor(1, colorEnd);

		// create the material for the path
		Appearance pointApp = new Appearance();
		// enlarge the points
		pointApp.setPointAttributes(new PointAttributes(pointSize, aliased));
		Shape3D pointShape = new Shape3D(pointArray, pointApp);
		// --------------------

		bg.addChild(lineShape);
		bg.addChild(pointShape);

		universe.addBranchGraph(bg);

		// --- ADD POINTS END

		// --- ADD COORDINATE SYSTEM AND SIMULATION
		if (showCoordinateSystem) {
			universe = this.addCoordinateSystem(universe);
		}
		if (showSimulation) {
			universe = this.addSimulation(universe, points_in);
		}
		// --- ADD COORDINATE SYSTEM END

		// scene done, now care about displaying and movement
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		Canvas3D c = new Canvas3D(config);

		universe.getViewingPlatform().setNominalViewingTransform();

		ViewingPlatform viewingPlatform = universe.getViewingPlatform();
		// This will move the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		viewingPlatform.setNominalViewingTransform();
		universe.getViewer().getView().setBackClipDistance(100d);
		
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);

		// add orbit behavior but disable translate
		OrbitBehavior orbit = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL
		/* | OrbitBehavior.DISABLE_TRANSLATE */);
		orbit.setSchedulingBounds(bounds);
		viewingPlatform.setViewPlatformBehavior(orbit);
	}

	protected SimpleUniverse addCoordinateSystem(SimpleUniverse universe) {
		final float minimum = -10;
		final float maximum = 10;
		final float grid_resolution = 1;

		final int axisWidth = 2;
		final int axisPattern = LineAttributes.PATTERN_SOLID;
		final boolean axisAliased = true;
		final float axisTransparency = 0.5f;

		final int gridWidth = 1;
		final int gridPattern = LineAttributes.PATTERN_SOLID;
		final boolean gridAliased = true;
		final float gridTransparency = 0.1f;
		// --------------------------------------------------------

		Appearance axisAppearance = new Appearance();
		axisAppearance.setLineAttributes(new LineAttributes(axisWidth,
				axisPattern, axisAliased));
		axisAppearance.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.FASTEST, axisTransparency));

		Appearance gridAppearance = new Appearance();
		gridAppearance.setLineAttributes(new LineAttributes(gridWidth,
				gridPattern, gridAliased));
		gridAppearance.setTransparencyAttributes(new TransparencyAttributes(
				TransparencyAttributes.FASTEST, gridTransparency));

		// x
		LineArray xAxis = new LineArray(2, LineArray.COORDINATES);
		xAxis.setCoordinate(0, new Point3d(minimum, 0, 0));
		xAxis.setCoordinate(1, new Point3d(maximum, 0, 0));
		// y
		LineArray yAxis = new LineArray(2, LineArray.COORDINATES);
		yAxis.setCoordinate(0, new Point3d(0, minimum, 0));
		yAxis.setCoordinate(1, new Point3d(0, maximum, 0));
		// z
		LineArray zAxis = new LineArray(2, LineArray.COORDINATES);
		zAxis.setCoordinate(0, new Point3d(0, 0, minimum));
		zAxis.setCoordinate(1, new Point3d(0, 0, maximum));

		Shape3D xShape = new Shape3D(xAxis, axisAppearance);
		Shape3D yShape = new Shape3D(yAxis, axisAppearance);
		Shape3D zShape = new Shape3D(zAxis, axisAppearance);

		BranchGroup coordinateSystem = new BranchGroup();
		coordinateSystem.addChild(xShape);
		coordinateSystem.addChild(yShape);
		coordinateSystem.addChild(zShape);

		// GRID
		ArrayList<Point3d> tmp = new ArrayList<Point3d>();
		for (float x = minimum; x <= maximum; x += grid_resolution) {
			if (x != 0) {
				tmp.add(new Point3d(x, minimum, 0));
				tmp.add(new Point3d(x, maximum, 0));
			}
		}
		for (float y = minimum; y <= maximum; y += grid_resolution) {
			if (y != 0) {
				tmp.add(new Point3d(minimum, y, 0));
				tmp.add(new Point3d(maximum, y, 0));
			}
		}

		LineArray gridLines = new LineArray(tmp.size(), LineArray.COORDINATES);
		for (int i = 0; i < tmp.size(); i++) {
			gridLines.setCoordinate(i, tmp.get(i));
		}
		Shape3D gridShape = new Shape3D(gridLines, gridAppearance);

		coordinateSystem.addChild(gridShape);
		// -------------------------------------------------------

		universe.addBranchGraph(coordinateSystem);

		return universe;
	}

	protected SimpleUniverse addSimulation(SimpleUniverse universe, ArrayList<Point3d> points) {
		final int num_points = points.size();
		final double secPerSegment = 0.1;
		
		final float tracerRadius = 0.025f;
		final float tracerHeight = 0.1f;
		
		final Color3f emissiveColor	= new Color3f(0.25f, 0.25f, 1f);
		final Color3f emptyColor	= new Color3f();
		// -------- config end ---------
	    final long cycleTime = Math.round(num_points * secPerSegment * 1000);
		
		//tracer object
		Appearance tracerAppear = new Appearance();
        tracerAppear.setMaterial(new Material(emptyColor, emissiveColor, emptyColor, emptyColor, 1f));
		
        //INSTANCIATE TRACER OBJECT
        //sphere:
        Sphere tracer = new Sphere(tracerRadius, tracerAppear);
        
        //cylinder:
        //Cylinder tracer = new Cylinder(tracerRadius, tracerHeight, tracerAppear);
		
		BoundingSphere bounds = new BoundingSphere();

		float[] knots = new float[num_points];
		for (int n = 0; n < num_points; ++n) {
			knots[n] = (float) n * (1 / (float) (num_points - 1));
		}

		Point3f[] keypoints = new Point3f[num_points];
		for (int i = 0; i < num_points; i++) {
			keypoints[i] = new Point3f(points.get(i));
		}

		TransformGroup tracerTransform = new TransformGroup();
		tracerTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		PositionPathInterpolator tracerInterpolator = new PositionPathInterpolator(
				new Alpha(-1, cycleTime), tracerTransform, new Transform3D(), knots,
				keypoints);
		tracerInterpolator.setSchedulingBounds(bounds);
		
		tracerTransform.addChild(tracer);
		tracerTransform.addChild(tracerInterpolator);
		
		BranchGroup tracerGroup = new BranchGroup();
		tracerGroup.addChild(tracerTransform);
		
		universe.addBranchGraph(tracerGroup);
		
		return universe;
	}
}
package ablauf;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.vecmath.Point3f;

/**
 *
 * @author Rafael Müller_2
 * 
 * Create:	
 * Version:	2.0
 * 
 * Last Change:	
 */
public class Programm_003 {
    public Programm_003() {
	JFrame frame = new JFrame();
	frame.setTitle("Rubik's Cube Version 3.0");
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	frame.setSize(800, 800);
	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
	frame.setLayout(new BorderLayout());


	frame.add("Center", createBranchGraph());
	frame.setVisible(true);
    }

    private Component createBranchGraph() {
	Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

	SimpleUniverse universe = new SimpleUniverse(canvas);
	universe.getViewingPlatform().setNominalViewingTransform();

	BranchGroup objRoot = new BranchGroup();

	Transform3D rotate = new Transform3D();
	Transform3D temp_rot = new Transform3D();
	temp_rot.rotY(Math.toRadians(45));
	rotate.rotX(Math.toRadians(20));
	rotate.mul(temp_rot);

	TransformGroup objTransform = new TransformGroup(rotate);
	objRoot.addChild(objTransform);
	
	Shape3D shape = new Shape3D();
	
	QuadArray quad = new QuadArray(4, GeometryArray.COORDINATES);
	
	quad.setCoordinate(0, new Point3f( 0.5f,-0.5f, 0));
	quad.setCoordinate(1, new Point3f( 0.5f, 0.5f, 0));
	quad.setCoordinate(2, new Point3f(-0.5f, 0.5f, 0));
	quad.setCoordinate(3, new Point3f(-0.5f,-0.5f, 0));
	
	shape.addGeometry(quad);

	objTransform.addChild(shape);

	objRoot.compile();
	universe.addBranchGraph(objRoot);
	return canvas;
    }

    public static void main(String[] args) {
	new Programm_003();
    }
}

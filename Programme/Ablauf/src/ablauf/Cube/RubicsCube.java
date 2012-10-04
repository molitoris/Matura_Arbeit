package ablauf.Cube;

import ablauf.Cube.Stone;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rafael Müller_2
 */
public class RubicsCube extends JFrame {
    public BranchGroup objRoot = new BranchGroup();

    RubicsCube() {
	JFrame frame = new JFrame();
	frame.setTitle("Rubik's Cube Version 4.0");
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	frame.setSize(800, 800);
	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
	frame.setLayout(new BorderLayout());

	frame.add("Center", createBranchGraph());
	frame.setVisible(true);
    }
    
    private Component createBranchGraph() {
	GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
	Canvas3D canvas = new Canvas3D(config);

	SimpleUniverse universe = new SimpleUniverse(canvas);
	universe.getViewingPlatform().setNominalViewingTransform();
	universe.addBranchGraph(createSceneGraph());
	
	return canvas;
    }

    public BranchGroup createSceneGraph() {
	Transform3D rotate = new Transform3D();
	Transform3D temp_rot = new Transform3D();
	temp_rot.rotX(Math.toRadians(20));
	rotate.rotY(Math.toRadians(-45));
	rotate.mul(temp_rot);
	TransformGroup objTransform = new TransformGroup(rotate);

	Shape3D stone = new Stone().createStone(0.0f, 0.0f, 0.0f);

	objTransform.addChild(stone);
	objRoot.addChild(objTransform);
	return objRoot;
    }
}

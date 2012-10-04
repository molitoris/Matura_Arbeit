package ablauf;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author Rafael Müller_2
 * 
 * Create:	
 * Version:	2.0
 * 
 * Last Change:	
 */
public class Programm_001 {
    public Programm_001() {
	JFrame frame = new JFrame();
	frame.setTitle("Rubik's Cube Version 1.0");
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
//	universe.getViewingPlatform().setNominalViewingTransform();
//
//	BranchGroup objRoot = new BranchGroup();
//
//	objRoot.addChild(new ColorCube(0.4f));
//
//	objRoot.compile();
//	universe.addBranchGraph(objRoot);
	return canvas;
    }

    public static void main(String[] args) {
	new Programm_001();
    }
}

package animation;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

/**
 *
 * @author Rafael Müller_2
 */
public class Animation extends Applet
{

    public Animation()
    {	
	setLayout(new BorderLayout());
	GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
	Canvas3D canvas3D = new Canvas3D(config);
	add("Center", canvas3D);       
	BranchGroup scene = createSceneGraph();
	scene.compile();
	SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
	simpleU.getViewingPlatform().setNominalViewingTransform();
	simpleU.addBranchGraph(scene);
    }
    
    private BranchGroup createSceneGraph()
    {
	BranchGroup objRoot = new BranchGroup();
	
	Transform3D  rotate = new Transform3D();
	TransformGroup objTransform = new TransformGroup(rotate);
	
	//Capability setzen
	objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	
	objTransform.addChild(new ColorCube(0.4d));
	
	Transform3D yAxis = new Transform3D();
	yAxis.rotZ(Math.PI/2.0f);
	

	Alpha objAlpha = new Alpha(1, 2000);	
	RotationInterpolator objInterpolator = new RotationInterpolator(objAlpha, objTransform, yAxis, 0.0f, (float) Math.toRadians(90));
	
	BoundingSphere objSphere = new BoundingSphere();	
	objInterpolator.setSchedulingBounds(objSphere);
		
	objRoot.addChild(objInterpolator);
	objRoot.addChild(objTransform);
	
	
	return objRoot;
    }    
    
    public static void main (String [] agrs)
    {
	Frame frame = new MainFrame(new Animation(), 500, 500);
    }

    
}

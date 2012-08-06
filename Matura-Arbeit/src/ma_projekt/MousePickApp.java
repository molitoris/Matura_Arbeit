/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ma_projekt;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.pickfast.behaviors.PickRotateBehavior;
import com.sun.j3d.utils.pickfast.behaviors.PickTranslateBehavior;
import com.sun.j3d.utils.pickfast.behaviors.PickZoomBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 *
 * @author Rafael Müller_2
 */
public class MousePickApp extends Applet
{
    public BranchGroup createSceneGraph(Canvas3D canvas)
    {
	BranchGroup objRoot = new BranchGroup();
	
	TransformGroup objRotate = null;
	Transform3D transform = new Transform3D();
	BoundingSphere behaveBounds = new BoundingSphere();
	
	transform.setTranslation(new Vector3f(0.0f, 0.0f, 0.0f));
	objRotate = new TransformGroup(transform);
	
	objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	objRotate.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
	
	objRoot.addChild(objRotate);
	objRotate.addChild(new ColorCube(0.4));
	
	PickRotateBehavior pickRotate = new PickRotateBehavior(objRoot, canvas, behaveBounds);
	PickZoomBehavior pickZoom = new PickZoomBehavior(objRoot, canvas, behaveBounds);
	PickTranslateBehavior pickTranslate = new PickTranslateBehavior(objRoot, canvas, behaveBounds);
	
	objRoot.addChild(pickRotate);
	objRoot.addChild(pickZoom);
	objRoot.addChild(pickTranslate);
	
	objRoot.compile();
	return objRoot;
    }
    
    public MousePickApp()
    {
	setLayout(new BorderLayout());
	setLayout(new BorderLayout());
	GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
	Canvas3D canvas = new Canvas3D(config);
	add("Center", canvas);
	
	SimpleUniverse universe = new SimpleUniverse(canvas);
	BranchGroup scene = createSceneGraph(canvas);
	
	universe.getViewingPlatform().setNominalViewingTransform();
	universe.addBranchGraph(scene);
    }
    public static void main(String[] args)
    {
	Frame frame = new MainFrame(new MousePickApp(), 500, 500);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ma_projekt;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;  //  Mousebehavior
import com.sun.j3d.utils.geometry.*;                   //  Box

public class HelloUniverse extends Applet
{
    public BranchGroup createSceneGraph()
    {
	BranchGroup objRoot = new BranchGroup();
	
	Transform3D zTrans = new Transform3D();
	    zTrans.set(new Vector3f(0.0f, 0.0f, -10.0f));
	    
	TransformGroup objTrans = new TransformGroup(zTrans);
	    objRoot.addChild(objTrans);
	    
	// Box erzeugen und einhängen 
	Box prim = new Box();
	    objTrans.addChild(prim);
	    
	// BoundingSpere für Mousebehavior und Lichtquelle erzeugen
	BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
	// Mouse-Rotation-Behavior ersetzen und in Transformgruppe einhängen + Capabilitybits setzen
	MouseRotate behavior = new MouseRotate(objTrans);
	    objTrans.addChild(behavior);
	    behavior.setSchedulingBounds(bounds);
	    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    
	//Lichtquelle erzeugen und in Szenengraphen hängen
	Color3f lColor1 = new Color3f(1.0f, 1.0f, 1.0f);
	Vector3f lDir1 = new Vector3f(0f, 0f, -1.0f);
	DirectionalLight lgt1 = new DirectionalLight(lColor1, lDir1);
	    lgt1.setInfluencingBounds(bounds);
	    objRoot.addChild(lgt1);
	// Content-Branch optimieren und zurückgeben
	objRoot.compile();
	return objRoot;
    }

    public HelloUniverse()
    {
	setLayout(new BorderLayout());
	GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
	Canvas3D c = new Canvas3D(config);
	add("Center", c);
	SimpleUniverse u = new SimpleUniverse(c);
	u.getViewingPlatform().setNominalViewingTransform();
	BranchGroup scene = createSceneGraph();
	u.addBranchGraph(scene);
    }

    public static void main(String[] args)
    {
	new MainFrame(new HelloUniverse(), 600, 600);
    }
}

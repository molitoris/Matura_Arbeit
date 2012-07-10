/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ma_projekt;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.applet.Applet;
import java.awt.*;
import javax.media.j3d.*;


/**
 *
 * @author Rafael Müller_2
 */
public class Test_5d extends Applet
{
    public Test_5d()
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
    
    public BranchGroup createSceneGraph()
    {
        BranchGroup objRoot = new BranchGroup();
        
        Transform3D rotate = new Transform3D();
        Transform3D tempRotate = new Transform3D();
        
        rotate.rotX(Math.PI/4.0d);
        tempRotate.rotY(Math.PI/5.0d);
        rotate.mul(tempRotate);
        
        TransformGroup objRotate = new TransformGroup(rotate);
        
        TransformGroup objSpin = new TransformGroup();
        objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        objRoot.addChild(objRotate);
        objRotate.addChild(objSpin);
        
        objSpin.addChild(new ColorCube(.4));
        
        Transform3D yAxis = new Transform3D();
        Alpha rotationAlpha = new Alpha(-1, 2000);
        
        RotationInterpolator rotator = new RotationInterpolator(rotationAlpha, objSpin, yAxis, 0.0f, (float) Math.PI*2.0f);
        
        BoundingSphere bounds = new BoundingSphere();
        rotator.setSchedulingBounds(bounds);
        objSpin.addChild(rotator);
        
        return objRoot;
    }
    
    public static void main(String[] args)
    {
        Frame frame = new MainFrame(new Test_5d(), 256, 256);
    }
}

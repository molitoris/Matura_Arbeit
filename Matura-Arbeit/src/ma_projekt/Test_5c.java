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
public class Test_5c extends Applet
{
    public Test_5c()
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
        //Erstellt neue Branch-Gruppe
        BranchGroup objRoot = new BranchGroup();
        
        //Erstellt neue Transformation-Gruppe
        TransformGroup objSpin = new TransformGroup();
        
        //Erlaubt der Transformations-Gruppe auf nach dem Rendern, sich
        //zu verändern
        objSpin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        
        //Fügt der Branch-Gruppe die Transformations-Gruppe hinzu
        objRoot.addChild(objSpin);

        objSpin.addChild(new ColorCube(0.4));


        Alpha rotationAlpha = new Alpha(-1, 4000);


        RotationInterpolator rotator =
        new RotationInterpolator(rotationAlpha, objSpin);

        BoundingSphere bounds = new BoundingSphere();
        rotator.setSchedulingBounds(bounds);
        objSpin.addChild(rotator);

        return objRoot;
   } 
    
    public static void main(String[] args)
    {
        Frame frame = new MainFrame(new Test_5c(), 256, 256);
    }
}

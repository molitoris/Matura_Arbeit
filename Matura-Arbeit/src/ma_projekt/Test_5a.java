/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ma_projekt;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import java.applet.*;
import java.awt.*;
import javax.media.j3d.*;

/**
 *
 * @author Rafael Müller_2
 */
public class Test_5a extends Applet
{
    public Test_5a()
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
    
    public BranchGroup  createSceneGraph()
    {
        BranchGroup objRoot = new BranchGroup();
        
        Transform3D rotate = new Transform3D();
        //Transform3D tempRotate = new Transform3D();
        
        //Rotation an der X-Achse
        rotate.rotX(Math.PI/4.0d);        

        
        TransformGroup objRotate = new TransformGroup(rotate);
        objRotate.addChild(new ColorCube(0.4));
        objRoot.addChild(objRotate);
        
        return objRoot;
    }
    
    public static void main(String[] args)
    {
        Frame frame = new MainFrame(new Test_5a(), 256, 256);
    }
}

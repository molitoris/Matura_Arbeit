package TestWithHasMap;

import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
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
public class RubicsCube extends JFrame
{
    public BranchGroup objRoot = new BranchGroup();
    
    RubicsCube()
    {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 500);
        
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        add(canvas);
        
        SimpleUniverse universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(createSceneGraph());
    }
    
    public BranchGroup createSceneGraph()
    {        
//        Transform3D rotate = new Transform3D();
//        rotate.rotY(-45 / ( 2* Math.PI));
        
        TransformGroup objTransform = new TransformGroup(/*rotate*/);
        
        Stone stone = new Stone(0.0f, 0.0f, 0.0f); 
        stone.moveStone( 0.5f, 0.0f, 0.0f);

        
        Shape3D shape = stone.makeShape3D();
        
        
        objTransform.addChild(shape);
        objRoot.addChild(objTransform);        
        return objRoot;
    }
}

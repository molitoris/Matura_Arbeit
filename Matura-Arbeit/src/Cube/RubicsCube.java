package Cube;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rafael Müller_2
 */
public class RubicsCube
{
    private Color3f white   = new Color3f(1.0f, 1.0f, 1.0f);
    private Color3f yellow  = new Color3f(1.0f, 1.0f, 0.0f);
    private Color3f blue    = new Color3f(0.0f, 0.0f, 1.0f);
    private Color3f green   = new Color3f(0.0f, 1.0f, 0.0f);
    private Color3f red     = new Color3f(1.0f, .0f, 0.0f);
    private Color3f orange  = new Color3f(1.0f, 0.5f, 0.0f);
    private Color3f black   = new Color3f(0.0f, 0.0f, 0.0f);
    
    public BranchGroup objRoot = new BranchGroup();
    
    Canvas3D canvas;
    Stone stone;
    Shape3D shape;
    
    RubicsCube()
    {        
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
        
        SimpleUniverse universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
        
        OrbitBehavior orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds (new BoundingSphere ());
        universe.getViewingPlatform().setViewPlatformBehavior (orbit);

        universe.addBranchGraph(createSceneGraph());        
    }    
    
    public BranchGroup createSceneGraph()
    {        
        shape = new Shape3D();
        stone = new Stone();
        
        stone.setColor("front", white);
        stone.setColor("back", yellow);
        stone.setColor("left", red);
        stone.setColor("right", orange);
        stone.setColor("down", blue);
        stone.setColor("top", green);
        
        
        shape.addGeometry(stone.getQuadArray());

        TransformGroup objTransform = new TransformGroup();
        objTransform.addChild(shape);
        objRoot.addChild(objTransform);
        return objRoot;        
        
    }
    
    public void turnCube()
    {
        Transform3D rotate = new Transform3D();
        
        rotate.rotX(10);
        TransformGroup tg = new TransformGroup(rotate);
        tg.addChild(shape);
        
        tg.removeAllChildren();
    }
    
    public Canvas3D returnCanvas()
    {
        return canvas;
    }
}

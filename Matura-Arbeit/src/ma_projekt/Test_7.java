/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ma_projekt;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Frame;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

/**
 *
 * @author Rafael Müller_2
 */
public class Test_7 extends Applet
{
    SimpleUniverse simpleU;
    
    public Test_7(){}
    
    @Override
    public void init()
    {
        setLayout(new BorderLayout());
        Canvas3D canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        
        add("Center", canvas);
        
        simpleU = new SimpleUniverse(canvas);
        
        BranchGroup scene = createSceneGraph();
        
        simpleU.getViewingPlatform().setNominalViewingTransform();
        
        scene.compile();
        simpleU.addBranchGraph(scene);
    }
    
    public BranchGroup createSceneGraph()
    {
        BranchGroup objRoot = new BranchGroup();        
        Appearance app = new Appearance();
        
        
        
        
        QuadArray front = new QuadArray(8,QuadArray.COORDINATES);
        front.setCoordinate(0, new Point3f(-0.5f, -0.5f, 0f));
        front.setCoordinate(1, new Point3f(0.5f, -0.5f, 0f));
        front.setCoordinate(2, new Point3f(0.5f, 0.5f, 0f));
        front.setCoordinate(3, new Point3f(-0.5f,0.5f, 0f));
        
        front.setCoordinate(4, new Point3f(-.5f, .5f, 0f));
        front.setCoordinate(5, new Point3f(-.5f, .5f, 1f));
        front.setCoordinate(6, new Point3f(.5f, .5f, 1f));
        front.setCoordinate(7, new Point3f(.5f, .5f, 0f));
        
        Shape3D front_face = new Shape3D(front, app);
        
        
        Transform3D rotate = new Transform3D();
        rotate.rotX(Math.PI/4.0d);
        TransformGroup objRotate = new TransformGroup(rotate);
        
        objRotate.addChild(front_face);
        
        objRoot.addChild(objRotate);
        
        return objRoot;
    }
    
    @Override
    public void destroy()
    {
        simpleU.removeAllLocales();
    }
    
    public static void main(String[] args)
    {
        Frame frame = new MainFrame(new Test_7(), 500, 500);
    }
}

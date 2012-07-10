/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ma_projekt;


import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.*;
import javax.media.j3d.*;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/**
 *
 * @author Rafael Müller_2
 */
public class Test_5a1 extends JFrame
{
    public Test_5a1(String title)
    {
        super(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(300, 300);
        
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();        
        Canvas3D canvas = new Canvas3D(config);        
        add(canvas);
        
        SimpleUniverse universe = new SimpleUniverse(canvas);        
        universe.getViewingPlatform().setNominalViewingTransform();        
        universe.addBranchGraph(createSceneGraph());
    }
    
    public BranchGroup  createSceneGraph()
    {
        BranchGroup objRoot = new BranchGroup();
        
        Color3f red     = new Color3f(1.0f, 0.0f, 0.0f);
        Color3f blue    = new Color3f(0.0f, 0.0f, 1.0f);
        Color3f green   = new Color3f(0.0f, 1.0f, 0.0f);
        
        TriangleArray tri = new TriangleArray(3, TriangleArray.COORDINATES | TriangleArray.COLOR_3);
        
        tri.setCoordinate(0, new Point3f(-0.7f, -0.2f, 0.0f));
        tri.setCoordinate(1, new Point3f(0.7f, -0.2f, 0.0f));
        tri.setCoordinate(2, new Point3f(0.0f, 0.5f, 0.0f));
        
        tri.setColor(0, blue);
        tri.setColor(1, red);
        tri.setColor(2, blue);
        
        objRoot.addChild(new Shape3D(tri));
        
        return objRoot;
    }
    
    public static void main (String[] args)
    {
        Test_5a1 a = new Test_5a1("Beispiel");
        a.setVisible(true);
    }
}

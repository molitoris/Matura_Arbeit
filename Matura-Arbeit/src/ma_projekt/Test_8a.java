/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ma_projekt;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/**
 *
 * @author Rafael Müller_2
 */
public final class Test_8a
{
    SimpleUniverse simpleU;
    Canvas3D canvas;
    
    // Coloreigenschaften
    Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
    Color3f orange = new Color3f(1.0f, 0.5f, 0.0f);
    Color3f blue = new Color3f(0.0f, 0.0f, 1.0f);
    Color3f green = new Color3f(0.0f, 1.0f, 0.0f);
    Color3f yellow = new Color3f(1.0f, 1.0f, 0.0f);
    Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
    Color3f black = new Color3f(0.0f, 0.0f, 0.0f);

    public Test_8a()
    {
	GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas = new Canvas3D(config);
	
	simpleU = new SimpleUniverse(canvas);
        simpleU.addBranchGraph(createSceneGraph());
	simpleU.getViewingPlatform().setNominalViewingTransform();
	
	JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	frame.setVisible(true);
	frame.add(canvas);
	
        
        
    }
    
    public BranchGroup createSceneGraph()
    {
        BranchGroup objRoot = new BranchGroup();
        
                Transform3D rotate_1 = new Transform3D();
                Transform3D rotate_2 = new Transform3D();
                rotate_1.rotX(Math.PI/4.0d);
                rotate_2.rotY(Math.PI/5.0d);
                rotate_1.mul(rotate_2);
                rotate_1.setScale(0.25d);        
                TransformGroup objRotate = new TransformGroup(rotate_1);
                
                
        Shape3D shape = new Shape3D(makeCube(-1.0f, 1.0f, -1.0f, white, yellow, blue, green, red, orange), makeApp());
        Shape3D shape_2 = new Shape3D(makeCube(1.0f, 1.0f, -1.0f, white, yellow, black, green, red, orange), makeApp());
        Shape3D line = new Shape3D(makeLine(), makeApp());
        objRotate.addChild(shape);
        objRotate.addChild(shape_2);
        objRotate.addChild(line);
        
        objRoot.addChild(objRotate);
        return objRoot;
    }
    
    public QuadArray makeCube(float x, float y, float z, Color3f font, Color3f back, Color3f rigth, Color3f left, Color3f down, Color3f top)
    {
        QuadArray plane = new QuadArray(24, GeometryArray.COORDINATES|GeometryArray.COLOR_3);
        
        float k = 1.9f;
        
        // Punkte und Farben
        Point3f pa = new Point3f(x, y, z);
        Point3f pb = new Point3f(x, y - k, z);
        Point3f pc = new Point3f(x + k, y - k, z);
        Point3f pd = new Point3f(x + k, y, z);
        Point3f pe = new Point3f(x, y, z + k);
        Point3f pf = new Point3f(x, y - k, z + k);
        Point3f pg = new Point3f(x + k, y - k, z + k);
        Point3f ph = new Point3f(x + k, y, z + k);
        
        
        
                //Front
            plane.setCoordinate(0, pa);
            plane.setColor(0, font);
            plane.setCoordinate(1, pb);
            plane.setColor(1, font);
            plane.setCoordinate(2, pc);
            plane.setColor(2, font);
            plane.setCoordinate(3, pd);
            plane.setColor(3, font);
                //Back
            plane.setCoordinate(4, pe);
            plane.setColor(4, back);
            plane.setCoordinate(5, pf);
            plane.setColor(5, back);
            plane.setCoordinate(6, pg);
            plane.setColor(6, back);
            plane.setCoordinate(7, ph);
            plane.setColor(7, back);
                //Right
            plane.setCoordinate(8, pe);
            plane.setColor(8, rigth);
            plane.setCoordinate(9, pa);
            plane.setColor(9, rigth);
            plane.setCoordinate(10, pb);
            plane.setColor(10, rigth);
            plane.setCoordinate(11, pf);
            plane.setColor(11, rigth);
                //left
            plane.setCoordinate(12, pd);
            plane.setColor(12, left);
            plane.setCoordinate(13, pc);
            plane.setColor(13, left);
            plane.setCoordinate(14, pg);
            plane.setColor(14, left);
            plane.setCoordinate(15, ph);
            plane.setColor(15, left);
                //Down
            plane.setCoordinate(16, pc);
            plane.setColor(16, down);
            plane.setCoordinate(17, pb);
            plane.setColor(17, down);
            plane.setCoordinate(18, pf);
            plane.setColor(18, down);
            plane.setCoordinate(19, pg);
            plane.setColor(19, down);
                //Top
            plane.setCoordinate(20, pd);
            plane.setColor(20, top);
            plane.setCoordinate(21, pa);
            plane.setColor(21, top);
            plane.setCoordinate(22, pe);
            plane.setColor(22, top);
            plane.setCoordinate(23, ph);
            plane.setColor(23, top);
            
            // ubergib den Wurfel
            return plane;
    }
    
    public LineArray makeLine()
    {
        LineArray kordinate = new LineArray(6, GeometryArray.COORDINATES| GeometryArray.COLOR_3);
       
        kordinate.setCoordinate(0, new Point3f(0, 0, 0));
        kordinate.setCoordinate(1, new Point3f(100, 0, 0));
        kordinate.setCoordinate(2, new Point3f(0, 0, 0));
        kordinate.setCoordinate(3, new Point3f(0, 100, 0));
        kordinate.setCoordinate(4, new Point3f(0, 0, 0));
        kordinate.setCoordinate(5, new Point3f(0, 0, 100));
        
        Color3f red = new Color3f(1.0f, 0.0f, 0.0f);
        Color3f blue = new Color3f(0.0f, 0.0f, 1.0f);
        Color3f green = new Color3f(0.0f, 1.0f, 0.0f);
        
        kordinate.setColor(0, red);
        kordinate.setColor(1, red);
        kordinate.setColor(2, blue);
        kordinate.setColor(3, blue);
        kordinate.setColor(4, green);
        kordinate.setColor(5, green);
                
        return kordinate;
    }

    private Appearance makeApp()
    {
        Appearance app = new Appearance();        
        return app;
    }
    
    public void destroy()
    {
        simpleU.removeAllLocales();
    }
    
    public static void main(String[] args)
    {
        Test_8a a = new Test_8a();
    }

    
    
    
}

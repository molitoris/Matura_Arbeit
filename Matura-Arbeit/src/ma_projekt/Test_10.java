/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ma_projekt;

import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/**
 *
 * @author Rafael Müller_2
 */
public class Test_10 extends JFrame
{
    public Test_10(String title)
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
    
    public BranchGroup createSceneGraph()
    {
        BranchGroup objRoot = new BranchGroup();
        
        Color3f blue = new Color3f(0.f, 0.0f, 1.0f);
        
        QuadArray stone = new QuadArray(24, GeometryArray.COORDINATES);
        TransformGroup objTransform = new TransformGroup();
        
        Point3f[][][] points = new Point3f[4][4][4];
        
        for(int k = 0; k < 4; k++)
            for(int j = 0; j < 4; j++)
                for(int i = 0; i < 4; i++)
                {
                    points[i][j][k] = new Point3f(-1.5f+i, -1.5f + j, 1.5f-k);
                }
        
        stone.setCoordinate(0, points[0][0][0]);
        stone.setColor(0, blue);
        stone.setCoordinate(1, points[1][0][0]);
        stone.setColor(1, blue);
        stone.setCoordinate(2, points[2][0][0]);
        stone.setColor(2, blue);
        stone.setCoordinate(3, points[3][0][0]);
        stone.setColor(4, blue);
        
        Shape3D shape = new Shape3D();
        shape.addGeometry(stone);
        objTransform.addChild(shape);
        objRoot.addChild(objTransform);
        
        return objRoot;
    }
    
    public static void main(String[] args)
    {
        Test_10 a= new Test_10("Beispiel");
        a.setVisible(true);
    }
}

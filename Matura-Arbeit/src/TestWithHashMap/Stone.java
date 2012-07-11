/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TestWithHashMap;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/**
 *
 * @author Rafael Müller_2
 */
public class Stone
{
    
    private Shape3D shape = new Shape3D();
    private QuadArray stone = new QuadArray(24, GeometryArray.COORDINATES|GeometryArray.COLOR_3);
    
    private Color3f white   = new Color3f(1.0f, 1.0f, 1.0f);
    private Color3f yellow  = new Color3f(1.0f, 1.0f, 0.0f);
    private Color3f blue    = new Color3f(0.0f, 0.0f, 1.0f);
    private Color3f green   = new Color3f(0.0f, 1.0f, 0.0f);
    private Color3f red     = new Color3f(1.0f, .0f, 0.0f);
    private Color3f orange  = new Color3f(1.0f, 0.5f, 0.0f);
    
    
    
    public Stone(float x, float y, float z, Color3f front, Color3f back,
                 Color3f left, Color3f right, Color3f top, Color3f down)
    {       
        Point3f pa = new Point3f(x-0.5f,y-0.5f,z+0.5f);
        Point3f pb = new Point3f(x+0.5f,y-0.5f,z+0.5f);
        Point3f pc = new Point3f(x+0.5f,y+0.5f,z+0.5f);
        Point3f pd = new Point3f(x-0.5f,y+0.5f,z+0.5f);

        Point3f pe = new Point3f(x-0.5f,y-0.5f,z-0.5f);
        Point3f pf = new Point3f(x+0.5f,y-0.5f,z-0.5f);
        Point3f pg = new Point3f(x+0.5f,y+0.5f,z-0.5f);
        Point3f ph = new Point3f(x-0.5f,y+0.5f,z-0.5f);
        
        //front
        stone.setCoordinate(0, pa);
        stone.setColor(0, white);
        stone.setCoordinate(1, pb);
        stone.setColor(1, white);
        stone.setCoordinate(2, pc);
        stone.setColor(2, white);
        stone.setCoordinate(3, pd);
        stone.setColor(3, white);
        
        //back
        stone.setCoordinate(4, pe);
        stone.setColor(4, yellow);
        stone.setCoordinate(5, pf);
        stone.setColor(5, yellow);
        stone.setCoordinate(6, pg);
        stone.setColor(6, yellow);
        stone.setCoordinate(7, ph);
        stone.setColor(7, yellow);
        
        //left
        stone.setCoordinate(8, pa);
        stone.setColor(8, orange);
        stone.setCoordinate(9, pe);
        stone.setColor(9, orange);
        stone.setCoordinate(10, ph);
        stone.setColor(10, orange);
        stone.setCoordinate(11, pd);
        stone.setColor(11, orange);
        
        //right
        stone.setCoordinate(12, pb);
        stone.setColor(12, red);
        stone.setCoordinate(13, pf);
        stone.setColor(13, red);
        stone.setCoordinate(14, pg);
        stone.setColor(14, red);
        stone.setCoordinate(15, pc);
        stone.setColor(15, red);
        
        //down
        stone.setCoordinate(16, pa);
        stone.setColor(16, green);
        stone.setCoordinate(17, pb);
        stone.setColor(17, green);
        stone.setCoordinate(18, pf);
        stone.setColor(18, green);
        stone.setCoordinate(19, pe);
        stone.setColor(19, green);
        
        //top
        stone.setCoordinate(20, pd);
        stone.setColor(20, blue);
        stone.setCoordinate(21, ph);
        stone.setColor(21, blue);
        stone.setCoordinate(22, pg);
        stone.setColor(22, blue);
        stone.setCoordinate(23, pc);
        stone.setColor(23, blue);
        
        shape.addGeometry(stone);    
    }
    
    public TransformGroup makeTransformGroup()
    {
        TransformGroup objCube = new TransformGroup();
        objCube.addChild(shape);
        return objCube;
    }
    
}
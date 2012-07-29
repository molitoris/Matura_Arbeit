/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Cube;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 *
 * @author Rafael Müller_2
 */
public class Stone
{
    private QuadArray stone = new QuadArray(24, GeometryArray.COORDINATES|GeometryArray.COLOR_3);
    
    public Stone()
    {
        Point3f pa = new Point3f(-0.5f,-0.5f,-0.5f);
        Point3f pb = new Point3f( 0.5f,-0.5f,-0.5f);
        Point3f pc = new Point3f( 0.5f, 0.5f,-0.5f);
        Point3f pd = new Point3f(-0.5f, 0.5f,-0.5f);

        Point3f pe = new Point3f(-0.5f,-0.5f, 0.5f);
        Point3f pf = new Point3f( 0.5f,-0.5f, 0.5f);
        Point3f pg = new Point3f( 0.5f, 0.5f, 0.5f);
        Point3f ph = new Point3f(-0.5f, 0.5f, 0.5f);        
        
        //front
        stone.setCoordinate(0, pa);
        stone.setCoordinate(1, pb);
        stone.setCoordinate(2, pc);
        stone.setCoordinate(3, pd);
        
        //back
        stone.setCoordinate(4, pe);
        stone.setCoordinate(5, pf);
        stone.setCoordinate(6, pg);
        stone.setCoordinate(7, ph);
        
        //left
        stone.setCoordinate(8, pa);
        stone.setCoordinate(9, pe);
        stone.setCoordinate(10, ph);
        stone.setCoordinate(11, pd);
        
        //right
        stone.setCoordinate(12, pb);
        stone.setCoordinate(13, pf);
        stone.setCoordinate(14, pg);
        stone.setCoordinate(15, pc);
        
        //down
        stone.setCoordinate(16, pa);
        stone.setCoordinate(17, pb);
        stone.setCoordinate(18, pf);
        stone.setCoordinate(19, pe);
        
        //top
        stone.setCoordinate(20, pd);
        stone.setCoordinate(21, ph);
        stone.setCoordinate(22, pg);
        stone.setCoordinate(23, pc);    
    }
    
    public void setPos(float x, float y, float z)
    {
        Transform3D transform = new Transform3D();
        Vector3f vector = new Vector3f(x, y, z);
        
        transform.setTranslation(vector);
        TransformGroup tg = new TransformGroup(transform);
        
        Shape3D shape = new Shape3D();
        
        shape.addGeometry(stone);
        tg.addChild(shape);
        
        shape.removeAllGeometries();
    }
    
    public QuadArray getQuadArray()
    {
        return stone;
    }
    
    public void setColor(String side, Color3f color)
    {
        if("front".equals(side))
            for(int i = 0; i < 4; i++)
                stone.setColor(i, color);
        
        if("back".equals(side))
            for(int i = 4; i < 8; i++)
                stone.setColor(i, color);
        
        if("left".equals(side))
            for(int i = 8; i < 12; i++)
                stone.setColor(i, color);
        
        if("right".equals(side))
            for(int i = 12; i < 16; i++)
                stone.setColor(i, color);
        
        if("down".equals(side))
            for(int i = 16; i < 10; i++)
                stone.setColor(i, color);
        
        if("top".equals(side))
            for(int i = 20; i < 24; i++)
                stone.setColor(i, color);        
    }    
}

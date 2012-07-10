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
import javax.vecmath.Point3f;

/**
 *
 * @author Rafael Müller_2
 */
public class Stone
{
    Shape3D shape = new Shape3D();
    QuadArray stone = new QuadArray(24, GeometryArray.COORDINATES);
    
    public Stone() {}
    
    public Shape3D createStone(float x, float y, float z)
    {
        Point3f pa = new Point3f(-0.5f,-0.5f, 0.5f);
        Point3f pb = new Point3f( 0.5f,-0.5f, 0.5f);
        Point3f pc = new Point3f( 0.5f, 0.5f, 0.5f);
        Point3f pd = new Point3f(-0.5f, 0.5f, 0.5f);
        
        Point3f pe = new Point3f(-0.5f,-0.5f,-0.5f);
        Point3f pf = new Point3f( 0.5f,-0.5f,-0.5f);
        Point3f pg = new Point3f( 0.5f, 0.5f,-0.5f);
        Point3f ph = new Point3f(-0.5f, 0.5f,-0.5f);
        
        
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
        stone.setCoordinate(9, pd);
        stone.setCoordinate(10, ph);
        stone.setCoordinate(11, pe);
        
        //right
        stone.setCoordinate(12, pb);
        stone.setCoordinate(13, pc);
        stone.setCoordinate(14, pg);
        stone.setCoordinate(15, pf);
        
        //down
        stone.setCoordinate(12, pa);
        stone.setCoordinate(13, pb);
        stone.setCoordinate(14, pf);
        stone.setCoordinate(15, pe);
        
        //top
        stone.setCoordinate(12, pd);
        stone.setCoordinate(13, pc);
        stone.setCoordinate(14, pg);
        stone.setCoordinate(15, ph);
        
        shape.addGeometry(stone);
        return shape;
    }
    
    public TransformGroup rotateStone(Shape3D stone, float x, float y, float z)
    {
        /*TODO:
         * Hier müsse ich den Stein um die eigene Achse drehen
         */
        
        
        Transform3D rotate_x = new Transform3D();
        Transform3D rotate_y = new Transform3D();
        Transform3D rotate_z = new Transform3D();
        
        rotate_x.rotX(x / (2 * Math.PI));
        rotate_y.rotX(y / (2 * Math.PI));
        rotate_z.rotX(z / (2 * Math.PI));
        rotate_x.mul(rotate_y, rotate_z);
        
        TransformGroup objRotate = new TransformGroup(rotate_x);
        
        objRotate.addChild(stone);
        
        return objRotate;
    }
    
    public TransformGroup moveStone(Shape3D stone, float x, float y, float z)
    {
        /* TODO:
         * Hier müsse ich die x-, y-, z-Koordinate auslesen und die oberen Paramerer
         * dazu addieren.
         */        
        return null;
    }
}

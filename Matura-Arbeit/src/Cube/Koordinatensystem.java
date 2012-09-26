package Cube;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/**
 *
 * @author Rafael Müller_2
 */
public class Koordinatensystem
{
    BranchGroup objLine = new BranchGroup();
    
    public Koordinatensystem()
    {
        LineArray la = new LineArray(6, GeometryArray.COORDINATES|GeometryArray.COLOR_3|LineAttributes.PATTERN_DASH);
        
        Point3f p0 = new Point3f( 0.0f,  0.0f,  0.0f);
        Point3f px = new Point3f(10.0f,  0.0f,  0.0f);
        Point3f py = new Point3f( 0.0f, 10.0f,  0.0f);
        Point3f pz = new Point3f( 0.0f,  0.0f, 10.0f);
        
        //x-Achse
        la.setCoordinate(0, p0);
        la.setCoordinate(1, px);
        
        //y-Achse
        la.setCoordinate(2, p0);
        la.setCoordinate(3, py);
        
        //z-Achse
        la.setCoordinate(4, p0);
        la.setCoordinate(5, pz);
        
        
        Color3f yellow  = new Color3f(1.0f, 1.0f, 0.0f);
        Color3f green   = new Color3f(0.0f, 1.0f, 0.0f);
        Color3f orange  = new Color3f(1.0f, 0.5f, 0.0f);
        
        la.setColor(0, orange);
        la.setColor(1, orange);
        
        la.setColor(2, green);
        la.setColor(3, green);
        
        la.setColor(4, yellow);
        la.setColor(5, yellow);
	
	Shape3D shape = new Shape3D();
	shape.addGeometry(la);
	objLine.addChild(shape);
    }
    
    public BranchGroup getBranchGroup()
    {
        return objLine;
    }
}
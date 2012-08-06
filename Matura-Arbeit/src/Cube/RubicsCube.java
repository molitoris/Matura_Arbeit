package Cube;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.*;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Vector3f;

/**
 *
 * @author Rafael Sebastian Müller
 */
public class RubicsCube
{
    //Attribute
    Canvas3D canvas;
    SimpleUniverse universe;
    Shape3D line;
    Stone[][][] stone;
    int angel   = 90;
    int angel2 = 90;
    int angel3 = 90;
    
    TransformGroup[][][] stoneTransform;
    
    /*
     *			    objRoot(BG)
     *				|
     *	    		  objTransform(TG)----------------------+
     *			    /	    \				|
     *	     stoneTransform(TG)    stoneTransform(TG)		|
     *		    |			    |			|
     *		shape(S3D)		shape(S3D)	     shape(S3D)
     *		    |			    |			|
     *		stone(Stone)		stone(Stone)		|
     *		    |			    |			|
     *		 QuadArray		QuadArray	    LineArray
     */


    //Konstruktor
    RubicsCube()
    {
	GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
	canvas = new Canvas3D(config);
	
	universe = new SimpleUniverse(canvas);
	
	canvas.getView().setFieldOfView(0.09);
	canvas.getView().setWindowEyepointPolicy(View.RELATIVE_TO_WINDOW);
	universe.getViewingPlatform().setNominalViewingTransform();
	universe.addBranchGraph(createSceneGraph());
    }

    //Methoden
    private BranchGroup createSceneGraph()
    {
	BranchGroup objRoot = new BranchGroup();	    
	TransformGroup objTransform = new TransformGroup(setStart());
	
	//Mouse-Rotation
	MouseRotate rotateMouse = new MouseRotate();
	objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	rotateMouse.setTransformGroup(objTransform);
	rotateMouse.setSchedulingBounds(new BoundingSphere());
	objRoot.addChild(rotateMouse);
	    
	//Steine erstellen    
	stone = new Stone[3][3][3];
	stoneTransform = new TransformGroup[3][3][3];		
	for(int z = 0; z < stoneTransform.length; z++)
	    for(int y = 0; y < stoneTransform.length; y++)
		for(int x = 0; x < stoneTransform.length; x++)
		{
		    stone[x][y][z]= new Stone(x-1, y-1, z-1);
		    stoneTransform[x][y][z] = new TransformGroup();
		    stoneTransform[x][y][z].setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		    stoneTransform[x][y][z].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		    stoneTransform[x][y][z].addChild(stone[x][y][z].getTransformGroup());
		    objTransform.addChild(stoneTransform[x][y][z]);		    
		}
	
	//drei Koordinatenlinien erstellen
	Koordinatensystem ka = new Koordinatensystem();
	objTransform.addChild(ka.getBranchGroup());
	
	objRoot.addChild(objTransform);
	objRoot.compile();
	return objRoot;
    }
    
    private Transform3D setStart()
    {
	Transform3D rotate = new Transform3D();
        Transform3D tempRotate = new Transform3D();

	    rotate.rotX(Math.PI/4.0d);
	    tempRotate.rotY(-Math.PI/5.0d);
	    rotate.mul(tempRotate);
	
	return rotate;
    }
    
    public Canvas3D returnCanvas()
    {
	return canvas;
    }
    
    void test()
    {	
	Transform3D rotate = new Transform3D();
	rotate.rotY(Math.toRadians(angel));
	for(int x = 0; x < stoneTransform.length; x++)
	    for(int z = 0; z < stoneTransform.length; z++)
	    {
		stoneTransform[x][2][z].setTransform(rotate);
	    }
	angel = angel + 90;
    }

    void test2()
    {
	Transform3D rotate = new Transform3D();
	rotate.rotZ(Math.toRadians(angel2));
	for(int x = 0; x < stoneTransform.length; x++)
	    for(int y = 0; y < stoneTransform.length; y++)
	    {
		stoneTransform[x][y][2].setTransform(rotate);
	    }
	angel2 = angel2 + 90;
    }

    void test3()
    {
	Transform3D rotate = new Transform3D();
	rotate.rotX(Math.toRadians(angel3));
	for(int y = 0; y < stoneTransform.length; y++)
	    for(int z = 0; z < stoneTransform.length; z++)
	    {
		stoneTransform[2][y][z].setTransform(rotate);
	    }
	angel3 = angel3 + 90;
    }
}
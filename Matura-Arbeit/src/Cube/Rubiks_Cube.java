package Cube;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

/**
 *
 * @author Rafael Sebastian Müller
 */
public class Rubiks_Cube{
    //attributes
    Canvas3D canvas;
    SimpleUniverse universe;
    Shape3D line;
    Stone[][][] stone;
    int angel   = 90;
    int angel2 = 90;
    int angel3 = 90;
    
    TransformGroup objTransform;
    BranchGroup[][][] branchStone;
    
    /* **** Structure of the Scene Graph ****
     * 
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


    //Constructor
    Rubiks_Cube(){
	GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
	canvas = new Canvas3D(config);
	
	BranchGroup scene = createSceneGraph();
	scene.compile();
	
	universe = new SimpleUniverse(canvas);
	
	canvas.getView().setFieldOfView(0.09);
	canvas.getView().setWindowEyepointPolicy(View.RELATIVE_TO_WINDOW);
	universe.getViewingPlatform().setNominalViewingTransform();
	universe.addBranchGraph(scene);
    }

    //methods
    
    
    private BranchGroup createSceneGraph(){
	BranchGroup objRoot = new BranchGroup();
	objTransform = new TransformGroup(setStart());
	objTransform.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
	objTransform.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		
	//cenerate roation Behavoir
	MouseRotate rotateMouse = createMouseRotation();
	objRoot.addChild(rotateMouse);
	
	
	    
	//construct stones    
	stone = new Stone[3][3][3];
	branchStone = new BranchGroup[3][3][3];
	for(int z = 0; z < stone.length; z++)
	    for(int y = 0; y < stone.length; y++)
		for(int x = 0; x < stone.length; x++)
		{
		    stone[x][y][z]= new Stone(x-1, y-1, z-1);
		    branchStone[x][y][z] = new BranchGroup();
		    branchStone[x][y][z].setCapability(BranchGroup.ALLOW_DETACH);
		    
		    branchStone[x][y][z].addChild(stone[x][y][z].getTransformGroup());
		    objTransform.addChild(branchStone[x][y][z]);
		}
	
	//construct coordinate system
	Shape3D cordSystem = createCoordinateSystem();
	objTransform.addChild(cordSystem);
		
	objRoot.addChild(objTransform);
	objRoot.compile();
	return objRoot;
    }
    
    private Transform3D setStart(){
	Transform3D rotate = new Transform3D();
        Transform3D tempRotate = new Transform3D();

	    rotate.rotX(Math.PI/4.0d);
	    tempRotate.rotY(-Math.PI/5.0d);
	    rotate.mul(tempRotate);
	
	return rotate;
    }
    
    public Canvas3D returnCanvas(){
	return canvas;
    }
    
    private MouseRotate createMouseRotation(){
	MouseRotate rotate = new MouseRotate();
	
	objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	rotate.setTransformGroup(objTransform);
	rotate.setSchedulingBounds(new BoundingSphere());
	
	return rotate;
    }

    private Shape3D createCoordinateSystem(){	
	LineArray lineArray = new LineArray(6, GeometryArray.COORDINATES|GeometryArray.COLOR_3|LineAttributes.PATTERN_DASH);
        
        Point3f p0 = new Point3f( 0.0f,  0.0f,  0.0f);
        Point3f px = new Point3f(10.0f,  0.0f,  0.0f);
        Point3f py = new Point3f( 0.0f, 10.0f,  0.0f);
        Point3f pz = new Point3f( 0.0f,  0.0f, 10.0f);
        
        //x-Achse
        lineArray.setCoordinate(0, p0);
        lineArray.setCoordinate(1, px);        
        //y-Achse
        lineArray.setCoordinate(2, p0);
        lineArray.setCoordinate(3, py);        
        //z-Achse
        lineArray.setCoordinate(4, p0);
        lineArray.setCoordinate(5, pz);
                
        Color3f yellow  = new Color3f(1.0f, 1.0f, 0.0f);
        Color3f green   = new Color3f(0.0f, 1.0f, 0.0f);
        Color3f orange  = new Color3f(1.0f, 0.5f, 0.0f);
        
        lineArray.setColor(0, orange);
        lineArray.setColor(1, orange);        
        lineArray.setColor(2, green);
        lineArray.setColor(3, green);        
        lineArray.setColor(4, yellow);
        lineArray.setColor(5, yellow);
	
	Shape3D shape = new Shape3D(lineArray);	
	return shape;
    }
    
    void rotateBlueFace()
    {		
	Transform3D rotate = new Transform3D();
	rotate.rotY(Math.toRadians(angel));
	
	BranchGroup branch = new BranchGroup();
	branch.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
	
	TransformGroup rotateBlue = new TransformGroup();
	rotateBlue.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
	rotateBlue.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
	
	branch.addChild(rotateBlue);
	
	for(int x = 0; x < branchStone.length; x++)
	    for(int z = 0; z < branchStone.length; z++)
	    {
		objTransform.removeChild(branchStone[x][2][z]);
		rotateBlue.addChild(branchStone[x][2][z]);
	    }
	
	rotateBlue.setTransform(rotate);
	objTransform.addChild(branch);
	
	
	
	angel = angel + 90;
	
    }

    void rotateWhiteFace()
    {}

    void rotateOrangeFace()
    {}
   
    public void rotateTop()
    {
//	TransformGroup temp_corner = stoneTransform[0][2][2];
//	
//	stoneTransform[0][2][2] = stoneTransform[0][2][0];
//	stoneTransform[0][2][0] = stoneTransform[2][2][0];
//	
//	stoneTransform[2][2][0] = stoneTransform[2][2][2];
//	stoneTransform[2][2][2] = temp_corner;
//	
//	
//	TransformGroup temp_edge = stoneTransform[1][2][2];
//	
//	stoneTransform[1][2][2] = stoneTransform[0][2][1];
//	stoneTransform[0][2][1] = stoneTransform[1][2][0];
//	stoneTransform[1][2][0] = stoneTransform[2][2][1];
//	stoneTransform[2][2][1] = temp_edge;
    }    
}
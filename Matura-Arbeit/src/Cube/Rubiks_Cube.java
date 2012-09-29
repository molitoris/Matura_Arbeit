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
 * 
 * ***Information***
 * In dieser Klasse werden die Stein-Objekte zum Würfel zusammengesetzt. Sie werden
 * in ein Array der Transform Group gesetzt. Bei der Drehung werden die Transform
 * Group ausgewählt und gedreht. Danach wird mit der Methode changeArrayPosition
 * die Transform Group so angeordnet, dass sie bei der nächsten Rotation wieder richtig
 * ausgewählt werden.
 * 
 */
public class Rubiks_Cube{
    //attributes
    Canvas3D canvas;
    SimpleUniverse universe;
    Shape3D line;
    int angel   = 90;
    int angel2 = 90;
    int angel3 = 90;
    BranchGroup scene = new BranchGroup();
    TransformGroup objTransform;
    TransformGroup[][][] stoneTransform;
    Transform3D[][][] rotate;
    
    /* **** Structure of the Scene Graph ****
     * 
     *			    scene(BG)
     *				|
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
	canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
	
	scene = createSceneGraph();	
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
	stoneTransform	= new TransformGroup[3][3][3];
	rotate		= new Transform3D[3][3][3];	
	for(int z = 0; z < stoneTransform.length; z++)
	    for(int y = 0; y < stoneTransform.length; y++)
		for(int x = 0; x < stoneTransform.length; x++)
		{
		    Stone stone = new Stone(x-1, y-1, z-1);
		    stoneTransform[x][y][z] = new TransformGroup();
		    rotate[x][y][z] = new Transform3D();
		    
		    stoneTransform[x][y][z].addChild(stone.getTransformGroup());
		    objTransform.addChild(stoneTransform[x][y][z]);
		    
		    stoneTransform[x][y][z].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);		    
		}
	
	//construct coordinate system
	Shape3D cordSystem = createCoordinateSystem();
	objTransform.addChild(cordSystem);
		
	objRoot.addChild(objTransform);
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
	for(int x = 0; x < stoneTransform.length; x++)
	    for(int z = 0; z < stoneTransform.length; z++)
	    {
		Transform3D temp_rot = new Transform3D();
		temp_rot.rotY(Math.toRadians(90));
		rotate[x][2][z].mul(temp_rot);		
		stoneTransform[x][2][z].setTransform(rotate[x][2][z]);
	    }
	changeArrayPosition(1);
    }

    void rotateWhiteFace()
    {
	for(int x = 0; x < stoneTransform.length; x++)
	    for(int y = 0; y < stoneTransform.length; y++)
	    {
		Transform3D temp_rot = new Transform3D();
		temp_rot.rotZ(Math.toRadians(90));
		rotate[x][y][2].mul(temp_rot);		
		stoneTransform[x][y][2].setTransform(rotate[x][y][2]);
	    }
    }

    void rotateOrangeFace(){}

    private void changeArrayPosition(int side)
    {
	switch (side)
	{
	    case 1:
		TransformGroup temp_corner = stoneTransform[0][2][2];	
		stoneTransform[0][2][2] = stoneTransform[0][2][0];
		stoneTransform[0][2][0] = stoneTransform[2][2][0];
		stoneTransform[2][2][0] = stoneTransform[2][2][2];
		stoneTransform[2][2][2] = temp_corner;
		
		Transform3D temp_rot_corner = rotate[0][2][2];	
		rotate[0][2][2] = rotate[0][2][0];
		rotate[0][2][0] = rotate[2][2][0];
		rotate[2][2][0] = rotate[2][2][2];
		rotate[2][2][2] = temp_rot_corner;


		TransformGroup temp_edge = stoneTransform[1][2][2];

		stoneTransform[1][2][2] = stoneTransform[0][2][1];
		stoneTransform[0][2][1] = stoneTransform[1][2][0];
		stoneTransform[1][2][0] = stoneTransform[2][2][1];
		stoneTransform[2][2][1] = temp_edge;
		
		Transform3D temp_rot_edge = rotate[1][2][2];

		rotate[1][2][2] = rotate[0][2][1];
		rotate[0][2][1] = rotate[1][2][0];
		rotate[1][2][0] = rotate[2][2][1];
		rotate[2][2][1] = temp_rot_edge;
		break;
	}
	    
    }
}
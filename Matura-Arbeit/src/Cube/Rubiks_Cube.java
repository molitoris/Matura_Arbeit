package Cube;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
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
public class Rubiks_Cube {
//Canvas verhält sich ähnlich, wie ein Spiegel, der die dreidimensionale Welt auf eine zweidimensionale Oberfläche proiziert. (http://fbim.fh-regensburg.de/~saj39122/fractal/docs/fractal/view/Canvas3D.html)
    public Canvas3D canvas;
//SimpleUniverse generiert automatisch den ViewBranchGraph. Dadurch muss man sich nicht um die Kameraeinstellungen kümmern.
    TransformGroup objTransform;
    SimpleUniverse universe;
    TransformGroup[][][] stoneTransform = new TransformGroup[3][3][3];
    Transform3D[][][] rotate = new Transform3D[3][3][3];
    Stone[][][] stone = new Stone[3][3][3];
    BranchGroup objRoot;

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
    Rubiks_Cube() {
	universe = createViewBranchGraph();
	universe.addBranchGraph(createContentBranchGraph());
    }
    
    void reset(){
	objRoot.detach();
	universe.addBranchGraph(createContentBranchGraph());
    }

    private SimpleUniverse createViewBranchGraph() {
	canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
	SimpleUniverse simpleUniverse = new SimpleUniverse(canvas);

	canvas.getView().setFieldOfView(0.15);
	canvas.getView().setWindowEyepointPolicy(View.RELATIVE_TO_WINDOW);
	simpleUniverse.getViewingPlatform().setNominalViewingTransform();

	return simpleUniverse;
    }

//methods    
    private BranchGroup createContentBranchGraph() {
	objRoot = new BranchGroup();
	objRoot.setCapability(BranchGroup.ALLOW_DETACH);

	objTransform = new TransformGroup(setStart());
	objTransform.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
	objTransform.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

//cenerate roation Behavior
	MouseRotate rotateMouse = createMouseRotation();
	objRoot.addChild(rotateMouse);

//construct stones		
	for (int x = 0; x < stone.length; x++)
	    for (int y = 0; y < stone.length; y++)
		for (int z = 0; z < stone.length; z++) {
		    stone[x][y][z] = new Stone(x, y, z);
		    stone[x][y][z].setLocalCord(1, 5);
		    stoneTransform[x][y][z] = new TransformGroup();
		    rotate[x][y][z] = new Transform3D();
		    stoneTransform[x][y][z].addChild(stone[x][y][z].getTransformGroup());
		    objTransform.addChild(stoneTransform[x][y][z]);
		    stoneTransform[x][y][z].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		}

//construct coordinate system
//	Shape3D cordSystem = createCoordSystem();
//	objTransform.addChild(cordSystem);

	objRoot.addChild(objTransform);
	return objRoot;
    }

    private Transform3D setStart() {
	Transform3D rotate = new Transform3D();
	Transform3D tempRotate = new Transform3D();

	rotate.rotX(Math.PI / 4.0d);
	tempRotate.rotY(-Math.PI / 5.0d);
	rotate.mul(tempRotate);

	return rotate;
    }

    public Canvas3D returnCanvas() {
	return canvas;
    }

    private MouseRotate createMouseRotation() {
	MouseRotate rotate = new MouseRotate();
	objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	rotate.setTransformGroup(objTransform);
	rotate.setSchedulingBounds(new BoundingSphere());

	return rotate;
    }

    private Shape3D createCoordSystem() {
	LineArray lineArray = new LineArray(6, GeometryArray.COORDINATES | GeometryArray.COLOR_3 | LineAttributes.PATTERN_DASH);

	Point3f p0 = new Point3f(0.0f, 0.0f, 0.0f);
	Point3f px = new Point3f(10.0f, 0.0f, 0.0f);
	Point3f py = new Point3f(0.0f, 10.0f, 0.0f);
	Point3f pz = new Point3f(0.0f, 0.0f, 10.0f);

	//x-Achse
	lineArray.setCoordinate(0, p0);
	lineArray.setCoordinate(1, px);
	//y-Achse
	lineArray.setCoordinate(2, p0);
	lineArray.setCoordinate(3, py);
	//z-Achse
	lineArray.setCoordinate(4, p0);
	lineArray.setCoordinate(5, pz);

	Color3f yellow = new Color3f(1.0f, 1.0f, 0.0f);
	Color3f green = new Color3f(0.0f, 1.0f, 0.0f);
	Color3f orange = new Color3f(1.0f, 0.5f, 0.0f);

	lineArray.setColor(0, orange);
	lineArray.setColor(1, orange);
	lineArray.setColor(2, green);
	lineArray.setColor(3, green);
	lineArray.setColor(4, yellow);
	lineArray.setColor(5, yellow);

	Shape3D shape = new Shape3D(lineArray);
	return shape;
    }

    void rotateWhiteFace() {
	for (int x = 0; x < stoneTransform.length; x++)
	    for (int y = 0; y < stoneTransform.length; y++) {
		Transform3D temp_rot = new Transform3D();
		temp_rot = stone[x][y][2].getTransform("z-Achse");
		rotate[x][y][2].mul(temp_rot);
		stoneTransform[x][y][2].setTransform(rotate[x][y][2]);
	    }
	changeArrayPosition(4);
    }

    void rotateYelllowFace() {
	for (int x = 0; x < stoneTransform.length; x++)
	    for (int y = 0; y < stoneTransform.length; y++) {
		Transform3D temp_rot = new Transform3D();
		temp_rot = stone[x][y][0].getTransform("z-Achse");
		rotate[x][y][0].mul(temp_rot);
		stoneTransform[x][y][0].setTransform(rotate[x][y][0]);
	    }
	changeArrayPosition(2);
    }

    void rotateRedFace() {
	for (int z = 0; z < stoneTransform.length; z++)
	    for (int y = 0; y < stoneTransform.length; y++) {
		Transform3D temp_rot = new Transform3D();
		temp_rot = stone[2][y][z].getTransform("x-Achse");
		rotate[2][y][z].mul(temp_rot);
		stoneTransform[2][y][z].setTransform(rotate[2][y][z]);
	    }
	changeArrayPosition(1);
    }

    void rotateOrangeFace() {
	for (int z = 0; z < stoneTransform.length; z++)
	    for (int y = 0; y < stoneTransform.length; y++) {
		Transform3D temp_rot = new Transform3D();
		temp_rot = stone[0][y][z].getTransform("x-Achse");
		rotate[0][y][z].mul(temp_rot);
		stoneTransform[0][y][z].setTransform(rotate[0][y][z]);
	    }
	changeArrayPosition(3);
    }

    void rotateBlueFace() {
	for (int x = 0; x < stoneTransform.length; x++)
	    for (int z = 0; z < stoneTransform.length; z++) {
		Transform3D temp_rot = new Transform3D();
		temp_rot = stone[x][2][z].getTransform("y-Achse");
		rotate[x][2][z].mul(temp_rot);
		stoneTransform[x][2][z].setTransform(rotate[x][2][z]);
	    }
	changeArrayPosition(5);
    }

    void rotateGreenFace() {
	for (int x = 0; x < stoneTransform.length; x++)
	    for (int z = 0; z < stoneTransform.length; z++) {
		Transform3D temp_rot = new Transform3D();
		temp_rot = stone[x][0][z].getTransform("y-Achse");
		rotate[x][0][z].mul(temp_rot);
		stoneTransform[x][0][z].setTransform(rotate[x][0][z]);
	    }
	changeArrayPosition(6);
    }

    private void changeArrayPosition(int side) {
	TransformGroup tempTG_corner;
	TransformGroup tempTG_edge;

	Transform3D tempTF_corner;
	Transform3D tempTF_edge;

	Stone tempST_corner;
	Stone tempST_edge;

	switch (side) {
	    //Rotation der Right (Rote Wand)
	    case 1:
		tempTG_corner = stoneTransform[2][0][2];
		stoneTransform[2][0][2] = stoneTransform[2][2][2];
		stoneTransform[2][2][2] = stoneTransform[2][2][0];
		stoneTransform[2][2][0] = stoneTransform[2][0][0];
		stoneTransform[2][0][0] = tempTG_corner;

		tempTG_edge = stoneTransform[2][0][1];
		stoneTransform[2][0][1] = stoneTransform[2][1][2];
		stoneTransform[2][1][2] = stoneTransform[2][2][1];
		stoneTransform[2][2][1] = stoneTransform[2][1][0];
		stoneTransform[2][1][0] = tempTG_edge;

		tempTF_corner = rotate[2][0][2];
		rotate[2][0][2] = rotate[2][2][2];
		rotate[2][2][2] = rotate[2][2][0];
		rotate[2][2][0] = rotate[2][0][0];
		rotate[2][0][0] = tempTF_corner;

		tempTF_edge = rotate[2][0][1];
		rotate[2][0][1] = rotate[2][1][2];
		rotate[2][1][2] = rotate[2][2][1];
		rotate[2][2][1] = rotate[2][1][0];
		rotate[2][1][0] = tempTF_edge;

		tempST_corner = stone[2][0][2];
		stone[2][0][2] = stone[2][2][2];
		stone[2][2][2] = stone[2][2][0];
		stone[2][2][0] = stone[2][0][0];
		stone[2][0][0] = tempST_corner;

		tempST_edge = stone[2][0][1];
		stone[2][0][1] = stone[2][1][2];
		stone[2][1][2] = stone[2][2][1];
		stone[2][2][1] = stone[2][1][0];
		stone[2][1][0] = tempST_edge;
		break;

	    //Rotation der Back (Gelbe Wand)
	    case 2:
		tempTG_corner = stoneTransform[2][0][0];
		stoneTransform[2][0][0] = stoneTransform[0][0][0];
		stoneTransform[0][0][0] = stoneTransform[0][2][0];
		stoneTransform[0][2][0] = stoneTransform[2][2][0];
		stoneTransform[2][2][0] = tempTG_corner;

		tempTG_edge = stoneTransform[1][0][0];
		stoneTransform[1][0][0] = stoneTransform[0][1][0];
		stoneTransform[0][1][0] = stoneTransform[1][2][0];
		stoneTransform[1][2][0] = stoneTransform[2][1][0];
		stoneTransform[2][1][0] = tempTG_edge;

		tempTF_corner = rotate[2][0][0];
		rotate[2][0][0] = rotate[0][0][0];
		rotate[0][0][0] = rotate[0][2][0];
		rotate[0][2][0] = rotate[2][2][0];
		rotate[2][2][0] = tempTF_corner;

		tempTF_edge = rotate[1][0][0];
		rotate[1][0][0] = rotate[0][1][0];
		rotate[0][1][0] = rotate[1][2][0];
		rotate[1][2][0] = rotate[2][1][0];
		rotate[2][1][0] = tempTF_edge;

		tempST_corner = stone[2][0][0];
		stone[2][0][0] = stone[0][0][0];
		stone[0][0][0] = stone[0][2][0];
		stone[0][2][0] = stone[2][2][0];
		stone[2][2][0] = tempST_corner;

		tempST_edge = stone[1][0][0];
		stone[1][0][0] = stone[0][1][0];
		stone[0][1][0] = stone[1][2][0];
		stone[1][2][0] = stone[2][1][0];
		stone[2][1][0] = tempST_edge;
		break;
	    
	    //Rotation der Left (orange Wand)
	    case 3:
		tempTG_corner = stoneTransform[0][0][0];
		stoneTransform[0][0][0] = stoneTransform[0][0][2];
		stoneTransform[0][0][2] = stoneTransform[0][2][2];
		stoneTransform[0][2][2] = stoneTransform[0][2][0];
		stoneTransform[0][2][0] = tempTG_corner;

		tempTG_edge = stoneTransform[0][0][1];
		stoneTransform[0][0][1] = stoneTransform[0][1][2];
		stoneTransform[0][1][2] = stoneTransform[0][2][1];
		stoneTransform[0][2][1] = stoneTransform[0][1][0];
		stoneTransform[0][1][0] = tempTG_edge;
		
		tempTF_corner = rotate[0][0][0];
		rotate[0][0][0] = rotate[0][0][2];
		rotate[0][0][2] = rotate[0][2][2];
		rotate[0][2][2] = rotate[0][2][0];
		rotate[0][2][0] = tempTF_corner;

		tempTF_edge = rotate[0][0][1];
		rotate[0][0][1] = rotate[0][1][2];
		rotate[0][1][2] = rotate[0][2][1];
		rotate[0][2][1] = rotate[0][1][0];
		rotate[0][1][0] = tempTF_edge;
		
		tempST_corner = stone[0][0][0];
		stone[0][0][0] = stone[0][0][2];
		stone[0][0][2] = stone[0][2][2];
		stone[0][2][2] = stone[0][2][0];
		stone[0][2][0] = tempST_corner;

		tempST_edge = stone[0][0][1];
		stone[0][0][1] = stone[0][1][2];
		stone[0][1][2] = stone[0][2][1];
		stone[0][2][1] = stone[0][1][0];
		stone[0][1][0] = tempST_edge;
		
		break;

	    //Rotation der Front (Weisse Wand)
	    case 4:
		tempTG_corner = stoneTransform[0][0][2];
		stoneTransform[0][0][2] = stoneTransform[0][2][2];
		stoneTransform[0][2][2] = stoneTransform[2][2][2];
		stoneTransform[2][2][2] = stoneTransform[2][0][2];
		stoneTransform[2][0][2] = tempTG_corner;

		tempTG_edge = stoneTransform[1][0][2];
		stoneTransform[1][0][2] = stoneTransform[0][1][2];
		stoneTransform[0][1][2] = stoneTransform[1][2][2];
		stoneTransform[1][2][2] = stoneTransform[2][1][2];
		stoneTransform[2][1][2] = tempTG_edge;

		tempTF_corner = rotate[0][0][2];
		rotate[0][0][2] = rotate[0][2][2];
		rotate[0][2][2] = rotate[2][2][2];
		rotate[2][2][2] = rotate[2][0][2];
		rotate[2][0][2] = tempTF_corner;

		tempTF_edge = rotate[1][0][2];
		rotate[1][0][2] = rotate[0][1][2];
		rotate[0][1][2] = rotate[1][2][2];
		rotate[1][2][2] = rotate[2][1][2];
		rotate[2][1][2] = tempTF_edge;

		tempST_corner = stone[0][0][2];
		stone[0][0][2] = stone[0][2][2];
		stone[0][2][2] = stone[2][2][2];
		stone[2][2][2] = stone[2][0][2];
		stone[2][0][2] = tempST_corner;

		tempST_edge = stone[1][0][2];
		stone[1][0][2] = stone[0][1][2];
		stone[0][1][2] = stone[1][2][2];
		stone[1][2][2] = stone[2][1][2];
		stone[2][1][2] = tempST_edge;
		break;

	    //Rotation der Top (blaue Wand)
	    case 5:
		tempTG_corner = stoneTransform[0][2][2];
		stoneTransform[0][2][2] = stoneTransform[0][2][0];
		stoneTransform[0][2][0] = stoneTransform[2][2][0];
		stoneTransform[2][2][0] = stoneTransform[2][2][2];
		stoneTransform[2][2][2] = tempTG_corner;

		tempTG_edge = stoneTransform[1][2][2];
		stoneTransform[1][2][2] = stoneTransform[0][2][1];
		stoneTransform[0][2][1] = stoneTransform[1][2][0];
		stoneTransform[1][2][0] = stoneTransform[2][2][1];
		stoneTransform[2][2][1] = tempTG_edge;

		tempTF_corner = rotate[0][2][2];
		rotate[0][2][2] = rotate[0][2][0];
		rotate[0][2][0] = rotate[2][2][0];
		rotate[2][2][0] = rotate[2][2][2];
		rotate[2][2][2] = tempTF_corner;

		tempTF_edge = rotate[1][2][2];
		rotate[1][2][2] = rotate[0][2][1];
		rotate[0][2][1] = rotate[1][2][0];
		rotate[1][2][0] = rotate[2][2][1];
		rotate[2][2][1] = tempTF_edge;

		tempST_corner = stone[0][2][2];
		stone[0][2][2] = stone[0][2][0];
		stone[0][2][0] = stone[2][2][0];
		stone[2][2][0] = stone[2][2][2];
		stone[2][2][2] = tempST_corner;

		tempST_edge = stone[1][2][2];
		stone[1][2][2] = stone[0][2][1];
		stone[0][2][1] = stone[1][2][0];
		stone[1][2][0] = stone[2][2][1];
		stone[2][2][1] = tempST_edge;
		break;
		
	    //Rotation der Bottom (grüne Wand)	
	    case 6:
		tempTG_corner = stoneTransform[0][0][0];
		stoneTransform[0][0][0] = stoneTransform[2][0][0];
		stoneTransform[2][0][0] = stoneTransform[2][0][2];
		stoneTransform[2][0][2] = stoneTransform[0][0][2];
		stoneTransform[0][0][2] = tempTG_corner;

		tempTG_edge = stoneTransform[1][0][0];
		stoneTransform[1][0][0] = stoneTransform[2][0][1];
		stoneTransform[2][0][1] = stoneTransform[1][0][2];
		stoneTransform[1][0][2] = stoneTransform[0][0][1];
		stoneTransform[0][0][1] = tempTG_edge;
		
		tempTF_corner = rotate[0][0][0];
		rotate[0][0][0] = rotate[2][0][0];
		rotate[2][0][0] = rotate[2][0][2];
		rotate[2][0][2] = rotate[0][0][2];
		rotate[0][0][2] = tempTF_corner;

		tempTF_edge = rotate[1][0][0];
		rotate[1][0][0] = rotate[2][0][1];
		rotate[2][0][1] = rotate[1][0][2];
		rotate[1][0][2] = rotate[0][0][1];
		rotate[0][0][1] = tempTF_edge;
		
		tempST_corner = stone[0][0][0];
		stone[0][0][0] = stone[2][0][0];
		stone[2][0][0] = stone[2][0][2];
		stone[2][0][2] = stone[0][0][2];
		stone[0][0][2] = tempST_corner;

		tempST_edge = stone[1][0][0];
		stone[1][0][0] = stone[2][0][1];
		stone[2][0][1] = stone[1][0][2];
		stone[1][0][2] = stone[0][0][1];
		stone[0][0][1] = tempST_edge;
		break;
	}
    }
}
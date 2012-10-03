package Cube;

import java.awt.Color;
import javax.media.j3d.*;
import javax.media.j3d.QuadArray;
import javax.vecmath.*;


/**
 *
 * @author Rafael Sebastian Müller
 */
public class Stone
{
    //Attribute
    QuadArray[] array = new QuadArray[6];
    TransformGroup objStone = new TransformGroup();
    final static Color3f white = new Color3f(Color.WHITE);
    final static Color3f yellow = new Color3f(Color.YELLOW);
    final static Color3f blue = new Color3f(Color.BLUE);
    final static Color3f green = new Color3f(Color.GREEN);
    final static Color3f red = new Color3f(Color.RED);
    final static Color3f orange = new Color3f(new Color(244, 158, 2));
    private String x_axe;
    private String y_axe;
    
    Shape3D shape = new Shape3D();
    int x_axis;
    int y_axis;

    /*
     * ZEICHENRICHTUNG
     * Es ist wichtig, dass die Koordianten so gesetzt werden, dass sie
     * -von aussen gesehen- immer gegen den Uhrzeigersinn laufen, da die
     * Flächen nicht gezeichnet werden (culling).
     * 
     * FARBE
     * Weil bezeichnet wird, dass die Steine eine Farbe erhalten, muss jeder
     * Seite unbedingt eine Farbe zugewiesen werden.
     * 
     * EINFÄRBUNG
     * Es werden nur die Steine eingefärbt, die sich aussen befinden. Das Ein-
     * färben ist nur einmal nötig, weil sie sich danach nie mehr ändert.
     */
    //constructor
    public Stone(float x, float y, float z)
    {
	shape = createGeometry(x, y, z);
	shape.setAppearance(createAppearance(x, y, z));
	objStone.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	objStone.addChild(shape);
    }

    //Methoden
    public TransformGroup getTransformGroup()
    {
	return objStone;
    }

    private Shape3D createGeometry(float x, float y, float z)
    {
	Float[] x_pos = new Float[2];
	x_pos[0] = new Float(x - 0.40);
	x_pos[1] = new Float(x + 0.40);

	Float[] y_pos = new Float[2];
	y_pos[0] = new Float(y - 0.40);
	y_pos[1] = new Float(y + 0.40);

	Float[] z_pos = new Float[2];
	z_pos[0] = new Float(z - 0.40);
	z_pos[1] = new Float(z + 0.40);

	Point3f[][][] point = new Point3f[2][2][2];

	for (int i = 0; i < x_pos.length; i++)
	    for (int j = 0; j < y_pos.length; j++)
		for (int k = 0; k < z_pos.length; k++)
		    point[i][j][k] = new Point3f(x_pos[i], y_pos[j], z_pos[k]);
	
	for(int i = 0; i < array.length; i++)
	{
	    array[i] = new QuadArray(4, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
	    array[i].setCapability(QuadArray.ALLOW_COLOR_WRITE);
	}

	//front
	    array[0].setCoordinate(0, point[0][0][1]);
	    array[0].setCoordinate(1, point[1][0][1]);
	    array[0].setCoordinate(2, point[1][1][1]);
	    array[0].setCoordinate(3, point[0][1][1]);
	//back
	    array[1].setCoordinate(0, point[0][0][0]);
	    array[1].setCoordinate(1, point[0][1][0]);
	    array[1].setCoordinate(2, point[1][1][0]);
	    array[1].setCoordinate(3, point[1][0][0]);
	//left
	    array[2].setCoordinate(0, point[0][0][1]);
	    array[2].setCoordinate(1, point[0][1][1]);
	    array[2].setCoordinate(2, point[0][1][0]);
	    array[2].setCoordinate(3, point[0][0][0]);
	//right
	    array[3].setCoordinate(0, point[1][0][1]);
	    array[3].setCoordinate(1, point[1][0][0]);
	    array[3].setCoordinate(2, point[1][1][0]);
	    array[3].setCoordinate(3, point[1][1][1]);
	//down
	    array[4].setCoordinate(0, point[0][0][1]);
	    array[4].setCoordinate(1, point[0][0][0]);
	    array[4].setCoordinate(2, point[1][0][0]);
	    array[4].setCoordinate(3, point[1][0][1]);
	//top
	    array[5].setCoordinate(0, point[0][1][1]);
	    array[5].setCoordinate(1, point[1][1][1]);
	    array[5].setCoordinate(2, point[1][1][0]);
	    array[5].setCoordinate(3, point[0][1][0]);

	for(int i = 0; i < array.length; i++)
	{
	   shape.addGeometry(array[i]);
	}
	
	return shape;
    }

    private Appearance createAppearance(float x, float y, float z)
    {
	Appearance appearance = new Appearance();
	    for(int i = 0; i < 4; i++)
	    {
		if (z == 1)
		    array[0].setColor(i, white);

		if (z == -1)
		    array[1].setColor(i, yellow);

		if (x == -1)
		    array[2].setColor(i, orange);

		if (x == 1)
		    array[3].setColor(i, red);

		if (y == -1)
		    array[4].setColor(i, green);

		if (y == 1)
		    array[5].setColor(i, blue);
	    }
	return appearance;
    }

    void setLocalCord()
    {
	x_axis = 1;
	y_axis = 5;
    }
    
    void setLocalCord(int x_axis, int y_axis)
    {
	this.x_axis = x_axis;
	this.y_axis = y_axis;
    }
    
    public int getX_axis()
    {
	return x_axis;
    }
    
    public int getY_axis()
    {
	return y_axis;
    }
    
    public Transform3D getTransform(float angle, int rotate_axis, int x_axis, int y_axis)
    {
	Transform3D rotate = new Transform3D();
	
// *** Es wird um die x-Achse rotiert *** //
	if(rotate_axis == 1)
	{
	    //x-Achse ist gegen rechts gerichtet
	    if(x_axis == 1 ){//
		if(y_axis == 5){
		    rotate.rotX(Math.toRadians(angle));
		    y_axis = 4;
		} else if(y_axis == 6){
		    rotate.rotX(Math.toRadians(angle));
		    y_axis = 2;
		} else if(y_axis == 2){
		    rotate.rotX(Math.toRadians(angle));
		    y_axis = 5;
		} else if(y_axis == 4){
		    rotate.rotX(Math.toRadians(angle));
		    y_axis = 6;
		}
	    } 
	    //x-Achse gegen hinten gerichtet
	    else if(x_axis == 2){//
		if(y_axis == 5){
		    rotate.rotZ(Math.toRadians(angle));
		    y_axis = 4;
		} else if(y_axis == 6){
		    rotate.rotZ(Math.toRadians(-angle));
		    y_axis = 2;
		} else if(y_axis == 1){
		    rotate.rotY(Math.toRadians(angle));
		} else if(y_axis == 3){
		    rotate.rotY(Math.toRadians(-angle));
		}
		x_axis = 5;
	    }
	    //x-Achse gegen links gerichtet
	    else if(x_axis == 3){//
		if(y_axis == 5){
		    rotate.rotX(Math.toRadians(-angle));
		    y_axis = 4;
		} else if(y_axis == 6){
		    rotate.rotX(Math.toRadians(-angle));
		    y_axis = 2;
		} else if(y_axis == 2){
		    rotate.rotX(Math.toRadians(-angle));
		    y_axis = 5;
		} else if(y_axis == 4){
		    rotate.rotX(Math.toRadians(-angle));
		    y_axis = 6;
		}
		x_axis = 3;
	    }
	    //x-Achse gegen vorne gerichtet
	    else if(x_axis == 4){//
		if(y_axis == 5){
		    rotate.rotZ(Math.toRadians(-angle));
		    y_axis = 4;
		} else if(y_axis == 6){
		    rotate.rotZ(Math.toRadians(angle));
		    y_axis = 2;
		} else if(y_axis == 1){
		    rotate.rotY(Math.toRadians(angle));
		} else if(y_axis == 3){
		    rotate.rotY(Math.toRadians(-angle));
		}
		x_axis = 6;
	    }
	    //x-Achse gegen oben gerichtet
	    else if(x_axis == 5){
		if(y_axis == 1){
		    rotate.rotY(Math.toRadians(angle));
		    y_axis = 1;
		} else if(y_axis == 3){
		    rotate.rotY(Math.toRadians(-angle));
		    y_axis = 3;
		} else if(y_axis == 2){
		    rotate.rotZ(Math.toRadians(-angle));
		    y_axis = 5;
		} else if(y_axis == 4){
		    rotate.rotZ(Math.toRadians(angle));
		    y_axis = 6;
		}
		x_axis = 4;
	    }
	    //x-Achse gegen unten gerichtet
	    else if(x_axis == 6){
		if(y_axis == 1){
		    rotate.rotY(Math.toRadians(angle));
		    y_axis = 1;
		} else if(y_axis == 3){
		    rotate.rotY(Math.toRadians(-angle));
		    y_axis = 3;
		} else if(y_axis == 2){
		    rotate.rotZ(Math.toRadians(angle));
		    y_axis = 5;
		} else if(y_axis == 4){
		    rotate.rotZ(Math.toRadians(-angle));
		    y_axis = 6;
		}
		x_axis = 2;
	    }
	}
// *** Es wird um die y-Achse rotiert *** //
 	else if(rotate_axis == 2)
	{
	    //x-Achse gegen rechts gerichtet
	    if(x_axis == 1){//
		if(y_axis == 5){
		    rotate.rotY(Math.toRadians(angle));
		} else if(y_axis == 6){
		    rotate.rotY(Math.toRadians(-angle));
		} else if(y_axis == 2){
		    rotate.rotZ(Math.toRadians(angle));
		    y_axis = 3;
		} else if(y_axis == 4){
		    rotate.rotZ(Math.toRadians(-angle));
		    y_axis = 1;
		}
		x_axis = 2;	
	    }
	    //x-Achse gegen hinten gerichtet
	    else if(x_axis == 2){//
		if(y_axis == 5){
		    rotate.rotY(Math.toRadians(angle));
		} else if(y_axis == 6){
		    rotate.rotY(Math.toRadians(-angle));
		} else if(y_axis == 1){
		    rotate.rotZ(Math.toRadians(-angle));
		    y_axis = 2;
		} else if(y_axis == 3){
		    rotate.rotZ(Math.toRadians(angle));
		    y_axis = 4;
		}
		x_axis = 3;
	    }
	    //x-Achse gegen links gerichtet
	    else if(x_axis == 3){//
		if(y_axis == 5){
		    rotate.rotY(Math.toRadians(angle));
		} else if(y_axis == 6){
		    rotate.rotY(Math.toRadians(-angle));
		} else if(y_axis == 2){
		    rotate.rotZ(Math.toRadians(-angle));
		    y_axis = 3;
		} else if(y_axis == 4){
		    rotate.rotZ(Math.toRadians(angle));
		    y_axis = 1;
		}
		x_axis = 4;
	    }
	    //x_Achse gegen vorne gerichtet
	    else if(x_axis == 4){
		if(y_axis == 5){
		    rotate.rotY(Math.toRadians(angle));
		    x_axis = 1;
		    y_axis = 5;
		} else if(y_axis == 6){
		    rotate.rotY(Math.toRadians(-angle));
		    x_axis = 1;
		    y_axis = 6;
		} else if(y_axis == 1){
		    rotate.rotZ(Math.toRadians(angle));
		    x_axis = 1;
		    y_axis = 2;
		} else if(y_axis == 3){
		    rotate.rotZ(Math.toRadians(-angle));
		    x_axis = 1;
		    y_axis = 4;
		}
	    }
	    //x-Achse gegen oben gerichtet
	    else if(x_axis == 5){
		if(y_axis == 1){
		    rotate.rotX(Math.toRadians(angle));
		    y_axis = 2;
		} else if(y_axis == 3){
		    rotate.rotX(Math.toRadians(angle));
		    y_axis = 4;
		} else if(y_axis == 2){
		    rotate.rotX(Math.toRadians(angle));
		    y_axis = 3;
		} else if(y_axis == 4){
		    rotate.rotX(Math.toRadians(angle));
		    y_axis = 1;
		}
		x_axis = 5;
	    }
	    //x-Achse gegen unten gerichtet
	    else if(x_axis == 6){//
		if(y_axis == 1){
		    rotate.rotX(Math.toRadians(-angle));
		    y_axis = 2;
		} else if(y_axis == 3){
		    rotate.rotX(Math.toRadians(-angle));
		    y_axis = 4;
		} else if(y_axis == 2){
		    rotate.rotX(Math.toRadians(-angle));
		    y_axis = 3;
		} else if(y_axis == 4){
		    rotate.rotX(Math.toRadians(-angle));
		    y_axis = 1;
		}
	    }
	}
// *** Es soll um die z-Achse rotiert werden *** //
	else if(rotate_axis == 3)
	{
	    //x-Achse gegen rechts gerichtet
	    if(x_axis == 1){//
		if(y_axis == 5){
		    rotate.rotZ(Math.toRadians(angle));
		    y_axis = 3;
		} else if(y_axis == 6){
		    rotate.rotZ(Math.toRadians(-angle));
		    y_axis = 1;
		} else if(y_axis == 2){
		    rotate.rotY(Math.toRadians(-angle));
		} else if(y_axis ==4){
		    rotate.rotY(Math.toRadians(angle));
		}
		x_axis = 5;
	    }
	    //x-Achse gegen hinten gerichtet
	    else if(x_axis == 2){//
		if(y_axis == 5){
		    rotate.rotX(Math.toRadians(-angle));
		    y_axis = 3;
		} else if(y_axis == 6){
		    rotate.rotX(Math.toRadians(-angle));
		    y_axis = 1;
		} else if(y_axis == 1){
		    rotate.rotX(Math.toRadians(-angle));
		    y_axis = 5;
		} else if(y_axis == 3){
		    rotate.rotX(Math.toRadians(-angle));
		    y_axis = 6;
		}
	    }
	    //x-Achse gegen links gerichtet
	    else if(x_axis == 3){//
		if(y_axis == 5){
		    rotate.rotZ(Math.toRadians(-angle));
		    y_axis = 3;
		} else if(y_axis == 6){
		    rotate.rotZ(Math.toRadians(angle));
		    y_axis = 1;
		} else if(y_axis == 2){
		    rotate.rotY(Math.toRadians(-angle));
		} else if(y_axis ==4){
		    rotate.rotY(Math.toRadians(angle));
		}
		x_axis = 6;
	    }
	    //x_Achse gegen vorne gerichtet
	    else if(x_axis == 4){//
		if(y_axis == 5){
		    rotate.rotX(Math.toRadians(angle));
		    y_axis = 3;
		} else if(y_axis == 6){
		    rotate.rotX(Math.toRadians(angle));
		    y_axis = 1;
		} else if(y_axis == 1){
		    rotate.rotX(Math.toRadians(angle));
		    y_axis = 5;
		} else if(y_axis == 3){
		    rotate.rotX(Math.toRadians(angle));
		    y_axis = 6;
		}
		x_axis = 4;
	    }
	    //x-Achse gegen oben gerichtet
	    else if(x_axis == 5){//
		if(y_axis == 1){
		    rotate.rotZ(Math.toRadians(-angle));
		    y_axis = 5;
		} else if(y_axis == 3){
		    rotate.rotZ(Math.toRadians(angle));
		    y_axis = 6;
		} else if(y_axis == 2){
		    rotate.rotY(Math.toRadians(-angle));
		} else if(y_axis == 4){
		    rotate.rotY(Math.toRadians(angle));
		}
		x_axis = 3;
	    }
	    //x-Achse gegen unten gerichtet
	    else if(x_axis == 6){//
		if(y_axis == 1){
		    rotate.rotZ(Math.toRadians(angle));
		    y_axis = 5;
		} else if(y_axis == 3){
		    rotate.rotZ(Math.toRadians(-angle));
		    y_axis = 6;
		} else if(y_axis == 2){
		    rotate.rotY(Math.toRadians(-angle));
		} else if(y_axis == 4){
		    rotate.rotY(Math.toRadians(angle));
		}
		x_axis = 1;
	    }
	}
	this.x_axis = x_axis;
	this.y_axis = y_axis;
	return rotate;
    }  
}
package Cube;

import java.awt.Color;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
 *
 * @author Rafael Sebastian Müller
 */
public class Stone
{
    //Attribute
    QuadArray array;
    TransformGroup objStone = new TransformGroup();
    final static Color3f white = new Color3f(Color.WHITE);
    final static Color3f yellow = new Color3f(Color.YELLOW);
    final static Color3f blue = new Color3f(Color.BLUE);
    final static Color3f green = new Color3f(Color.GREEN);
    final static Color3f red = new Color3f(Color.RED);
    final static Color3f orange = new Color3f(Color.ORANGE);

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
	Geometry geometry = createGeometry(x, y, z);
	Appearance appearance = createAppearance(x, y, z);

	objStone.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

	Shape3D shape = new Shape3D(geometry, appearance);
	objStone.addChild(shape);
    }

    //Methoden
    public TransformGroup getTransformGroup()
    {
	return objStone;
    }

    public void turneStone()
    {
	Transform3D rotate = new Transform3D();
	rotate.rotY(Math.toRadians(-90));
	objStone.setTransform(rotate);
    }

    private Geometry createGeometry(float x, float y, float z)
    {
	array = new QuadArray(24, GeometryArray.COORDINATES | GeometryArray.COLOR_3|GeometryArray.NORMALS);

	array.setCapability(QuadArray.ALLOW_COLOR_WRITE);

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

	//front
	array.setCoordinate(0, point[0][0][1]);
	array.setCoordinate(1, point[1][0][1]);
	array.setCoordinate(2, point[1][1][1]);
	array.setCoordinate(3, point[0][1][1]);
	//back
	array.setCoordinate(4, point[0][0][0]);
	array.setCoordinate(5, point[0][1][0]);
	array.setCoordinate(6, point[1][1][0]);
	array.setCoordinate(7, point[1][0][0]);
	//left
	array.setCoordinate(8, point[0][0][1]);
	array.setCoordinate(9, point[0][1][1]);
	array.setCoordinate(10, point[0][1][0]);
	array.setCoordinate(11, point[0][0][0]);
	//right
	array.setCoordinate(12, point[1][0][1]);
	array.setCoordinate(13, point[1][0][0]);
	array.setCoordinate(14, point[1][1][0]);
	array.setCoordinate(15, point[1][1][1]);
	//down
	array.setCoordinate(16, point[0][0][1]);
	array.setCoordinate(17, point[0][0][0]);
	array.setCoordinate(18, point[1][0][0]);
	array.setCoordinate(19, point[1][0][1]);
	//top
	array.setCoordinate(20, point[0][1][1]);
	array.setCoordinate(21, point[1][1][1]);
	array.setCoordinate(22, point[1][1][0]);
	array.setCoordinate(23, point[0][1][0]);

	return array;
    }

    private Appearance createAppearance(float x, float y, float z)
    {
	Appearance appearance = new Appearance();

	if (z == 1)
	    for (int i = 0; i < 4; i++)
		array.setColor(i, white);

	if (z == -1)
	    for (int i = 4; i < 8; i++)
		array.setColor(i, yellow);

	if (x == -1)
	    for (int i = 8; i < 12; i++)
		array.setColor(i, orange);

	if (x == 1)
	    for (int i = 12; i < 16; i++)
		array.setColor(i, red);

	if (y == -1)
	    for (int i = 16; i < 20; i++)
		array.setColor(i, green);

	if (y == 1)
	    for (int i = 20; i < 24; i++)
		array.setColor(i, blue);

	return appearance;
    }

    void changeColor()
    {
	for (int i = 0; i < 4; i++)
	    array.setColor(i, orange);
	for (int i = 8; i < 12; i++)
	    array.setColor(i, yellow);
	for (int i = 4; i < 8; i++)
	    array.setColor(i, red);
	for (int i = 12; i < 16; i++)
	    array.setColor(i, white);
    }
    

}
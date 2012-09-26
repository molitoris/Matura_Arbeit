package Cube;

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
    Shape3D shape = new Shape3D();
    TransformGroup objStone = new TransformGroup();
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
    private final float x;
    private final float y;
    private final float z;
    
    //Konstruktor
    public Stone(float x, float y, float z)
    {
        array = new QuadArray(24, GeometryArray.COORDINATES|GeometryArray.COLOR_3);
        this.x = x;
	this.y = y;
	this.z = z;
        /*
	 * Die Position der Ecksteine werden durch die drei Parameter ausge-
	 * rechnet und in Punkt-Variabeln gespeichert.
	 */
        Point3f pa = new Point3f(x-0.49f,y-0.49f,z+0.49f);
        Point3f pb = new Point3f(x+0.49f,y-0.49f,z+0.49f);
        Point3f pc = new Point3f(x+0.49f,y+0.49f,z+0.49f);
        Point3f pd = new Point3f(x-0.49f,y+0.49f,z+0.49f);

        Point3f pe = new Point3f(x-0.49f,y-0.49f,z-0.49f);
        Point3f pf = new Point3f(x+0.49f,y-0.49f,z-0.49f);
        Point3f pg = new Point3f(x+0.49f,y+0.49f,z-0.49f);
        Point3f ph = new Point3f(x-0.49f,y+0.49f,z-0.49f);
        
	/*
	 * Es werden sechs Fächen durch die Punkt-Variabeln gesetzt, welche den 
	 * Würfel zusammensetzten.
	 */
        //front
        array.setCoordinate(0, pa);
        array.setCoordinate(1, pb);
        array.setCoordinate(2, pc);
        array.setCoordinate(3, pd);
        
        //back
        array.setCoordinate(4, pe);
        array.setCoordinate(5, ph);
        array.setCoordinate(6, pg);
        array.setCoordinate(7, pf);
        
        //left
        array.setCoordinate(8, pa);
        array.setCoordinate(9, pd);
        array.setCoordinate(10, ph);
        array.setCoordinate(11, pe);
        
        //right
        array.setCoordinate(12, pb);
        array.setCoordinate(13, pf);
        array.setCoordinate(14, pg);
        array.setCoordinate(15, pc);
        
        //down
        array.setCoordinate(16, pa);
        array.setCoordinate(17, pe);
        array.setCoordinate(18, pf);
        array.setCoordinate(19, pb);
        
        //top
        array.setCoordinate(20, pd);
        array.setCoordinate(21, pc);
        array.setCoordinate(22, pg);
        array.setCoordinate(23, ph);
        
        
        Color3f white   = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f yellow  = new Color3f(1.0f, 1.0f, 0.0f);
        Color3f blue    = new Color3f(0.0f, 0.0f, 1.0f);
        Color3f green   = new Color3f(0.0f, 1.0f, 0.0f);
        Color3f red     = new Color3f(1.0f, 0.0f, 0.0f);
        Color3f orange  = new Color3f(1.0f, 0.5f, 0.0f);        
        
        if(z == 1)
            for(int i = 0; i < 4; i++)
                array.setColor(i, white);

        if(z == -1)
            for(int i = 4; i < 8; i++)
                array.setColor(i, yellow);

        if(x == -1)
            for(int i = 8; i < 12; i++)
                array.setColor(i, orange);

        if(x == 1)
            for(int i = 12; i < 16; i++)
                array.setColor(i, red);

        if(y == -1)
            for(int i = 16; i < 20; i++)
                array.setColor(i, green);

        if(y == 1)
            for(int i = 20; i < 24; i++)
                array.setColor(i, blue);
	
	shape.addGeometry(array);
	objStone.addChild(shape);
    }    

    //Methoden
    public TransformGroup getTransformGroup()
    {
        return objStone;
    }
    
    public void rotateTop()
    {
	
    }
    
    
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
    QuadArray stone;
    Shape3D shape = new Shape3D();
    TransformGroup objStone = new TransformGroup();
    /*
     * ZEICHENRICHTUNG
     * Es ist wichtig, dass von die Koordianten so gesetzt werden, dass von sie
     * -von aussen gesehen- immer gegen den Uhrzeigersinn laufen, da sie die
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
    
    //Konstruktor
    public Stone(float x, float y, float z)
    {
        stone = new QuadArray(24, GeometryArray.COORDINATES|GeometryArray.COLOR_3);
        
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
        stone.setCoordinate(0, pa);
        stone.setCoordinate(1, pb);
        stone.setCoordinate(2, pc);
        stone.setCoordinate(3, pd);
        
        //back
        stone.setCoordinate(4, pe);
        stone.setCoordinate(5, ph);
        stone.setCoordinate(6, pg);
        stone.setCoordinate(7, pf);
        
        //left
        stone.setCoordinate(8, pa);
        stone.setCoordinate(9, pd);
        stone.setCoordinate(10, ph);
        stone.setCoordinate(11, pe);
        
        //right
        stone.setCoordinate(12, pb);
        stone.setCoordinate(13, pf);
        stone.setCoordinate(14, pg);
        stone.setCoordinate(15, pc);
        
        //down
        stone.setCoordinate(16, pa);
        stone.setCoordinate(17, pe);
        stone.setCoordinate(18, pf);
        stone.setCoordinate(19, pb);
        
        //top
        stone.setCoordinate(20, pd);
        stone.setCoordinate(21, pc);
        stone.setCoordinate(22, pg);
        stone.setCoordinate(23, ph);
        
        
        Color3f white   = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f yellow  = new Color3f(1.0f, 1.0f, 0.0f);
        Color3f blue    = new Color3f(0.0f, 0.0f, 1.0f);
        Color3f green   = new Color3f(0.0f, 1.0f, 0.0f);
        Color3f red     = new Color3f(1.0f, 0.0f, 0.0f);
        Color3f orange  = new Color3f(1.0f, 0.5f, 0.0f);        
        
        if(z == 1)
            for(int i = 0; i < 4; i++)
                stone.setColor(i, white);

        if(z == -1)
            for(int i = 4; i < 8; i++)
                stone.setColor(i, yellow);

        if(x == -1)
            for(int i = 8; i < 12; i++)
                stone.setColor(i, red);

        if(x == 1)
            for(int i = 12; i < 16; i++)
                stone.setColor(i, orange);

        if(y == -1)
            for(int i = 16; i < 20; i++)
                stone.setColor(i, green);

        if(y == 1)
            for(int i = 20; i < 24; i++)
                stone.setColor(i, blue);
	
	shape.addGeometry(stone);
	objStone.addChild(shape);
	
	
    }    

    //Methoden
    public TransformGroup getQuadArray()
    {
        return objStone;
    }
}
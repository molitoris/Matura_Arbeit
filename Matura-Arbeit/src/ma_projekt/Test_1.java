package ma_projekt;

import ch.aplu.util.*;          
/*Hilfsklasse, welche in der Schule benutzt wird. Ich habe mich noch nicht mit
 * den Tücken der Fenster der Java-Libraris befasst.
 * Download-Link: http://www.aplu.ch/home/download.jsp
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class Test_1 extends JPanel
{        
    private GWindow w = new GWindow();
    private int count = 0;
    
    Test_1()
    {
        w.setWinSize(500, 300);
        w.showComponent(this);
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        mittelstein(g);
        eckstein(g);
        kantenstein(g);        
        System.out.println(++count);
        /*
         *Eventuell kann ich hier noch verbessern. Ich zeichne immer alles neu,
         * wenn ich dasFenster verändere ==> viel Leistungsgebrauch
         */
    }
    
    private void mittelstein(Graphics g)
    {
        Graphics2D g2D = (Graphics2D)g;
        
        Line2D[] lines =
        {
            new Line2D.Double(100, 100, 150, 100),
            new Line2D.Double(150, 100, 150, 150),
            new Line2D.Double(150, 150, 100, 150),
            new Line2D.Double(100, 150, 100, 100)
        };
        
        for(int i = 0; i < lines.length; i++)
            g2D.draw(lines[i]);
    }
    
    private void eckstein(Graphics g)
    {
        Graphics2D g2D = (Graphics2D)g;
        
        Line2D[] lines =
        {
            new Line2D.Double(200, 100, 250, 100),
            new Line2D.Double(250, 100, 250, 150),
            new Line2D.Double(250, 150, 200, 150),
            new Line2D.Double(200, 150, 200, 100),
            
            /*Dadurch entsteht kein richtiges 3D-Bild. Wenn es gedreht würde,
             * wäre sichtbar, dass es ein Fläche ist und nur durch eine obtische
             * Täuschung dreidimensional wirkt. 
             */
            
            new Line2D.Double(200, 100, 225, 75),
            new Line2D.Double(225, 75, 275, 75),
            new Line2D.Double(250, 100, 275, 75),
            new Line2D.Double(250, 150, 275, 125),
            new Line2D.Double(275, 75, 275, 125)            
        };
        
        for(int i = 0; i < lines.length; i++)
            g2D.draw(lines[i]);
    }
    
    private void kantenstein(Graphics g)
    {
        Graphics2D g2D = (Graphics2D)g;
        
        Line2D[] lines =
        {
            new Line2D.Double(300, 100, 350, 100),
            new Line2D.Double(350, 100, 350, 150),
            new Line2D.Double(350, 150, 300, 150),
            new Line2D.Double(300, 150, 300, 100),
            
            new Line2D.Double(300, 100, 325, 75),
            new Line2D.Double(325, 75, 375, 75),
            new Line2D.Double(350, 100, 375, 75)            
        };
        
        for(int i = 0; i < lines.length; i++)
            g2D.draw(lines[i]);
    }

    public static void main(String[] args)
    {
        new Test_1();
    }
}
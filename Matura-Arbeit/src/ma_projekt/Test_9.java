/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ma_projekt;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author Rafael Müller_2
 */
public class Test_9 extends JPanel
{
    JFrame frame;
    public Test_9() 
    {
	
    }
    
    
    @Override
    protected void paintComponent(Graphics g)
    {
	Graphics2D g2 = (Graphics2D) g;
	
	for(int x = 0; x < 3; x++)
	    for(int y = 0; y < 3; y++)
		g2.drawRect(x*50, y*50, 50, 50);
    }
    
    public static void main (String[] args)
    {
	JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	frame.add(new Test_9());
	frame.setSize(200, 200);
	frame.setLocationRelativeTo(null);	
	frame.setVisible(true);
    }
}

package Cube;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
/**
 *
 * @author Rafael Sebastian Müller
 */

public final class GUI
{
    //Attribute
    RubicsCube cube;
            
    //Konstruktor
    public GUI()
    {
        JFrame frame = new JFrame();
        frame.setTitle("Cube Solver");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 700);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);        
        frame.setLayout(new BorderLayout());
        	
        Container container = new Container();
        container = frame.getContentPane();
        
        cube = new RubicsCube();
        container.add("Center", cube.returnCanvas());
	
	container.add("North", createPanel());
        
        frame.setVisible(true);
    }    
    //Methoden
    
    public JPanel createPanel()
    {
	JPanel panel = new JPanel();
	
	final JButton button = new JButton("Test");
	final JButton button2 = new JButton("Test2");
	final JButton button3 = new JButton("Test3");
	
	ActionListener al = new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		if(e.getSource() == button)
		    cube.test();
		if(e.getSource() == button2)
		    cube.test2();
		if(e.getSource() == button3)
		   cube.test3(); 
	    }
	};
	button.addActionListener(al);
	button2.addActionListener(al);
	button3.addActionListener(al);
	
	panel.add(button);
	panel.add(button2);
	panel.add(button3);
	
	return panel;
    }
}
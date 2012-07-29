/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author Rafael Müller_2
 */
public class GUI
{
    Container container;
    RubicsCube cube;
            
    public GUI()
    {
        JFrame frame = new JFrame();
        frame.setTitle("Cube Solver");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(500, 700);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        
        frame.setLayout(new BorderLayout());
        
        container = new Container();
        container = frame.getContentPane();
        
        
        container.add("East", createButtonPanel());
        
        cube = new RubicsCube();
        container.add("Center", cube.returnCanvas());
        
        frame.setVisible(true);
    }
    
    public JPanel createButtonPanel()
    {
        JPanel panel = new JPanel();        
        JButton button = new JButton("turne");        
        
        ActionListener al = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                
            }
        };
        button.addActionListener(al);
        panel.add(button);
        
        return panel;
    }
}

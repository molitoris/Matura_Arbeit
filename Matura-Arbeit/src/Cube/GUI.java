package Cube;

import java.awt.BorderLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
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
public final class GUI {
    //Attribute
    Rubiks_Cube cube;

    //Konstruktor
    public GUI() {
	JFrame frame = new JFrame();
	frame.setTitle("Cube Solver");
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	frame.setSize(800, 800);
	frame.setResizable(false);
	frame.setLocationRelativeTo(null);
	frame.setLayout(new BorderLayout());

	cube = new Rubiks_Cube();

	frame.add("Center", cube.returnCanvas());
	frame.setMenuBar(createMenuBar());
	frame.add("South", createPanel());
	frame.setVisible(true);
    }

    private JPanel createPanel() {
	JPanel panel = new JPanel();

	final JButton white = new JButton("White");
	final JButton yellow = new JButton("Yellow");
	final JButton red = new JButton("Red");
	final JButton orange = new JButton("Orange");
	final JButton blue = new JButton("Blue");
	final JButton green = new JButton("Green");

	ActionListener al = new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == white)
		    cube.rotateWhiteFace();
		if (e.getSource() == yellow)
		    cube.rotateYelllowFace();
		if (e.getSource() == red)
		    cube.rotateRedFace();
		if (e.getSource() == orange)
		    cube.rotateOrangeFace();
		if (e.getSource() == blue)
		    cube.rotateBlueFace();
		if (e.getSource() == green)
		    cube.rotateGreenFace();
	    }
	};
	white.addActionListener(al);
	yellow.addActionListener(al);
	red.addActionListener(al);
	orange.addActionListener(al);
	blue.addActionListener(al);
	green.addActionListener(al);

	panel.add(white);
	panel.add(yellow);
	panel.add(red);
	panel.add(orange);
	panel.add(blue);
	panel.add(green);

	return panel;
    }

    private MenuBar createMenuBar() {
	MenuBar menuBar = new MenuBar();
	
	Menu data = new Menu("Datei");
	menuBar.add(data);
	
	final MenuItem itemReset = new MenuItem("Zurücksetzen");
	data.add(itemReset);
	
	ActionListener ListenerMenu = new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		if(e.getSource() == itemReset)
		    cube.reset();
	    }
	};
	
	itemReset.addActionListener(ListenerMenu);
	
	
	
	return menuBar;
    }
}
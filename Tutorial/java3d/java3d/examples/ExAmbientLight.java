//
//  CLASS
//    ExAmbientLight  -  illustrate use of ambient lights
//
//  LESSON
//    Add an AmbientLight node to illuminate a scene.
//
//  SEE ALSO
//    ExDirectionalLight
//    ExPointLight
//    ExSpotLight
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//

import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class ExAmbientLight
	extends Example
{
	//--------------------------------------------------------------
	//  SCENE CONTENT
	//--------------------------------------------------------------

	//
	//  Nodes (updated via menu)
	//
	private AmbientLight light = null;

	//
	//  Build scene
	//
	public Group buildScene( )
	{
		// Get the current color
		Color3f color = (Color3f)colors[currentColor].value;

		// Turn off the example headlight
		setHeadlightEnable( false );

		// Build the scene group
		Group scene = new Group( );


	// BEGIN EXAMPLE TOPIC
		// Create influencing bounds
		BoundingSphere worldBounds = new BoundingSphere(
			new Point3d( 0.0, 0.0, 0.0 ),  // Center
			1000.0 );                      // Extent

		// Set the light color and its influencing bounds
		light = new AmbientLight( );
		light.setEnable( lightOnOff );
		light.setColor( color );
		light.setCapability( AmbientLight.ALLOW_STATE_WRITE );
		light.setCapability( AmbientLight.ALLOW_COLOR_WRITE );
		light.setInfluencingBounds( worldBounds );
		scene.addChild( light );
	// END EXAMPLE TOPIC


		// Build foreground geometry
		scene.addChild( new SphereGroup( ) );

		return scene;
	}



	//--------------------------------------------------------------
	//  USER INTERFACE
	//--------------------------------------------------------------

	//
	//  Main
	//
	public static void main( String[] args )
	{
		ExAmbientLight ex = new ExAmbientLight( );
		ex.initialize( args );
		ex.buildUniverse( );
		ex.showFrame( );
	}


	//  On/off choices
	private boolean lightOnOff = true;
	private CheckboxMenuItem lightOnOffMenu;

	//  Color menu choices
	private NameValue[] colors = {
		new NameValue( "White",    White ),
		new NameValue( "Gray",     Gray ),
		new NameValue( "Black",    Black ),
		new NameValue( "Red",      Red ),
		new NameValue( "Yellow",   Yellow ),
		new NameValue( "Green",    Green ),
		new NameValue( "Cyan",     Cyan ),
		new NameValue( "Blue",     Blue ),
		new NameValue( "Magenta",  Magenta ),
	};
	private int currentColor = 0;
	private CheckboxMenu colorMenu = null;


	//
	//  Initialize the GUI (application and applet)
	//
	public void initialize( String[] args )
	{
		// Initialize the window, menubar, etc.
		super.initialize( args );
		exampleFrame.setTitle( "Java 3D Ambient Light Example" );


		//
		//  Add a menubar menu to change node parameters
		//    Light on/off
		//    Color -->
		//

		Menu m = new Menu( "AmbientLight" );

		lightOnOffMenu = new CheckboxMenuItem( "Light on/off",
			lightOnOff );
		lightOnOffMenu.addItemListener( this );
		m.add( lightOnOffMenu );

		colorMenu = new CheckboxMenu( "Color", colors,
			currentColor, this );
		m.add( colorMenu );

		exampleMenuBar.add( m );
	}


	//
	//  Handle checkboxes and menu choices
	//
	public void checkboxChanged( CheckboxMenu menu, int check )
	{
		if ( menu == colorMenu )
		{
			// Change the light color
			currentColor = check;
			Color3f color = (Color3f)colors[check].value;
			light.setColor( color );
			return;
		}

		// Handle all other checkboxes
		super.checkboxChanged( menu, check );
	}

	public void itemStateChanged( ItemEvent event )
	{
		Object src = event.getSource( );
		if ( src == lightOnOffMenu )
		{
			// Turn the light on or off
			lightOnOff = lightOnOffMenu.getState( );
			light.setEnable( lightOnOff );
			return;
		}

		// Handle all other checkboxes
		super.itemStateChanged( event );
	}
}

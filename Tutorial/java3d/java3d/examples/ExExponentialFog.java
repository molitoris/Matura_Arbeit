//
//  CLASS
//    ExExponentialFog  -  illustrate use of exponential fog
//
//  LESSON
//    Add an ExponentialFog node to apply fog of varying thickness.
//
//  SEE ALSO
//    ExLinearFog
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//

import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.*;

public class ExExponentialFog
	extends Example
{
	//--------------------------------------------------------------
	//  SCENE CONTENT
	//--------------------------------------------------------------

	//
	//  Nodes (updated via menu)
	//
	private ExponentialFog fog = null;
	private Background background = null;

	//
	//  Build scene
	//
	public Group buildScene( )
	{
		// Get the current color
		Color3f color = (Color3f)colors[currentColor].value;
		float density = ((Float)densities[currentDensity].value).floatValue( );

		// Turn off the example headlight
		setHeadlightEnable( false );

		// Default to walk navigation
		setNavigationType( Walk );

		// Create the scene group
		Group scene = new Group( );


	// BEGIN EXAMPLE TOPIC
		// Create influencing bounds
		BoundingSphere worldBounds = new BoundingSphere(
			new Point3d( 0.0, 0.0, 0.0 ),  // Center
			1000.0 );                      // Extent

		// Set the fog color, density, and its influencing bounds
		fog = new ExponentialFog( );
		fog.setColor( color );
		fog.setDensity( density );
		fog.setCapability( Fog.ALLOW_COLOR_WRITE );
		fog.setCapability( ExponentialFog.ALLOW_DENSITY_WRITE );
		fog.setInfluencingBounds( worldBounds );
		scene.addChild( fog );
	// END EXAMPLE TOPIC


		// Set the background color and its application bounds
		//   Usually, the background color should match the fog color
		//   or the results look odd.
		background = new Background( );
		background.setColor( color );
		background.setApplicationBounds( worldBounds );
		background.setCapability( Background.ALLOW_COLOR_WRITE );
		scene.addChild( background );


		// Build foreground geometry
		scene.addChild( new ColumnScene( this ) );

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
		ExExponentialFog ex = new ExExponentialFog( );
		ex.initialize( args );
		ex.buildUniverse( );
		ex.showFrame( );
	}


	//  Coupled background and fog color
	private boolean coupledBackgroundOnOff = true;
	private CheckboxMenuItem coupledBackgroundOnOffMenu = null;

	//  Color menu choices
	private NameValue[] colors = {
		new NameValue( "White",       White ),
		new NameValue( "Gray",        Gray ),
		new NameValue( "Dark Gray",   DarkGray ),
		new NameValue( "Black",       Black ),
		new NameValue( "Red",         Red ),
		new NameValue( "Dark Red",    DarkRed ),
		new NameValue( "Green",       Green ),
		new NameValue( "Dark Green",  DarkGreen ),
		new NameValue( "Blue",        Blue ),
		new NameValue( "Dark Blue",   DarkBlue ),
	};
	private int currentColor = 0;
	private int currentBackgroundColor = currentColor;
	private CheckboxMenu colorMenu = null;
	private CheckboxMenu backgroundColorMenu = null;

	//  Density menu choices
	private NameValue[] densities = {
		new NameValue( "No fog",          new Float( 0.0f ) ),
		new NameValue( "Haze (0.5)",      new Float( 0.5f ) ),
		new NameValue( "Light fog (1.0)", new Float( 1.0f ) ),
		new NameValue( "Heavy fog (2.0)", new Float( 2.0f ) ),
	};
	private int currentDensity = 0;
	private CheckboxMenu densityMenu = null;


	//
	//  Initialize the GUI (application and applet)
	//
	public void initialize( String[] args )
	{
		// Initialize the window, menubar, etc.
		super.initialize( args );
		exampleFrame.setTitle( "Java 3D ExponentialFog Example" );


		//
		//  Add a menubar menu to change node parameters
		//    Coupled background color
		//    Background color -->
		//    Fog color -->
		//    Fog density -->
		//

		Menu m = new Menu( "ExponentialFog" );

		coupledBackgroundOnOffMenu = new CheckboxMenuItem(
			"Couple background color", coupledBackgroundOnOff );
		coupledBackgroundOnOffMenu.addItemListener( this );
		m.add( coupledBackgroundOnOffMenu );

		backgroundColorMenu = new CheckboxMenu( "Background color", colors,
			currentBackgroundColor, this );
		m.add( backgroundColorMenu );
		backgroundColorMenu.setEnabled( !coupledBackgroundOnOff );

		colorMenu = new CheckboxMenu( "Fog color", colors,
			currentColor, this );
		m.add( colorMenu );

		densityMenu = new CheckboxMenu( "Fog density", densities,
			currentDensity, this );
		m.add( densityMenu );

		exampleMenuBar.add( m );
	}


	//
	//  Handle checkboxes and menu choices
	//
	public void checkboxChanged( CheckboxMenu menu, int check )
	{
		if ( menu == backgroundColorMenu )
		{
			// Change the background color
			currentBackgroundColor = check;
			Color3f color = (Color3f)colors[check].value;
			background.setColor( color );
			return;
		}
		if ( menu == colorMenu )
		{
			// Change the fog color
			currentColor = check;
			Color3f color = (Color3f)colors[check].value;
			fog.setColor( color );

			// If background is coupled, set the background color
			if ( coupledBackgroundOnOff )
			{
				currentBackgroundColor = currentColor;
				backgroundColorMenu.setCurrent( check );
				background.setColor( color );
			}
			return;
		}
		if ( menu == densityMenu )
		{
			// Change the fog density
			currentDensity = check;
			float density = ((Float)densities[currentDensity].value).floatValue( );
			fog.setDensity( density );
			return;
		}

		// Handle all other checkboxes
		super.checkboxChanged( menu, check );
	}

	public void itemStateChanged( ItemEvent event )
	{
		Object src = event.getSource( );

		// Check if it is the coupled background choice
		if ( src == coupledBackgroundOnOffMenu )
		{
			coupledBackgroundOnOff = coupledBackgroundOnOffMenu.getState( );
			if ( coupledBackgroundOnOff )
			{
				currentBackgroundColor = currentColor;
				backgroundColorMenu.setCurrent( currentColor );
				Color3f color = (Color3f)colors[currentColor].value;
				background.setColor( color );
				backgroundColorMenu.setEnabled( false );
			}
			else
			{
				backgroundColorMenu.setEnabled( true );
			}
		}

		// Handle all other checkboxes
		super.itemStateChanged( event );
	}
}

//
//  CLASS
//    ExLinearFog  -  illustrate use of linear fog
//
//  LESSON
//    Add an LinearFog node to apply fog of varying thickness.
//
//  SEE ALSO
//    ExExponentialFog
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//

import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.*;

public class ExLinearFog
	extends Example
{
	//--------------------------------------------------------------
	//  SCENE CONTENT
	//--------------------------------------------------------------

	//
	//  Nodes (updated via menu)
	//
	private LinearFog fog = null;
	private Background background = null;

	//
	//  Build scene
	//
	public Group buildScene( )
	{
		// Get the current color
		Color3f color = (Color3f)colors[currentColor].value;
		float front   = ((Float)fronts[currentFront].value).floatValue( );
		float back    = ((Float)backs[currentBack].value).floatValue( );

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

		// Set the fog color, front & back distances, and
		// its influencing bounds
		fog = new LinearFog( );
		fog.setColor( color );
		fog.setFrontDistance( front );
		fog.setBackDistance( back );
		fog.setCapability( Fog.ALLOW_COLOR_WRITE );
		fog.setCapability( LinearFog.ALLOW_DISTANCE_WRITE );
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
		ExLinearFog ex = new ExLinearFog( );
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
	private CheckboxMenu colorMenu;
	private CheckboxMenu backgroundColorMenu;

	//  Front distance menu choices
	private NameValue[] fronts = {
		new NameValue( "At front (0.0)",      new Float( 0.0f ) ),
		new NameValue( "Near distance (2.0)", new Float( 2.0f ) ),
		new NameValue( "Mid distance (12.0)", new Float( 12.0f ) ),
		new NameValue( "Far distance (22.0)", new Float( 22.0f ) ),
	};
	private int currentFront = 0;
	private CheckboxMenu frontMenu;

	//  Back distance menu choices
	private NameValue[] backs = {
		new NameValue( "At front (0.0)",      new Float( 0.0f ) ),
		new NameValue( "Near distance (2.0)", new Float( 2.0f ) ),
		new NameValue( "Mid distance (12.0)", new Float( 12.0f ) ),
		new NameValue( "Far distance (22.0)", new Float( 22.0f ) ),
	};
	private int currentBack = 3;
	private CheckboxMenu backMenu;


	//
	//  Initialize the GUI (application and applet)
	//
	public void initialize( String[] args )
	{
		// Initialize the window, menubar, etc.
		super.initialize( args );
		exampleFrame.setTitle( "Java 3D LinearFog Example" );


		//
		//  Add a menubar menu to change node parameters
		//    Coupled background color
		//    Background color -->
		//    Fog color -->
		//    Fog front distance -->
		//    Fog back distance -->
		//

		Menu m = new Menu( "LinearFog" );

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

		frontMenu = new CheckboxMenu( "Fog front distance", fronts,
			currentFront, this );
		m.add( frontMenu );

		backMenu = new CheckboxMenu( "Fog back distance", backs,
			currentBack, this );
		m.add( backMenu );

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
		if ( menu == frontMenu )
		{
			// Change the fog front distance
			currentFront = check;
			float front = ((Float)fronts[currentFront].value).floatValue( );
			fog.setFrontDistance( front );
			return;
		}
		if ( menu == backMenu )
		{
			// Change the fog back distance
			currentBack = check;
			float back = ((Float)backs[currentBack].value).floatValue( );
			fog.setBackDistance( back );
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

//
//  CLASS
//    ExDirectionalLight  -  illustrate use of directional lights
//
//  LESSON
//    Add a DirectionalLight node to illuminate a scene.
//
//  SEE ALSO
//    ExAmbientLight
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

public class ExDirectionalLight
	extends Example
{
	//--------------------------------------------------------------
	//  SCENE CONTENT
	//--------------------------------------------------------------

	//
	//  Nodes (updated via menu)
	//
	private DirectionalLight light = null;

	//
	//  Build scene
	//
	public Group buildScene( )
	{
		// Get the current color and direction
		Color3f color = (Color3f)colors[currentColor].value;
		Vector3f dir  = (Vector3f)directions[currentDirection].value;

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
		light = new DirectionalLight( );
		light.setEnable( lightOnOff );
		light.setColor( color );
		light.setDirection( dir );
		light.setCapability( DirectionalLight.ALLOW_STATE_WRITE );
		light.setCapability( DirectionalLight.ALLOW_COLOR_WRITE );
		light.setCapability( DirectionalLight.ALLOW_DIRECTION_WRITE);
		light.setInfluencingBounds( worldBounds );
		scene.addChild( light );
	// END EXAMPLE TOPIC


		// Build foreground geometry
		scene.addChild( new SphereGroup( ) );


		// Add anotation arrows pointing in +-X, +-Y, +-Z to
		// illustrate aim direction
		scene.addChild( buildArrows( ) );

		return scene;
	}



	//--------------------------------------------------------------
	//  FOREGROUND AND ANNOTATION CONTENT
	//--------------------------------------------------------------

	//
	//  Create a set of annotation arrows initially pointing in
	//  the +X direciton.  Next, build an array of Transform3D's,
	//  one for each of the aim directions shown on the directions
	//  menu.  Save these Transform3Ds and a top-level TransformGroup
	//  surrounding the arrows.  Later, when the user selects a new
	//  light direction, we poke the corresponding Transform3D into
	//  the TransformGroup to cause the arrows to change direction.
	//
	private TransformGroup arrowDirectionTransformGroup = null;
	private Transform3D[]  arrowDirectionTransforms     = null;

	private Group buildArrows( )
	{
		// Create a transform group surrounding the arrows.
		// Enable writing of its transform.
		arrowDirectionTransformGroup = new TransformGroup( );
		arrowDirectionTransformGroup.setCapability(
			TransformGroup.ALLOW_TRANSFORM_WRITE );


		// Create a group of arrows and add the group to the
		// transform group.  The arrows point in the +X direction.
		AnnotationArrowGroup ag = new AnnotationArrowGroup(
			-2.0f, 2.0f,    // X start and end
			1.5f, -1.5f,    // Y start and end
			5 );            // Number of arrows
		arrowDirectionTransformGroup.addChild( ag );


		// Create a set of Transform3Ds for the different
		// arrow directions.
		arrowDirectionTransforms =
			new Transform3D[directions.length];
		Vector3f dir = new Vector3f( );
		Vector3f positiveX = new Vector3f( 1.0f, 0.0f, 0.0f );
		Vector3f axis = new Vector3f( );
		float angle;
		float dot;

		for ( int i = 0; i < directions.length; i++ )
		{
			// Normalize the direction vector
			dir.normalize( (Vector3f)directions[i].value );

			// Cross the direction vector with the arrow's
			// +X aim direction to get a vector orthogonal
			// to both.  This is the rotation axis.
			axis.cross( positiveX, dir );
			if ( axis.x == 0.0f && axis.y == 0.0f && axis.z == 0.0f )
			{
				// New direction is parallel to current
				// arrow direction.  Default to a Y axis.
				axis.y = 1.0f;
			}

			// Compute the angle between the direction and +X
			// vectors, where:
			//
			//   cos(angle) = (dir dot positiveX)
			//                -------------------------------
			//                (positiveX.length * dir.length)
			//
			// but since positiveX is normalized (as created
			// above and dir has been normalized, both have a
			// length of 1.  So, the angle between the
			// vectors is:
			//
			//   angle = arccos(dir dot positiveX)
			//
			dot = dir.dot( positiveX );
			angle = (float)Math.acos( dot );

			// Create a Transform3D, setting its rotation using
			// an AxisAngle4f, which takes an XYZ rotation vector
			// and an angle to rotate by around that vector.
			arrowDirectionTransforms[i] = new Transform3D( );
			arrowDirectionTransforms[i].setRotation(
				new AxisAngle4f( axis.x, axis.y, axis.z,
					angle ) );
		}

		// Set the initial transform to be the current aim direction.
		arrowDirectionTransformGroup.setTransform(
			arrowDirectionTransforms[currentDirection] );

		return arrowDirectionTransformGroup;
	}



	//--------------------------------------------------------------
	//  USER INTERFACE
	//--------------------------------------------------------------

	//
	//  Main
	//
	public static void main( String[] args )
	{
		ExDirectionalLight ex = new ExDirectionalLight( );
		ex.initialize( args );
		ex.buildUniverse( );
		ex.showFrame( );
	}


	//  On/off choices
	private boolean lightOnOff = true;
	private CheckboxMenuItem lightOnOffMenu = null;

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

	//  Direction menu choices
	private NameValue[] directions = {
		new NameValue( "Positive X",    PosX ),
		new NameValue( "Negative X",    NegX ),
		new NameValue( "Positive Y",    PosY ),
		new NameValue( "Negative Y",    NegY ),
		new NameValue( "Positive Z",    PosZ ),
		new NameValue( "Negative Z",    NegZ ),
	};
	private int currentDirection = 0;
	private CheckboxMenu directionMenu = null;


	//
	//  Initialize the GUI (application and applet)
	//
	public void initialize( String[] args )
	{
		// Initialize the window, menubar, etc.
		super.initialize( args );
		exampleFrame.setTitle( "Java 3D Directional Light Example" );


		//
		//  Add a menubar menu to change node parameters
		//    Light on/off
		//    Color -->
		//    Direction -->
		//

		Menu m = new Menu( "DirectionalLight" );

		lightOnOffMenu = new CheckboxMenuItem( "Light on/off",
			lightOnOff );
		lightOnOffMenu.addItemListener( this );
		m.add( lightOnOffMenu );

		colorMenu = new CheckboxMenu( "Color", colors,
			currentColor, this );
		m.add( colorMenu );

		directionMenu = new CheckboxMenu( "Direction", directions,
			currentDirection, this );
		m.add( directionMenu );

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
		if ( menu == directionMenu )
		{
			// Change the light direction
			currentDirection = check;
			Vector3f dir = (Vector3f)directions[check].value;
			light.setDirection( dir );

			// Change the arrow group direction
			arrowDirectionTransformGroup.setTransform(
				arrowDirectionTransforms[check] );
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

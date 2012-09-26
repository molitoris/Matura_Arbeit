//
//  CLASS
//    ExLightBounds  -  illustrate use of light influencing bounds, and
//                      bounding leaves
//
//  LESSON
//    Add a DirectionalLight node to illuminate a scene, then adjust
//    its influencing bounds
//
//  SEE ALSO
//    ExAmbientLight
//    ExPointLight
//    ExSpotLight
//    ExLightScope
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//

import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class ExLightBounds
	extends Example
{
	//--------------------------------------------------------------
	//  SCENE CONTENT
	//--------------------------------------------------------------

	//
	//  Nodes (updated via menu)
	//
	private DirectionalLight light = null;
	private Bounds worldBounds = new BoundingSphere(
		new Point3d( 0.0, 0.0, 0.0 ),  // Center
		1000.0 );                      // Extent
	private Bounds smallBounds = new BoundingSphere(
		new Point3d( 0.0, 0.0, 0.0 ),  // Center
		1.0 );                         // Extent
	private Bounds tinyBounds = new BoundingSphere(
		new Point3d( 0.0, 0.0, 0.0 ),  // Center
		0.2 );                         // Extent
	private BoundingLeaf leafBounds = null;
	private TransformGroup leafTransformGroup = null;

	//
	//  Build scene
	//
	public Group buildScene( )
	{
		// Get the current bounding leaf position
		Point3f pos = (Point3f)positions[currentPosition].value;

		// Turn off the example headlight
		setHeadlightEnable( false );

		// Build the scene group
		Group scene = new Group( );

	// BEGIN EXAMPLE TOPIC
		// Create a bounding leaf we'll use or not use depending
		// upon menu selections.  Put it within a transform group
		// so that we can move the leaf about.
		leafTransformGroup = new TransformGroup( );
		leafTransformGroup.setCapability(
			TransformGroup.ALLOW_TRANSFORM_WRITE );
		Transform3D tr = new Transform3D( );
		tr.setTranslation( new Vector3f( pos ) );
		leafTransformGroup.setTransform( tr );

		leafBounds = new BoundingLeaf( worldBounds );
		leafBounds.setCapability( BoundingLeaf.ALLOW_REGION_WRITE );
		leafTransformGroup.addChild( leafBounds );
		scene.addChild( leafTransformGroup );

		// Add a directional light whose bounds we'll modify
		// Set its color and aim direction
		light = new DirectionalLight( );
		light.setEnable( true );
		light.setColor( White );
		light.setDirection( new Vector3f( 1.0f, 0.0f, -1.0f ) );
		light.setCapability(
			DirectionalLight.ALLOW_INFLUENCING_BOUNDS_WRITE );

		// Set the bounds to be either from the leaf or from
		// explicit bounds, depending upon the menu initial state
		if ( boundingLeafOnOff )
			// Use bounding leaf
			light.setInfluencingBoundingLeaf( leafBounds );
		else
			// Use bounds on the light
			light.setInfluencingBounds( worldBounds );

		// Set the scope list to include nothing initially.
		// This defaults to "universal scope" which covers
		// everything.

		scene.addChild( light );


		// Add an ambient light to dimly illuminate the rest of
		// the shapes in the scene to help illustrate that the
		// directional light is being bounded... otherwise it looks
		// like we're just removing shapes from the scene
		AmbientLight ambient = new AmbientLight( );
		ambient.setEnable( true );
		ambient.setColor( White );
		ambient.setInfluencingBounds( worldBounds );
		scene.addChild( ambient );
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
		ExLightBounds ex = new ExLightBounds( );
		ex.initialize( args );
		ex.buildUniverse( );
		ex.showFrame( );
	}


	//  Bounds mode On/off choices
	private boolean boundingLeafOnOff = true;
	private CheckboxMenuItem boundingLeafOnOffMenu = null;

	//  Bounds menu choices
	private NameValue[] bounds = {
		new NameValue( "Tiny bounds",    tinyBounds ),
		new NameValue( "Small bounds", smallBounds ),
		new NameValue( "Big bounds",   worldBounds ),
	};
	private int currentBounds = 2;
	private CheckboxMenu boundsMenu = null;

	//  Position menu choices
	private NameValue[] positions = {
		new NameValue( "Origin", Origin ),
		new NameValue( "+X",     PlusX ),
		new NameValue( "-X",     MinusX ),
		new NameValue( "+Y",     PlusY ),
		new NameValue( "-Y",     MinusY ),
		new NameValue( "+Z",     PlusZ ),
		new NameValue( "-Z",     MinusZ ),
	};
	private int currentPosition = 0;
	private CheckboxMenu positionMenu = null;



	//
	//  Initialize the GUI (application and applet)
	//
	public void initialize( String[] args )
	{
		// Initialize the window, menubar, etc.
		super.initialize( args );
		exampleFrame.setTitle( "Java 3D Light Bounds Example" );


		//
		//  Add a menubar menu to change node parameters
		//    Use bounding leaf
		//    Bounds size -->
		//    Bounding leaf position -->
		//

		Menu m = new Menu( "DirectionalLight" );

		boundingLeafOnOffMenu = new CheckboxMenuItem( "Use bounding leaf",
			boundingLeafOnOff );
		boundingLeafOnOffMenu.addItemListener( this );
		m.add( boundingLeafOnOffMenu );

		boundsMenu = new CheckboxMenu( "Bounds size", bounds,
			currentBounds, this );
		m.add( boundsMenu );

		positionMenu = new CheckboxMenu( "Bounding leaf position", positions,
			currentPosition, this );
		if ( boundingLeafOnOff )
			// Bounding leaf on
			positionMenu.setEnabled( true );
		else
			// Bounding leaf off
			positionMenu.setEnabled( false );
		m.add( positionMenu );

		exampleMenuBar.add( m );
	}


	//
	//  Handle checkboxes and menu choices
	//
	public void checkboxChanged( CheckboxMenu menu, int check )
	{
		if ( menu == boundsMenu )
		{
			// Change the light bounds
			currentBounds = check;
			Bounds bou = (Bounds)bounds[check].value;
			if ( boundingLeafOnOff )
			{
				// Change the bounding leaf's bounds
				leafBounds.setRegion( bou );

				// Kick the light to get it to update
				// its bounds now that the leaf has
				// changed... (only necessary in the
				// Alpha release of Java3D)
				light.setInfluencingBoundingLeaf( leafBounds );
			}
			else
			{
				// Change the light's own bounds
				light.setInfluencingBounds( bou );
			}
			return;
		}
		if ( menu == positionMenu )
		{
			// Change the bounding leaf position
			currentPosition = check;
			Point3f pos = (Point3f)positions[check].value;
			Transform3D tr = new Transform3D( );
			tr.setTranslation( new Vector3f( pos ) );
			leafTransformGroup.setTransform( tr );

			// Kick the light to get it to update
			// its bounds now that the leaf has
			// changed... (only necessary in the
			// Alpha release of Java3D)
			light.setInfluencingBoundingLeaf( leafBounds );

			return;
		}

		// Handle all other checkboxes
		super.checkboxChanged( menu, check );
	}

	public void itemStateChanged( ItemEvent event )
	{
		Object src = event.getSource( );
		if ( src == boundingLeafOnOffMenu )
		{
			boundingLeafOnOff = boundingLeafOnOffMenu.getState( );
			if ( boundingLeafOnOff )
			{
				// Use the bounding leaf
				light.setInfluencingBoundingLeaf( leafBounds );

				// A bounding leaf overrides bounds,
				// but for neatness we can turn them off
				// (doesn't work in Alpha release of Java3D)
				light.setInfluencingBounds( null );
				positionMenu.setEnabled( true );
			}
			else
			{
				// Use bounds on the light itself
				Bounds bou = (Bounds)bounds[currentBounds].value;
				light.setInfluencingBoundingLeaf( null );
				light.setInfluencingBounds( bou );
				positionMenu.setEnabled( false );
			}
			return;
		}

		// Handle all other checkboxes
		super.itemStateChanged( event );
	}
}

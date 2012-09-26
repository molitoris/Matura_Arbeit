//
//  CLASS
//    ExLightScope  -  illustrate use of light scope groups
//
//  LESSON
//    Add three DirectionalLight nodes to illuminate a scene, selecting
//    one of them to use at a time.  Each light has a different scope
//    group list, and thus illuminates different parts of the scene.
//
//  SEE ALSO
//    ExAmbientLight
//    ExPointLight
//    ExSpotLight
//    ExLightBounds
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//

import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class ExLightScope
	extends Example
{
	//--------------------------------------------------------------
	//  SCENE CONTENT
	//--------------------------------------------------------------

	//
	//  Nodes (updated via menu)
	//
	private DirectionalLight light1 = null;
	private DirectionalLight light2 = null;
	private DirectionalLight light3 = null;

	private Group content1 = null;
	private Group content2 = null;


	//
	//  Build scene
	//
	public Group buildScene( )
	{
		// Turn off the example headlight
		setHeadlightEnable( false );

		// Build the scene group
		Group scene = new Group( );

		// Build foreground geometry into two groups.  We'll
		// create three directional lights below, one each with
		// scope to cover the first geometry group only, the
		// second geometry group only, or both geometry groups.
		content1 = new SphereGroup(
			0.25f,   // radius of spheres
			1.5f,    // x spacing
			0.75f,   // y spacing
			3,       // number of spheres in X
			5,       // number of spheres in Y
			null );  // appearance
		scene.addChild( content1 );

		content2 = new SphereGroup(
			0.25f,   // radius of spheres
			1.5f,    // x spacing
			0.75f,   // y spacing
			2,       // number of spheres in X
			5,       // number of spheres in Y
			null );  // appearance
		scene.addChild( content2 );


	// BEGIN EXAMPLE TOPIC
		// Create influencing bounds
		BoundingSphere worldBounds = new BoundingSphere(
			new Point3d( 0.0, 0.0, 0.0 ),  // Center
			1000.0 );                      // Extent

		// Add three directional lights whose scopes are set
		// to cover one, the other, or both of the shape groups
		// above.  Also set the lights' color and aim direction.

		// Light #1 with content1 scope
		light1 = new DirectionalLight( );
		light1.setEnable( light1OnOff );
		light1.setColor( Red );
		light1.setDirection( new Vector3f( 1.0f, 0.0f, -1.0f ) );
		light1.setInfluencingBounds( worldBounds );
		light1.addScope( content1 );
		light1.setCapability( Light.ALLOW_STATE_WRITE );
		scene.addChild( light1 );

		// Light #2 with content2 scope
		light2 = new DirectionalLight( );
		light2.setEnable( light2OnOff );
		light2.setColor( Blue );
		light2.setDirection( new Vector3f( 1.0f, 0.0f, -1.0f ) );
		light2.setInfluencingBounds( worldBounds );
		light2.addScope( content2 );
		light2.setCapability( Light.ALLOW_STATE_WRITE );
		scene.addChild( light2 );

		// Light #3 with universal scope (the default)
		light3 = new DirectionalLight( );
		light3.setEnable( light3OnOff );
		light3.setColor( White );
		light3.setDirection( new Vector3f( 1.0f, 0.0f, -1.0f ) );
		light3.setInfluencingBounds( worldBounds );
		light3.setCapability( Light.ALLOW_STATE_WRITE );
		scene.addChild( light3 );


		// Add an ambient light to dimly illuminate the rest of
		// the shapes in the scene to help illustrate that the
		// directional lights are being scoped... otherwise it looks
		// like we're just removing shapes from the scene
		AmbientLight ambient = new AmbientLight( );
		ambient.setEnable( true );
		ambient.setColor( White );
		ambient.setInfluencingBounds( worldBounds );
		scene.addChild( ambient );
	// END EXAMPLE TOPIC

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
		ExLightScope ex = new ExLightScope( );
		ex.initialize( args );
		ex.buildUniverse( );
		ex.showFrame( );
	}


	//  Menu choices
	private boolean light1OnOff = true;
	private CheckboxMenuItem light1OnOffMenu;

	private boolean light2OnOff = true;
	private CheckboxMenuItem light2OnOffMenu;

	private boolean light3OnOff = false;
	private CheckboxMenuItem light3OnOffMenu;



	//
	//  Initialize the GUI (application and applet)
	//
	public void initialize( String[] args )
	{
		// Initialize the window, menubar, etc.
		super.initialize( args );
		exampleFrame.setTitle( "Java 3D Light Scoping Example" );


		//
		//  Add a menubar menu to change node parameters
		//    Use bounding leaf
		//

		Menu m = new Menu( "DirectionalLights" );

		light1OnOffMenu =
			new CheckboxMenuItem( "Red light with sphere set 1 scope",
			light1OnOff );
		light1OnOffMenu.addItemListener( this );
		m.add( light1OnOffMenu );

		light2OnOffMenu =
			new CheckboxMenuItem( "Blue light with sphere set 2 scope",
			light2OnOff );
		light2OnOffMenu.addItemListener( this );
		m.add( light2OnOffMenu );

		light3OnOffMenu =
			new CheckboxMenuItem( "White light with universal scope",
			light3OnOff );
		light3OnOffMenu.addItemListener( this );
		m.add( light3OnOffMenu );


		exampleMenuBar.add( m );
	}


	//
	//  Handle checkboxes and menu choices
	//
	public void itemStateChanged( ItemEvent event )
	{
		Object src = event.getSource( );
		if ( src == light1OnOffMenu )
		{
			light1OnOff = light1OnOffMenu.getState( );
			light1.setEnable( light1OnOff );
			return;
		}
		if ( src == light2OnOffMenu )
		{
			light2OnOff = light2OnOffMenu.getState( );
			light2.setEnable( light2OnOff );
			return;
		}
		if ( src == light3OnOffMenu )
		{
			light3OnOff = light3OnOffMenu.getState( );
			light3.setEnable( light3OnOff );
			return;
		}

		// Handle all other checkboxes
		super.itemStateChanged( event );
	}
}

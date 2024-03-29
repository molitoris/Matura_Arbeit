//
//  CLASS
//    ExBluePrint  -  illustrate use of background images
//
//  LESSON
//    Add a Background node to place a background image of a blueprint
//    behind foreground geometry of a mechanical part.
//
//  SEE ALSO
//    ExBackgroundImage
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.geometry.*;

public class ExBluePrint
	extends Example
{
	//--------------------------------------------------------------
	//  SCENE CONTENT
	//--------------------------------------------------------------

	//
	//  Nodes (updated via menu)
	//
	private Background background = null;
	private Switch shadingSwitch = null;

	//
	//  Build scene
	//
	public Group buildScene( )
	{
		// Get the current image
		ImageComponent2D image = imageComponents[currentImage];

		// Build the scene root
		Group scene = new Group( );


	// BEGIN EXAMPLE TOPIC
		// Create application bounds
		BoundingSphere worldBounds = new BoundingSphere(
			new Point3d( 0.0, 0.0, 0.0 ),  // Center
			1000.0 );                      // Extent

		// Set the background color and its application bounds
		background = new Background( );
		background.setColor( White );
		background.setImage( image );
		background.setCapability( Background.ALLOW_IMAGE_WRITE );
		background.setApplicationBounds( worldBounds );
		scene.addChild( background );
	// END EXAMPLE TOPIC


		// Build foreground geometry
		scene.addChild( buildGadget( ) );

		return scene;
	}



	//--------------------------------------------------------------
	//  FOREGROUND AND ANNOTATION CONTENT
	//--------------------------------------------------------------

	//
	//  Build a mechanical gadget including a few gears and a
	//  shaft going through them.
	//
	private Group buildGadget( )
	{
		if( debug ) System.err.println( "  gadget..." );
		//
		//  Create two appearances:
		//    wireframeApp:  draw as blue wireframe
		//    shadedApp:     draw as metalic shaded polygons
		//

		//  Wireframe:
		//    no Material - defaults to coloring attributes color
		//    polygons as lines, with backfaces
		//    thick lines
		Appearance wireframeApp = new Appearance( );

		ColoringAttributes wireframeCatt = new ColoringAttributes( );
		wireframeCatt.setColor( 0.0f, 0.2559f, 0.4213f );
		wireframeCatt.setShadeModel( ColoringAttributes.SHADE_FLAT );
		wireframeApp.setColoringAttributes( wireframeCatt );

		PolygonAttributes wireframePatt = new PolygonAttributes( );
		wireframePatt.setPolygonMode( PolygonAttributes.POLYGON_LINE );
		wireframePatt.setCullFace( PolygonAttributes.CULL_NONE );
		wireframeApp.setPolygonAttributes( wireframePatt );

		LineAttributes wireframeLatt = new LineAttributes( );
		wireframeLatt.setLineWidth( 2.0f );
		wireframeApp.setLineAttributes( wireframeLatt );


		//  Shaded:
		//    silver material
		Appearance shadedApp = new Appearance( );

		Material shadedMat = new Material( );
		shadedMat.setAmbientColor(  0.30f, 0.30f, 0.30f );
		shadedMat.setDiffuseColor(  0.30f, 0.30f, 0.50f );
		shadedMat.setSpecularColor( 0.60f, 0.60f, 0.80f );
		shadedMat.setShininess( 0.10f );
		shadedApp.setMaterial( shadedMat );

		ColoringAttributes shadedCatt = new ColoringAttributes( );
		shadedCatt.setShadeModel( ColoringAttributes.SHADE_GOURAUD );
		shadedApp.setColoringAttributes( shadedCatt );


		//
		//  Create a switch group to hold two versions of the
		//  shape:  one wireframe, and one shaded
		//
		Transform3D tr = new Transform3D( );
		tr.set( new Vector3f( -1.0f, 0.2f, 0.0f ) );
		TransformGroup gadget = new TransformGroup( tr );
		shadingSwitch = new Switch( );
		shadingSwitch.setCapability( Switch.ALLOW_SWITCH_WRITE );
		Group wireframe = new Group( );
		Group shaded = new Group( );
		shadingSwitch.addChild( wireframe );
		shadingSwitch.addChild( shaded );
		shadingSwitch.setWhichChild( 1 ); // shaded
		gadget.addChild( shadingSwitch );


		//
		//  Build a gear (wireframe and shaded)
		//
		tr = new Transform3D( );
		tr.rotY( Math.PI/2.0 );
		TransformGroup tg = new TransformGroup( tr );
		SpurGear gear = new SpurGearThinBody(
			24,           // tooth count
			1.6f,         // pitch circle radius
			0.3f,         // shaft radius
			0.08f,        // addendum
			0.05f,        // dedendum
			0.3f,         // gear thickness
			0.28f,        // tooth tip thickness
			wireframeApp );// appearance
		tg.addChild( gear );
		wireframe.addChild( tg );

		tg = new TransformGroup( tr );
		gear = new SpurGearThinBody(
			24,           // tooth count
			1.6f,         // pitch circle radius
			0.3f,         // shaft radius
			0.08f,        // addendum
			0.05f,        // dedendum
			0.3f,         // gear thickness
			0.28f,        // tooth tip thickness
			shadedApp );  // appearance
		tg.addChild( gear );
		shaded.addChild( tg );


		//
		//  Build another gear (wireframe and shaded)
		//
		tr.rotY( Math.PI/2.0 );
		Vector3f trans = new Vector3f( -0.5f, 0.0f, 0.0f );
		tr.setTranslation( trans );
		tg = new TransformGroup( tr );
		gear = new SpurGearThinBody(
			30,           // tooth count
			2.0f,         // pitch circle radius
			0.3f,         // shaft radius
			0.08f,        // addendum
			0.05f,        // dedendum
			0.3f,         // gear thickness
			0.28f,        // tooth tip thickness
			wireframeApp );// appearance
		tg.addChild( gear );
		wireframe.addChild( tg );

		tg = new TransformGroup( tr );
		gear = new SpurGearThinBody(
			30,           // tooth count
			2.0f,         // pitch circle radius
			0.3f,         // shaft radius
			0.08f,        // addendum
			0.05f,        // dedendum
			0.3f,         // gear thickness
			0.28f,        // tooth tip thickness
			shadedApp );  // appearance
		tg.addChild( gear );
		shaded.addChild( tg );


		//
		//  Build a cylindrical shaft (wireframe and shaded)
		//
		tr.rotZ( -Math.PI/2.0 );
		trans = new Vector3f( 1.0f, 0.0f, 0.0f );
		tr.setTranslation( trans );
		tg = new TransformGroup( tr );
		Cylinder cyl = new Cylinder(
			0.3f,         // radius
			4.0f,         // length
			Primitive.GENERATE_NORMALS,  // format
			16,           // radial resolution
			1,            // length-wise resolution
			wireframeApp );// appearance
		tg.addChild( cyl );
		wireframe.addChild( tg );

		tg = new TransformGroup( tr );
		cyl = new Cylinder(
			0.3f,         // radius
			4.0f,         // length
			Primitive.GENERATE_NORMALS,  // format
			16,           // radial resolution
			1,            // length-wise resolution
			shadedApp );  // appearance
		tg.addChild( cyl );
		shaded.addChild( tg );


		//
		//  Build shaft teeth (wireframe and shaded)
		//
		tr.rotY( Math.PI/2.0 );
		trans = new Vector3f( 2.05f, 0.0f, 0.0f );
		tr.setTranslation( trans );
		tg = new TransformGroup( tr );
		gear = new SpurGear(
			12,           // tooth count
			0.5f,         // pitch circle radius
			0.3f,         // shaft radius
			0.05f,        // addendum
			0.05f,        // dedendum
			1.5f,         // gear thickness
			0.8f,         // tooth tip thickness
			wireframeApp );// appearance
		tg.addChild( gear );
		wireframe.addChild( tg );

		tg = new TransformGroup( tr );
		gear = new SpurGear(
			12,           // tooth count
			0.5f,         // pitch circle radius
			0.3f,         // shaft radius
			0.05f,        // addendum
			0.05f,        // dedendum
			1.5f,         // gear thickness
			0.8f,         // tooth tip thickness
			shadedApp );  // appearance
		tg.addChild( gear );
		shaded.addChild( tg );

		return gadget;
	}



	//--------------------------------------------------------------
	//  USER INTERFACE
	//--------------------------------------------------------------

	//
	//  Main
	//
	public static void main( String[] args )
	{
		ExBluePrint ex = new ExBluePrint( );
		ex.initialize( args );
		ex.buildUniverse( );
		ex.showFrame( );
	}

	//  Image menu choices
	private NameValue[] images = {
		new NameValue( "None",       null ),
		new NameValue( "Blueprint",  "blueprint.jpg" ),
	};
	private int currentImage = 0;
	private ImageComponent2D[] imageComponents;
	private CheckboxMenuItem[] imageMenu;

	private int currentAppearance = 0;
	private CheckboxMenuItem[] appearanceMenu;


	//
	//  Initialize the GUI (application and applet)
	//
	public void initialize( String[] args )
	{
		// Initialize the window, menubar, etc.
		super.initialize( args );
		exampleFrame.setTitle( "Java 3D Blueprint Example" );

		//
		//  Add a menubar menu to change parameters
		//    (images)
		//    --------
		//    Wireframe
		//    Shaded
		//

		// Add a menu to select among background and shading options
		Menu m = new Menu( "Options" );

		imageMenu = new CheckboxMenuItem[images.length];
		for ( int i = 0; i < images.length; i++ )
		{
			imageMenu[i] =
				new CheckboxMenuItem( images[i].name );
			imageMenu[i].addItemListener( this );
			imageMenu[i].setState( false );
			m.add( imageMenu[i] );
		}
		imageMenu[currentImage].setState( true );

		m.addSeparator( );

		appearanceMenu = new CheckboxMenuItem[2];
		appearanceMenu[0] = new CheckboxMenuItem( "Wireframe" );
		appearanceMenu[0].addItemListener( this );
		appearanceMenu[0].setState( false );
		m.add( appearanceMenu[0] );

		appearanceMenu[1] = new CheckboxMenuItem( "Shaded" );
		appearanceMenu[1].addItemListener( this );
		appearanceMenu[1].setState( true );
		m.add( appearanceMenu[1] );

		exampleMenuBar.add( m );


		// Preload background images
		TextureLoader texLoader = null;
		imageComponents = new ImageComponent2D[images.length];
		String value = null;
		for ( int i = 0; i < images.length; i++ )
		{
			value = (String)images[i].value;
			if ( value == null )
			{
				imageComponents[i] = null;
				continue;
			}
			texLoader = new TextureLoader( value, this);
			imageComponents[i] = texLoader.getImage( );
		}
	}


	//
	//  Handle checkboxes
	//
	public void itemStateChanged( ItemEvent event )
	{
		Object src = event.getSource( );

		// Check if it is an image choice
		for ( int i = 0; i < imageMenu.length; i++ )
		{
			if ( src == imageMenu[i] )
			{
				// Update the checkboxes
				imageMenu[currentImage].setState( false );
				currentImage = i;
				imageMenu[currentImage].setState( true );

				// Set the background image
				ImageComponent2D image =
					imageComponents[currentImage];
				background.setImage( image );
				return;
			}
		}

		// Check if it is an appearance choice
		if ( src == appearanceMenu[0] )
		{
				appearanceMenu[1].setState( false );
				shadingSwitch.setWhichChild( 0 );
				return;
		}
		if ( src == appearanceMenu[1] )
		{
				appearanceMenu[0].setState( false );
				shadingSwitch.setWhichChild( 1 );
				return;
		}

		// Handle all other checkboxes
		super.itemStateChanged( event );
	}
}

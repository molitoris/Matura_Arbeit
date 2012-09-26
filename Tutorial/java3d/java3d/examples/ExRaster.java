//
//  CLASS
//    ExRaster	-  illustrate use of rasters
//
//  LESSON
//    Add Raster nodes to place image on the screen based upon
//    a transformed 3D coordinate
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//

import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.net.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;

public class ExRaster
	extends Example
{
	//--------------------------------------------------------------
	//  SCENE CONTENT
	//--------------------------------------------------------------

	//
	//  Build scene
	//
	public Group buildScene( )
	{
		// Turn on the headlight
		setHeadlightEnable( true );

		// Default to examine navigation
		setNavigationType( Examine );

		// Build the scene root
		Group scene = new Group();

		if ( debug ) System.err.println( "  rasters..." );

	// BEGIN EXAMPLE TOPIC
		// Create three raster geometry shapes, each with a
		// different annotation text image

		// Load the texture images
		TextureLoader texLoader = new TextureLoader( "one.jpg", this);
		ImageComponent2D oneImage = texLoader.getImage();
		if ( oneImage == null )
		{
			System.err.println( "Cannot load 'one.jpg'" );
		}

		texLoader = new TextureLoader( "two.jpg", this);
		ImageComponent2D twoImage = texLoader.getImage();
		if ( twoImage == null )
		{
			System.err.println( "Cannot load 'two.jpg'" );
		}

		texLoader = new TextureLoader( "three.jpg", this);
		ImageComponent2D threeImage = texLoader.getImage();
		if ( threeImage == null )
		{
			System.err.println( "Cannot load 'three.jpg'" );
		}

		// Create raster geometries and shapes
		Vector3f trans = new Vector3f( );
		Transform3D tr = new Transform3D( );
		TransformGroup tg;

		// Left
		Raster raster = new Raster( );
		raster.setPosition( new Point3f( -2.0f, 0.75f, 0.0f ) );
		raster.setType( Raster.RASTER_COLOR );
		raster.setOffset( 0, 0 );
		raster.setSize( 64, 32 );
		raster.setImage( oneImage );
		Shape3D sh = new Shape3D( raster, new Appearance( ) );
		scene.addChild( sh );

		// Middle-back
		raster = new Raster( );
		raster.setPosition( new Point3f( 0.0f, 0.75f, -2.0f ) );
		raster.setType( Raster.RASTER_COLOR );
		raster.setOffset( 0, 0 );
		raster.setSize( 64, 32 );
		raster.setImage( twoImage );
		sh = new Shape3D( raster, new Appearance( ) );
		scene.addChild( sh );

		// Right
		raster = new Raster( );
		raster.setPosition( new Point3f( 2.0f, 0.75f, 0.0f ) );
		raster.setType( Raster.RASTER_COLOR );
		raster.setOffset( 0, 0 );
		raster.setSize( 64, 32 );
		raster.setImage( threeImage );
		sh = new Shape3D( raster, new Appearance( ) );
		scene.addChild( sh );
	// END EXAMPLE TOPIC


		// Build foreground geometry including a floor and
		// cones on which the raster images sit
		if ( debug ) System.err.println( "  cones..." );
		Appearance app0 = new Appearance( );
		Material mat0 = new Material();
		mat0.setAmbientColor( 0.2f, 0.2f, 0.2f );
		mat0.setDiffuseColor( 1.0f, 0.0f, 0.0f );
		mat0.setSpecularColor( 0.7f, 0.7f, 0.7f );
		app0.setMaterial( mat0 );

		Transform3D t3d = new Transform3D( );
		t3d.setTranslation( new Vector3f( -2.0f, 0.0f, 0.0f ) );
		TransformGroup tg0 = new TransformGroup( t3d );
		Cone cone0 = new Cone(
			0.5f,    // radius
			1.5f,    // height
			Primitive.GENERATE_NORMALS, // flags
			16,      // x division
			16,      // y division
			app0 );  // appearance
		tg0.addChild( cone0 );
		scene.addChild( tg0 );

		Appearance app1 = new Appearance( );
		Material mat1 = new Material();
		mat1.setAmbientColor( 0.2f, 0.2f, 0.2f );
		mat1.setDiffuseColor( 0.0f, 1.0f, 0.0f );
		mat1.setSpecularColor( 0.7f, 0.7f, 0.7f );
		app1.setMaterial( mat1 );

		t3d = new Transform3D( );
		t3d.setTranslation( new Vector3f( 0.0f, 0.0f, -2.0f ) );
		TransformGroup tg1 = new TransformGroup( t3d );
		Cone cone1 = new Cone(
			0.5f,    // radius
			1.5f,    // height
			Primitive.GENERATE_NORMALS, // flags
			16,      // x division
			16,      // y division
			app1 );  // appearance
		tg1.addChild( cone1 );
		scene.addChild( tg1 );

		Appearance app2 = new Appearance( );
		Material mat2 = new Material();
		mat2.setAmbientColor( 0.2f, 0.2f, 0.2f );
		mat2.setDiffuseColor( 0.0f, 0.6f, 1.0f );
		mat2.setSpecularColor( 0.7f, 0.7f, 0.7f );
		app2.setMaterial( mat2 );

		t3d = new Transform3D( );
		t3d.setTranslation( new Vector3f( 2.0f, 0.0f, 0.0f ) );
		TransformGroup tg2 = new TransformGroup( t3d );
		Cone cone2 = new Cone(
			0.5f,    // radius
			1.5f,    // height
			Primitive.GENERATE_NORMALS, // flags
			16,      // x division
			16,      // y division
			app2 );  // appearance
		tg2.addChild( cone2 );
		scene.addChild( tg2 );

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
		ExRaster ex = new ExRaster( );
		ex.initialize( args );
		ex.buildUniverse( );
		ex.showFrame( );
	}


	//
	//  Initialize the GUI (application and applet)
	//
	public void initialize( String[] args )
	{
		// Initialize the window, menubar, etc.
		super.initialize( args );
		exampleFrame.setTitle( "Java 3D Raster Example" );
	}
}

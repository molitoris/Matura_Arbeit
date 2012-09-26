//
//  CLASS
//    ExSwitch	-  illustrate use of switches
//
//  LESSON
//    Add a Switch grouping node to select which of several shapes
//    to draw.
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//

import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.geometry.*;

public class ExSwitch
	extends Example
{
	//--------------------------------------------------------------
	//  SCENE CONTENT
	//--------------------------------------------------------------

	//
	//  Nodes (updated via menu)
	//
	private Switch swtch = null;
	private int currentSwitch = 0;

	//
	//  Build scene
	//
	public Group buildScene( )
	{
		// Turn on the example headlight
		setHeadlightEnable( true );

		// Default to walk navigation
		setNavigationType( Walk );

		// Build the scene group
		Group scene = new Group( );

		if ( debug ) System.err.println( "  switch shapes..." );

	// BEGIN EXAMPLE TOPIC
		// Build the switch group and allow its switch
		// value to be changed via menu items
		swtch = new Switch( );
		swtch.setCapability( Switch.ALLOW_SWITCH_WRITE );

		//  Create several shapes to place in a switch group

		// Child 0:  a red sphere
		Appearance app0 = new Appearance( );
		Material mat0 = new Material();
		mat0.setAmbientColor( 0.2f, 0.2f, 0.2f );
		mat0.setDiffuseColor( 1.0f, 0.0f, 0.2f );
		mat0.setSpecularColor( 0.7f, 0.7f, 0.7f );
		app0.setMaterial( mat0 );

		Transform3D t3d = new Transform3D( );
		t3d.setTranslation( new Vector3f( -2.0f, 1.5f, 0.0f ) );
		TransformGroup tg0 = new TransformGroup( t3d );
		Sphere sph0 = new Sphere(
			0.5f,     // radius
			Primitive.GENERATE_NORMALS,  // components
			16,       // facets
			app0 );   // appearance
		tg0.addChild( sph0 );
		swtch.addChild( tg0 );  // Child 0

		// Child 1:  a green sphere
		Appearance app1 = new Appearance( );
		Material mat1 = new Material();
		mat1.setAmbientColor( 0.2f, 0.2f, 0.2f );
		mat1.setDiffuseColor( 0.0f, 1.0f, 0.0f );
		mat1.setSpecularColor( 0.7f, 0.7f, 0.7f );
		app1.setMaterial( mat1 );
		t3d.setTranslation( new Vector3f( 0.0f, 1.5f, 0.0f ) );
		TransformGroup tg1 = new TransformGroup( t3d );
		Sphere sph1 = new Sphere(
			0.5f,     // radius
			Primitive.GENERATE_NORMALS,  // components
			16,       // facets
			app1 );   // appearance
		tg1.addChild( sph1 );
		swtch.addChild( tg1 );  // Child 1

		// Child 2:  a blue sphere
		Appearance app2 = new Appearance( );
		Material mat2 = new Material();
		mat2.setAmbientColor( 0.2f, 0.2f, 0.2f );
		mat2.setDiffuseColor( 0.0f, 0.6f, 1.0f );
		mat2.setSpecularColor( 0.7f, 0.7f, 0.7f );
		app2.setMaterial( mat2 );
		t3d.setTranslation( new Vector3f( 2.0f, 1.5f, 0.0f ) );
		TransformGroup tg2 = new TransformGroup( t3d );
		Sphere sph2 = new Sphere(
			0.5f,     // radius
			Primitive.GENERATE_NORMALS,  // components
			16,       // facets
			app2 );   // appearance
		tg2.addChild( sph2 );
		swtch.addChild( tg2 );

		// Set the initial child choice
		swtch.setWhichChild( options[currentSwitch].child );
		scene.addChild( swtch );
	// END EXAMPLE TOPIC


		// Build foreground geometry including a floor and
		// columns on which the switchable shapes stand

		// Load textures
		TextureLoader texLoader = new TextureLoader( "granite07rev.jpg", this );
		Texture columnTex = texLoader.getTexture( );
		if ( columnTex == null )
			System.err.println( "Cannot load granite07rev.jpg texture" );
		else
		{
			columnTex.setBoundaryModeS( Texture.WRAP );
			columnTex.setBoundaryModeT( Texture.WRAP );
			columnTex.setMinFilter( Texture.NICEST );
			columnTex.setMagFilter( Texture.NICEST );
			columnTex.setMipMapMode( Texture.BASE_LEVEL );
			columnTex.setEnable( true );
		}

		texLoader = new TextureLoader( "flooring.jpg", this );
		Texture groundTex = texLoader.getTexture( );
		if ( groundTex == null )
			System.err.println( "Cannot load flooring.jpg texture" );
		else
		{
			groundTex.setBoundaryModeS( Texture.WRAP );
			groundTex.setBoundaryModeT( Texture.WRAP );
			groundTex.setMinFilter( Texture.NICEST );
			groundTex.setMagFilter( Texture.NICEST );
			groundTex.setMipMapMode( Texture.BASE_LEVEL );
			groundTex.setEnable( true );
		}


		//
		// Build several columns on the floor
		//
		if ( debug ) System.err.println( "  columns..." );
		SharedGroup column = new SharedGroup( );
		Appearance columnApp = new Appearance( );

		Material columnMat = new Material( );
		columnMat.setAmbientColor( 0.6f, 0.6f, 0.6f );
		columnMat.setDiffuseColor( 1.0f, 1.0f, 1.0f );
		columnMat.setSpecularColor( 0.0f, 0.0f, 0.0f );
		columnApp.setMaterial( columnMat );

		TextureAttributes columnTexAtt = new TextureAttributes( );
		columnTexAtt.setTextureMode( TextureAttributes.MODULATE );
		columnTexAtt.setPerspectiveCorrectionMode( TextureAttributes.NICEST);
		columnApp.setTextureAttributes( columnTexAtt );

		if ( columnTex != null )
			columnApp.setTexture( columnTex );

		GothicColumn columnShape = new GothicColumn(
			1.8f,  // height
			0.25f,  // radius
			GothicColumn.BUILD_TOP,  // flags
			columnApp );   // appearance
		column.addChild( columnShape );


		Vector3f trans = new Vector3f( );
		Transform3D tr = new Transform3D( );
		TransformGroup tg;

		// Left
		trans.set( -2.0f, -1.0f, 0.0f );
		tr.set( trans );
		tg = new TransformGroup( tr );
		tg.addChild( new Link( column ) );
		scene.addChild( tg );

		// Middle
		trans.set( 0.0f, -1.0f, 0.0f );
		tr.set( trans );
		tg = new TransformGroup( tr );
		tg.addChild( new Link( column ) );
		scene.addChild( tg );

		// Right
		trans.set( 2.0f, -1.0f, 0.0f );
		tr.set( trans );
		tg = new TransformGroup( tr );
		tg.addChild( new Link( column ) );
		scene.addChild( tg );

		//
		//  Add the ground
		//
		if ( debug ) System.err.println( "  ground..." );

		Appearance groundApp = new Appearance( );

		Material groundMat = new Material( );
		groundMat.setAmbientColor( 0.6f, 0.6f, 0.6f );
		groundMat.setDiffuseColor( 1.0f, 1.0f, 1.0f );
		groundMat.setSpecularColor( 0.0f, 0.0f, 0.0f );
		groundApp.setMaterial( groundMat );

		tr = new Transform3D( );
		tr.setScale( new Vector3d( 4.0, 4.0, 1.0 ) );

		TextureAttributes groundTexAtt = new TextureAttributes( );
		groundTexAtt.setTextureMode( TextureAttributes.MODULATE );
		groundTexAtt.setPerspectiveCorrectionMode(
			TextureAttributes.NICEST );
		groundTexAtt.setTextureTransform( tr );
		groundApp.setTextureAttributes( groundTexAtt );

		if ( groundTex != null )
			groundApp.setTexture( groundTex );

		ElevationGrid ground = new ElevationGrid(
			11,           // X dimension
			11,           // Z dimension
			2.0f,         // X spacing
			2.0f,         // Z spacing
			              // Automatically use zero heights
			groundApp );  // Appearance

		trans.set( 0.0f, -1.0f, 0.0f );
		tr.set( trans );
		tg = new TransformGroup( tr );
		tg.addChild( ground );
		scene.addChild( tg );

		// Add a light
		BoundingSphere worldBounds = new BoundingSphere(
			new Point3d( 0.0, 0.0, 0.0 ),  // Center
			1000.0 );                      // Extent

		DirectionalLight light = new DirectionalLight( );
		light.setEnable( true );
		light.setColor( new Color3f( 1.0f, 1.0f, 1.0f ) );
		light.setDirection( new Vector3f( 0.5f, -1.0f, -0.5f ) );
		light.setInfluencingBounds( worldBounds );
		scene.addChild( light );

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
		ExSwitch ex = new ExSwitch( );
		ex.initialize( args );
		ex.buildUniverse( );
		ex.showFrame( );
	}


	//
	//  Private class for holding switch options
	//
	private class NameChildMask
	{
		public String name;
		public int child;
		public BitSet mask;

		public NameChildMask( String n, int c, int m )
		{
			name = n;
			child = c;
			mask = new BitSet(3);
			if( (m&1) != 0 )	mask.set( 0 );
			if( (m&2) != 0 )	mask.set( 1 );
			if( (m&4) != 0 )	mask.set( 2 );
		}
	}

	//  Switch menu choices
	private NameChildMask[] options =
	{
		new NameChildMask( "CHILD_ALL",      Switch.CHILD_ALL,	0 ),
		new NameChildMask( "CHILD_NONE",     Switch.CHILD_NONE,	0 ),
		new NameChildMask( "Child 0",	     0,			0 ),
		new NameChildMask( "Child 1",	     1,			0 ),
		new NameChildMask( "Child 2",	     2,			0 ),
		new NameChildMask( "Children 0 & 1", Switch.CHILD_MASK,	3 ),
		new NameChildMask( "Children 0 & 2", Switch.CHILD_MASK,	5 ),
		new NameChildMask( "Children 1 & 2", Switch.CHILD_MASK,	6 ),
	};
	private CheckboxMenuItem[] switchMenu;
	

	//
	//  Initialize the GUI (application and applet)
	//
	public void initialize( String[] args )
	{
		// Initialize the window, menubar, etc.
		super.initialize( args );
		exampleFrame.setTitle( "Java 3D Switch Example" );

		// Add a menu to select among switch options
		Menu mt = new Menu( "Switch" );
		switchMenu = new CheckboxMenuItem[ options.length ];
		for( int i = 0; i < options.length; i++ )
		{
			switchMenu[i] = new CheckboxMenuItem( options[i].name );
			switchMenu[i].addItemListener( this );
			switchMenu[i].setState( false );
			mt.add( switchMenu[i] );
		}
		exampleMenuBar.add( mt );

		currentSwitch = 0;
		switchMenu[currentSwitch].setState( true );
	}


	//
	//  Handle checkboxes
	//
	public void itemStateChanged( ItemEvent event )
	{
		Object src = event.getSource();

		// Check if it is switch choice
		for( int i = 0; i < switchMenu.length; i++ )
		{
			if( src == switchMenu[i] )
			{
				// Update the checkboxes
				switchMenu[currentSwitch].setState( false );
				currentSwitch = i;
				switchMenu[currentSwitch].setState( true );

				// Set the switch
				swtch.setWhichChild( options[currentSwitch].child );
				swtch.setChildMask( options[currentSwitch].mask );
				return;
			}
		}


		// Handle all other checkboxes
		super.itemStateChanged( event );
	}
}

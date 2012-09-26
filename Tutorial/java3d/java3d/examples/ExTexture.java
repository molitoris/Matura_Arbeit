//
//  CLASS
//    ExTexture	-  illustrate use of textures
//
//  LESSON
//    Use Texture2D and TextureAttributes to apply a texture image
//    to a shape.
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.*;
import java.net.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.*;

public class ExTexture
	extends Example
{
	//--------------------------------------------------------------
	//  SCENE CONTENT
	//--------------------------------------------------------------

	//
	//  Nodes (updated via menu)
	//
	private Shape3D shape = null;			// overall scene shape
	private Appearance app = null;			// geometry appearance
	private Appearance dummyApp = null;		// temporary appearance
	private TextureAttributes texatt = null;// texture attributes
	private TextureAttributes dummyAtt = null;// temporary texture attributes
	private Texture2D tex = null;			// current texture

	//
	//  Build scene
	//
	public Group buildScene( )
	{
		// Get the current menu choices for appearance attributes
		int textureMode = ((Integer)modes[currentMode].value).intValue( );
		Color3f color = (Color3f)colors[currentColor].value;
		Color3f blendColor = (Color3f)colors[currentBlendColor].value;

		// Turn on the example headlight
		setHeadlightEnable( true );

		// Default to examine navigation
		setNavigationType( Examine );

		// Disable scene graph compilation for this example
		setCompilable( false );

		// Create the scene group
		Group scene = new Group( );


	// BEGIN EXAMPLE TOPIC
		// Set up a basic material
		Material mat = new Material( );
		mat.setAmbientColor( 0.2f, 0.2f, 0.2f );
		mat.setDiffuseColor( 1.0f, 1.0f, 1.0f );
		mat.setSpecularColor( 0.0f, 0.0f, 0.0f );
		mat.setLightingEnable( true );

		// Set up the texturing attributes with an initial
		// texture mode, texture transform, and blend color
		texatt = new TextureAttributes( );
		texatt.setPerspectiveCorrectionMode( TextureAttributes.NICEST );
		texatt.setTextureMode( textureMode );
		texatt.setTextureTransform( new Transform3D( ) );  // Identity
		texatt.setTextureBlendColor( blendColor.x, blendColor.y, blendColor.z, 0.5f );

		// Enable changing these while the node component is live
		texatt.setCapability( TextureAttributes.ALLOW_MODE_WRITE );
		texatt.setCapability( TextureAttributes.ALLOW_BLEND_COLOR_WRITE );
		texatt.setCapability( TextureAttributes.ALLOW_TRANSFORM_WRITE );

		// Create an appearance using these attributes
		app = new Appearance( );
		app.setMaterial( mat );
		app.setTextureAttributes( texatt );
		app.setTexture( tex );

		// And enable changing these while the node component is live
		app.setCapability( Appearance.ALLOW_TEXTURE_WRITE );
		app.setCapability( Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE );

		// Build a shape and enable changing its appearance
		shape = new Shape3D( buildGeometry( ), app );
		shape.setCapability( Shape3D.ALLOW_APPEARANCE_WRITE );
	// END EXAMPLE TOPIC

		// Create some dummy appearance and tex attribute node components
		// In response to menu choices, we quickly switch the shape to
		// use one of these, then diddle with the main appearance or
		// tex attribute, then switch the shape back.  This effectively
		// makes the appearance or tex attributes we want to change
		// become un-live during a change.  We have to do this approach
		// because some texture features have no capability bits to set
		// to allow changes while live.
		dummyApp = new Appearance();
		dummyAtt = new TextureAttributes( );

		scene.addChild( shape );

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
		ExTexture ex = new ExTexture( );
		ex.initialize( args );
		ex.buildUniverse( );
		ex.showFrame( );
	}


	//  Texture enable/disable
	private boolean textureOnOff = true;
	private CheckboxMenuItem textureOnOffMenu = null;

	//  Texture image choices
	private NameValue[] images =
	{
		new NameValue( "Red bricks",  	"brick.jpg" ),
		new NameValue( "Stone bricks", 	"stonebrk2.jpg" ),
		new NameValue( "Marble",  		"granite07rev.jpg" ),
		new NameValue( "Mud",  			"mud01.jpg" ),
		new NameValue( "Wood",  		"flooring.jpg" ),
		new NameValue( "Earth",  		"earthmap.jpg" ),
	};
	private CheckboxMenu imageMenu = null;
	private int currentImage = 0;


	//  Texture boundary mode choices
	private NameValue[] boundaries =
	{
		new NameValue( "CLAMP",  	new Integer( Texture.CLAMP ) ),
		new NameValue( "WRAP",  	new Integer( Texture.WRAP ) ),
	};
	private CheckboxMenu boundaryMenu = null;
	private int currentBoundary = 0;

	//  Texture boundary color choices
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
	private CheckboxMenu colorMenu = null;
	private int currentColor = 6;

	//  Texture filter choices
	private NameValue[] filters =
	{
		new NameValue( "POINT",  	new Integer( Texture.BASE_LEVEL_POINT ) ),
		new NameValue( "LINEAR",  	new Integer( Texture.BASE_LEVEL_LINEAR ) ),
	};
	private CheckboxMenu filterMenu = null;
	private int currentFilter = 0;


	// Texture attributes mode choices
	private NameValue[] modes =
	{
		new NameValue( "BLEND",  	new Integer( TextureAttributes.BLEND ) ),
		new NameValue( "DECAL",  	new Integer( TextureAttributes.DECAL ) ),
		new NameValue( "MODULATE", 	new Integer( TextureAttributes.MODULATE ) ),
		new NameValue( "REPLACE",  	new Integer( TextureAttributes.REPLACE ) ),
	};
	private CheckboxMenu modeMenu = null;
	private int currentMode = 2;

	//  Texture attributes blend color choices
	private CheckboxMenu blendColorMenu = null;
	private int currentBlendColor = 6;

	private NameValue[] xforms =
	{
		new NameValue( "Identity", 	new Integer( 0 ) ),
		new NameValue( "Scale by 2", 	new Integer( 1 ) ),
		new NameValue( "Scale by 4", 	new Integer( 2 ) ),
		new NameValue( "Rotate by 45 degrees", 	new Integer( 3 ) ),
		new NameValue( "Translate by 0.25", 	new Integer( 4 ) ),
	};
	private CheckboxMenu xformMenu = null;
	private int currentXform = 0;



	private Texture2D[]	textureComponents;


	//
	//  Initialize the GUI (application and applet)
	//
	public void initialize( String[] args )
	{
		// Initialize the window, menubar, etc.
		super.initialize( args );
		exampleFrame.setTitle( "Java 3D Texture Mapping Example" );


		//
		//  Add a menubar menu to change texture parameters
		//    Image -->
		//    Boundary mode -->
		//    Boundary color -->
		//    Filter mode -->
		//

		Menu m = new Menu( "Texture" );

		textureOnOffMenu = new CheckboxMenuItem(
			"Texturing enabled", textureOnOff );
		textureOnOffMenu.addItemListener( this );
		m.add( textureOnOffMenu );

		imageMenu = new CheckboxMenu( "Image", images,
			currentImage, this );
		m.add( imageMenu );

		boundaryMenu = new CheckboxMenu( "Boundary mode", boundaries,
			currentBoundary, this );
		m.add( boundaryMenu );

		colorMenu = new CheckboxMenu( "Boundary color", colors,
			currentColor, this );
		m.add( colorMenu );

		filterMenu = new CheckboxMenu( "Filter mode", filters,
			currentFilter, this );
		m.add( filterMenu );

		exampleMenuBar.add( m );


		//
		//  Add a menubar menu to change texture attributes parameters
		//    Mode -->
		//    Blend color -->
		//

		m = new Menu( "TextureAttributes" );

		modeMenu = new CheckboxMenu( "Mode", modes,
			currentMode, this );
		m.add( modeMenu );

		blendColorMenu = new CheckboxMenu( "Blend color", colors,
			currentBlendColor, this );
		m.add( blendColorMenu );

		xformMenu = new CheckboxMenu( "Transform", xforms,
			currentXform, this );
		m.add( xformMenu );

		exampleMenuBar.add( m );



		// Preload the texture images
		//   Use the texture loading utility to read in the texture
		//   files and process them into an ImageComponent2D
		//   for use in the Background node.
		if( debug )
			System.err.println( "Loading textures..." );

		textureComponents = new Texture2D[images.length];

		String value = null;
		for ( int i = 0; i < images.length; i++ )
		{
			value = (String)images[i].value;
			textureComponents[i] = loadTexture( value );
		}

		tex = textureComponents[ currentImage ];

	}


	//
	//  Handle checkboxes and menu choices
	//
	public void checkboxChanged( CheckboxMenu menu, int check )
	{
		if ( menu == imageMenu )
		{
			// Change the texture image
			currentImage = check;
			Texture tex = textureComponents[currentImage];
			int mode = ((Integer)boundaries[currentBoundary].value).intValue();
			Color3f color = (Color3f)colors[currentColor].value;
			int filter = ((Integer)filters[currentFilter].value).intValue( );

			shape.setAppearance( dummyApp );
			tex.setEnable( textureOnOff );
			tex.setBoundaryModeS( mode );
			tex.setBoundaryModeT( mode );
			tex.setBoundaryColor( color.x, color.y, color.z, 0.0f );
			tex.setMagFilter( filter );
			tex.setMinFilter( filter );
			app.setTexture( tex );
			shape.setAppearance( app );

			return;
		}

		if ( menu == boundaryMenu )
		{
			// Change the texture boundary mode
			currentBoundary = check;
			Texture tex = textureComponents[currentImage];
			int mode = ((Integer)boundaries[currentBoundary].value).intValue();

			shape.setAppearance( dummyApp );
			tex.setBoundaryModeS( mode );
			tex.setBoundaryModeT( mode );
			app.setTexture( tex );
			shape.setAppearance( app );

			return;
		}

		if ( menu == colorMenu )
		{
			// Change the boundary color
			currentColor = check;
			Color3f color = (Color3f)colors[currentColor].value;
			Texture tex = textureComponents[currentImage];

			shape.setAppearance( dummyApp );
			tex.setBoundaryColor( color.x, color.y, color.z, 0.0f );
			app.setTexture( tex );
			shape.setAppearance( app );

			return;
		}

		if ( menu == filterMenu )
		{
			// Change the filter mode
			currentFilter = check;
			int filter = ((Integer)filters[currentFilter].value).intValue( );
			Texture tex = textureComponents[currentImage];

			shape.setAppearance( dummyApp );
			tex.setMagFilter( filter );
			tex.setMinFilter( filter );
			app.setTexture( tex );
			shape.setAppearance( app );

			return;
		}

		if ( menu == modeMenu )
		{
			// Change the texture mode
			currentMode = check;
			int mode = ((Integer)modes[currentMode].value).intValue( );

			app.setTextureAttributes( dummyAtt );
			texatt.setTextureMode( mode );
			app.setTextureAttributes( texatt );

			return;
		}

		if ( menu == blendColorMenu )
		{
			// Change the boundary color
			currentBlendColor = check;
			Color3f color = (Color3f)colors[currentBlendColor].value;

			app.setTextureAttributes( dummyAtt );
			texatt.setTextureBlendColor( color.x, color.y, color.z, 0.5f );
			app.setTextureAttributes( texatt );

			return;
		}

		if ( menu == xformMenu )
		{
			// Change the texture transform
			currentXform = check;
			Transform3D tt = new Transform3D( );
			switch ( currentXform )
			{
			default:
			case 0:
				// Identity
				texatt.setTextureTransform( tt );	
				return;

			case 1:
				// Scale by 2
				tt.setScale( 2.0 );
				texatt.setTextureTransform( tt );	
				return;

			case 2:
				// Scale by 4
				tt.setScale( 4.0 );
				texatt.setTextureTransform( tt );	
				return;

			case 3:
				// Z rotate by 45 degrees
				tt.rotZ( Math.PI/4.0 );
				texatt.setTextureTransform( tt );	
				return;

			case 4:
				// Translate by 0.25
				tt.set( new Vector3f( 0.25f, 0.0f, 0.0f ) );
				texatt.setTextureTransform( tt );	
				return;
			}
		}

		// Handle all other checkboxes
		super.checkboxChanged( menu, check );
	}


	public void itemStateChanged( ItemEvent event )
	{
		Object src = event.getSource( );

		// Check if it is the texture on/off choice
		if ( src == textureOnOffMenu )
		{
			textureOnOff = textureOnOffMenu.getState( );
			Texture tex = textureComponents[currentImage];
			tex.setEnable( textureOnOff );
		}

		// Handle all other checkboxes
		super.itemStateChanged( event );
	}





	//--------------------------------------------------------------
	//  UTILITY METHODS
	//--------------------------------------------------------------

	//
	//  Handle loading a texture and setting up its attributes
	//
	private Texture2D loadTexture( String filename )
	{
		// Load the texture image file
		if ( debug )
			System.err.println( "Loading texture '" + filename + "'" );
		TextureLoader texLoader = new TextureLoader( filename, this);

		// If the image is NULL, something went wrong
		ImageComponent2D ic = texLoader.getImage( );
		if ( ic == null )
		{
			System.err.println( "Cannot load texture '" + filename + "'" );
			return null;
		}

		// Configure a Texture2D with the image
		Texture2D t = (Texture2D)texLoader.getTexture( );

		int mode = ((Integer)boundaries[currentBoundary].value).intValue();
		t.setBoundaryModeS( mode );
		t.setBoundaryModeT( mode );

		Color3f color = (Color3f)colors[currentColor].value;
		t.setBoundaryColor( color.x, color.y, color.z, 0.0f );

		int filter = ((Integer)filters[currentFilter].value).intValue( );
		t.setMagFilter( filter );
		t.setMinFilter( filter );

		t.setMipMapMode( Texture.BASE_LEVEL );

		// Turn it on and allow future changes
		t.setEnable( true );
		t.setCapability( Texture.ALLOW_ENABLE_WRITE );

		return t;
	}

	//
	//  Build a cube using a QuadArray
	//
	public QuadArray buildGeometry( )
	{
		QuadArray cube = new QuadArray(
					24,
					GeometryArray.COORDINATES |
					GeometryArray.NORMALS     |
					GeometryArray.TEXTURE_COORDINATE_2 );
		cube.setCapability( GeometryArray.ALLOW_COORDINATE_WRITE );
		cube.setCapability( GeometryArray.ALLOW_TEXCOORD_WRITE );

		VertexList vl = new VertexList( cube );

		float MAX =  1.0f;
		float MIN =  0.0f;

		//           Coordinate             Normal               Texture
		//             X      Y      Z       I      J      K      S    T

		// Front
		vl.xyzijkst( -1.0f, -1.0f,  1.0f,   0.0f,  0.0f,  1.0f,  MIN, MIN );
		vl.xyzijkst(  1.0f, -1.0f,  1.0f,   0.0f,  0.0f,  1.0f,  MAX, MIN );
		vl.xyzijkst(  1.0f,  1.0f,  1.0f,   0.0f,  0.0f,  1.0f,  MAX, MAX );
		vl.xyzijkst( -1.0f,  1.0f,  1.0f,   0.0f,  0.0f,  1.0f,  MIN, MAX );

		// Back
		vl.xyzijkst(  1.0f, -1.0f, -1.0f,   0.0f,  0.0f, -1.0f,  MAX, MIN );
		vl.xyzijkst( -1.0f, -1.0f, -1.0f,   0.0f,  0.0f, -1.0f,  MIN, MIN );
		vl.xyzijkst( -1.0f,  1.0f, -1.0f,   0.0f,  0.0f, -1.0f,  MIN, MAX );
		vl.xyzijkst(  1.0f,  1.0f, -1.0f,   0.0f,  0.0f, -1.0f,  MAX, MAX );

		// Right
		vl.xyzijkst(  1.0f, -1.0f,  1.0f,   1.0f,  0.0f,  0.0f,  MIN, MAX );
		vl.xyzijkst(  1.0f, -1.0f, -1.0f,   1.0f,  0.0f,  0.0f,  MIN, MIN );
		vl.xyzijkst(  1.0f,  1.0f, -1.0f,   1.0f,  0.0f,  0.0f,  MAX, MIN );
		vl.xyzijkst(  1.0f,  1.0f,  1.0f,   1.0f,  0.0f,  0.0f,  MAX, MAX );

		// Left
		vl.xyzijkst( -1.0f, -1.0f, -1.0f,  -1.0f,  0.0f,  0.0f,  MIN, MIN );
		vl.xyzijkst( -1.0f, -1.0f,  1.0f,  -1.0f,  0.0f,  0.0f,  MIN, MAX );
		vl.xyzijkst( -1.0f,  1.0f,  1.0f,  -1.0f,  0.0f,  0.0f,  MAX, MAX );
		vl.xyzijkst( -1.0f,  1.0f, -1.0f,  -1.0f,  0.0f,  0.0f,  MAX, MIN );

		// Top
		vl.xyzijkst( -1.0f,  1.0f,  1.0f,   0.0f,  1.0f,  0.0f,  MIN, MAX );
		vl.xyzijkst(  1.0f,  1.0f,  1.0f,   0.0f,  1.0f,  0.0f,  MAX, MAX );
		vl.xyzijkst(  1.0f,  1.0f, -1.0f,   0.0f,  1.0f,  0.0f,  MAX, MIN );
		vl.xyzijkst( -1.0f,  1.0f, -1.0f,   0.0f,  1.0f,  0.0f,  MIN, MIN );

		// Bottom
		vl.xyzijkst( -1.0f, -1.0f, -1.0f,   0.0f, -1.0f,  0.0f,  MIN, MIN );
		vl.xyzijkst(  1.0f, -1.0f, -1.0f,   0.0f, -1.0f,  0.0f,  MAX, MIN );
		vl.xyzijkst(  1.0f, -1.0f,  1.0f,   0.0f, -1.0f,  0.0f,  MAX, MAX );
		vl.xyzijkst( -1.0f, -1.0f,  1.0f,   0.0f, -1.0f,  0.0f,  MIN, MAX );

		return cube;
	}

	//
	//  Use a helper class to manage the coordinate lists
	//
	private class VertexList
	{
		private int index = 0;
		private GeometryArray ga = null;

		public VertexList( GeometryArray newga )
		{
			index = 0;
			ga = newga;
		}

		public void xyzst( float x, float y, float z, float s, float t )
		{
			ga.setCoordinate( index, new Point3f( x, y, z ) );
			ga.setTextureCoordinate( index, new Point2f( s, t ) );
			index++;
		}

		public void xyzijkst(	float x, float y, float z,
					float i, float j, float k,
					float s, float t )
		{
			ga.setCoordinate( index, new Point3f( x, y, z ) );
			ga.setNormal( index, new Vector3f( i, j, k ) );
			ga.setTextureCoordinate( index, new Point2f( s, t ) );
			index++;
		}
	}
}

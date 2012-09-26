//
//  CLASS
//    ExSound  -  illustrate the use of sounds
//
//  LESSON
//    Add a PointSound and a BackgroundSound to an environment.
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//

import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.*;

public class ExSound
	extends Example
{
	//--------------------------------------------------------------
	//  SCENE CONTENT
	//--------------------------------------------------------------

	//
	//  Nodes (updated via menu)
	//
	private BackgroundSound  backgroundSound = null;
	private PointSound       pointSound = null;

	private float            soundHeight = 1.6f;
	private float            pointX = 0.0f;

	private AmbientLight     ambientLight = null;
	private PointLight       pointLight = null;


	//
	//  Build scene
	//
	public Group buildScene( )
	{
		// Get the initial sound volume
		float vol = ((Float)volumes[currentVolume].value).floatValue();

		// Turn off the example headlight
		setHeadlightEnable( false );

		// Default to walk navigation
		setNavigationType( Walk );

		// Build the scene group
		Group scene = new Group( );

		//
		// Preload the sounds
		//
		if ( debug ) System.err.println( "  sounds..." );
		String path = getCurrentDirectory( );
		MediaContainer backgroundMedia  = new MediaContainer(
			path + "canon.wav" );
		backgroundMedia.setCacheEnable( true );

		MediaContainer pointMedia  = new MediaContainer(
			path + "willow1.wav" );
		pointMedia.setCacheEnable( true );

	// BEGIN EXAMPLE TOPIC
		// Create influencing bounds
		BoundingSphere worldBounds = new BoundingSphere(
			new Point3d( 0.0, 0.0, 0.0 ),  // Center
			1000.0 );                      // Extent

		// Background sound
		backgroundSound = new BackgroundSound( );
		backgroundSound.setEnable( backgroundSoundOnOff );
		backgroundSound.setLoop( Sound.INFINITE_LOOPS );
		backgroundSound.setSoundData( backgroundMedia );
		backgroundSound.setInitialGain( vol );
		backgroundSound.setSchedulingBounds( worldBounds );
		backgroundSound.setCapability( Sound.ALLOW_ENABLE_WRITE );
		backgroundSound.setCapability( Sound.ALLOW_INITIAL_GAIN_WRITE );
		scene.addChild( backgroundSound );

		// Create a distance gain array for the point sound
		Point2f[] distanceGain = {
			new Point2f(  9.0f, 1.0f ),    // Full volume
			new Point2f( 10.0f, 0.5f ),    // Half volume
			new Point2f( 20.0f, 0.25f ),   // Quarter volume
			new Point2f( 30.0f, 0.0f ),    // Zero volume
		};

		// Point sound
		pointSound = new PointSound( );
		pointSound.setEnable( pointSoundOnOff );
		pointSound.setPosition(
			new Point3f( pointX, soundHeight, 0.0f ) );
		pointSound.setLoop( Sound.INFINITE_LOOPS );
		pointSound.setSoundData( pointMedia );
		pointSound.setInitialGain( vol );
		pointSound.setDistanceGain( distanceGain );
		pointSound.setSchedulingBounds( worldBounds );
		pointSound.setCapability( Sound.ALLOW_ENABLE_WRITE );
		pointSound.setCapability( Sound.ALLOW_INITIAL_GAIN_WRITE );
		scene.addChild( pointSound );
	// END EXAMPLE TOPIC


		// Build a few lights, one per sound.  We'll turn them
		// on when the associated sound is on.
		ambientLight = new AmbientLight( );
		ambientLight.setEnable( backgroundSoundOnOff );
		ambientLight.setColor( Gray );
		ambientLight.setInfluencingBounds( worldBounds );
		ambientLight.setCapability( Light.ALLOW_STATE_WRITE );
		scene.addChild( ambientLight );

		pointLight = new PointLight( );
		pointLight.setEnable( pointSoundOnOff );
		pointLight.setColor( White );
		pointLight.setPosition( 0.0f, soundHeight, 0.0f );
		pointLight.setInfluencingBounds( worldBounds );
		pointLight.setCapability( Light.ALLOW_STATE_WRITE );
		scene.addChild( pointLight );

		// Add a basic ambient light for when all sounds (and
		// their lights) are off so that the world isn't dark
		AmbientLight amb = new AmbientLight( );
		amb.setEnable( true );
		amb.setColor( Gray );
		amb.setInfluencingBounds( worldBounds );
		amb.setCapability( Light.ALLOW_STATE_WRITE );
		scene.addChild( amb );

		// Build foreground geometry
		scene.addChild( buildForeground( ) );

		return scene;
	}



	//--------------------------------------------------------------
	//  FOREGROUND CONTENT
	//--------------------------------------------------------------

	private Group buildForeground( )
	{
		//
		//  Create a group for the foreground, and move
		//  everything up a bit.
		//
		TransformGroup group = new TransformGroup( );
		Transform3D tr = new Transform3D( );
		tr.setTranslation( new Vector3f( 0.0f, -1.6f, 0.0f ) );
		group.setTransform( tr );


		//
		//  Load textures
		//
		if ( debug ) System.err.println( "  textures..." );
		Texture groundTex = null;
		Texture spurTex = null;
		Texture domeTex = null;
		TextureLoader texLoader = null;
		ImageComponent image = null;

		texLoader = new TextureLoader( "flooring.jpg", this );
		image = texLoader.getImage( );
		if ( image == null )
			System.err.println( "Cannot load flooring.jpg texture" );
		else
		{
			groundTex = texLoader.getTexture( );
			groundTex.setBoundaryModeS( Texture.WRAP );
			groundTex.setBoundaryModeT( Texture.WRAP );
			groundTex.setMinFilter( Texture.NICEST );
			groundTex.setMagFilter( Texture.NICEST );
			groundTex.setMipMapMode( Texture.BASE_LEVEL );
			groundTex.setEnable( true );
		}

		texLoader = new TextureLoader( "granite07rev.jpg", this );
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

		texLoader = new TextureLoader( "brtsky.jpg", this );
		Texture boxTex = texLoader.getTexture( );
		if ( boxTex == null )
			System.err.println( "Cannot load brtsky.jpg texture" );
		else
		{
			boxTex.setBoundaryModeS( Texture.WRAP );
			boxTex.setBoundaryModeT( Texture.WRAP );
			boxTex.setMinFilter( Texture.NICEST );
			boxTex.setMagFilter( Texture.NICEST );
			boxTex.setMipMapMode( Texture.BASE_LEVEL );
			boxTex.setEnable( true );
		}


		//
		//  Build the ground
		//
		if ( debug ) System.err.println( "  ground..." );

		Appearance groundApp = new Appearance( );

		Material groundMat = new Material( );
		groundMat.setAmbientColor( 0.3f, 0.3f, 0.3f );
		groundMat.setDiffuseColor( 0.7f, 0.7f, 0.7f );
		groundMat.setSpecularColor( 0.0f, 0.0f, 0.0f );
		groundApp.setMaterial( groundMat );

		tr = new Transform3D( );
		tr.setScale( new Vector3d( 16.0, 4.0, 1.0 ) );

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
		group.addChild( ground );


		//
		//  Create a column appearance used for both columns.
		//
		Appearance columnApp = new Appearance( );

		Material columnMat = new Material( );
		columnMat.setAmbientColor( 0.6f, 0.6f, 0.6f );
		columnMat.setDiffuseColor( 1.0f, 1.0f, 1.0f );
		columnMat.setSpecularColor( 0.0f, 0.0f, 0.0f );
		columnApp.setMaterial( columnMat );

		TextureAttributes columnTexAtt = new TextureAttributes( );
		columnTexAtt.setTextureMode( TextureAttributes.MODULATE );
		columnTexAtt.setPerspectiveCorrectionMode(
			TextureAttributes.NICEST);
		columnApp.setTextureAttributes( columnTexAtt );

		if ( columnTex != null )
			columnApp.setTexture( columnTex );


		//
		//  To give the point sound an apparent location,
		//  build a column and a set of three co-located
		//  tumbling boxes hovering above the column.
		//
		TransformGroup pointGroup = new TransformGroup( );
		tr.setIdentity( );
		tr.setTranslation( new Vector3f( pointX, 0.0f, 0.0f ) );
		pointGroup.setTransform( tr );

		GothicColumn column = new GothicColumn(
			1.0f,          // height
			0.2f,          // radius
			GothicColumn.BUILD_TOP,  // flags
			columnApp );   // appearance
		pointGroup.addChild( column );

		TransformGroup rotThing = new TransformGroup( );
		tr.setIdentity( );
		tr.setTranslation( new Vector3f( 0.0f, soundHeight, 0.0f ) );
		rotThing.setTransform( tr );

		Appearance boxApp = new Appearance( );
			// No material -- make it emissive
		TextureAttributes boxTexAtt = new TextureAttributes( );
		boxTexAtt.setTextureMode( TextureAttributes.REPLACE );
		boxTexAtt.setPerspectiveCorrectionMode(
			TextureAttributes.NICEST );
		boxApp.setTextureAttributes( boxTexAtt );

		if ( boxTex != null )
			boxApp.setTexture( boxTex );

		rotThing.addChild( buildTumblingBox(
			0.4f, 0.4f, 0.4f,  // width, height, depth
			boxApp,            // Appearance
			40000, 32000, 26000 ) );// XYZ tumble durations
		rotThing.addChild( buildTumblingBox(
			0.4f, 0.4f, 0.4f,  // width, height, depth
			boxApp,            // Appearance
			38000, 30000, 28000 ) );// XYZ tumble durations
		rotThing.addChild( buildTumblingBox(
			0.4f, 0.4f, 0.4f,  // width, height, depth
			boxApp,            // Appearance
			30000, 26000, 34000 ) );// XYZ tumble durations

		pointGroup.addChild( rotThing );

		group.addChild( pointGroup );


		return group;
	}



	private int[] coordinateIndices = {
		0, 1, 5, 4,  // front
		1, 2, 6, 5,  // right
		2, 3, 7, 6,  // back
		3, 0, 4, 7,  // left
		4, 5, 6, 7,  // top
		3, 2, 1, 0,  // bottom
	};

	private float[] textureCoordinates = {
		0.0f, 0.0f,
		1.0f, 0.0f,
		1.0f, 1.0f,
		0.0f, 1.0f,
	};

	private int[] textureCoordinateIndices = {
		0, 1, 2, 3,
		0, 1, 2, 3,
		0, 1, 2, 3,
		0, 1, 2, 3,
		0, 1, 2, 3,
		0, 1, 2, 3,
	};

	private float[] normals = {
		0.0f, 0.0f, 1.0f,  // front
		1.0f, 0.0f, 0.0f,  // right
		0.0f, 0.0f, -1.0f, // back
		-1.0f, 0.0f, 0.0f, // left
		0.0f, 1.0f, 0.0f,  // top
		0.0f, -1.0f, 0.0f, // bottom
	};

	private int[] normalIndices = {
		0, 0, 0, 0,
		1, 1, 1, 1,
		2, 2, 2, 2,
		3, 3, 3, 3,
		4, 4, 4, 4,
		5, 5, 5, 5,
	};

	private Shape3D buildBox( float width, float height, float depth,
		Appearance app )
	{
		float w2 = width/2.0f;
		float h2 = height/2.0f;
		float d2 = depth/2.0f;

		float[] coordinates = new float[8*3];
		int n = 0;
		// Around the bottom of the box
		coordinates[n+0] = -w2;
		coordinates[n+1] = -h2;
		coordinates[n+2] = d2;
		n += 3;
		coordinates[n+0] = w2;
		coordinates[n+1] = -h2;
		coordinates[n+2] = d2;
		n += 3;
		coordinates[n+0] = w2;
		coordinates[n+1] = -h2;
		coordinates[n+2] = -d2;
		n += 3;
		coordinates[n+0] = -w2;
		coordinates[n+1] = -h2;
		coordinates[n+2] = -d2;
		n += 3;

		// Around the top of the box
		coordinates[n+0] = -w2;
		coordinates[n+1] = h2;
		coordinates[n+2] = d2;
		n += 3;
		coordinates[n+0] = w2;
		coordinates[n+1] = h2;
		coordinates[n+2] = d2;
		n += 3;
		coordinates[n+0] = w2;
		coordinates[n+1] = h2;
		coordinates[n+2] = -d2;
		n += 3;
		coordinates[n+0] = -w2;
		coordinates[n+1] = h2;
		coordinates[n+2] = -d2;
		n += 3;

		IndexedQuadArray quads = new IndexedQuadArray(
			coordinates.length,  // vertex count
			GeometryArray.COORDINATES |
			GeometryArray.NORMALS |
			GeometryArray.TEXTURE_COORDINATE_2,
			coordinateIndices.length );
		quads.setCoordinates( 0, coordinates );
		quads.setCoordinateIndices( 0, coordinateIndices );
		quads.setNormals( 0, normals );
		quads.setNormalIndices( 0, normalIndices );
		quads.setTextureCoordinates( 0, textureCoordinates );
		quads.setTextureCoordinateIndices( 0, textureCoordinateIndices);

		Shape3D shape = new Shape3D( quads, app );
		return shape;
	}


	private Group buildTumblingBox( float width, float height,
		float depth, Appearance app,
		int xDur, int yDur, int zDur )
	{
		BoundingSphere worldBounds = new BoundingSphere(
			new Point3d( 0.0, 0.0, 0.0 ),  // Center
			1000.0 );                      // Extent

		//  Build a box to tumble
		Shape3D box = buildBox( width, height, depth, app );

		//  Build a set of nested transform groups.  Attach
		//  to each one a behavior that rotates around an X,
		//  Y, or Z axis.  Use different rotation speeds for
		//  each axis to create a tumbling effect.
		TransformGroup outerGroup = new TransformGroup( );
		outerGroup.setCapability(
			TransformGroup.ALLOW_TRANSFORM_WRITE );
		Transform3D yAxis = new Transform3D();
		Alpha alpha = new Alpha(
			-1,            // loop count:  -1 = forever
			Alpha.INCREASING_ENABLE,  // increasing
			0,             // trigger time:  0 = now
			0,             // delay:  0 = none
			xDur,          // increasing duration
			0,             // increasing ramp duration
			0,             // at one (sustain) duration
			0,             // decreasing duration
			0,             // decreasing ramp duration
			0 );           // at zero duration
		RotationInterpolator rot = new RotationInterpolator(
			alpha,         // Alpha control
			outerGroup,    // Target transform group
			yAxis,         // Y axis rotation
			0.0f,          // Minimum angle
			2.0f*(float)Math.PI );// Maximum angle
		rot.setSchedulingBounds( worldBounds );
		outerGroup.addChild( rot );

		TransformGroup middleGroup = new TransformGroup( );
		middleGroup.setCapability(
			TransformGroup.ALLOW_TRANSFORM_WRITE );
		Transform3D xAxis = new Transform3D();
		xAxis.rotZ( -1.571f );
		alpha = new Alpha(
			-1,            // loop count:  -1 = forever
			Alpha.INCREASING_ENABLE,  // increasing
			0,             // trigger time:  0 = now
			0,             // delay:  0 = none
			yDur,          // increasing duration
			0,             // increasing ramp duration
			0,             // at one (sustain) duration
			0,             // decreasing duration
			0,             // decreasing ramp duration
			0 );           // at zero duration
		rot = new RotationInterpolator(
			alpha,         // Alpha control
			middleGroup,   // Target transform group
			xAxis,         // Y axis rotation
			0.0f,          // Minimum angle
			2.0f*(float)Math.PI );// Maximum angle
		rot.setSchedulingBounds( worldBounds );
		middleGroup.addChild( rot );
		outerGroup.addChild( middleGroup );

		TransformGroup innerGroup = new TransformGroup( );
		innerGroup.setCapability(
			TransformGroup.ALLOW_TRANSFORM_WRITE );
		Transform3D zAxis = new Transform3D();
		zAxis.rotX( 1.571f );
		alpha = new Alpha(
			-1,            // loop count:  -1 = forever
			Alpha.INCREASING_ENABLE,  // increasing
			0,             // trigger time:  0 = now
			0,             // delay:  0 = none
			zDur,          // increasing duration
			0,             // increasing ramp duration
			0,             // at one (sustain) duration
			0,             // decreasing duration
			0,             // decreasing ramp duration
			0 );           // at zero duration
		rot = new RotationInterpolator(
			alpha,         // Alpha control
			innerGroup,    // Target transform group
			zAxis,         // Y axis rotation
			0.0f,          // Minimum angle
			2.0f*(float)Math.PI );// Maximum angle
		rot.setSchedulingBounds( worldBounds );
		innerGroup.addChild( rot );
		middleGroup.addChild( innerGroup );

		innerGroup.addChild( box );
		return outerGroup;
	}



	//--------------------------------------------------------------
	//  USER INTERFACE
	//--------------------------------------------------------------

	//
	//  Main
	//
	public static void main( String[] args )
	{
		ExSound ex = new ExSound( );
		ex.initialize( args );
		ex.buildUniverse( );
		ex.showFrame( );
	}


	//  On/off choices
	private boolean backgroundSoundOnOff = false;
	private CheckboxMenuItem backgroundSoundOnOffMenu = null;

	private boolean pointSoundOnOff = false;
	private CheckboxMenuItem pointSoundOnOffMenu = null;

	//  Volume menu choices
	private NameValue[] volumes = {
		new NameValue( "Silent",        new Float( 0.0f ) ),
		new NameValue( "Low volume",    new Float( 0.5f ) ),
		new NameValue( "Medium volume", new Float( 1.0f ) ),
		new NameValue( "High volume",   new Float( 2.0f ) ),
	};
	private int currentVolume = 2;
	private CheckboxMenu volumeMenu = null;


	//
	//  Initialize the GUI (application and applet)
	//
	public void initialize( String[] args )
	{
		// Initialize the window, menubar, etc.
		super.initialize( args );
		exampleFrame.setTitle( "Java 3D Sound Example" );


		//
		//  Add a menubar menu to change node parameters
		//    Background sound on/off
		//    Point sound on/off
		//

		Menu m = new Menu( "Sounds" );

		backgroundSoundOnOffMenu = new CheckboxMenuItem( "Background sound on/off",
			backgroundSoundOnOff );
		backgroundSoundOnOffMenu.addItemListener( this );
		m.add( backgroundSoundOnOffMenu );

		pointSoundOnOffMenu = new CheckboxMenuItem( "Point sound on/off",
			pointSoundOnOff );
		pointSoundOnOffMenu.addItemListener( this );
		m.add( pointSoundOnOffMenu );

		volumeMenu = new CheckboxMenu( "Volume", volumes,
			currentVolume, this );
		m.add( volumeMenu );

		exampleMenuBar.add( m );
	}


	//
	//  Handle checkboxes and menu choices
	//
	public void checkboxChanged( CheckboxMenu menu, int check )
	{
		if ( menu == volumeMenu )
		{
			// Change the sound volumes
			currentVolume = check;
			float vol = ((Float)volumes[check].value).floatValue( );
			backgroundSound.setInitialGain( vol );
			pointSound.setInitialGain( vol );
			return;
		}

		// Handle all other checkboxes
		super.checkboxChanged( menu, check );
	}

	public void itemStateChanged( ItemEvent event )
	{
		Object src = event.getSource( );
		if ( src == backgroundSoundOnOffMenu )
		{
			// Turn the background sound on or off
			backgroundSoundOnOff = backgroundSoundOnOffMenu.getState( );
			backgroundSound.setEnable( backgroundSoundOnOff );
			ambientLight.setEnable( backgroundSoundOnOff );
			return;
		}
		if ( src == pointSoundOnOffMenu )
		{
			// Turn the point sound on or off
			pointSoundOnOff = pointSoundOnOffMenu.getState( );
			pointSound.setEnable( pointSoundOnOff );
			pointLight.setEnable( pointSoundOnOff );

			return;
		}

		// Handle all other checkboxes
		super.itemStateChanged( event );
	}
}

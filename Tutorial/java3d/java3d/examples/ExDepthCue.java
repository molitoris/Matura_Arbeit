//
//  CLASS
//    ExDepthCue  -  illustrate use of exponential fog for depth-cueing
//
//  SEE ALSO
//    ExLinearFog
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

public class ExDepthCue
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
	private Switch fogSwitch = null;

	//
	//  Build scene
	//
	public Group buildScene( )
	{
		// Get the current color
		Color3f color = (Color3f)colors[currentColor].value;

		// Turn off the example headlight
		setHeadlightEnable( false );

		// Create the scene group
		Group scene = new Group( );


		// Create a switch group to hold the fog node.  This enables
		// us to turn the fog node on and off via the GUI.
		fogSwitch = new Switch( );
		fogSwitch.setCapability( Switch.ALLOW_SWITCH_WRITE );


		// Create influencing bounds
		Bounds worldBounds = new BoundingSphere(
			new Point3d( 0.0, 0.0, 0.0 ),  // Center
			1000.0 );                      // Extent

		// Set the fog color, density, and its influencing bounds
		fog = new LinearFog( );
		fog.setColor( color );
		// front and back distances set below
		fog.setCapability( Fog.ALLOW_COLOR_WRITE );
		fog.setCapability( Fog.ALLOW_INFLUENCING_BOUNDS_WRITE );
		fog.setInfluencingBounds( worldBounds );
		fogSwitch.addChild( fog );
		scene.addChild( fogSwitch );
		if ( depthCueOnOff )
			fogSwitch.setWhichChild( 0 );  // on
		else
			fogSwitch.setWhichChild( Switch.CHILD_NONE );  // off


		// Set the background color and its application bounds
		//   Usually, the background color should match the fog color
		//   or the results look odd.
		background = new Background( );
		background.setColor( color );
		background.setApplicationBounds( worldBounds );
		background.setCapability( Background.ALLOW_COLOR_WRITE );
		scene.addChild( background );


		// Build foreground geometry
		Group content = buildIsoline( );
		scene.addChild( content );


		// Automatically compute good front and back distances for
		// fog to get good depth-cueing.  To do this, first get the
		// dimensions of the bounds around the content.  Then,
		// set the front distance to be at the center of the content
		// (or closer by a tad) and the back distance at the front
		// distance PLUS half the depth of the content's bounding box
		BoundingSphere sampleSphere = new BoundingSphere( );
		BoundingBox sampleBox = new BoundingBox( );
		Bounds bounds = content.getBounds( );
		double deltaDistance = 0.0;
		double centerZ = 0.0;

		if ( bounds == null )
		{
			// No bounds available.  Estimate the values knowing
			// that the above content is what it is.
			centerZ = 0.5;  // 0.5 closer than true center
			deltaDistance = 2.0;
		}
		else if ( bounds.getClass( ) == sampleSphere.getClass( ) )
		{
			BoundingSphere sphereBounds = (BoundingSphere)bounds;
			deltaDistance = Math.abs( sphereBounds.getRadius() );
			Point3d center = new Point3d( );
			sphereBounds.getCenter( center );
			centerZ = center.z + 0.5;  // 0.5 closer than true center
		}
		else if ( bounds.getClass( ) == sampleBox.getClass( ) )
		{
			BoundingBox boxBounds = (BoundingBox)bounds;
			Point3d p1 = new Point3d( );
			Point3d p2 = new Point3d( );
			boxBounds.getLower( p1 );
			boxBounds.getUpper( p2 );
			deltaDistance = p2.z - p1.z;
			if ( deltaDistance < 0.0 )
				deltaDistance *= -1.0;
			if ( p1.z > p2.z )
				centerZ = p1.z - deltaDistance/2.0;
			else
				centerZ = p2.z - deltaDistance/2.0;
			centerZ += 0.5; // 0.5 closer than true center
		}
		else
		{
			System.err.println( "Unknown bounds type" );
		}

		// Set front distance to the distance from the default
		// viewing position (0,0,10) to the center of the bounds.
		fog.setFrontDistance( 10.0f - (float)centerZ );

		// Set back distance to the distance from the default
		// viewing position (0,0,10) to the back of the bounds.
		fog.setBackDistance( 10.0f - (float)centerZ + (float)deltaDistance );

		return scene;
	}


	public Group buildIsoline( )
	{
		Group group = new Group( );

		//                           alpha theta radius  r     g     b
		group.addChild( buildSurface( 3.0, 5.0,  2.194,  1.0f, 0.0f, 0.0f ));
		group.addChild( buildSurface( 2.0, 4.0,  1.181,  1.0f, 0.5f, 0.0f ));
		group.addChild( buildSurface( 1.0, 3.0,  0.506,  1.0f, 1.0f, 0.0f ));

		return group;
	}

	public Shape3D buildSurface( double freqAlpha, double freqTheta,
			double radius,
			float red, float green, float blue )
	{
		int nAngles = 64;
		double amp = radius / 4.0;

		int nAlpha = nAngles / 2;
		double theta, alpha;
		double x, y, z, rprime, r;
		double deltaTheta, deltaAlpha;
		int i, j;
		int i1, i2, i3, i4;

		deltaTheta = 360.0 / (nAngles - 1.0);
		deltaAlpha = 180.0 / (nAlpha - 1.0);


		// Build an appearance
		Appearance app = new Appearance( );

		LineAttributes latt = new LineAttributes( );
		latt.setLineWidth( 1.0f );
		app.setLineAttributes( latt );

		ColoringAttributes catt = new ColoringAttributes( );
		catt.setColor( red, green, blue );
		app.setColoringAttributes( catt );

		PolygonAttributes patt = new PolygonAttributes( );
		patt.setCullFace( PolygonAttributes.CULL_NONE );
		patt.setPolygonMode( PolygonAttributes.POLYGON_LINE );
		app.setPolygonAttributes( patt );


		// Compute coordinates
		double[] coordinates = new double[nAlpha*nAngles*3];
		alpha = 90.0;
		int n = 0;
		for ( i = 0; i < nAlpha; i++ )
		{
			theta = 0.0;
			for ( j = 0; j < nAngles; j++ )
			{
				r = radius +
					amp * Math.sin( (freqAlpha * ((double)i/(double)(nAlpha-1)) +
					freqTheta * ((double)j/(double)(nAngles-1))) * 2.0 * Math.PI );
				y = r * Math.sin( alpha / 180.0 * Math.PI );
				rprime = y / Math.tan( alpha / 180.0 * Math.PI );
				x = rprime * Math.cos( theta / 180.0 * Math.PI );
				z = rprime * Math.sin( theta / 180.0 * Math.PI );

				coordinates[n+0] = x;
				coordinates[n+1] = y;
				coordinates[n+2] = z;
				n += 3;
				theta += deltaTheta;
			}
			alpha -= deltaAlpha;
		}


		// Compute coordinate indexes
		int[] indexes = new int[(nAlpha-1)*nAngles*4];
		n = 0;
		for ( i = 0; i < nAlpha-1; i++ )
		{
			for ( j = 0; j < nAngles; j++ )
			{
				i1 = i * nAngles + j;
				if ( j == nAngles-1 )
				{
					i2 = i1 - j;
					i3 = (i+1) * nAngles;
				}
				else
				{
					i2 = i1 + 1;
					i3 = (i+1) * nAngles + j + 1;
				}
				i4 = (i+1) * nAngles + j;

				indexes[n+0] = i1;
				indexes[n+1] = i2;
				indexes[n+2] = i3;
				indexes[n+3] = i4;
				n += 4;
			}
		}


		// Build the shape
		IndexedQuadArray lines = new IndexedQuadArray(
			coordinates.length/3,   // Number of coordinates
			GeometryArray.COORDINATES,  // coordinates only
			indexes.length );       // Number of indexes
		lines.setCoordinates( 0, coordinates );
		lines.setCoordinateIndices( 0, indexes );

		Shape3D shape = new Shape3D( lines, app );

		return shape;
	}



	//--------------------------------------------------------------
	//  USER INTERFACE
	//--------------------------------------------------------------

	//
	//  Main
	//
	public static void main( String[] args )
	{
		ExDepthCue ex = new ExDepthCue( );
		ex.initialize( args );
		ex.buildUniverse( );
		ex.showFrame( );
	}


	//  Depth cueing on/off
	private boolean depthCueOnOff = true;
	private CheckboxMenuItem depthCueOnOffMenu = null;

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
	private int currentColor = 3;
	private CheckboxMenu colorMenu = null;



	//
	//  Initialize the GUI (application and applet)
	//
	public void initialize( String[] args )
	{
		// Initialize the window, menubar, etc.
		super.initialize( args );
		exampleFrame.setTitle( "Java 3D ExDepthCue Example" );


		//
		//  Add a menubar menu to change node parameters
		//    Depth cueing
		//    Depth-cue color -->
		//

		Menu m = new Menu( "Depth Cueing" );

		depthCueOnOffMenu = new CheckboxMenuItem(
			"Depth cueing", depthCueOnOff );
		depthCueOnOffMenu.addItemListener( this );
		m.add( depthCueOnOffMenu );

		colorMenu = new CheckboxMenu( "Depth-cue color", colors,
			currentColor, this );
		m.add( colorMenu );

		exampleMenuBar.add( m );
	}


	//
	//  Handle checkboxes and menu choices
	//
	public void checkboxChanged( CheckboxMenu menu, int check )
	{
		if ( menu == colorMenu )
		{
			// Change the fog color and background color
			currentColor = check;
			Color3f color = (Color3f)colors[check].value;
			fog.setColor( color );
			background.setColor( color );
			return;
		}

		// Handle all other checkboxes
		super.checkboxChanged( menu, check );
	}

	public void itemStateChanged( ItemEvent event )
	{
		Object src = event.getSource( );

		// Check if depth-cueing is enabled
		if ( src == depthCueOnOffMenu )
		{
			depthCueOnOff = depthCueOnOffMenu.getState( );
			if ( depthCueOnOff )
			{
				// If on, enable color menu and fog
				colorMenu.setEnabled( true );
				fogSwitch.setWhichChild( 0 );  // on
			}
			else
			{
				// If off, disable color menu and fog
				colorMenu.setEnabled( false );
				fogSwitch.setWhichChild( Switch.CHILD_NONE );  // off
			}
		}

		// Handle all other checkboxes
		super.itemStateChanged( event );
	}
}

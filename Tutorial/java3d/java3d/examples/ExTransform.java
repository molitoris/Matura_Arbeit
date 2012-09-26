//
//  CLASS
//    ExTransform -- illustrate use of transforms
//
//  LESSON
//    Use Transform3D and TransformGroup to translate, rotate, and
//    scale shapes
//
//  AUTHOR
//    Michael J. Bailey / San Diego Supercomputer Center
//

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.*;
import java.net.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.geometry.*;

public class ExTransform
	extends Example
{
	//  nodes that can be updated via a menu:

	Switch switchGroup = null;

	SharedGroup sharedObject = null;

	private int currentSwitch = 0;


	//  Build the scene:

	public Group buildScene()
	{
		// Turn on the headlight
		setHeadlightEnable( true );

		// Build the scene root
		switchGroup = new Switch( );
		switchGroup.setCapability( Switch.ALLOW_SWITCH_WRITE );

		// Create application bounds
		BoundingSphere worldBounds = new BoundingSphere(
			new Point3d( 0.0, 0.0, 0.0 ),  // Center
			1000.0 );                      // Extent


		Transform3D t3d;

		Appearance app = new Appearance( );
			Material mat = new Material();
			mat.setAmbientColor( 0.2f, 0.8f, 0.4f );
			mat.setDiffuseColor( 0.2f, 0.8f, 0.4f );
			mat.setSpecularColor( 0.0f, 0.0f, 0.f );
			app.setMaterial( mat );

		// Build the 3D object:
		Box box = new Box( 3.0f, 2.0f, 1.0f, app );

		// Build the shared object:
		sharedObject = new SharedGroup( );
		sharedObject.addChild( box );

		// Build 4 separate transforms:

		Transform3D id = new Transform3D ( );
		TransformGroup idGroup = new TransformGroup( id );
		idGroup.addChild( new Link( sharedObject ) );
		switchGroup.addChild( idGroup );

		Transform3D rot = new Transform3D( );
		rot.set( new AxisAngle4d( 0., 1., 0., Math.PI/4. ) );
		TransformGroup rotGroup = new TransformGroup( rot );
		rotGroup.addChild( new Link( sharedObject ) );
		switchGroup.addChild( rotGroup );

		Transform3D trans = new Transform3D( );
		trans.set( new Vector3d( 2., 0., 0. ) );
		TransformGroup transGroup = new TransformGroup( trans );
		transGroup.addChild( new Link( sharedObject ) );
		switchGroup.addChild( transGroup );

		Transform3D scale = new Transform3D( );
		scale.set( 2.0 );
		TransformGroup scaleGroup = new TransformGroup( scale );
		scaleGroup.addChild( new Link( sharedObject ) );
		switchGroup.addChild( scaleGroup );

		switchGroup.setWhichChild( options[currentSwitch].child );

		return switchGroup;
	}


	//
	//  Main (if invoked as an application)
	//
	public static void main( String[] args )
	{
		ExTransform ex = new ExTransform();
		ex.initialize( args );
		ex.buildUniverse();
		ex.showFrame();
	}

	private NameChildMask[] options =
	{
		new NameChildMask( "Identity",		0,		0 ),
		new NameChildMask( "Rotation", 		1,		0 ),
		new NameChildMask( "Translation", 	2,		0 ),
		new NameChildMask( "Scale",		3,		0 ),
		new NameChildMask( "I+R",	 Switch.CHILD_MASK,	3 ),
		new NameChildMask( "I+T",	 Switch.CHILD_MASK,	5 ),
		new NameChildMask( "I+S",	 Switch.CHILD_MASK,	9 ),
	};

	private CheckboxMenuItem[] switchMenu;
	

	//
	//  Initialize the GUI (application and applet)
	//
	public void initialize( String[] args )
	{
		// Initialize the window, menubar, etc.
		super.initialize( args );
		exampleFrame.setTitle( "Java 3D Transform Example" );

		// Add a menu to select among transform options

		Menu mt = new Menu( "Transform" );
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
				switchGroup.setWhichChild( options[currentSwitch].child );
				switchGroup.setChildMask( options[currentSwitch].mask );
				return;
			}
		}


		// Handle all other checkboxes
		super.itemStateChanged( event );
	}


	public class
	NameChildMask
	{
		public String name;
		public int child;
		public BitSet mask;

		public NameChildMask( String n, int c, int m )
		{
			name = n;
			child = c;
			mask = new BitSet(4);
			if( (m&1) != 0 )	mask.set( 0 );
			if( (m&2) != 0 )	mask.set( 1 );
			if( (m&4) != 0 )	mask.set( 2 );
			if( (m&8) != 0 )	mask.set( 3 );
		}
	}

}

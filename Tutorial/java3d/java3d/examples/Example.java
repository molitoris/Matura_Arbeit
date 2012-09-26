/*
 *
 *  Copyright (c) 1998  David R. Nadeau
 *
 */
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.io.File;
import com.sun.j3d.utils.universe.*;


/**
 *  The Example class is a base class extended by example applications.
 *  The class provides basic features to create a top-level frame, add
 *  a menubar and Canvas3D, build the universe, set up "examine" and
 *  "walk" style navigation behaviors, and provide hooks so that
 *  subclasses can add 3D content to the example's universe.
 *  <P>
 *  Using this Example class simplifies the construction of example
 *  applications, enabling the author to focus upon 3D content and
 *  not the busywork of creating windows, menus, and universes.
 *
 *  @version     1.0, 98/04/16
 *  @author      David R. Nadeau, San Diego Supercomputer Center
 */

public class Example
	extends Applet
	implements WindowListener, ActionListener,
		ItemListener, CheckboxMenuListener
{
	//  Navigation types
	public final static int Walk    = 0;
	public final static int Examine = 1;

	//  Should the scene be compiled?
	private boolean shouldCompile = true;

	//  GUI objects for our subclasses
	protected Example  example         = null;
	protected Frame    exampleFrame    = null;
	protected MenuBar  exampleMenuBar  = null;
	protected Canvas3D exampleCanvas   = null;
	protected TransformGroup exampleViewTransform = null;
	protected TransformGroup exampleSceneTransform = null;
	protected boolean debug = false;

	//  Private GUI objects and state
	private boolean headlightOnOff = true;
	private int navigationType = Examine;
	private CheckboxMenuItem headlightMenuItem = null;
	private CheckboxMenuItem walkMenuItem = null;
	private CheckboxMenuItem examineMenuItem = null;
	private DirectionalLight headlight = null;
	private ExamineViewerBehavior examineBehavior = null;
	private WalkViewerBehavior walkBehavior = null;


	//--------------------------------------------------------------
	//  ADMINISTRATION
	//--------------------------------------------------------------

	/**
	 *  The main program entry point when invoked as an application.
	 *  Each example application that extends this class must define
	 *  their own main.
	 *
	 *  @param   args    a String array of command-line arguments
	 */
	public static void main( String[] args )
	{
		Example ex = new Example( );
		ex.initialize( args );
		ex.buildUniverse( );
		ex.showFrame( );
	}


	/**
	 *  Constructs a new Example object.
	 *
	 *  @return          a new Example that draws no 3D content
	 */
	public Example( )
	{
		// Do nothing
	}


	/**
	 *  Initializes the application when invoked as an applet.
	 */
	public void init( )
	{
		// Collect properties into String array
		String[] args = new String[2];
		// NOTE:  to be done still...

		this.initialize( args );
		this.buildUniverse( );
		this.showFrame( );

		// NOTE:  add something to the browser page?
	}


	/**
	 *  Initializes the Example by parsing command-line arguments,
	 *  building an AWT Frame, constructing a menubar, and creating
	 *  the 3D canvas.
	 *
	 *  @param   args    a String array of command-line arguments
	 */
	protected void initialize( String[] args )
	{
		example = this;

		// Parse incoming arguments
		parseArgs( args );

		// Build the frame
		if ( debug ) System.err.println( "Building GUI..." );
		exampleFrame = new Frame( );
		exampleFrame.setSize( 640, 480 );
		exampleFrame.setTitle( "Java 3D Example" );
		exampleFrame.setLayout( new BorderLayout( ) );

		// Set up a close behavior
		exampleFrame.addWindowListener( this );

		// Create a canvas
		exampleCanvas = new Canvas3D( null );
		exampleCanvas.setSize( 630, 460 );
		exampleFrame.add( "Center", exampleCanvas );

		// Build the menubar
		exampleMenuBar = this.buildMenuBar( );
		exampleFrame.setMenuBar( exampleMenuBar );

		// Pack
		exampleFrame.pack( );
		exampleFrame.validate( );
		//		exampleFrame.setVisible( true );
	}


	/**
	 *  Parses incoming command-line arguments.  Applications that
	 *  subclass this class may override this method to support
	 *  their own command-line arguments.
	 *
	 *  @param   args    a String array of command-line arguments
	 */
	protected void parseArgs( String[] args )
	{
		for ( int i = 0; i < args.length; i++ )
		{
			if ( args[i].equals( "-d" ) )
				debug = true;
		}
	}


	//--------------------------------------------------------------
	//  SCENE CONTENT
	//--------------------------------------------------------------

	/**
	 *  Builds the 3D universe by constructing a virtual universe
	 *  (via SimpleUniverse), a view platform (via SimpleUniverse), and
	 *  a view (via SimpleUniverse).  A headlight is added and a
	 *  set of behaviors initialized to handle navigation types.
	 */
	protected void buildUniverse( )
	{
		//
		//  Create a SimpleUniverse object, which builds:
		//
		//    - a Locale using the given hi-res coordinate origin
		//
		//    - a ViewingPlatform which in turn builds:
		//          - a MultiTransformGroup with which to move the
		//            the ViewPlatform about
		//
		//          - a ViewPlatform to hold the view
		//
		//          - a BranchGroup to hold avatar geometry (if any)
		//
		//          - a BranchGroup to hold view platform
		//            geometry (if any)
		//
		//    - a Viewer which in turn builds:
		//          - a PhysicalBody which characterizes the user's
		//            viewing preferences and abilities
		//
		//          - a PhysicalEnvironment which characterizes the
		//            user's rendering hardware and software
		//
		//          - a JavaSoundMixer which initializes sound
		//            support within the 3D environment
		//
		//          - a View which renders the scene into a Canvas3D
		//
		//  All of these actions could be done explicitly, but
		//  using the SimpleUniverse utilities simplifies the code.
		//
		if ( debug ) System.err.println( "Building scene graph..." );
		SimpleUniverse universe = new SimpleUniverse(
			null,          // Hi-res coordinate for the origin - use default
			1,             // Number of transforms in MultiTransformGroup
			exampleCanvas, // Canvas3D into which to draw
			null );        // URL for user configuration file - use defaults


		//
		//  Get the viewer and create an audio device so that
		//  sound will be enabled in this content.
		//
		Viewer viewer = universe.getViewer( );
		viewer.createAudioDevice( );


		//
		//  Get the viewing platform created by SimpleUniverse.
		//  From that platform, get the inner-most TransformGroup
		//  in the MultiTransformGroup.  That inner-most group
		//  contains the ViewPlatform.  It is this inner-most
		//  TransformGroup we need in order to:
		//
		//    -  add a "headlight" that always aims forward from
		//       the viewer
		//
		//    -  change the viewing direction in a "walk" style
		//
		//  The inner-most TransformGroup's transform will be
		//  changed by the walk behavior (when enabled).
		//
		ViewingPlatform viewingPlatform = universe.getViewingPlatform( );
		exampleViewTransform = viewingPlatform.getViewPlatformTransform( );


		//
		//  Create a "headlight" as a forward-facing directional light.
		//  Set the light's bounds to huge.  Since we want the light
		//  on the viewer's "head", we need the light within the
		//  TransformGroup containing the ViewPlatform.  The
		//  ViewingPlatform class creates a handy hook to do this
		//  called "platform geometry".  The PlatformGeometry class is
		//  subclassed off of BranchGroup, and is intended to contain
		//  a description of the 3D platform itself... PLUS a headlight!
		//  So, to add the headlight, create a new PlatformGeometry group,
		//  add the light to it, then add that platform geometry to the
		//  ViewingPlatform.
		//
		BoundingSphere allBounds = new BoundingSphere(
			new Point3d( 0.0, 0.0, 0.0 ), 100000.0 );

		PlatformGeometry pg = new PlatformGeometry( );
		headlight = new DirectionalLight( );
		headlight.setColor( White );
		headlight.setDirection( new Vector3f( 0.0f, 0.0f, -1.0f ) );
		headlight.setInfluencingBounds( allBounds );
		headlight.setCapability( Light.ALLOW_STATE_WRITE );
		pg.addChild( headlight );
		viewingPlatform.setPlatformGeometry( pg );


		//
		//  Create the 3D content BranchGroup, containing:
		//
		//    - a TransformGroup who's transform the examine behavior
		//      will change (when enabled).
		//
		//    - 3D geometry to view
		//
		// Build the scene root
		BranchGroup sceneRoot = new BranchGroup( );

		// Build a transform that we can modify
		exampleSceneTransform = new TransformGroup( );
		exampleSceneTransform.setCapability(
			TransformGroup.ALLOW_TRANSFORM_READ );
		exampleSceneTransform.setCapability(
			TransformGroup.ALLOW_TRANSFORM_WRITE );
		exampleSceneTransform.setCapability(
			Group.ALLOW_CHILDREN_EXTEND );


		//
		//  Build the scene, add it to the transform, and add
		//  the transform to the scene root
		//
		if ( debug ) System.err.println( "  scene..." );
		Group scene = this.buildScene( );
		exampleSceneTransform.addChild( scene );
		sceneRoot.addChild( exampleSceneTransform );


		//
		//  Create a pair of behaviors to implement two navigation
		//  types:
		//
		//    - "examine":  a style where mouse drags rotate about
		//      the scene's origin as if it is an object under
		//      examination.  This is similar to the "Examine"
		//      navigation type used by VRML browsers.
		//
		//    - "walk":  a style where mouse drags rotate about
		//      the viewer's center as if the viewer is turning
		//      about to look at a scene they are in.  This is
		//      similar to the "Walk" navigation type used by
		//      VRML browsers.
		//
		//  Aim the examine behavior at the scene's TransformGroup
		//  and add the behavior to the scene root.
		//
		//  Aim the walk behavior at the viewing platform's
		//  TransformGroup and add the behavior to the scene root.
		//
		//  Enable one (and only one!) of the two behaviors
		//  depending upon the current navigation type.
		//
		examineBehavior = new ExamineViewerBehavior(
			exampleSceneTransform, // Transform gorup to modify
			exampleFrame );        // Parent frame for cusor changes
		examineBehavior.setSchedulingBounds( allBounds );
		sceneRoot.addChild( examineBehavior );

		walkBehavior = new WalkViewerBehavior(
			exampleViewTransform,  // Transform group to modify
			exampleFrame );        // Parent frame for cusor changes
		walkBehavior.setSchedulingBounds( allBounds );
		sceneRoot.addChild( walkBehavior );

		if ( navigationType == Walk )
		{
			examineBehavior.setEnable( false );
			walkBehavior.setEnable( true );
		}
		else
		{
			examineBehavior.setEnable( true );
			walkBehavior.setEnable( false );
		}


		//
		//  Compile the scene branch group and add it to the
		//  SimpleUniverse.
		//
		if ( shouldCompile )
			sceneRoot.compile( );
		universe.addBranchGraph( sceneRoot );

		reset( );
	}


	/**
	 *  Builds the scene.  Example application subclasses should replace
	 *  this method with their own method to build 3D content.
	 *
	 *  @return          a Group containing 3D content to display
	 */
	public Group buildScene( )
	{
		// Build the scene group containing nothing
		Group scene = new Group( );
		return scene;
	}



	//--------------------------------------------------------------
	//  SET/GET METHODS
	//--------------------------------------------------------------

	/**
	 *  Sets the headlight on/off state.  The headlight faces forward
	 *  in the direction the viewer is facing.  Example applications
	 *  that add their own lights will typically turn the headlight off.
	 *  A standard menu item enables the headlight to be turned on and
	 *  off via user control.
	 *
	 *  @param   onOff   a boolean turning the light on (true) or off (false)
	 */
	public void setHeadlightEnable( boolean onOff )
	{
		headlightOnOff = onOff;
		if ( headlight != null )
			headlight.setEnable( headlightOnOff );
		if ( headlightMenuItem != null )
			headlightMenuItem.setState( headlightOnOff );
	}


	/**
	 *  Gets the headlight on/off state.
	 *
	 *  @return          a boolean indicating if the headlight is on or off
	 */
	public boolean getHeadlightEnable( )
	{
		return headlightOnOff;
	}


	/**
	 *  Sets the navigation type to be either Examine or Walk.  The
	 *  Examine navigation type sets up behaviors that use mouse drags
	 *  to rotate and translate scene content as if it is an object
	 *  held at arm's length and under examination.  The Walk navigation
	 *  type uses mouse drags to rotate and translate the viewer as if
	 *  they are walking through the content.  The Examine type is the
	 *  default.
	 *
	 *  @param   nav     either Walk or Examine
	 */
	public void setNavigationType( int nav )
	{
		if ( nav == Walk )
		{
			navigationType = Walk;
			if ( walkMenuItem != null )
				walkMenuItem.setState( true );
			if ( examineMenuItem != null )
				examineMenuItem.setState( false );
			if ( walkBehavior != null )
				walkBehavior.setEnable( true );
			if ( examineBehavior != null )
				examineBehavior.setEnable( false );
		}
		else
		{
			navigationType = Examine;
			if ( walkMenuItem != null )
				walkMenuItem.setState( false );
			if ( examineMenuItem != null )
				examineMenuItem.setState( true );
			if ( walkBehavior != null )
				walkBehavior.setEnable( false );
			if ( examineBehavior != null )
				examineBehavior.setEnable( true );
		}
	}


	/**
	 *  Gets the current navigation type, returning either Walk or
	 *  Examine.
	 *
	 *  @return          either Walk or Examine
	 */
	public int getNavigationType( )
	{
		return navigationType;
	}


	/**
	 *  Sets whether the scene graph should be compiled or not.
	 *  Normally this is always a good idea.  For some example
	 *  applications that use this Example framework, it is useful
	 *  to disable compilation - particularly when nodes and node
	 *  components will need to be made un-live in order to make
	 *  changes.  Once compiled, such components can be made un-live,
	 *  but they are still unchangable unless appropriate capabilities
	 *  have been set.
	 *
	 *  @param   onOff   a boolean turning compilation on (true) or off (false)
	 */
	public void setCompilable( boolean onOff )
	{
		shouldCompile = onOff;
	}


	/**
	 *  Gets whether the scene graph will be compiled or not.
	 *
	 *  @return          a boolean indicating if scene graph compilation is on or off
	 */
	public boolean getCompilable( )
	{
		return shouldCompile;
	}






// These methods will be replaced
	//  Set the view position and direction
	public void setViewpoint( Point3f position, Vector3f direction )
	{
		Transform3D t = new Transform3D( );
		t.set( new Vector3f( position ) );
		exampleViewTransform.setTransform( t );
		// how to set direction?
	}


	//  Reset transforms
	public void reset( )
	{
		Transform3D trans = new Transform3D( );
		exampleSceneTransform.setTransform( trans );
		trans.set( new Vector3f( 0.0f, 0.0f, 10.0f ) );
		exampleViewTransform.setTransform( trans );
		setNavigationType( navigationType );
	}


	//
	//  Gets the URL (with file: prepended) for the current directory.
	//  This is a terrible hack needed in the Alpha release of Java3D
	//  in order to build a full path URL for loading sounds with
	//  MediaContainer.  When MediaContainer is fully implemented,
	//  it should handle relative path names, but not yet.
	//
	public String getCurrentDirectory( )
	{
		// Create a bogus file so that we can query it's path
		File dummy = new File( "dummy.tmp" );
		String dummyPath =  dummy.getAbsolutePath();

		// strip "/dummy.tmp" from end of dummyPath and put into 'path'
		if (dummyPath.endsWith( File.separator + "dummy.tmp" ))
		{
			int index = dummyPath.lastIndexOf(
				File.separator + "dummy.tmp" );
			if ( index >= 0 )
			{
				int pathLength = index + 5; // pre-pend 'file:'
				char[] charPath = new char[pathLength];
				dummyPath.getChars(0, index, charPath, 5);
				String path = new String(charPath, 0, pathLength);
				path = "file:" + path.substring(5, pathLength);
				return path + File.separator;
			}
		}
		return dummyPath + File.separator;
	}




	//--------------------------------------------------------------
	//  USER INTERFACE
	//--------------------------------------------------------------

	/**
	 *  Builds the example AWT Frame menubar.  Standard menus and
	 *  their options are added.  Applications that subclass this
	 *  class should build their menubar additions within their
	 *  initialize method.
	 *
	 *  @return          a MenuBar for the AWT Frame
	 */
	private MenuBar buildMenuBar( )
	{
		// Build the menubar
		MenuBar menuBar = new MenuBar( );

		// File menu
		Menu m = new Menu( "File" );
		m.addActionListener( this );

			m.add( "Exit" );

		menuBar.add( m );

		// View menu
		m = new Menu( "View" );
		m.addActionListener( this );

			m.add( "Reset view" );

			m.addSeparator( );

			walkMenuItem =
				new CheckboxMenuItem( "Walk" );
			walkMenuItem.addItemListener( this );
			m.add( walkMenuItem );

			examineMenuItem =
				new CheckboxMenuItem( "Examine" );
			examineMenuItem.addItemListener( this );
			m.add( examineMenuItem );

			if ( navigationType == Walk )
			{
				walkMenuItem.setState( true );
				examineMenuItem.setState( false );
			}
			else
			{
				walkMenuItem.setState( false );
				examineMenuItem.setState( true );
			}

			m.addSeparator( );

			headlightMenuItem =
				new CheckboxMenuItem( "Headlight on/off" );
			headlightMenuItem.addItemListener( this );
			headlightMenuItem.setState( headlightOnOff );
			m.add( headlightMenuItem );

		menuBar.add( m );

		return menuBar;
	}


	/**
	 *  Shows the application's frame, making it and its menubar,
	 *  3D canvas, and 3D content visible.
	 */
	public void showFrame( )
	{
		exampleFrame.show( );
	}


	/**
	 *  Quits the application.
	 */
	public void quit( )
	{
		System.exit( 0 );
	}


	/**
	 *  Handles menu selections.
	 *
	 *  @param   event   an ActionEvent indicating what menu action
	 *                   requires handling
	 */
	public void actionPerformed( ActionEvent event )
	{
		String arg = event.getActionCommand( );
		if ( arg.equals( "Reset view" ) )
			reset( );
		else if ( arg.equals( "Exit" ) )
			quit( );
	}


	/**
	 *  Handles checkbox items on a CheckboxMenu.  The Example class has
	 *  none of its own, but subclasses may have some.
	 *
	 *  @param   menu    which CheckboxMenu needs action
	 *  @param   check   which CheckboxMenu item has changed
	 */
	public void checkboxChanged( CheckboxMenu menu, int check )
	{
		// None for us
	}


	/**
	 *  Handles on/off checkbox items on a standard menu.
	 *
	 *  @param   event   an ItemEvent indicating what requires handling
	 */
	public void itemStateChanged( ItemEvent event )
	{
		Object src = event.getSource( );
		boolean state;
		if ( src == headlightMenuItem )
		{
			state = headlightMenuItem.getState( );
			headlight.setEnable( state );
		}
		else if ( src == walkMenuItem )
			setNavigationType( Walk );
		else if ( src == examineMenuItem )
			setNavigationType( Examine );
	}


	/**
	 *  Handles a window closing event notifying the application
	 *  that the user has chosen to close the application without
	 *  selecting the "Exit" menu item.
	 *
	 *  @param   event   a WindowEvent indicating the window is closing
	 */
	public void windowClosing( WindowEvent event )
	{
		quit( );
	}
	public void windowClosed( WindowEvent event )
	{
	}
	public void windowOpened( WindowEvent event )
	{
	}
	public void windowIconified( WindowEvent event )
	{
	}
	public void windowDeiconified( WindowEvent event )
	{
	}
	public void windowActivated( WindowEvent event )
	{
	}
	public void windowDeactivated( WindowEvent event )
	{
	}



	//  Well known colors, positions, and directions
	public final static Color3f White    = new Color3f( 1.0f, 1.0f, 1.0f );
	public final static Color3f Gray     = new Color3f( 0.7f, 0.7f, 0.7f );
	public final static Color3f DarkGray = new Color3f( 0.2f, 0.2f, 0.2f );
	public final static Color3f Black    = new Color3f( 0.0f, 0.0f, 0.0f );
	public final static Color3f Red      = new Color3f( 1.0f, 0.0f, 0.0f );
	public final static Color3f DarkRed  = new Color3f( 0.3f, 0.0f, 0.0f );
	public final static Color3f Yellow   = new Color3f( 1.0f, 1.0f, 0.0f );
	public final static Color3f DarkYellow=new Color3f( 0.3f, 0.3f, 0.0f );
	public final static Color3f Green    = new Color3f( 0.0f, 1.0f, 0.0f );
	public final static Color3f DarkGreen= new Color3f( 0.0f, 0.3f, 0.0f );
	public final static Color3f Cyan     = new Color3f( 0.0f, 1.0f, 1.0f );
	public final static Color3f Blue     = new Color3f( 0.0f, 0.0f, 1.0f );
	public final static Color3f DarkBlue = new Color3f( 0.0f, 0.0f, 0.3f );
	public final static Color3f Magenta  = new Color3f( 1.0f, 0.0f, 1.0f );

	public final static Vector3f PosX = new Vector3f(  1.0f,  0.0f,  0.0f );
	public final static Vector3f NegX = new Vector3f( -1.0f,  0.0f,  0.0f );
	public final static Vector3f PosY = new Vector3f(  0.0f,  1.0f,  0.0f );
	public final static Vector3f NegY = new Vector3f(  0.0f, -1.0f,  0.0f );
	public final static Vector3f PosZ = new Vector3f(  0.0f,  0.0f,  1.0f );
	public final static Vector3f NegZ = new Vector3f(  0.0f,  0.0f, -1.0f );

	public final static Point3f Origin = new Point3f(  0.0f,   0.0f,   0.0f );
	public final static Point3f PlusX  = new Point3f(  0.75f,  0.0f,   0.0f );
	public final static Point3f MinusX = new Point3f( -0.75f,  0.0f,   0.0f );
	public final static Point3f PlusY  = new Point3f(  0.0f,   0.75f,  0.0f );
	public final static Point3f MinusY = new Point3f(  0.0f,  -0.75f,  0.0f );
	public final static Point3f PlusZ  = new Point3f(  0.0f,   0.0f,   0.75f);
	public final static Point3f MinusZ = new Point3f(  0.0f,   0.0f,  -0.75f);
}

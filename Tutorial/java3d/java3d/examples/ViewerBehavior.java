import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;


/**
 * ViewerBehavior
 *
 * @version		1.0, 98/04/16
 **/

/**
 * Wakeup on mouse button presses, releases, and mouse movements and
 * generate transforms for a transform group.  Classes that extend this
 * class impose specific symantics, such as "Examine" or "Walk" viewing,
 * similar to the navigation types used by VRML browsers.
 *
 * To support systems with 2 or 1 mouse buttons, the following
 * alternate mappings are supported while dragging with any mouse
 * button held down and zero or more keyboard modifiers held down:
 *
 *   No modifiers = Button 1
 *   ALT = Button 2
 *   Meta = Button 3
 *   Control = Button 3
 *
 * The behavior automatically modifies a TransformGroup provided
 * to the constructor.  The TransformGroup's transform can be set
 * at any time by the application or other behaviors to cause the
 * viewer's rotation and translation to be reset.
 */

// This class is inspired by the MouseBehavior, MouseRotate,
// MouseTranslate, and MouseZoom utility behaviors provided with
// Java 3D.  This class differs from those utilities in that it:
//
//    (a) encapsulates all three behaviors into one in order to
//        enforce a specific viewing symantic
//
//    (b) supports set/get of the rotation and translation factors
//        that control the speed of movement.
//
//    (c) supports the "Control" modifier as an alternative to the
//        "Meta" modifier not present on PC, Mac, and most non-Sun
//        keyboards.  This makes button3 behavior usable on PCs,
//        Macs, and other systems with fewer than 3 mouse buttons.

public abstract class ViewerBehavior
	extends Behavior
{
	// Keep track of the transform group who's transform we modify
	// during mouse motion.
	protected TransformGroup subjectTransformGroup = null;

	// Keep a set of wakeup criterion for different mouse-generated
	// event types.
	protected WakeupCriterion[] mouseEvents = null;
	protected WakeupOr mouseCriterion = null;

	// Track which button was last pressed
	protected static final int BUTTONNONE = -1;
	protected static final int BUTTON1 = 0;
	protected static final int BUTTON2 = 1;
	protected static final int BUTTON3 = 2;
	protected int buttonPressed = BUTTONNONE;

	// Keep a few Transform3Ds for use during event processing.  This
	// avoids having to allocate new ones on each event.
	protected Transform3D currentTransform = new Transform3D( );
	protected Transform3D transform1 = new Transform3D( );
	protected Transform3D transform2 = new Transform3D( );
	protected Matrix4d matrix = new Matrix4d( );
	protected Vector3d origin = new Vector3d( 0.0, 0.0, 0.0 );
	protected Vector3d translate = new Vector3d( 0.0, 0.0, 0.0 );

	// Unusual X and Y delta limits.
	protected static final int UNUSUAL_XDELTA = 400;
	protected static final int UNUSUAL_YDELTA = 400;

	protected Component parentComponent = null;


	/**
	 * Construct a viewer behavior that listens to mouse movement
	 * and button presses to generate rotation and translation
	 * transforms written into a transform group given later
	 * with the setTransformGroup( ) method.
	 */
	public ViewerBehavior( )
	{
		super( );
	}


	/**
	 * Construct a viewer behavior that listens to mouse movement
	 * and button presses to generate rotation and translation
	 * transforms written into a transform group given later
	 * with the setTransformGroup( ) method.
	 *
	 * @param parent The AWT Component that contains the area
	 * generating mouse events.
	 */
	public ViewerBehavior( Component parent )
	{
		super( );
		parentComponent = parent;
	}


	/**
	 * Construct a viewer behavior that listens to mouse movement
	 * and button presses to generate rotation and translation
	 * transforms written into the given transform group.
	 *
	 * @param transformGroup The transform group to be modified
	 * by the behavior.
	 */
	public ViewerBehavior( TransformGroup transformGroup )
	{
		super( );
		subjectTransformGroup = transformGroup;
	}


	/**
	 * Construct a viewer behavior that listens to mouse movement
	 * and button presses to generate rotation and translation
	 * transforms written into the given transform group.
	 *
	 * @param transformGroup The transform group to be modified
	 * by the behavior.
	 * @param parent The AWT Component that contains the area
	 * generating mouse events.
	 */
	public ViewerBehavior( TransformGroup transformGroup, Component parent )
	{
		super( );
		subjectTransformGroup = transformGroup;
		parentComponent = parent;
	}


	/**
	 * Set the transform group modified by the viewer behavior.
	 * Setting the transform group to null disables the behavior
	 * until the transform group is again set to an existing group.
	 *
	 * @param transformGroup The new transform group to be modified
	 * by the behavior.
	 */
	public void setTransformGroup( TransformGroup transformGroup )
	{
		subjectTransformGroup = transformGroup;
	}


	/**
	 * Get the transform group modified by the viewer behavior.
	 */
	public TransformGroup getTransformGroup( )
	{
		return subjectTransformGroup;
	}


	/**
	 *  Sets the parent component who's cursor will be changed during
	 *  mouse drags.  If no component is given is given to the
	 *  constructor, or set via this method, no cursor changes
	 *  will be done.
	 *
	 *  @param  parent  the AWT Component, such as a Frame, within which
	 *                  cursor changes should take place during
	 *                  mouse drags
	 */
	public void setParentComponent( Component parent )
	{
		parentComponent = parent;
	}


	/*
	 *  Gets the parent frame within which the cursor changes
	 *  during mouse drags.
	 *
	 *  @return         the AWT Component, such as a Frame, within which
	 *                  cursor changes should take place during
	 *                  mouse drags.  Returns null if no parent is set.
	 */
	public Component getParentComponent( )
	{
		return parentComponent;
	}


	/**
	 * Initialize the behavior.
	 */
	public void initialize( )
	{
		// Wakeup when the mouse is dragged or when a mouse button
		// is pressed or released.
		mouseEvents = new WakeupCriterion[3];
		mouseEvents[0] = new WakeupOnAWTEvent( MouseEvent.MOUSE_DRAGGED );
		mouseEvents[1] = new WakeupOnAWTEvent( MouseEvent.MOUSE_PRESSED );
		mouseEvents[2] = new WakeupOnAWTEvent( MouseEvent.MOUSE_RELEASED );
		mouseCriterion = new WakeupOr( mouseEvents );
		wakeupOn( mouseCriterion );
	}



	/**
	 * Process a new wakeup.  Interpret mouse button presses, releases,
	 * and mouse drags.
	 *
	 * @param criteria The wakeup criteria causing the behavior wakeup.
	 */
	public void processStimulus( Enumeration criteria )
	{
		WakeupCriterion wakeup = null;
		AWTEvent[] event = null;
		int whichButton = BUTTONNONE;


		// Process all pending wakeups
		while( criteria.hasMoreElements( ) )
		{
			wakeup = (WakeupCriterion)criteria.nextElement( );
			if ( wakeup instanceof WakeupOnAWTEvent )
			{
				event = ((WakeupOnAWTEvent)wakeup).getAWTEvent( );

				// Process all pending events
				for ( int i = 0; i < event.length; i++ )
				{
					if ( event[i].getID( ) != MouseEvent.MOUSE_PRESSED &&
						event[i].getID( ) != MouseEvent.MOUSE_RELEASED &&
						event[i].getID( ) != MouseEvent.MOUSE_DRAGGED )
						// Ignore uninteresting mouse events
						continue;

					//
					// Regretably, Java event handling (or perhaps
					// underlying OS event handling) doesn't always
					// catch button bounces (redundant presses and
					// releases), or order events so that the last
					// drag event is delivered before a release.
					// This means we can get stray events that we
					// filter out here.
					//
					if ( event[i].getID( ) == MouseEvent.MOUSE_PRESSED &&
						buttonPressed != BUTTONNONE )
						// Ignore additional button presses until a release
						continue;

					if ( event[i].getID( ) == MouseEvent.MOUSE_RELEASED &&
						buttonPressed == BUTTONNONE )
						// Ignore additional button releases until a press
						continue;

					if ( event[i].getID( ) == MouseEvent.MOUSE_DRAGGED &&
						buttonPressed == BUTTONNONE )
						// Ignore drags until a press
						continue;

					MouseEvent mev = (MouseEvent)event[i];
					int modifiers = mev.getModifiers( );

					//
					// Unfortunately, the underlying event handling
					// doesn't do a "grab" operation when a mouse button
					// is pressed.  This means that once a button is
					// pressed, if another mouse button or a keyboard
					// modifier key is pressed, the delivered mouse event
					// will show that a different button is being held
					// down.  For instance:
					//
					// Action                           Event
					//  Button 1 press                  Button 1 press
					//  Drag with button 1 down         Button 1 drag
					//  ALT press                       -
					//  Drag with ALT & button 1 down   Button 2 drag
					//  Button 1 release                Button 2 release
					//
					// The upshot is that we can get a button press
					// without a matching release, and the button
					// associated with a drag can change mid-drag.
					//
					// To fix this, we watch for an initial button
					// press, and thenceforth consider that button
					// to be the one held down, even if additional
					// buttons get pressed, and despite what is
					// reported in the event.  Only when a button is
					// released, do we end such a grab.
					//

					if ( buttonPressed == BUTTONNONE )
					{
						// No button is pressed yet, figure out which
						// button is down now and how to direct events
						if ( ( (modifiers & InputEvent.BUTTON3_MASK)
								!= 0 ) ||
							( ( (modifiers & InputEvent.BUTTON1_MASK)
								!= 0 ) &&
							( (modifiers & InputEvent.CTRL_MASK)
								== InputEvent.CTRL_MASK ) ) )
						{
							// Button 3 activity (META or CTRL down)
							whichButton = BUTTON3;
						}
						else if ( (modifiers & InputEvent.BUTTON2_MASK)
							!= 0 )
						{
							// Button 2 activity (ALT down)
							whichButton = BUTTON2;
						}
						else
						{
							// Button 1 activity (no modifiers down)
							whichButton = BUTTON1;
						}

						// If the event is to press a button, then
						// record that that button is now down
						if ( event[i].getID( ) == MouseEvent.MOUSE_PRESSED )
							buttonPressed = whichButton;
					}
					else
					{
						// Otherwise a button was pressed earlier and
						// hasn't been released yet.  Assign all further
						// events to it, even if ALT, META, CTRL, or
						// another button has been pressed as well.
						whichButton = buttonPressed;
					}

					// Distribute the event
					switch( whichButton )
					{
					case BUTTON1:
						onButton1( mev );
						break;
					case BUTTON2:
						onButton2( mev );
						break;
					case BUTTON3:
						onButton3( mev );
						break;
					default:
						break;
					}

					// If the event is to release a button, then
					// record that that button is now up
					if ( event[i].getID( ) == MouseEvent.MOUSE_RELEASED )
						buttonPressed = BUTTONNONE;
				}
				continue;
			}

			if ( wakeup instanceof WakeupOnElapsedFrames )
			{
				onElapsedFrames( (WakeupOnElapsedFrames) wakeup );
				continue;
			}
		}

		// Reschedule us for another wakeup
		wakeupOn( mouseCriterion );
	}




	/**
	 * Default X and Y rotation factors, and XYZ translation factors.
	 */
	public static final double DEFAULT_XROTATION_FACTOR = 0.02;
	public static final double DEFAULT_YROTATION_FACTOR = 0.005;
	public static final double DEFAULT_XTRANSLATION_FACTOR = 0.02;
	public static final double DEFAULT_YTRANSLATION_FACTOR = 0.02;
	public static final double DEFAULT_ZTRANSLATION_FACTOR = 0.04;

	protected double XRotationFactor    = DEFAULT_XROTATION_FACTOR;
	protected double YRotationFactor    = DEFAULT_YROTATION_FACTOR;
	protected double XTranslationFactor = DEFAULT_XTRANSLATION_FACTOR;
	protected double YTranslationFactor = DEFAULT_YTRANSLATION_FACTOR;
	protected double ZTranslationFactor = DEFAULT_ZTRANSLATION_FACTOR;


	/**
	 * Set the X rotation scaling factor for X-axis rotations.
	 *
	 * @param factor The new scaling factor.
	 */
	public void setXRotationFactor( double factor )
	{
		XRotationFactor = factor;
	}

	/**
	 * Get the current X rotation scaling factor for X-axis rotations.
	 */
	public double getXRotationFactor( )
	{
		return XRotationFactor;
	}

	/**
	 * Set the Y rotation scaling factor for Y-axis rotations.
	 *
	 * @param factor The new scaling factor.
	 */
	public void setYRotationFactor( double factor )
	{
		YRotationFactor = factor;
	}

	/**
	 * Get the current Y rotation scaling factor for Y-axis rotations.
	 */
	public double getYRotationFactor( )
	{
		return YRotationFactor;
	}


	/**
	 * Set the X translation scaling factor for X-axis translations.
	 *
	 * @param factor The new scaling factor.
	 */
	public void setXTranslationFactor( double factor )
	{
		XTranslationFactor = factor;
	}

	/**
	 * Get the current X translation scaling factor for X-axis translations.
	 */
	public double getXTranslationFactor( )
	{
		return XTranslationFactor;
	}

	/**
	 * Set the Y translation scaling factor for Y-axis translations.
	 *
	 * @param factor The new scaling factor.
	 */
	public void setYTranslationFactor( double factor )
	{
		YTranslationFactor = factor;
	}

	/**
	 * Get the current Y translation scaling factor for Y-axis translations.
	 */
	public double getYTranslationFactor( )
	{
		return YTranslationFactor;
	}

	/**
	 * Set the Z translation scaling factor for Z-axis translations.
	 *
	 * @param factor The new scaling factor.
	 */
	public void setZTranslationFactor( double factor )
	{
		ZTranslationFactor = factor;
	}

	/**
	 * Get the current Z translation scaling factor for Z-axis translations.
	 */
	public double getZTranslationFactor( )
	{
		return ZTranslationFactor;
	}


	/**
	 * Respond to a button1 event (press, release, or drag).
	 *
	 * @param mouseEvent A MouseEvent to respond to.
	 */
	public abstract void onButton1( MouseEvent mouseEvent );


	/**
	 * Respond to a button2 event (press, release, or drag).
	 *
	 * @param mouseEvent A MouseEvent to respond to.
	 */
	public abstract void onButton2( MouseEvent mouseEvent );


	/**
	 * Responed to a button3 event (press, release, or drag).
	 *
	 * @param mouseEvent A MouseEvent to respond to.
	 */
	public abstract void onButton3( MouseEvent mouseEvent );


	/**
	 * Respond to an elapsed frames event (assuming subclass has set up a
	 * wakeup criterion for it).
	 *
	 * @param time A WakeupOnElapsedFrames criterion to respond to.
	 */
	public abstract void onElapsedFrames( WakeupOnElapsedFrames timeEvent );
}

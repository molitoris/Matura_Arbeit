/*
 *
 *  Copyright (c) 1998  David R. Nadeau
 *
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;


/**
 *  WalkViewerBehavior is a utility class that creates a "walking style"
 *  navigation symantic.
 *
 *  The behavior wakes up on mouse button presses, releases, and
 *  mouse movements and generates transforms in a "walk style" that
 *  enables the user to walk through a scene, translating and turning
 *  about as if they are within the scene.  Such a walk style is
 *  similar to the "Walk" navigation type used by VRML browsers.
 *  <P>
 *  The behavior maps mouse drags to different transforms depending
 *  upon the mouse button held down:
 *  <DL>
 *  <DT> Button 1 (left)
 *     <DD> Horizontal movement --> Y-axis rotation
 *     <DD> Vertical movement --> Z-axis translation
 *
 *  <DT> Button 2 (middle)
 *     <DD> Horizontal movement --> Y-axis rotation
 *     <DD> Vertical movement --> X-axis rotation
 *
 *  <DT> Button 3 (right)
 *     <DD> Horizontal movement --> X-axis translation
 *     <DD> Vertical movement --> Y-axis translation
 *  </DL>
 *
 *  To support systems with 2 or 1 mouse buttons, the following
 *  alternate mappings are supported while dragging with any mouse
 *  button held down and zero or more keyboard modifiers held down:
 *  <UL>
 *   <LI>No modifiers = Button 1
 *   <LI>ALT = Button 2
 *   <LI>Meta = Button 3
 *   <LI>Control = Button 3
 *  </UL>
 *  The behavior automatically modifies a TransformGroup provided
 *  to the constructor.  The TransformGroup's transform can be set
 *  at any time by the application or other behaviors to cause the
 *  walk rotation and translation to be reset.
 *  <P>
 *  While a mouse button is down, the behavior automatically changes
 *  the cursor in a given parent AWT Component.  If no parent
 *  Component is given, no cursor changes are attempted.
 *
 *  @version     1.0, 98/04/16
 *  @author      David R. Nadeau, San Diego Supercomputer Center
 */
public class WalkViewerBehavior
	extends ViewerBehavior
{
	// This class is inspired by the MouseBehavior, MouseRotate,
	// MouseTranslate, and MouseZoom utility behaviors provided with
	// Java 3D.  This class differs from those utilities in that it:
	//
	//    (a) encapsulates all three behaviors into one in order to
	//        enforce a specific "Walk" symantic
	//
	//    (b) supports set/get of the rotation and translation factors
	//        that control the speed of movement.
	//
	//    (c) supports the "Control" modifier as an alternative to the
	//        "Meta" modifier not present on PC, Mac, and most non-Sun
	//        keyboards.  This makes button3 behavior usable on PCs,
	//        Macs, and other systems with fewer than 3 mouse buttons.

	// Previous and initial cursor locations
	protected int previousX = 0;
	protected int previousY = 0;
	protected int initialX = 0;
	protected int initialY = 0;

	// Deadzone size (delta from initial XY for which no
	// translate or rotate action is taken
	protected static final int DELTAX_DEADZONE = 10;
	protected static final int DELTAY_DEADZONE = 10;

	// Keep a set of wakeup criterion for animation-generated
	// event types.
	protected WakeupCriterion[] mouseAndAnimationEvents = null;
	protected WakeupOr mouseAndAnimationCriterion = null;
	protected WakeupOr savedMouseCriterion = null;

	// Saved standard cursor
	protected Cursor savedCursor = null;


	/**
	 *  Default Rotation and translation scaling factors for
	 *  animated movements (Button 1 press).
	 */
	public static final double DEFAULT_YROTATION_ANIMATION_FACTOR = 0.0002;
	public static final double DEFAULT_ZTRANSLATION_ANIMATION_FACTOR = 0.01;

	protected double YRotationAnimationFactor =
		DEFAULT_YROTATION_ANIMATION_FACTOR;
	protected double ZTranslationAnimationFactor =
		DEFAULT_ZTRANSLATION_ANIMATION_FACTOR;


	/**
	 *  Constructs a new walk behavior that converts mouse actions
	 *  into rotations and translations.  Rotations and translations
	 *  are written into a TransformGroup that must be set using the
	 *  setTransformGroup method.  The cursor will be changed during
	 *  mouse actions if the parent frame is set using the
	 *  setParentComponent method.
	 *
	 *  @return          a new WalkViewerBehavior that needs its
	 *                   TransformGroup and parent Component set
	 */
	public WalkViewerBehavior( )
	{
		super( );
	}


	/**
	 *  Constructs a new walk behavior that converts mouse actions
	 *  into rotations and translations.  Rotations and translations
	 *  are written into a TransformGroup that must be set using the
	 *  setTransformGroup method.  The cursor will be changed within
	 *  the given AWT parent Component during mouse drags.
	 *
	 *  @param   parent  a parent AWT Component within which the cursor
	 *                   will change during mouse drags
	 *
	 *  @return          a new WalkViewerBehavior that needs its
	 *                   TransformGroup and parent Component set
	 */
	public WalkViewerBehavior( Component parent )
	{
		super( parent );
	}


	/**
	 *  Constructs a new walk behavior that converts mouse actions
	 *  into rotations and translations.  Rotations and translations
	 *  are written into the given TransformGroup.  The cursor will
	 *  be changed during mouse actions if the parent frame is set
	 *  using the setParentComponent method.
	 *
	 *  @param   transformGroup   a TransformGroup whos transform is
	 *                   read and written by the behavior
	 *
	 *  @return          a new WalkViewerBehavior that needs its
	 *                   TransformGroup and parent Component set
	 */
	public WalkViewerBehavior( TransformGroup transformGroup )
	{
		super( );
		subjectTransformGroup = transformGroup;
	}


	/**
	 *  Constructs a new walk behavior that converts mouse actions
	 *  into rotations and translations.  Rotations and translations
	 *  are written into the given TransformGroup.  The cursor will be
	 *  changed within the given AWT parent Component during mouse drags.
	 *
	 *  @param   transformGroup   a TransformGroup whos transform is
	 *                   read and written by the behavior
	 *
	 *  @param   parent  a parent AWT Component within which the cursor
	 *                   will change during mouse drags
	 *
	 *  @return          a new WalkViewerBehavior that needs its
	 *                   TransformGroup and parent Component set
	 */
	public WalkViewerBehavior( TransformGroup transformGroup,
		Component parent )
	{
		super( parent );
		subjectTransformGroup = transformGroup;
	}


	/**
	 *  Initializes the behavior.
	 */
	public void initialize( )
	{
		super.initialize( );
		savedMouseCriterion = mouseCriterion;  // from parent class
		mouseAndAnimationEvents = new WakeupCriterion[4];
		mouseAndAnimationEvents[0] =
			new WakeupOnAWTEvent( MouseEvent.MOUSE_DRAGGED );
		mouseAndAnimationEvents[1] =
			new WakeupOnAWTEvent( MouseEvent.MOUSE_PRESSED );
		mouseAndAnimationEvents[2] =
			new WakeupOnAWTEvent( MouseEvent.MOUSE_RELEASED );
		mouseAndAnimationEvents[3] =
			new WakeupOnElapsedFrames( 0 );
		mouseAndAnimationCriterion =
			new WakeupOr( mouseAndAnimationEvents );
		// Don't use the above criterion until a button 1 down event
	}


	/**
	 *  Sets the Y rotation animation scaling factor for Y-axis rotations.
	 *  This scaling factor is used to control the speed of Y rotation
	 *  when button 1 is pressed and dragged.
	 *
	 *  @param  factor   the double Y rotation scaling factor
	 */
	public void setYRotationAnimationFactor( double factor )
	{
		YRotationAnimationFactor = factor;
	}


	/**
	 *  Gets the current Y animation rotation scaling factor for Y-axis
	 *  rotations.
	 *
	 *  @return          the double Y rotation scaling factor
	 */
	public double getYRotationAnimationFactor( )
	{
		return YRotationAnimationFactor;
	}


	/**
	 *  Sets the Z animation translation scaling factor for Z-axis
	 *  translations.  This scaling factor is used to control the speed
	 *  of Z translation when button 1 is pressed and dragged.
	 *
	 *  @param  factor   the double Z translation scaling factor
	 */
	public void setZTranslationAnimationFactor( double factor )
	{
		ZTranslationAnimationFactor = factor;
	}


	/**
	 *  Gets the current Z animation translation scaling factor for Z-axis
	 *  translations.
	 *
	 *  @return          the double Z translation scaling factor
	 */
	public double getZTranslationAnimationFactor( )
	{
		return ZTranslationAnimationFactor;
	}


	/**
	 *  Responds to an elapsed frames event.  Such an event is generated
	 *  on every frame while button 1 is held down.  On each call, this
	 *  method computes new Y-axis rotation and Z-axis translation
	 *  values and writes them to the behavior's TransformGroup.
	 *  The translation and rotation amounts are computed based upon the
	 *  distance between the current cursor location and the cursor
	 *  location when button 1 was pressed.  As this distance increases,
	 *  the translation or rotation amount increases.
	 *
	 *  @param  time     the WakeupOnElapsedFrames criterion to respond to
	 */
	public void onElapsedFrames( WakeupOnElapsedFrames timeEvent )
	{
		//
		// Time elapsed while button down:  create a rotation and
		// a translation.
		//
		// Compute the delta in X and Y from the initial position to
		// the previous position.  Multiply the delta times a scaling
		// factor to compute an offset to add to the current translation
		// and rotation.  Use the mapping:
		//
		//   positive X mouse delta --> negative Y-axis rotation
		//   positive Y mouse delta --> positive Z-axis translation
		//
		// where positive X mouse movement is to the right, and
		// positive Y mouse movement is **down** the screen.
		//
		if ( buttonPressed != BUTTON1 )
			return;
		int deltaX = previousX - initialX;
		int deltaY = previousY - initialY;

		double yRotationAngle = -deltaX * YRotationAnimationFactor;
		double zTranslationDistance = deltaY * ZTranslationAnimationFactor;

		//
		// Build transforms
		//
		transform1.rotY( yRotationAngle );
		translate.set( 0.0, 0.0, zTranslationDistance );

		// Get and save the current transform matrix
		subjectTransformGroup.getTransform( currentTransform );
		currentTransform.get( matrix );

		// Translate to the origin, rotate, then translate back
		currentTransform.setTranslation( origin );
		currentTransform.mul( transform1, currentTransform );

		// Translate back from the origin by the original translation
		// distance, plus the new walk translation... but force walk
		// to travel on a plane by ignoring the Y component of a
		// transformed translation vector.
		currentTransform.transform( translate );
		translate.x += matrix.m03;  // add in existing X translation
		translate.y  = matrix.m13;  // use Y translation
		translate.z += matrix.m23;  // add in existing Z translation
		currentTransform.setTranslation( translate );

		// Update the transform group
		subjectTransformGroup.setTransform( currentTransform );
	}


	/**
	 *  Responds to a button1 event (press, release, or drag).  On a
	 *  press,  the method adds a wakeup criterion to the behavior's
	 *  set, callling for the behavior to be awoken on each frame.
	 *  On a button prelease, this criterion is removed from the set.
	 *
	 *  @param  mouseEvent  the MouseEvent to respond to
	 */
	public void onButton1( MouseEvent mev )
	{
		if ( subjectTransformGroup == null )
			return;

		int x = mev.getX( );
		int y = mev.getY( );

		if ( mev.getID( ) == MouseEvent.MOUSE_PRESSED )
		{
			// Mouse button pressed:  record position and change
			// the wakeup criterion to include elapsed time wakeups
			// so we can animate.
			previousX = x;
			previousY = y;
			initialX  = x;
			initialY  = y;

			// Swap criterion... parent class will not reschedule us
			mouseCriterion = mouseAndAnimationCriterion;

			// Change to a "move" cursor
			if ( parentComponent != null )
			{
				savedCursor = parentComponent.getCursor( );
				parentComponent.setCursor(
					Cursor.getPredefinedCursor( Cursor.HAND_CURSOR ) );
			}
			return;
		}
		if ( mev.getID( ) == MouseEvent.MOUSE_RELEASED )
		{
			// Mouse button released:  restore original wakeup
			// criterion which only includes mouse activity, not
			// elapsed time
			mouseCriterion = savedMouseCriterion;

			// Switch the cursor back
			if ( parentComponent != null )
				parentComponent.setCursor( savedCursor );
			return;
		}

		previousX = x;
		previousY = y;
	}


	/**
	 *  Responds to a button2 event (press, release, or drag).  On a
	 *  press,  the method records the initial cursor location.
	 *  On a drag, the difference between the current and previous
	 *  cursor location provides a delta that controls the amount by
	 *  which to rotate in X and Y.
	 *
	 *  @param  mouseEvent  the MouseEvent to respond to
	 */
	public void onButton2( MouseEvent mev )
	{
		if ( subjectTransformGroup == null )
			return;

		int x = mev.getX( );
		int y = mev.getY( );

		if ( mev.getID( ) == MouseEvent.MOUSE_PRESSED )
		{
			// Mouse button pressed:  record position
			previousX = x;
			previousY = y;
			initialX = x;
			initialY = y;

			// Change to a "rotate" cursor
			if ( parentComponent != null )
			{
				savedCursor = parentComponent.getCursor( );
				parentComponent.setCursor(
					Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR ) );
			}
			return;
		}
		if ( mev.getID( ) == MouseEvent.MOUSE_RELEASED )
		{
			// Mouse button released:  do nothing

			// Switch the cursor back
			if ( parentComponent != null )
				parentComponent.setCursor( savedCursor );
			return;
		}

		//
		// Mouse moved while button down:  create a rotation
		//
		// Compute the delta in X and Y from the previous
		// position.  Use the delta to compute rotation
		// angles with the mapping:
		//
		//   positive X mouse delta --> negative Y-axis rotation
		//   positive Y mouse delta --> negative X-axis rotation
		//
		// where positive X mouse movement is to the right, and
		// positive Y mouse movement is **down** the screen.
		//
		int deltaX = x - previousX;
		int deltaY = 0;

		if ( Math.abs(y-initialY) > DELTAY_DEADZONE )
		{
			// Cursor has moved far enough vertically to consider
			// it intentional, so get it's delta.
			deltaY = y - previousY;
		}

		if ( deltaX > UNUSUAL_XDELTA || deltaX < -UNUSUAL_XDELTA ||
			deltaY > UNUSUAL_YDELTA || deltaY < -UNUSUAL_YDELTA )
		{
			// Deltas are too huge to be believable.  Probably a glitch.
			// Don't record the new XY location, or do anything.
			return;
		}

		double xRotationAngle = -deltaY * XRotationFactor;
		double yRotationAngle = -deltaX * YRotationFactor;

		//
		// Build transforms
		//
		transform1.rotX( xRotationAngle );
		transform2.rotY( yRotationAngle );

		// Get and save the current transform matrix
		subjectTransformGroup.getTransform( currentTransform );
		currentTransform.get( matrix );
		translate.set( matrix.m03, matrix.m13, matrix.m23 );

		// Translate to the origin, rotate, then translate back
		currentTransform.setTranslation( origin );
		currentTransform.mul( transform2, currentTransform );
		currentTransform.mul( transform1 );
		currentTransform.setTranslation( translate );

		// Update the transform group
		subjectTransformGroup.setTransform( currentTransform );

		previousX = x;
		previousY = y;
	}



	/**
	 *  Responds to a button3 event (press, release, or drag).
	 *  On a drag, the difference between the current and previous
	 *  cursor location provides a delta that controls the amount by
	 *  which to translate in X and Y.
	 *
	 *  @param  mouseEvent  the MouseEvent to respond to
	 */
	public void onButton3( MouseEvent mev )
	{
		if ( subjectTransformGroup == null )
			return;

		int x = mev.getX( );
		int y = mev.getY( );

		if ( mev.getID( ) == MouseEvent.MOUSE_PRESSED )
		{
			// Mouse button pressed:  record position
			previousX = x;
			previousY = y;

			// Change to a "move" cursor
			if ( parentComponent != null )
			{
				savedCursor = parentComponent.getCursor( );
				parentComponent.setCursor(
					Cursor.getPredefinedCursor( Cursor.MOVE_CURSOR ) );
			}
			return;
		}
		if ( mev.getID( ) == MouseEvent.MOUSE_RELEASED )
		{
			// Mouse button released:  do nothing

			// Switch the cursor back
			if ( parentComponent != null )
				parentComponent.setCursor( savedCursor );
			return;
		}

		//
		// Mouse moved while button down:  create a translation
		//
		// Compute the delta in X and Y from the previous
		// position.  Use the delta to compute translation
		// distances with the mapping:
		//
		//   positive X mouse delta --> positive X-axis translation
		//   positive Y mouse delta --> negative Y-axis translation
		//
		// where positive X mouse movement is to the right, and
		// positive Y mouse movement is **down** the screen.
		//
		int deltaX = x - previousX;
		int deltaY = y - previousY;

		if ( deltaX > UNUSUAL_XDELTA || deltaX < -UNUSUAL_XDELTA ||
			deltaY > UNUSUAL_YDELTA || deltaY < -UNUSUAL_YDELTA )
		{
			// Deltas are too huge to be believable.  Probably a glitch.
			// Don't record the new XY location, or do anything.
			return;
		}

		double xTranslationDistance = deltaX * XTranslationFactor;
		double yTranslationDistance = -deltaY * YTranslationFactor;

		//
		// Build transforms
		//
		translate.set( xTranslationDistance, yTranslationDistance, 0.0 );
		transform1.set( translate );

		// Get and save the current transform
		subjectTransformGroup.getTransform( currentTransform );

		// Translate as needed
		currentTransform.mul( transform1 );

		// Update the transform group
		subjectTransformGroup.setTransform( currentTransform );

		previousX = x;
		previousY = y;
	}
}

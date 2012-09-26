//
//  CLASS
//    AnnotationArrow	-  3D arrow used for annotation & diagrams
//
//  DESCRIPTION
//    This class creates a 3D, unlighted line between two 3D coordinates
//    plus a cone-shaped arrow at the line's endpoint.  The line's width
//    and color can be controlled.  The arrow head's width and length
//    can be controlled.
//
//  SEE ALSO
//    AnnotationLine
//    AnnotationAxes
//    AnnotationArrowFan
//    AnnotationArrowGroup
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;

class AnnotationArrow
	extends AnnotationLine
{
	// Parameters
	private Color3f arrowColor = new Color3f( 1.0f, 1.0f, 1.0f );
	private float arrowRadius = 0.1f;
	private float arrowLength = 0.20f;
	private float lineWidth   = 3.0f;
	private int   radialDivisions = 8;
	private int   sideDivisions   = 1;

	// 3D Nodes
	private Cone arrowHead = null;
	private Appearance arrowAppearance = null;
	private TransformGroup arrowTrans = null;
	private ColoringAttributes coloringAttributes = null;


	//
	//  Construct a straight line
	//
	public AnnotationArrow( float x2, float y2, float z2 )
	{
		//    origin            to given coordinate
		this( 0.0f, 0.0f, 0.0f, x2, y2, z2 );
	}

	public AnnotationArrow( float x, float y, float z,
		float x2, float y2, float z2 )
	{
		super( x, y, z, x2, y2, z2 );
		setLineWidth( lineWidth );

		// Compute the length and direction of the line
		float deltaX = x2-x;
		float deltaY = y2-y;
		float deltaZ = z2-z;

		float theta = -(float)Math.atan2( deltaZ, deltaX );
		float phi   =  (float)Math.atan2( deltaY, deltaX );
		if ( deltaX < 0.0f )
		{
			phi = (float)Math.PI - phi;
		}

		// Compute a matrix to rotate a cone to point in the line's
		// direction, then place the cone at the line's endpoint.
		Matrix4f mat = new Matrix4f( );
		Matrix4f mat2 = new Matrix4f( );
		mat.setIdentity( );

		// Move to the endpoint of the line
		mat2.setIdentity( );
		mat2.setTranslation( new Vector3f( x2, y2, z2 ) );
		mat.mul( mat2 );

		// Spin around Y
		mat2.setIdentity( );
		mat2.rotY( theta );
		mat.mul( mat2 );

		// Tilt up or down around Z
		mat2.setIdentity( );
		mat2.rotZ( phi );
		mat.mul( mat2 );

		// Tilt cone to point right
		mat2.setIdentity( );
		mat2.rotZ( -1.571f );
		mat.mul( mat2 );

		arrowTrans = new TransformGroup( );
		arrowTrans.setCapability( Group.ALLOW_CHILDREN_WRITE );
		Transform3D trans = new Transform3D( mat );
		arrowTrans.setTransform( trans );

		// Create an appearance
		arrowAppearance = new Appearance( );
		arrowAppearance.setCapability(
			Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE );

		getLineColor( arrowColor );
		coloringAttributes = new ColoringAttributes( );
		coloringAttributes.setColor( arrowColor );
		coloringAttributes.setShadeModel( ColoringAttributes.SHADE_FLAT );
		arrowAppearance.setColoringAttributes( coloringAttributes );

		// Build a cone for the arrow head
		arrowHead = new Cone(
			arrowRadius,                // base radius
			arrowLength,                // height
			0,                          // don't generate normals
			radialDivisions,            // divisions radially
			sideDivisions,              // divisions vertically
			arrowAppearance );          // appearance

		arrowTrans.addChild( arrowHead );
		addChild( arrowTrans );
	}


	//
	//  Control the arrow head size
	//
	public void setArrowHeadRadius( float radius )
	{
		arrowRadius = radius;

		arrowTrans.removeChild( 0 );
		arrowHead = new Cone(
			arrowRadius,                // base radius
			arrowLength,                // height
			0,                          // don't generate normals
			radialDivisions,            // divisions radially
			sideDivisions,              // divisions vertically
			arrowAppearance );          // appearance
		arrowTrans.addChild( arrowHead );
	}

	public void setArrowHeadLength( float length )
	{
		arrowLength = length;

		arrowTrans.removeChild( 0 );
		arrowHead = new Cone(
			arrowRadius,                // base radius
			arrowLength,                // height
			0,                          // don't generate normals
			radialDivisions,            // divisions radially
			sideDivisions,              // divisions vertically
			arrowAppearance );          // appearance
		arrowTrans.addChild( arrowHead );
	}

	public float getArrowHeadRadius( )
	{
		return arrowRadius;
	}

	public float getArrowHeadLength( )
	{
		return arrowLength;
	}


	//
	//  Control the line color
	//
	public void setLineColor( Color3f color )
	{
		super.setLineColor( color );

		getLineColor( arrowColor );
		coloringAttributes.setColor( arrowColor );
		arrowAppearance.setColoringAttributes( coloringAttributes );
		arrowHead.setAppearance( arrowAppearance );
	}

	public void setLineColor( float r, float g, float b )
	{
		super.setLineColor( r, g, b );

		getLineColor( arrowColor );
		coloringAttributes.setColor( arrowColor );
		arrowAppearance.setColoringAttributes( coloringAttributes );
		arrowHead.setAppearance( arrowAppearance );
	}

	public void setLineColor( float[] color )
	{
		super.setLineColor( color );

		getLineColor( arrowColor );
		coloringAttributes.setColor( arrowColor );
		arrowAppearance.setColoringAttributes( coloringAttributes );
		arrowHead.setAppearance( arrowAppearance );
	}


	//
	//  Control the appearance
	//
	public void setAppearance( Appearance app )
	{
		super.setAppearance( app );

		arrowAppearance = app;
		arrowAppearance.setCapability(
			Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE );
		arrowAppearance.setColoringAttributes( coloringAttributes );
		arrowHead.setAppearance( arrowAppearance );
	}


	//
	//  Provide info on the shape and geometry
	//
	public Shape3D getShape( int partid )
	{
		if ( partid == Cone.BODY )
			return arrowHead.getShape( Cone.BODY );
		else if ( partid == Cone.CAP )
			return arrowHead.getShape( Cone.CAP );
		else
			return super.getShape( partid );
	}

	public int getNumTriangles( )
	{
		return arrowHead.getNumTriangles( );
	}

	public int getNumVertices( )
	{
		return arrowHead.getNumVertices( ) + super.getNumVertices( );
	}
}

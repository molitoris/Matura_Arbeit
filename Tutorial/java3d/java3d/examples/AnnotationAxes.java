//
//  CLASS
//    AnnotationAxes	-  3D X, Y, and Z axes used for annotation & diagrams
//
//  DESCRIPTION
//    This class creates three orthogonal arrows, one each for X, Y, and
//    Z positive directions.  All three arrows are 3D and unlighted.
//    The line's width and color can be controlled.
//
//  SEE ALSO
//    AnnotationArrow
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//
//
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;

class AnnotationAxes
	extends Primitive
{
	// 3D nodes
	private AnnotationArrow xAxis = null;
	private AnnotationArrow yAxis = null;
	private AnnotationArrow zAxis = null;

	//
	//  Construct a triple of arrows
	//
	public AnnotationAxes( )
	{
		//    length  line width
		this( 1.0f,   1.0f );
	}

	public AnnotationAxes( float length )
	{
		//    length  line width
		this( length, 1.0f );
	}

	public AnnotationAxes( float length, float lineWidth )
	{
		xAxis = new AnnotationArrow( length, 0.0f, 0.0f );
		yAxis = new AnnotationArrow( 0.0f, length, 0.0f );
		zAxis = new AnnotationArrow( 0.0f, 0.0f, length );
		xAxis.setLineWidth( lineWidth );
		yAxis.setLineWidth( lineWidth );
		zAxis.setLineWidth( lineWidth );
		addChild( xAxis );
		addChild( yAxis );
		addChild( zAxis );
	}


	//
	//  Control the arrow heads
	//
	public void setArrowHeadRadius( float radius )
	{
		xAxis.setArrowHeadRadius( radius );
		yAxis.setArrowHeadRadius( radius );
		zAxis.setArrowHeadRadius( radius );
	}

	public float getArrowHeadRadius( )
	{
		return xAxis.getArrowHeadRadius( );
	}

	public void setArrowHeadLength( float length )
	{
		xAxis.setArrowHeadLength( length );
		yAxis.setArrowHeadLength( length );
		zAxis.setArrowHeadLength( length );
	}

	public float getArrowHeadLength( )
	{
		return xAxis.getArrowHeadLength( );
	}


	//
	//  Control color
	//
	public void getAxesColor( Color3f xColor, Color3f yColor, Color3f zColor )
	{
		xAxis.getLineColor( xColor );
		yAxis.getLineColor( yColor );
		zAxis.getLineColor( zColor );
	}

	public void getAxesColor( float[] xColor, float[] yColor, float[] zColor )
	{
		xAxis.getLineColor( xColor );
		yAxis.getLineColor( yColor );
		zAxis.getLineColor( zColor );
	}

	public void setAxesColor( Color3f xColor, Color3f yColor, Color3f zColor )
	{
		xAxis.setLineColor( xColor );
		yAxis.setLineColor( yColor );
		zAxis.setLineColor( zColor );
	}

	public void setAxesColor( float[] xColor, float[] yColor, float[] zColor )
	{
		xAxis.setLineColor( xColor );
		yAxis.setLineColor( yColor );
		zAxis.setLineColor( zColor );
	}


	//
	//  Control the appearance
	//
	public void setAppearance( Appearance app )
	{
		xAxis.setAppearance( app );
		yAxis.setAppearance( app );
		zAxis.setAppearance( app );
	}


	//
	//  Control line width (all three axes have the same width)
	//
	public float getLineWidth( )
	{
		return xAxis.getLineWidth( );
	}

	public void setLineWidth( float lineWidth )
	{
		xAxis.setLineWidth( lineWidth );
		yAxis.setLineWidth( lineWidth );
		zAxis.setLineWidth( lineWidth );
	}


	//
	//  Provide info on the axes
	//
	public Shape3D getShape( int partid )
	{
		return null;
	}

	public int getNumTriangles( )
	{
		return xAxis.getNumTriangles( ) + yAxis.getNumTriangles( ) +
			zAxis.getNumTriangles( );
	}

	public int getNumVertices( )
	{
		return xAxis.getNumVertices( ) + yAxis.getNumVertices( ) +
			zAxis.getNumVertices( );
	}
}

//
//  CLASS
//    AnnotationArrowFan	-  A group of arrows in a fan
//
//  DESCRIPTION
//    This class creates one or more 3D, unlighted arrows arranged in a
//    fan around the xyz position.  Such arrow fans can be used to indicate
//    point light directions, and so forth.
//
//    The arrow fan is drawn in the XY plane, pointing right (middle arrow).
//    The fan origin, arrow length, start and end angles, and number of
//    arrows all may be controlled.
//
//  SEE ALSO
//    AnnotationArrow
//    AnnotationArrowGroup
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//
//
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;

public class AnnotationArrowFan
	extends Group
{
	// 3D nodes
	AnnotationArrow[] arrows;

	//  Constructors
	public AnnotationArrowFan( )
	{
		//    xyz                 length  start/end angles  count
		this( 0.0f, 0.0f, 0.0f,   1.0f,   1.571f,  -1.571f, 5 );
	}

	public AnnotationArrowFan( float x, float y, float z, float length,
		float startAngle, float endAngle, int count )
	{
		arrows = new AnnotationArrow[count];
		float x2, y2;
		float angle = startAngle;
		float deltaAngle = (endAngle - startAngle) / (float)(count-1);

		for ( int i = 0; i < count; i++ )
		{
			x2 = (float)(length * Math.cos( angle ));
			y2 = (float)(length * Math.sin( angle ));
			arrows[i] = new AnnotationArrow( x, y, z, x2, y2, z );
			addChild( arrows[i] );
			angle += deltaAngle;
		}
	}
}

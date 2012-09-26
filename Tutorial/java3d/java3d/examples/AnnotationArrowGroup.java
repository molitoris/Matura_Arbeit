//
//  CLASS
//    AnnotationArrowGroup	-  A group of parallel arrows
//
//  DESCRIPTION
//    This class creates one or more parallel 3D, unlighted arrows.
//    Such arrow groups can be used to indicate directional light
//    directions, and so forth.
//
//    The arrow group is drawn in the XY plane, pointing right.
//    The X start and end values, and the Y start and end values
//    can be set, along with the count of the number of arrows to
//    build.
//
//  SEE ALSO
//    AnnotationArrow
//    AnnotationArrowFan
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//
//
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;

public class AnnotationArrowGroup
	extends Group
{
	// 3D nodes
	AnnotationArrow[] arrows;

	//  Constructors
	public AnnotationArrowGroup( )
	{
		//    xStart  xEnd   yStart  yEnd   count
		this( -1.0f,  1.0f,  1.0f,   -1.0f, 3 );
	}

	public AnnotationArrowGroup( float xStart, float xEnd,
		float yStart, float yEnd, int count )
	{
		arrows = new AnnotationArrow[count];
		float y = yStart;
		float deltaY = (yEnd - yStart) / (float)(count-1);
		for ( int i = 0; i < count; i++ )
		{
			arrows[i] = new AnnotationArrow( xStart, y, 0.0f, xEnd, y, 0.0f);
			addChild( arrows[i] );
			y += deltaY;
		}
	}
}

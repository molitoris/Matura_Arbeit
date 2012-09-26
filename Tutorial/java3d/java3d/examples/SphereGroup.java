//
//  CLASS
//    SphereGroup - create a group of spheres on the XY plane
//
//  DESCRIPTION
//    An XY grid of spheres is created.  The number of spheres in X and Y,
//    the spacing in X and Y, the sphere radius, and the appearance can
//    all be set.
//
//    This grid of spheres is used by several of the examples as a generic
//    bit of foreground geometry.
//
//  SEE ALSO
//    Ex*Light
//    ExBackground*
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;

public class SphereGroup
	extends Group
{
	//  Constructors
	public SphereGroup( )
	{
		//    radius   x,y spacing   x,y count  appearance
		this( 0.25f,   0.75f, 0.75f,   5, 5,      null );
	}

	public SphereGroup( Appearance app )
	{
		//    radius   x,y spacing   x,y count  appearance
		this( 0.25f,   0.75f, 0.75f,   5, 5,      app );
	}

	public SphereGroup( float radius, float xSpacing, float ySpacing,
		int xCount, int yCount )
	{
		this( radius,  xSpacing, ySpacing, xCount, yCount, null );
	}

	public SphereGroup( float radius, float xSpacing, float ySpacing,
		int xCount, int yCount, Appearance app )
	{
		if ( app == null )
		{
			app = new Appearance( );
			Material material = new Material( );
			material.setDiffuseColor( new Color3f( 0.8f, 0.8f, 0.8f ) );
			material.setSpecularColor( new Color3f( 0.0f, 0.0f, 0.0f ) );
			material.setShininess( 0.0f );
			app.setMaterial( material );
		}

		double xStart = -xSpacing * (double)(xCount-1) / 2.0;
		double yStart = -ySpacing * (double)(yCount-1) / 2.0;

		Sphere sphere = null;
		TransformGroup trans = null;
		Transform3D t3d = new Transform3D( );
		Vector3d vec = new Vector3d( );
		double x, y = yStart, z = 0.0;
		for ( int i = 0; i < yCount; i++ )
		{
			x = xStart;
			for ( int j = 0; j < xCount; j++ )
			{
				vec.set( x, y, z );
				t3d.setTranslation( vec );
				trans = new TransformGroup( t3d );
				addChild( trans );

				sphere = new Sphere(
					radius,     // sphere radius
					Primitive.GENERATE_NORMALS,  // generate normals
					16,         // 16 divisions radially
					app );      // it's appearance
				trans.addChild( sphere );
				x += xSpacing;
			}
			y += ySpacing;
		}
	}
}

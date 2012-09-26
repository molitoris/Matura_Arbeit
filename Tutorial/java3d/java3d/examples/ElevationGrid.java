//
//  CLASS
//    ElevationGrid	-  a 3D terrain grid built from a list of heights
//
//  DESCRIPTION
//    This class creates a 3D terrain on a grid whose X and Z dimensions,
//    and row/column spacing are parameters, along with a list of heights
//    (elevations), one per grid row/column pair.
//
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;

class ElevationGrid
	extends Primitive
{
	// Parameters
	protected int xDimension = 0, zDimension = 0;
	protected double xSpacing = 0.0, zSpacing = 0.0;
	protected double[] heights = null;

	// 3D nodes
	private Appearance mainAppearance = null;
	private Shape3D shape = null;
	private IndexedTriangleStripArray tristrip = null;


	//
	//  Construct an elevation grid
	//
	public ElevationGrid( )
	{
		xDimension = 2;
		zDimension = 2;
		xSpacing = 1.0;
		zSpacing = 1.0;
		mainAppearance = null;
		zeroHeights( );
		rebuild( );
	}

	public ElevationGrid( int xDim, int zDim )
	{
		xDimension = xDim;
		zDimension = zDim;
		xSpacing = 1.0;
		zSpacing = 1.0;
		mainAppearance = null;
		zeroHeights( );
		rebuild( );
	}

	public ElevationGrid( int xDim, int zDim,
		Appearance app )
	{
		xDimension = xDim;
		zDimension = zDim;
		xSpacing = 1.0;
		zSpacing = 1.0;
		mainAppearance = app;
		zeroHeights( );
		rebuild( );
	}

	public ElevationGrid( int xDim, int zDim,
		double xSpace, double zSpace )
	{
		xDimension = xDim;
		zDimension = zDim;
		xSpacing = xSpace;
		zSpacing = zSpace;
		mainAppearance = null;
		zeroHeights( );
		rebuild( );
	}

	public ElevationGrid( int xDim, int zDim,
		double xSpace, double zSpace,
		Appearance app )
	{
		xDimension = xDim;
		zDimension = zDim;
		xSpacing = xSpace;
		zSpacing = zSpace;
		mainAppearance = app;
		zeroHeights( );
		rebuild( );
	}

	public ElevationGrid( int xDim, int zDim,
		double[] h )
	{
		this( xDim, zDim, 1.0, 1.0, h, null );
	}

	public ElevationGrid( int xDim, int zDim,
		double[] h,
		Appearance app )
	{
		this( xDim, zDim, 1.0, 1.0, h, app );
	}

	public ElevationGrid( int xDim, int zDim,
		double xSpace, double zSpace,
		double[] h )
	{
		this( xDim, zDim, xSpace, zSpace, h, null );
	}

	public ElevationGrid( int xDim, int zDim,
		double xSpace, double zSpace,
		double[] h,
		Appearance app )
	{
		xDimension = xDim;
		zDimension = zDim;
		xSpacing   = xSpace;
		zSpacing   = zSpace;
		mainAppearance = app;
		if ( h == null )
			zeroHeights( );
		else
		{
			heights = new double[h.length];
			for ( int i = 0; i < h.length; i++ )
				heights[i] = h[i];
		}
		rebuild( );
	}


	private void zeroHeights( )
	{
		int n = xDimension * zDimension;
		heights = new double[n];
		for ( int i = 0; i < n; i++ )
			heights[i] = 0.0;
	}



	private void rebuild( )
	{
		// Build a shape
		if ( shape == null )
		{
			shape = new Shape3D( );
			shape.setCapability( Shape3D.ALLOW_APPEARANCE_WRITE );
			shape.setCapability( Shape3D.ALLOW_GEOMETRY_WRITE );
			shape.setAppearance( mainAppearance );
			addChild( shape );
		}
		else
		{
			shape.setAppearance( mainAppearance );
		}

		if ( xDimension < 2 || zDimension < 2 ||
			heights == null || heights.length < 4 )
		{
			tristrip = null;
			shape.setGeometry( null );
			return;
		}


		// Create a list of coordinates, one per grid row/column
		double[] coordinates = new double[xDimension*zDimension*3];
		double x, z;
		int n = 0, k = 0;
		z = ((double)(zDimension-1)) * zSpacing / 2.0;  // start at front edge
		for ( int i = 0; i < zDimension; i++ )
		{
			x = -((double)(xDimension-1)) * xSpacing / 2.0;// start at left edge
			for ( int j = 0; j < xDimension; j++ )
			{
				coordinates[n++] = x;
				coordinates[n++] = heights[k++];
				coordinates[n++] = z;
				x += xSpacing;
			}
			z -= zSpacing;
		}

		// Create a list of normals, one per grid row/column
		float[] normals = new float[xDimension*zDimension*3];
		Vector3f one = new Vector3f( 0.0f, 0.0f, 0.0f );
		Vector3f two = new Vector3f( 0.0f, 0.0f, 0.0f );
		Vector3f norm = new Vector3f( 0.0f, 0.0f, 0.0f );
		n = 0;
		k = 0;
		for ( int i = 0; i < zDimension-1; i++ )
		{
			for ( int j = 0; j < xDimension-1; j++ )
			{
				// Vector to right in X
				one.set( (float)xSpacing, (float)(heights[k+1] - heights[k]), 0.0f );

				// Vector back in Z
				two.set( 0.0f, (float)(heights[k+xDimension] - heights[k]), (float)-zSpacing );

				// Cross them to get the normal
				norm.cross( one, two );
				normals[n++] = norm.x;
				normals[n++] = norm.y;
				normals[n++] = norm.z;
				k++;
			}

			// Last normal in row is a copy of the previous one
			normals[n]   = normals[n-3]; // X
			normals[n+1] = normals[n-2]; // Y
			normals[n+2] = normals[n-1]; // Z
			n += 3;
			k++;
		}

		// Last row of normals is a copy of the previous row
		for ( int j = 0; j < xDimension; j++ )
		{
			normals[n]   = normals[n-xDimension*3];   // X
			normals[n+1] = normals[n-xDimension*3+1]; // Y
			normals[n+2] = normals[n-xDimension*3+2]; // Z
			n += 3;
		}

		// Create a list of texture coordinates, one per grid row/column
		float[] texcoordinates = new float[xDimension*zDimension*2];
		float deltaS = 1.0f / (float)(xDimension-1);
		float deltaT = 1.0f / (float)(zDimension-1);
		float s = 0.0f;
		float t = 0.0f;
		n = 0;
		for ( int i = 0; i < zDimension; i++ )
		{
			s = 0.0f;
			for ( int j = 0; j < xDimension; j++ )
			{
				texcoordinates[n++] = s;
				texcoordinates[n++] = t;
				s += deltaS;
			}
			t += deltaT;
		}

		// Create a list of triangle strip indexes.  Each strip goes
		// down one row (X direction) of the elevation grid.
		int[] indexes = new int[xDimension*(zDimension-1)*2];
		int[] stripCounts = new int[zDimension-1];
		n = 0;
		k = 0;
		for ( int i = 0; i < zDimension-1; i++ )
		{
			stripCounts[i] = xDimension*2;
			for ( int j = 0; j < xDimension; j++ )
			{
				indexes[n++] = k+xDimension;
				indexes[n++] = k;
				k++;
			}
		}


		// Create geometry for collection of triangle strips, one
		// strip per row of the elevation grid
		tristrip = new IndexedTriangleStripArray(
			coordinates.length,
			GeometryArray.COORDINATES |
			GeometryArray.NORMALS |
			GeometryArray.TEXTURE_COORDINATE_2,
			indexes.length,
			stripCounts );
		tristrip.setCoordinates( 0, coordinates );
		tristrip.setNormals( 0, normals );
		tristrip.setTextureCoordinates( 0, texcoordinates );
		tristrip.setCoordinateIndices( 0, indexes );
		tristrip.setNormalIndices( 0, indexes );
		tristrip.setTextureCoordinateIndices( 0, indexes );

		// Set the geometry for the shape
		shape.setGeometry( tristrip );
	}


	//
	//  Control the appearance
	//
	public void setAppearance( Appearance app )
	{
		mainAppearance = app;
		if ( shape != null )
			shape.setAppearance( mainAppearance );
	}


	//
	//  Control grid parameters
	//
	public void setHeights( double[] h )
	{
		if ( h == null )
			zeroHeights( );
		else
		{
			heights = new double[h.length];
			for ( int i = 0; i < h.length; i++ )
				heights[i] = h[i];
		}
		rebuild( );
	}
	public double[] getHeights( )
	{
		return heights;
	}

	public void setXDimension( int xDim )
	{
		xDimension = xDim;
		rebuild( );
	}
	public int getXDimension( )
	{
		return xDimension;
	}

	public void setZDimension( int zDim )
	{
		zDimension = zDim;
		rebuild( );
	}
	public int getZDimension( )
	{
		return zDimension;
	}

	public void setXSpacing( double xSpace )
	{
		xSpacing = xSpace;
		rebuild( );
	}
	public double getXSpacing( )
	{
		return xSpacing;
	}

	public void setZSpacing( double zSpace )
	{
		zSpacing = zSpace;
		rebuild( );
	}
	public double getZSpacing( )
	{
		return zSpacing;
	}


	//
	//  Provide info on the shape and geometry
	//
	public Shape3D getShape( int partid )
	{
		return shape;
	}

	public int getNumTriangles( )
	{
		return xDimension * zDimension * 2;
	}

	public int getNumVertices( )
	{
		return xDimension * zDimension;
	}
}

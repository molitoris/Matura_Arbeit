//
//  CLASS
//    CraterGrid	-  a 3D terrain grid built from a list of heights
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

class CraterGrid
	extends ElevationGrid
{
	// Parameters
	double[][] craters = null;
	double exagerationFactor = 1.0;

	// 3D nodes
	private Shape3D shape = null;
	private IndexedTriangleStripArray tristrip = null;


	//
	//  Construct a crater grid
	//
	public CraterGrid( )
	{
		super( );
		craters = null;
	}

	public CraterGrid( int xDim, int zDim,
		double[][] craters, Appearance app )
	{
		this( xDim, zDim, 1.0, 1.0, 1.0, craters, app );
	}

	public CraterGrid( int xDim, int zDim,
		double xSpace, double zSpace, double exagerate,
		double[][] crat, Appearance app )
	{
		super( xDim, zDim, xSpace, zSpace, null, app );
		exagerationFactor = exagerate;
		if ( crat == null )
			craters = null;
		else
		{
				craters = new double[crat.length][4];
				for ( int i = 0; i < crat.length; i++ )
				{
					craters[i][0] = crat[i][0];
					craters[i][1] = crat[i][1];
					craters[i][2] = crat[i][2];
					craters[i][3] = crat[i][3];
				}
		}
		computeHeights( );
		
	}

	private void computeHeights( )
	{
		if ( craters == null )
			return;

		double[] high = new double[xDimension * zDimension];
		int n = 0;
		double xdelta = 2.0 / (double)xDimension;
		double zdelta = 2.0 / (double)zDimension;
		double x, z, dx, dz;
		double angle, distance;

		// Compute heights
		z = 1.0;
		for ( int i = 0; i < zDimension; i++ )
		{
			x = -1.0;
			for ( int j = 0; j < xDimension; j++ )
			{
				// Compute a distance to the center of each crater.
				// If that distance is <= the radius, drop the height
				// by the crater's depth.  Sum across all craters
				// within range to get the height at this grid point.
				high[n] = 0.0;
				for ( int k = 0; k < craters.length; k++ )
				{
					dx = craters[k][0] - x;
					dz = craters[k][1] - z;
					distance = Math.sqrt( dx*dx + dz*dz );
					if ( distance > craters[k][2] )
						continue;
					high[n] -= exagerationFactor*craters[k][3];
				}
				n++;
				x += xdelta;
			}
			z -= zdelta;
		}
		setHeights( high );
	}


	//
	//  Control grid parameters
	//
	public void setCraters( double[][] crat )
	{
		craters = new double[crat.length][4];
		for ( int i = 0; i < crat.length; i++ )
		{
			craters[i][0] = crat[i][0];
			craters[i][1] = crat[i][1];
			craters[i][2] = crat[i][2];
			craters[i][3] = crat[i][3];
		}
		computeHeights( );
	}
	public void getCraters( double[][] crat )
	{
		for ( int i = 0; i < craters.length; i++ )
		{
			crat[i][0] = craters[i][0];
			crat[i][1] = craters[i][1];
			crat[i][2] = craters[i][2];
			crat[i][3] = craters[i][3];
		}
	}

	public void setExageration( double exagerate )
	{
		exagerationFactor = exagerate;
		computeHeights( );
	}
	public double getExageration( )
	{
		return exagerationFactor;
	}
}

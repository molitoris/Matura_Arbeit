//
//  CLASS
//    Arch	-  generalized arch
//
//  DESCRIPTION
//    This class builds a generalized arch where incoming parameters
//    specify the angle range in theta (around the equator of a sphere),
//    the angle range in phi (north-south), the number of subdivisions
//    in theta and phi, and optionally radii and outer-to-inner wall
//    thickness variations as phi varies from its starting value to
//    its ending value.  If the thicknesses are 0.0, then only an outer
//    surface is created.
//
//    Using this class, you can create spheres with or without inner
//    surfaces, hemisphers, quarter spheres, and arches stretched or
//    compressed vertically.
//
//    This is probably not as general as it could be, but it was enough
//    for the purposes at hand.
//
//  SEE ALSO
//    ModernFire
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//
//
import javax.media.j3d.*;
import javax.vecmath.*;

public class Arch
	extends Group
{
	// The shape
	private Shape3D arch = null;


	// Construct an arch
	public Arch( )
	{
		// Default to a sphere
		this( 0.0, Math.PI/2.0, 9, 0.0, Math.PI, 17,
			1.0, 1.0, 0.0, 0.0, new Appearance( ) );
	}
	public Arch( Appearance app )
	{
		// Default to a sphere
		this( 0.0, Math.PI/2.0, 9, 0.0, Math.PI, 17,
			1.0, 1.0, 0.0, 0.0, app );
	}

	public Arch( double startPhi, double endPhi, int nPhi,
		double startTheta, double endTheta, int nTheta, Appearance app )
	{
		// Default to constant radius, no thickness
		this( startPhi, endPhi, nPhi, startTheta, endTheta, nTheta,
			1.0, 1.0, 0.0, 0.0, app );
	}

	public Arch( double startPhi, double endPhi, int nPhi,
		double startTheta, double endTheta, int nTheta,
		double startPhiRadius, double endPhiRadius,
		double startPhiThickness, double endPhiThickness,
		Appearance app )
	{
		double theta, phi, radius, radius2, thickness;
		double x, y, z;
		double[] xyz  = new double[3];
		float[]  norm = new float[3];
		float[]  tex  = new float[3];

		// Compute some values for our looping
		double deltaTheta = (endTheta - startTheta) / (double)(nTheta-1);
		double deltaPhi   = (endPhi - startPhi) / (double)(nPhi-1);
		double deltaTexX  = 1.0 / (double)(nTheta-1);
		double deltaTexY  = 1.0 / (double)(nPhi-1);
		double deltaPhiRadius = (endPhiRadius - startPhiRadius) /
			(double)(nPhi-1);
		double deltaPhiThickness = (endPhiThickness - startPhiThickness) /
			(double)(nPhi-1);

		boolean doThickness = true;
		if ( startPhiThickness == 0.0 && endPhiThickness == 0.0 )
			doThickness = false;


		//  Create geometry
		int vertexCount = nTheta * nPhi;
		if ( doThickness )
			vertexCount *= 2;
		int indexCount = (nTheta-1) * (nPhi-1) * 4;  // Outer surface
		if ( doThickness )
		{
			indexCount *= 2;  // plus inner surface
			indexCount += (nPhi-1) * 4 * 2;  // plus left & right edges
		}

		IndexedQuadArray polys = new IndexedQuadArray(
			vertexCount,
			GeometryArray.COORDINATES |
			GeometryArray.NORMALS |
			GeometryArray.TEXTURE_COORDINATE_2,
			indexCount );



		//
		//  Compute coordinates, normals, and texture coordinates
		//
		theta = startTheta;
		tex[0] = 0.0f;
		int index = 0;
		for ( int i = 0; i < nTheta; i++ )
		{
			phi = startPhi;
			radius = startPhiRadius;
			thickness = startPhiThickness;
			tex[1] = 0.0f;

			for ( int j = 0; j < nPhi; j++ )
			{
				norm[0] = (float)(Math.cos( phi ) * Math.cos( theta ));
				norm[1] = (float)(Math.sin( phi ));
				norm[2] = (float)(-Math.cos( phi ) * Math.sin( theta ));
				xyz[0] = radius * norm[0];
				xyz[1] = radius * norm[1];
				xyz[2] = radius * norm[2];
				polys.setCoordinate( index, xyz );
				polys.setNormal( index, norm );
				polys.setTextureCoordinate( index, tex );
				index++;

				if ( doThickness )
				{
					radius2 = radius - thickness;
					xyz[0] = radius2 * norm[0];
					xyz[1] = radius2 * norm[1];
					xyz[2] = radius2 * norm[2];
					norm[0] *= -1.0f;
					norm[1] *= -1.0f;
					norm[2] *= -1.0f;
					polys.setCoordinate( index, xyz );
					polys.setNormal( index, norm );
					polys.setTextureCoordinate( index, tex );
					index++;
				}

				phi += deltaPhi;
				radius += deltaPhiRadius;
				thickness += deltaPhiThickness;
				tex[1] += deltaTexY;
			}
			theta += deltaTheta;
			tex[0] += deltaTexX;
		}


		//
		//  Compute coordinate indexes
		//  (also used as normal and texture indexes)
		//
		index = 0;
		int phiRow = nPhi;
		int phiCol = 1;
		if ( doThickness )
		{
			phiRow += nPhi;
			phiCol += 1;
		}
		int[] indices = new int[indexCount];

		// Outer surface
		int n;
		for ( int i = 0; i < nTheta-1; i++ )
		{
			for ( int j = 0; j < nPhi-1; j++ )
			{
				n = i*phiRow + j*phiCol;
				indices[index+0] = n;
				indices[index+1] = n+phiRow;
				indices[index+2] = n+phiRow+phiCol;
				indices[index+3] = n+phiCol;
				index += 4;
			}
		}

		// Inner surface
		if ( doThickness )
		{
			for ( int i = 0; i < nTheta-1; i++ )
			{
				for ( int j = 0; j < nPhi-1; j++ )
				{
					n = i*phiRow + j*phiCol;
					indices[index+0] = n+1;
					indices[index+1] = n+phiCol+1;
					indices[index+2] = n+phiRow+phiCol+1;
					indices[index+3] = n+phiRow+1;
					index += 4;
				}
			}
		}

		// Edges
		if ( doThickness )
		{
			for ( int j = 0; j < nPhi-1; j++ )
			{
				n = j*phiCol;
				indices[index+0] = n;
				indices[index+1] = n+phiCol;
				indices[index+2] = n+phiCol+1;
				indices[index+3] = n+1;
				index += 4;
			}
			for ( int j = 0; j < nPhi-1; j++ )
			{
				n = (nTheta-1)*phiRow + j*phiCol;
				indices[index+0] = n;
				indices[index+1] = n+1;
				indices[index+2] = n+phiCol+1;
				indices[index+3] = n+phiCol;
				index += 4;
			}
		}

		polys.setCoordinateIndices( 0, indices );
		polys.setNormalIndices( 0, indices );
		polys.setTextureCoordinateIndices( 0, indices );


		//
		//  Build a shape
		//
		arch = new Shape3D( );
		arch.setCapability( Shape3D.ALLOW_APPEARANCE_WRITE );
		arch.setGeometry( polys );
		arch.setAppearance( app );
		addChild( arch );
	}

	public void setAppearance( Appearance app )
	{
		if ( arch != null )
			arch.setAppearance( app );
	}
}

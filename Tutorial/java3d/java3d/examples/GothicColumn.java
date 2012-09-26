//
//  CLASS
//    GothicColumn	-  Gothic-style column used in example scenes
//
//  DESCRIPTION
//    This class builds a Gothic-column architectural column.
//
//  SEE ALSO
//    ?
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

class GothicColumn
	extends Primitive
{
	//
	//  Construction Flags
	//
	public final static int BUILD_TAPERED_CROWN = 0x1;
	public final static int BUILD_TOP = 0x2;
	public final static int BUILD_BOTTOM = 0x4;

	//
	//  3D nodes
	//
	private Appearance mainAppearance = null;


	//
	//  Construct a column
	//
	public GothicColumn( float height, float radius, Appearance app )
	{
		this( height, radius, 0, app );
	}

	public GothicColumn( float height, float radius,
		int flags, Appearance app )
	{
		mainAppearance = app;

		// Compute sizes and positions based upon the
		// desired main column radius

		// Base box
		float baseWidth = 2.7f * radius;
		float baseDepth = baseWidth;
		float baseHeight = 0.75f * radius / 2.0f;

		// Base box #2
		float base2Width = 0.8f * baseWidth;
		float base2Depth = base2Width;
		float base2Height = baseHeight / 2.0f;

		// Tapered crown
		float crownWidth1 = 2.0f * 0.707f * radius;
		float crownDepth1 = crownWidth1;
		float crownWidth2 = 1.0f * base2Width;
		float crownDepth2 = 1.0f * base2Depth;
		float crownHeight = 2.0f * baseHeight;

		// Box above tapered crown
		float crown2Width = 1.1f * base2Width;
		float crown2Depth = 1.1f * base2Depth;
		float crown2Height = base2Height;

		// Final crown box
		float crown3Width = 1.1f * baseWidth;
		float crown3Depth = 1.1f * baseDepth;
		float crown3Height = baseHeight;

		// Cylindrical column
		//   Extend it up and into the tapered crown
		float columnHeight = height - baseHeight - base2Height -
			crown2Height - crown3Height;
		float columnRadius = radius;

		float baseY   = baseHeight/2.0f;
		float base2Y  = baseHeight + base2Height/2.0f;
		float columnY = baseHeight + base2Height + columnHeight/2.0f;
		float crown2Y = baseHeight + base2Height + columnHeight +
			crown2Height/2.0f;
		float crown3Y = baseHeight + base2Height + columnHeight +
			crown2Height + crown3Height/2.0f;

		float crownY  = crown2Y - crown2Height/2.0f - crownHeight/2.0f;


		// Column base box
		int fl = BUILD_TOP;
		if ( (flags & BUILD_BOTTOM) != 0 )
			fl |= BUILD_BOTTOM;
		addBox( baseWidth, baseHeight, baseDepth, baseY, fl );

		// Column base box #2 (no bottom)
		addBox( base2Width, base2Height, base2Depth, base2Y, BUILD_TOP );

		// Main column (no top or bottom)
		addCylinder( columnRadius, columnHeight, columnY );

		// Column crown tapered box (no top or bottom)
		if ( (flags & BUILD_TAPERED_CROWN) != 0 )
		{
			addBox( crownWidth1, crownHeight, crownDepth1, crownY,
				crownWidth2, crownDepth2, 0 );
		}

		// Box above tapered crown (no top)
		addBox( crown2Width, crown2Height, crown2Depth, crown2Y, BUILD_BOTTOM );

		// Final crown box
		fl = BUILD_BOTTOM;
		if ( (flags & BUILD_TOP) != 0 )
			fl |= BUILD_TOP;
		addBox( crown3Width, crown3Height, crown3Depth, crown3Y, fl );
	}





	//
	//  Add an untapered box
	//
	private void addBox( float width, float height, float depth, float y )
	{
		addBox( width, height, depth, y, width, depth, 0 );
	}

	private void addBox( float width, float height, float depth, float y,
		int flags )
	{
		addBox( width, height, depth, y, width, depth, flags );
	}

	private void addBox( float width, float height, float depth, float y,
		float width2, float depth2 )
	{
		addBox( width, height, depth, y, width2, depth2, 0 );
	}





	//
	//  Add a tapered box
	//
	private void addBox( float width, float height, float depth, float y,
		float width2, float depth2, int flags )
	{
		float[] coordinates = {
			// around the bottom
			-width/2.0f, -height/2.0f,  depth/2.0f,
			 width/2.0f, -height/2.0f,  depth/2.0f,
			 width/2.0f, -height/2.0f, -depth/2.0f,
			-width/2.0f, -height/2.0f, -depth/2.0f,

			// around the top
			-width2/2.0f, height/2.0f,  depth2/2.0f,
			 width2/2.0f, height/2.0f,  depth2/2.0f,
			 width2/2.0f, height/2.0f, -depth2/2.0f,
			-width2/2.0f, height/2.0f, -depth2/2.0f,
		};
		int[] fullCoordinateIndexes = {
			0, 1, 5, 4,  // front
			1, 2, 6, 5,  // right
			2, 3, 7, 6,  // back
			3, 0, 4, 7,  // left
			4, 5, 6, 7,  // top
			3, 2, 1, 0,  // bottom
		};
		float v = -(width2 - width) / height;
		float[] normals = {
			 0.0f,  v,    1.0f,  // front
			 1.0f,  v,    0.0f,  // right
			 0.0f,  v,   -1.0f,  // back
			-1.0f,  v,    0.0f,  // left
			 0.0f,  1.0f, 0.0f,  // top
			 0.0f, -1.0f, 0.0f,  // bottom
		};
		int[] fullNormalIndexes = {
			0, 0, 0, 0,  // front
			1, 1, 1, 1,  // right
			2, 2, 2, 2,  // back
			3, 3, 3, 3,  // left
			4, 4, 4, 4,  // top
			5, 5, 5, 5,  // bottom
		};
		float[] textureCoordinates = {
			0.0f, 0.0f,
			1.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 1.0f,
		};
		int[] fullTextureCoordinateIndexes = {
			0, 1, 2, 3,  // front
			0, 1, 2, 3,  // right
			0, 1, 2, 3,  // back
			0, 1, 2, 3,  // left
			0, 1, 2, 3,  // top
			0, 1, 2, 3,  // bottom
		};


		// Select indexes needed
		int[] coordinateIndexes;
		int[] normalIndexes;
		int[] textureCoordinateIndexes;
		if ( flags == 0 )
		{
			// build neither top or bottom
			coordinateIndexes    = new int[4*4];
			textureCoordinateIndexes = new int[4*4];
			normalIndexes        = new int[4*4];
			for ( int i = 0; i < 4*4; i++ )
			{
				coordinateIndexes[i] = fullCoordinateIndexes[i];
				textureCoordinateIndexes[i] = fullTextureCoordinateIndexes[i];
				normalIndexes[i] = fullNormalIndexes[i];
			}
		}
		else if ( (flags & (BUILD_TOP|BUILD_BOTTOM)) == (BUILD_TOP|BUILD_BOTTOM) )
		{
			// build top and bottom
			coordinateIndexes = fullCoordinateIndexes;
			textureCoordinateIndexes = fullTextureCoordinateIndexes;
			normalIndexes = fullNormalIndexes;
		}
		else if ( (flags & BUILD_TOP) != 0 )
		{
			// build top but not bottom
			coordinateIndexes    = new int[5*4];
			textureCoordinateIndexes = new int[5*4];
			normalIndexes        = new int[5*4];
			for ( int i = 0; i < 5*4; i++ )
			{
				coordinateIndexes[i] = fullCoordinateIndexes[i];
				textureCoordinateIndexes[i] = fullTextureCoordinateIndexes[i];
				normalIndexes[i] = fullNormalIndexes[i];
			}
		}
		else
		{
			// build bottom but not top
			coordinateIndexes    = new int[5*4];
			textureCoordinateIndexes = new int[5*4];
			normalIndexes        = new int[5*4];
			for ( int i = 0; i < 4*4; i++ )
			{
				coordinateIndexes[i] = fullCoordinateIndexes[i];
				textureCoordinateIndexes[i] = fullTextureCoordinateIndexes[i];
				normalIndexes[i] = fullNormalIndexes[i];
			}
			for ( int i = 5*4; i < 6*4; i++ )
			{
				coordinateIndexes[i-4] = fullCoordinateIndexes[i];
				textureCoordinateIndexes[i-4] = fullTextureCoordinateIndexes[i];
				normalIndexes[i-4] = fullNormalIndexes[i];
			}
		}


		IndexedQuadArray quads = new IndexedQuadArray(
			coordinates.length,   // number of vertexes
			GeometryArray.COORDINATES |// vertex coordinates given
			GeometryArray.NORMALS |    // normals given
			GeometryArray.TEXTURE_COORDINATE_2, // texture coordinates given
			coordinateIndexes.length );  // number of coordinate indexes
		quads.setCoordinates( 0, coordinates );
		quads.setCoordinateIndices( 0, coordinateIndexes );
		quads.setNormals( 0, normals );
		quads.setNormalIndices( 0, normalIndexes );
		quads.setTextureCoordinates( 0, textureCoordinates );
		quads.setTextureCoordinateIndices( 0, textureCoordinateIndexes );
		Shape3D box = new Shape3D( quads, mainAppearance );

		Vector3f trans = new Vector3f( 0.0f, y, 0.0f );
		Transform3D tr = new Transform3D( );
		tr.set( trans );    // translate
		TransformGroup tg = new TransformGroup( tr );
		tg.addChild( box );
		addChild( tg );
	}


	private final static int NSTEPS = 16;

	private void addCylinder( float radius, float height, float y )
	{
		//
		//  Compute coordinates, normals, and texture coordinates
		//  around the top and bottom of a cylinder
		//
		float[] coordinates = new float[NSTEPS*2*3];  // xyz
		float[] normals     = new float[NSTEPS*2*3];  // xyz vector
		float[] textureCoordinates = new float[NSTEPS*2*2];  // st
		float angle = 0.0f;
		float deltaAngle = 2.0f * (float)Math.PI / ((float)NSTEPS-1);
		float s = 0.0f;
		float deltaS = 1.0f / ((float)NSTEPS-1);
		int n = 0;
		int tn = 0;
		float h2 = height/2.0f;
		for ( int i = 0; i < NSTEPS; i++ )
		{
			// bottom
			normals[n+0] = (float)Math.cos( angle );
			normals[n+1] = 0.0f;
			normals[n+2] = -(float)Math.sin( angle );
			coordinates[n+0] = radius * normals[n+0];
			coordinates[n+1] = -h2;
			coordinates[n+2] = radius * normals[n+2];
			textureCoordinates[tn+0] = s;
			textureCoordinates[tn+1] = 0.0f;
			n += 3;
			tn += 2;

			// top
			normals[n+0] = normals[n-3];
			normals[n+1] = 0.0f;
			normals[n+2] = normals[n-1];
			coordinates[n+0] = coordinates[n-3];
			coordinates[n+1] = h2;
			coordinates[n+2] = coordinates[n-1];
			textureCoordinates[tn+0] = s;
			textureCoordinates[tn+1] = 1.0f;
			n += 3;
			tn += 2;

			angle += deltaAngle;
			s += deltaS;
		}


		//
		//  Compute coordinate indexes, normal indexes, and texture
		//  coordinate indexes awround the sides of a cylinder.
		//  For this application, we don't need top or bottom, so
		//  skip them.
		//
		int[] indexes = new int[NSTEPS*4];
		n = 0;
		int p = 0;  // panel count
		for ( int i = 0; i < NSTEPS-1; i++ )
		{
			indexes[n+0] = p;    // bottom left
			indexes[n+1] = p+2;  // bottom right (next panel)
			indexes[n+2] = p+3;  // top right (next panel)
			indexes[n+3] = p+1;  // top left
			n += 4;
			p += 2;
		}
		indexes[n+0] = p;    // bottom left
		indexes[n+1] = 0;    // bottom right (next panel)
		indexes[n+2] = 1;    // top right (next panel)
		indexes[n+3] = p+1;  // top left

		IndexedQuadArray quads = new IndexedQuadArray(
			coordinates.length/3,  // number of vertexes
			GeometryArray.COORDINATES |  // format
			GeometryArray.NORMALS |
			GeometryArray.TEXTURE_COORDINATE_2,
			indexes.length );    // number of indexes
		quads.setCoordinates( 0, coordinates );
		quads.setTextureCoordinates( 0, textureCoordinates );
		quads.setNormals( 0, normals );
		quads.setCoordinateIndices( 0, indexes );
		quads.setTextureCoordinateIndices( 0, indexes );
		quads.setNormalIndices( 0, indexes );

		Shape3D shape = new Shape3D( quads, mainAppearance );

		Vector3f trans = new Vector3f( 0.0f, y, 0.0f );
		Transform3D tr = new Transform3D( );
		tr.set( trans );    // translate
		TransformGroup tg = new TransformGroup( tr );

		tg.addChild( shape );
		addChild( tg );
	}



	//
	//  Control the appearance
	//
	public void setAppearance( Appearance app )
	{
		mainAppearance = app;
	}


	//
	//  Provide info on the shape and geometry
	//
	public Shape3D getShape( int partid )
	{
		return null;
	}

	public int getNumTriangles( )
	{
		return 0;
	}

	public int getNumVertices( )
	{
		return 2;
	}
}

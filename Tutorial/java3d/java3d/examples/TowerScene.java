//
//  CLASS
//    TowerScene	-  shapes and lights for a scene with towers
//
//  DESCRIPTION
//    This class builds a scene containing a cratered surface, a set of
//    stone towers, plus appropriate lighting.  The scene is used in several
//    of the examples to provide content to affect with lights, background
//    colors and images, and so forth.
//
//  SEE ALSO
//    ExBackgroundColor
//    ExBackgroundImage
//    ExBackgroundGeometry
//
//  AUTHOR
//    David R. Nadeau / San Diego Supercomputer Center
//

import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.*;

public class TowerScene
	extends Group
{
	private static final double[][] craters = {
		// x,z,radius are in a normalized -1.0 to 1.0 space
		// x     z     radius  depth
		{  0.0,  0.0,  0.7,    0.20 },
		{  0.3,  0.3,  0.5,    0.20 },
		{ -0.3,  0.1,  0.6,    0.20 },
		{ -0.2,  0.4,  0.4,    0.20 },
		{ -0.9, -0.9,  0.5,    0.20 },
		{  0.4,  0.5,  0.3,    0.10 },
		{  0.9, -0.2,  0.4,    0.10 },
		{ -0.8,  0.1,  0.2,    0.10 },
		{  0.2,  0.7,  0.3,    0.20 },
		{  0.5, -0.5,  0.21,   0.20 },
		{  0.8, -0.8,  0.16,   0.10 },
		{ -0.3,  0.7,  0.23,   0.20 },
		{  0.5,  0.5,  0.22,   0.10 },
		{ -0.7,  0.8,  0.15,   0.10 },
		{ -0.5, -0.3,  0.22,   0.10 },
		{  0.2,  0.2,  0.15,   0.10 },
		{  0.1,  0.8,  0.25,   0.20 },
		{  0.4,  0.9,  0.28,   0.09 },
		{  0.9, -0.1,  0.23,   0.10 },
		{  0.1, -0.0,  0.33,   0.08 },
		{  0.1, -0.9,  0.23,   0.20 },
		{ -1.0,  0.8,  0.13,   0.15 },
		{ -0.9,  0.7,  0.10,   0.15 },
		{ -0.2,  0.1,  0.10,   0.16 },
		{  1.1,  1.0,  0.12,   0.15 },
		{  0.9,  0.5,  0.13,   0.14 },
		{ -0.1, -0.1,  0.14,   0.15 },
		{ -0.5, -0.5,  0.10,   0.13 },
		{  0.1, -0.4,  0.10,   0.15 },
		{ -0.4, -1.0,  0.25,   0.15 },
		{  0.4,  1.0,  0.25,   0.15 },
	};

	public TowerScene( Component observer )
	{
		BoundingSphere worldBounds = new BoundingSphere(
			new Point3d( 0.0, 0.0, 0.0 ),  // Center
			1000.0 );                      // Extent

		// Add a few lights
		AmbientLight ambient = new AmbientLight( );
		ambient.setEnable( true );
		ambient.setColor( new Color3f( 0.2f, 0.2f, 0.2f ) );
		ambient.setInfluencingBounds( worldBounds );
		addChild( ambient );

		DirectionalLight dir1 = new DirectionalLight( );
		dir1.setEnable( true );
		dir1.setColor( new Color3f( 1.0f, 0.15f, 0.15f ) );
		dir1.setDirection( new Vector3f( 0.8f, -0.35f, -0.5f ) );
		dir1.setInfluencingBounds( worldBounds );
		addChild( dir1 );

		DirectionalLight dir2 = new DirectionalLight( );
		dir2.setEnable( true );
		dir2.setColor( new Color3f( 0.15f, 0.15f, 1.0f ) );
		dir2.setDirection( new Vector3f( -0.7f, -0.35f, 0.5f ) );
		dir2.setInfluencingBounds( worldBounds );
		addChild( dir2 );


		// Load textures
		TextureLoader texLoader = new TextureLoader( "moon5.jpg", observer );
		Texture moon = texLoader.getTexture( );
		if ( moon == null )
			System.err.println( "Cannot load moon5.jpg texture" );
		else
		{
			moon.setBoundaryModeS( Texture.WRAP );
			moon.setBoundaryModeT( Texture.WRAP );
			moon.setMinFilter( Texture.NICEST );
			moon.setMagFilter( Texture.NICEST );
			moon.setMipMapMode( Texture.BASE_LEVEL );
			moon.setEnable( true );
		}

		texLoader = new TextureLoader( "stonebrk2.jpg", observer );
		Texture stone = texLoader.getTexture( );
		if ( stone == null )
			System.err.println( "Cannot load stonebrk2.jpg texture" );
		else
		{
			stone.setBoundaryModeS( Texture.WRAP );
			stone.setBoundaryModeT( Texture.WRAP );
			stone.setMinFilter( Texture.NICEST );
			stone.setMagFilter( Texture.NICEST );
			stone.setMipMapMode( Texture.BASE_LEVEL );
			stone.setEnable( true );
		}


		//
		//  Build a rough terrain
		//
		Appearance moonApp = new Appearance( );

		Material moonMat = new Material( );
		moonMat.setAmbientColor( 0.5f, 0.5f, 0.5f );
		moonMat.setDiffuseColor( 1.0f, 1.0f, 1.0f );
		moonMat.setSpecularColor( 0.0f, 0.0f, 0.0f );
		moonApp.setMaterial( moonMat );

		TextureAttributes moonTexAtt = new TextureAttributes( );
		moonTexAtt.setTextureMode( TextureAttributes.MODULATE );
		moonTexAtt.setPerspectiveCorrectionMode( TextureAttributes.NICEST );
		moonApp.setTextureAttributes( moonTexAtt );

		if ( moon != null )
			moonApp.setTexture( moon );

		CraterGrid grid = new CraterGrid(
			50, 50,      // grid dimensions
			1.0, 1.0,    // grid spacing
			4.0,         // height exageration factor
			craters,     // grid elevations
			moonApp );   // grid appearance
		addChild( grid );


		//
		// Build several towers on the terrain
		//
		SharedGroup tower = new SharedGroup( );
		Appearance towerApp = new Appearance( );

		Material towerMat = new Material( );
		towerMat.setAmbientColor( 0.6f, 0.6f, 0.6f );
		towerMat.setDiffuseColor( 1.0f, 1.0f, 1.0f );
		towerMat.setSpecularColor( 0.0f, 0.0f, 0.0f );
		towerApp.setMaterial( towerMat );

		Transform3D tr = new Transform3D( );
		tr.setScale( new Vector3d( 4.0, 4.0, 1.0 ) );

		TextureAttributes towerTexAtt = new TextureAttributes( );
		towerTexAtt.setTextureMode( TextureAttributes.MODULATE );
		towerTexAtt.setPerspectiveCorrectionMode( TextureAttributes.NICEST );
		towerTexAtt.setTextureTransform( tr );
		towerApp.setTextureAttributes( towerTexAtt );

		if ( stone != null )
			towerApp.setTexture( stone );

		Arch towerShape = new Arch(
			0.0,          // start Phi
			1.571,        // end Phi
			2,            // nPhi
			0.0,          // start Theta
			Math.PI*2.0,  // end Theta
			5,            // nTheta
			3.0,          // start radius
			8.0,          // end radius
			0.0,          // start phi thickness
			0.0,          // end phi thickness
			towerApp );   // appearance
		tower.addChild( towerShape );


		// Place towers
		Matrix3f rot = new Matrix3f( );
		rot.setIdentity( );

		TransformGroup tg = new TransformGroup(
			new Transform3D( rot, new Vector3d( 2.0, -3.0, -8.0 ), 1.0 ) );
		tg.addChild( new Link( tower ) );
		addChild( tg );

		tg = new TransformGroup(
			new Transform3D( rot, new Vector3d( -1.0, -3.0, -6.0 ), 0.5 ) );
		tg.addChild( new Link( tower ) );
		addChild( tg );

		tg = new TransformGroup(
			new Transform3D( rot, new Vector3d( 5.0, -3.0, -6.0 ), 0.75 ) );
		tg.addChild( new Link( tower ) );
		addChild( tg );

		tg = new TransformGroup(
			new Transform3D( rot, new Vector3d( 1.0, -3.0, -3.0 ), 0.35 ) );
		tg.addChild( new Link( tower ) );
		addChild( tg );
	}
}

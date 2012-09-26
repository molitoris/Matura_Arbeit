/*
 *	@(#)ExAppearance.java 1.16 98/04/09 15:33:34
 *
 * Copyright (c) 1996-1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.event.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.*;
import javax.vecmath.*;

public class ExAppearance
	extends Example
{
	//--------------------------------------------------------------
	//  SCENE CONTENT
	//--------------------------------------------------------------

	public Group buildScene( )
	{
		// Turn off the example headlight
		setHeadlightEnable( false );

		// Build the scene group
		Group objRoot = new Group( );

		// Set up the global lights
		Color3f lColor1 = new Color3f(0.7f, 0.7f, 0.7f);
		Vector3f lDir1  = new Vector3f(-1.0f, -1.0f, -1.0f);
		Color3f alColor = new Color3f(0.2f, 0.2f, 0.2f);
		BoundingSphere bounds =
		    new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);

		AmbientLight aLgt = new AmbientLight(alColor);
		aLgt.setInfluencingBounds(bounds);
		DirectionalLight lgt1 = new DirectionalLight(lColor1, lDir1);
		lgt1.setInfluencingBounds(bounds);
		objRoot.addChild(aLgt);
		objRoot.addChild(lgt1);

		// Add a background
		Background background = new Background( );
		background.setColor( new Color3f( 0.0f, 0.5f, 1.0f ) );
		background.setApplicationBounds( bounds );
		objRoot.addChild( background );

		// Create a bunch of objects with a behavior and add them
		// into the scene graph.
		int row, col;
		Appearance[][] app = new Appearance[3][3];

		for (row = 0; row < 3; row++)
			for (col = 0; col < 3; col++)
			app[row][col] = createAppearance(row * 3 + col);

		for (int i = 0; i < 3; i++) {
			double ypos = (double)(i - 1) * 1.9;
			for (int j = 0; j < 3; j++) {
				double xpos = (double)(j - 1) * 2.1;
				objRoot.addChild(createObject(app[i][j], 1.0,  xpos, ypos));
			}
		}

		return objRoot;
	}

	private Appearance createAppearance(int idx) {
		Appearance app = new Appearance();

		// Globally used colors
		Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

		switch (idx) {
		// Unlit solid
		case 0:
			{
			// Set up the coloring properties
			Color3f objColor = new Color3f(1.0f, 0.2f, 0.4f);
			ColoringAttributes ca = new ColoringAttributes();
			ca.setColor(objColor);
			app.setColoringAttributes(ca);
			break;
			}


		// Unlit wire frame
		case 1:
			{
			// Set up the coloring properties
			Color3f objColor = new Color3f(1.0f, 0.2f, 0.4f);
			ColoringAttributes ca = new ColoringAttributes();
			ca.setColor(objColor);
			app.setColoringAttributes(ca);

			// Set up the polygon attributes
			PolygonAttributes pa = new PolygonAttributes();
			pa.setPolygonMode(pa.POLYGON_LINE);
			pa.setCullFace(pa.CULL_NONE);
			app.setPolygonAttributes(pa);

			// Set up line attributes
			LineAttributes lta = new LineAttributes();
			lta.setLineWidth(10.0f);
			app.setLineAttributes(lta);
			break;
			}

		// Unlit points
		case 2:
			{
			// Set up the coloring properties
			Color3f objColor = new Color3f(1.0f, 0.2f, 0.4f);
			ColoringAttributes ca = new ColoringAttributes();
			ca.setColor(objColor);
			app.setColoringAttributes(ca);

			// Set up the polygon attributes
			PolygonAttributes pa = new PolygonAttributes();
			pa.setPolygonMode(pa.POLYGON_POINT);
			pa.setCullFace(pa.CULL_NONE);
			app.setPolygonAttributes(pa);

			// Set up point attributes
			PointAttributes pta = new PointAttributes();
			pta.setPointSize(10.0f);
			app.setPointAttributes(pta);
			break;
			}

		// Lit solid
		case 3:
			{
			// Set up the material properties
			Color3f objColor = new Color3f(0.8f, 0.0f, 0.0f);
			app.setMaterial(new Material(objColor, black, objColor,
							 white, 80.0f));
			break;
			}

		// Texture mapped, lit solid
		case 4:
			{
			// Set up the texture map
			TextureLoader tex = new TextureLoader("apimage.jpg", this);
			app.setTexture(tex.getTexture());

			// Set up the material properties
			app.setMaterial(new Material(white, black, white, black, 1.0f));
			break;
			}

		// Transparent, lit solid
		case 5:
			{
			// Set up the transparency properties
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(ta.BLENDED);
			ta.setTransparency(0.6f);
			app.setTransparencyAttributes(ta);

			// Set up the polygon attributes
			PolygonAttributes pa = new PolygonAttributes();
			pa.setCullFace(pa.CULL_NONE);
			app.setPolygonAttributes(pa);

			// Set up the material properties
			Color3f objColor = new Color3f(0.7f, 0.8f, 1.0f);
			app.setMaterial(new Material(objColor, black, objColor,
							 black, 1.0f));
			break;
			}

		// Lit solid, no specular
		case 6:
			{
			// Set up the material properties
			Color3f objColor = new Color3f(0.0f, 0.0f, 0.8f);
			app.setMaterial(new Material(objColor, black, objColor,
							 black, 80.0f));
			break;
			}

		// Lit solid, specular only
		case 7:
			{
			// Set up the material properties
			Color3f objColor = new Color3f(0.8f, 0.0f, 0.0f);
			app.setMaterial(new Material(black, black, black,
							 white, 80.0f));
			break;
			}

		// Another lit solid with a different color
		case 8:
			{
			// Set up the material properties
			Color3f objColor = new Color3f(0.8f, 0.8f, 0.0f);
			app.setMaterial(new Material(objColor, black, objColor,
							 white, 80.0f));
			break;
			}

		default:
			{
			ColoringAttributes ca = new ColoringAttributes();
			ca.setColor(new Color3f(0.0f, 1.0f, 0.0f));
			app.setColoringAttributes(ca);
			}
		}

		return app;
	}


	private Group createObject(Appearance app, double scale,
					   double xpos, double ypos) {

		// Create a transform group node to scale and position the object.
		Transform3D t = new Transform3D();
		t.set(scale, new Vector3d(xpos, ypos, 0.0));
		TransformGroup objTrans = new TransformGroup(t);

		// Create a second transform group node and initialize it to the
		// identity.  Enable the TRANSFORM_WRITE capability so that
		// our behavior code can modify it at runtime.
		TransformGroup spinTg = new TransformGroup();
		spinTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		// Create a simple shape leaf node and set the appearance
		Shape3D shape = new Tetrahedron( );
		shape.setAppearance(app);

		// add it to the scene graph.
		spinTg.addChild(shape);

		// Create a new Behavior object that will perform the desired
		// operation on the specified transform object and add it into
		// the scene graph.
		Transform3D yAxis = new Transform3D();
		Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE,
						0, 0,
						5000, 0, 0,
						0, 0, 0);

		RotationInterpolator rotator =
			new RotationInterpolator(rotationAlpha, spinTg, yAxis,
						 0.0f, (float) Math.PI*2.0f);

		BoundingSphere bounds =
			new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);

		rotator.setSchedulingBounds(bounds);

		// Add the behavior and the transform group to the object
		objTrans.addChild(rotator);
		objTrans.addChild(spinTg);

		return objTrans;
	}



	//--------------------------------------------------------------
	//  USER INTERFACE
	//--------------------------------------------------------------

	//
	//  Main
	//
	public static void main( String[] args )
	{
		ExAppearance ex = new ExAppearance( );
		ex.initialize( args );
		ex.buildUniverse( );
		ex.showFrame( );
	}


	//
	//  Initialize the GUI (application and applet)
	//
	public void initialize( String[] args )
	{
		// Initialize the window, menubar, etc.
		super.initialize( args );
		exampleFrame.setTitle( "Java 3D Appearance Example" );
	}
}

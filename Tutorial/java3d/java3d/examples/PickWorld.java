/*
 *	@(#)PickWorld.java 1.10 98/04/13 13:49:14
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
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.behaviors.picking.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
 * PickWorld creates spheres, cylinders, and cones of different resolutions
 * and colors. You can pick each one and drag/zoom them around. 
 * The setup comes from TickTockPicking.
 */

public class PickWorld extends Applet {

    public BranchGroup createSceneGraph(Canvas3D c) {
	// Create the root of the branch graph
	BranchGroup objRoot = new BranchGroup();

        // Create a Transformgroup to scale all objects so they
        // appear in the scene.
        TransformGroup objScale = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setScale(1.0);
        objScale.setTransform(t3d);
        objRoot.addChild(objScale);

	// Create a bounds for the background and behaviors
	BoundingSphere bounds =
	    new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);

	// Attach picking behavior utlities to the scene root.
	// They will wake up when user manipulates a scene node.
	PickRotateBehavior behavior = new
	  PickRotateBehavior(objRoot, c, bounds);
	objRoot.addChild(behavior);

	PickZoomBehavior behavior2 = new
	  PickZoomBehavior(objRoot, c, bounds);
	objRoot.addChild(behavior2);

	PickTranslateBehavior behavior3 = new
	  PickTranslateBehavior(objRoot, c, bounds);
	objRoot.addChild(behavior3);

	// Set up the background
	Color3f bgColor = new Color3f(0.05f, 0.05f, 0.4f);
	Background bg = new Background(bgColor);
	bg.setApplicationBounds(bounds);
	objRoot.addChild(bg);

	// Set up the global lights
	Color3f lColor1 = new Color3f(0.7f, 0.7f, 0.7f);
	Vector3f lDir1  = new Vector3f(-1.0f, -1.0f, -1.0f);
	Color3f alColor = new Color3f(0.2f, 0.2f, 0.2f);

	AmbientLight aLgt = new AmbientLight(alColor);
	aLgt.setInfluencingBounds(bounds);
	DirectionalLight lgt1 = new DirectionalLight(lColor1, lDir1);
	lgt1.setInfluencingBounds(bounds);
	objRoot.addChild(aLgt);
	objRoot.addChild(lgt1);

	// Create a bunch of objects with a behavior and add them
	// into the scene graph.

	int row, col;
	int numRows = 3, numCols = 5;
	Appearance[][] app = new Appearance[numRows][numCols];

	for (row = 0; row < numRows; row++)
	    for (col = 0; col < numCols; col++)
		app[row][col] = createAppearance(row * numCols + col);

	for (int i = 0; i < numRows; i++) {
	    double ypos = (double)(i - numRows/2) * 0.6;
	    for (int j = 0; j < numCols; j++) {
		double xpos = (double)(j - numCols/2) * 0.4;
		objScale.addChild(createObject(i, j, app[i][j], 0.1,  xpos, ypos));
	    }
	}

        // Let Java 3D perform optimizations on this scene graph.
        objRoot.compile();

	return objRoot;
    }


    private Appearance createAppearance(int idx) {
	Appearance app = new Appearance();

	// Globally used colors
	Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
	Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
	Color3f gray = new Color3f(0.4f, 0.4f, 0.4f);
	idx = idx % 5;

	switch (idx) {
	// Lit solid
	case 0:
	    {
		// Set up the material properties
		Color3f objColor = new Color3f(0.0f, 0.8f, 0.0f);
		app.setMaterial(new Material(objColor, black, objColor,
					     white, 80.0f));
		break;
	    }
	// Lit solid, specular only
	case 1:
	    {

		// Set up the material properties
		Color3f objColor = new Color3f(0.0f, 0.4f, 0.2f);
		app.setMaterial(new Material(black, black, objColor,
					     white, 80.0f));
		break;
	    }

	case 2:
	    {
		// Set up the texture map
		TextureLoader tex = new TextureLoader("apimage.jpg", this);
		app.setTexture(tex.getTexture());

		// Set up the material properties
		app.setMaterial(new Material(gray, black, gray, white, 1.0f));
		break;
	    }


	// Texture mapped, lit solid
	case 3:
	    {
		// Set up the texture map
		TextureLoader tex = new TextureLoader("earth.jpg", this);
		app.setTexture(tex.getTexture());

		// Set up the material properties
		app.setMaterial(new Material(gray, black, gray, white, 1.0f));
		break;
	    }


	// Another lit solid with a different color
	case 4:
	    {
		// Set up the material properties
		Color3f objColor = new Color3f(1.0f, 1.0f, 0.0f);
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


    private Group createObject(int i, int j, Appearance app, double scale,
			       double xpos, double ypos) {

	// Create a transform group node to scale and position the object.
	Transform3D t = new Transform3D();
	t.set(scale, new Vector3d(xpos, ypos, 0.0));
	TransformGroup objTrans = new TransformGroup(t);
	objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	objTrans.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

	// Create a second transform group node and initialize it to the
	// identity.  Enable the TRANSFORM_WRITE capability so that
	// our behavior code can modify it at runtime.
	TransformGroup spinTg = new TransformGroup();
	spinTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	spinTg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	spinTg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
	Primitive obj = null;

	if (i % 3 == 2){
	  obj = (Primitive) new Sphere(1.0f, 
				       Sphere.GENERATE_NORMALS | 
				       Sphere.GENERATE_TEXTURE_COORDS,
				       j*8 + 4,app);
	}
	else 
	  if (i % 3 == 1){
	  obj = (Primitive) new Cylinder(1.0f, 2.0f,
			     Cylinder.GENERATE_TEXTURE_COORDS | 
			     Cylinder.GENERATE_NORMALS,
					 j*8+4,j*8+4,
					 app);
	}
	else 
	  if (i % 3 == 0){
	  obj = (Primitive) new Cone(1.0f, 2.0f, 
			 Cone.GENERATE_NORMALS |
			 Cone.GENERATE_TEXTURE_COORDS, 
				     j*8+4,j*8+4,
				     app);
	}

	// add it to the scene graph.
	spinTg.addChild(obj);
	objTrans.addChild(spinTg);

	return objTrans;
    }


    public PickWorld() {
	setLayout(new BorderLayout());
	Canvas3D c = new Canvas3D(null);
	add("Center", c);

	// Create a simple scene and attach it to the virtual universe
	BranchGroup scene = createSceneGraph(c);
	SimpleUniverse u = new SimpleUniverse(c);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        u.getViewingPlatform().setNominalViewingTransform();

	u.addBranchGraph(scene);
    }

    //
    // The following allows PickWorld to be run as an application
    // as well as an applet
    //
    public static void main(String[] args) {
	new MainFrame(new PickWorld(), 640, 480);
    }
}

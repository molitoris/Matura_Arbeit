/*
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



import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.Scene;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.event.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import java.io.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class A3DApplet extends Applet {

  public A3DApplet(String filename) {
    setLayout(new BorderLayout());
    Canvas3D c = new Canvas3D(null);
    add("Center", c);

    // Create a simple scene and attach it to the virtual universe
    BranchGroup scene = createSceneGraph(filename);
    SimpleUniverse u = new SimpleUniverse(c);
    u.getViewingPlatform().setNominalViewingTransform();
    u.addBranchGraph(scene);
  }

  public BranchGroup createSceneGraph(String filename) {
    // Create the root of the branch graph
    BranchGroup sceneRoot = new BranchGroup();

    // Create a bounds for the background and lights
    BoundingSphere bounds =
      new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);

    // Set up the background Color
    Color3f bgColor = new Color3f(0.05f, 0.05f, 0.2f);
    Background bgNode = new Background(bgColor);
    bgNode.setApplicationBounds(bounds);
    sceneRoot.addChild(bgNode);

    // Set up the global lights
    // First, define the color of the lights
    Color3f light1Color = new Color3f(1.0f, 1.0f, 0.9f);
    Vector3f light1Direction  = new Vector3f(4.0f, -7.0f, -12.0f);
    Color3f light2Color = new Color3f(0.3f, 0.3f, 0.4f);
    Vector3f light2Direction  = new Vector3f(-6.0f, -2.0f, -1.0f);
    Color3f ambientColor = new Color3f(0.1f, 0.1f, 0.1f);

    // Second, define the ambient light, and insert it in the branch
    AmbientLight ambientLightNode = new AmbientLight(ambientColor);
    ambientLightNode.setInfluencingBounds(bounds);
    sceneRoot.addChild(ambientLightNode);

    // Lastly, define the directional lights and insert it
    DirectionalLight light1
      = new DirectionalLight(light1Color, light1Direction);
    light1.setInfluencingBounds(bounds);
    sceneRoot.addChild(light1);

    DirectionalLight light2
      = new DirectionalLight(light2Color, light2Direction);
    light2.setInfluencingBounds(bounds);
    sceneRoot.addChild(light2);

    // Create the scene's transform group node (starts as
    // identity).  Enable the TRANSFORM_WRITE capability so that
    // our behavior code can modify it at runtime.  Note, we DO
    // NOT set the TRANSFORM_READ capability because we only do
    // writes. Add it to the root of the branch graph
    TransformGroup sceneTG = new TransformGroup();
    sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    sceneRoot.addChild(sceneTG);

    // Create an Alpha object that goes from 0 to 1 in 8 seconds,
    // repeatedly.
    Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE,
				    0, 0,
				    4000, 0, 0,
				    0, 0, 0);

    // Create a new Behavior object that will rotate the scene
    // add it into the scene graph.
    Transform3D yAxis = new Transform3D();
    RotationInterpolator rotator =
      new RotationInterpolator(rotationAlpha, sceneTG, yAxis,
			       0.0f, (float) Math.PI*2.0f);
    rotator.setSchedulingBounds(bounds);
    sceneTG.addChild(rotator);

    // Load the object
    TransformGroup objTG = new TransformGroup();
    Transform3D objTrans = new Transform3D();
    objTG.getTransform(objTrans);
    objTrans.setScale( 0.6 );
    objTG.setTransform(objTrans);
    sceneTG.addChild(objTG);
	
    int flags = ObjectFile.RESIZE;
    ObjectFile f =  new ObjectFile(flags, (float)(49.0 * Math.PI / 180.0));

    Scene s = null;
    try {
      s = f.load(filename == null ? "galleon.obj" : filename);
    }
    catch (FileNotFoundException e) {
	System.err.println(e);
	System.exit(1);
    }
    catch (ParsingErrorException e) {
	System.err.println(e);
	System.exit(1);
    }
    catch (IncorrectFormatException e) {
	System.err.println(e);
	System.exit(1);
    }

    //sceneTG.addChild(f);
    objTG.addChild(s.getSceneGroup());

    return sceneRoot;
  }

  //
  // The following allows A3DApplet to be run as an application
  // as well as an applet
  //
  public static void main(String[] args) {
      if (args.length >= 1)
	  new MainFrame(new A3DApplet(args[0]), 640, 480);
      else
	  new MainFrame(new A3DApplet(null), 640, 480);
  }
}

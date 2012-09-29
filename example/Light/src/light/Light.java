/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package light;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.text.AttributeSet.ColorAttribute;
import javax.vecmath.Color3f;

/**
 *
 * @author Rafael Müller_2
 */
public class Light extends Applet
{

    public Light()
    {
	GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
	Canvas3D canvas = new Canvas3D(config);
	
	SimpleUniverse universe = new SimpleUniverse(canvas);
	universe.getViewingPlatform().setNominalViewingTransform();
	
	BranchGroup scene = createSceneGraph();
	
	
	scene.compile();
	universe.addBranchGraph(scene);
	
	setLayout(new BorderLayout());
	add("Center", canvas);
    }
    
    private BranchGroup createSceneGraph()
    {
	BranchGroup objRoot = new BranchGroup();
	
	TransformGroup transformGroup = new TransformGroup();
	transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	
	Sphere sphere = new Sphere(0.5f, Sphere.GENERATE_NORMALS, createAppearance());
	
	transformGroup.addChild(sphere);
	
	Transform3D yAxis = new Transform3D();
	yAxis.rotY(Math.PI/2.0f);	

	Alpha objAlpha = new Alpha(1, 2000);	
	RotationInterpolator objInterpolator = new RotationInterpolator(objAlpha, transformGroup, yAxis, 0.0f, (float) Math.toRadians(90));
	
	BoundingSphere objSphere = new BoundingSphere();	
	objInterpolator.setSchedulingBounds(objSphere);
		
	objRoot.addChild(objInterpolator);
	objRoot.addChild(transformGroup);
	
	//Ambient Light
	AmbientLight ambientLight = new AmbientLight();
	ambientLight.setInfluencingBounds(new BoundingSphere());
	ambientLight.setEnable(true);
	objRoot.addChild(ambientLight);
	
	
	//Directional Light
	DirectionalLight directionalLight = new DirectionalLight();
	directionalLight.setInfluencingBounds(new BoundingSphere());
	directionalLight.setColor(new Color3f(Color.yellow));
	directionalLight.setDirection(-0.5f, -0.5f, -1);
	objRoot.addChild(directionalLight);
		
	return objRoot;
    }
    
    private Appearance createAppearance()
    {
	Appearance appearance = new Appearance();
	Material material = new Material();
		
	appearance.setMaterial(material);
	
	return appearance;
    }
    
    public static void main(String[] args)
    {
	MainFrame frame = new MainFrame(new Light(), 500, 500);
    }
}

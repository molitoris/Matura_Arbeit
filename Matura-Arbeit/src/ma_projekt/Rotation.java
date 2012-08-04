package ma_projekt;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import javax.media.j3d.*;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.vecmath.Point3d;


public class Rotation
{
    public class MyBehavoir extends Behavior
    {
	/*
	 * INIZIALISE
	 * Die Bedinung für den Listener wird definiert.
	 * 
	 * PROCESSSTIMULUS
	 * Die Aktion wird beschriben. Der Listener wird wieder eingefügt, damit
	 * die Methode mehrmals aufgerufen werden kann.
	 */
	
	private TransformGroup tg;
	private Transform3D drehung = new Transform3D();
	private double winkel = 0.0;

	public MyBehavoir(TransformGroup tg)
	{
	    this.tg = tg;
	}	
	
	@Override
	public void initialize()
	{
	    this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
	}

	@Override
	public void processStimulus(Enumeration enmrtn)
	{
		winkel += 0.02;
		drehung.rotY(winkel);
		tg.setTransform(drehung);

	    
	    this.wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
	}
	
    }
    
    public Rotation()
    {
	JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	
	
	GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
	Canvas3D canvas = new Canvas3D(config);
	frame.add(canvas);
	
	SimpleUniverse universe = new SimpleUniverse();
	universe.getViewingPlatform().setNominalViewingTransform();
	universe.addBranchGraph(createSceneGraph());	
    }
    
    public BranchGroup createSceneGraph()
    {
	BranchGroup objRoot = new BranchGroup();
	BoundingSphere bs = new BoundingSphere(new Point3d(0.0f, 0.0f, 0.0f), Double.MAX_VALUE);
	
	TransformGroup objRotate = new TransformGroup();
	objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	objRotate.addChild(new ColorCube(0.4d));
	objRoot.addChild(objRotate);
	
	MyBehavoir mb = new MyBehavoir(objRotate);
	mb.setSchedulingBounds(bs);
	objRoot.addChild(mb);
	
	objRoot.compile();
	return objRoot;
    }
    
    public static void main (String[] args)
    {
	Rotation rot = new Rotation();
    }
}
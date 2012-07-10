
package ma_projekt;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.*;
import javax.swing.*;


/**
 *
 * @author Markus Jordi
 */
public class Test_6 extends JFrame
{
    public Test_6(String title)
    {
        super(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(300, 300);
        
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();        
        Canvas3D canvas = new Canvas3D(config);        
        add(canvas);
        
        SimpleUniverse universe = new SimpleUniverse(canvas);        
        universe.getViewingPlatform().setNominalViewingTransform();        
        universe.addBranchGraph(createSceneGraph());
    }
    
    public BranchGroup createSceneGraph()
    {
        BranchGroup bg =new BranchGroup();
        
        TransformGroup tg = new TransformGroup();
        tg.addChild(new ColorCube(0.5d));
        
        bg.addChild(tg);
        
        return bg;
    }
    
    public static void main(String[] args)
    {
        Test_6 a = new Test_6("Beispiel");
        a.setVisible(true);
    }
}

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.geometry.ColorCube;
import javax.media.j3d.BranchGroup;
import javax.swing.JFrame;

public class Cube_001
{    
    public Cube_001()
    {        
        //Erstellt neue, einfaches Universum
        SimpleUniverse universe = new SimpleUniverse();
	
	//Setzt die Ansicht auf einen Grundwert
        universe.getViewingPlatform().setNominalViewingTransform();
	
        //Erstelle eine BranchGroup
        BranchGroup group = new BranchGroup();
	
	//Fügt einen ColorCube in die BranchGroup
        group.addChild(new ColorCube(0.3));
	
	//Fügt Brnachgroup ins SimpleUniverse ein.
        universe.addBranchGraph(group);        
    }
    
    public static void main(String[] args){
        new Cube_001();
    }
}


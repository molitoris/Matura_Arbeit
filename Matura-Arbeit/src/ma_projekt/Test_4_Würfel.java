/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ma_projekt;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.geometry.ColorCube;
import javax.media.j3d.BranchGroup;

/**
 *
 * @author Rafael Müller_2
 */
public class Test_4_Würfel
{
    public Test_4_Würfel()
    {
        
        //Erstellt neue Umgebung
        SimpleUniverse universe = new SimpleUniverse();
        
        //Ist das einzige Element, dass in ein Universum gegeben werden kann
        BranchGroup group = new BranchGroup();
        
        //Fügt ins die Branchgroup einen Würfel ein
        group.addChild(new ColorCube(0.3));
        
        //Setzt Sichtfeld ein
        universe.getViewingPlatform().setNominalViewingTransform();
        
        //Fügt Brnachgroup ins Universum ein.
        universe.addBranchGraph(group);
    }
    
    public static void main(String[] args)
    {
        new Test_4_Würfel();
    }
}

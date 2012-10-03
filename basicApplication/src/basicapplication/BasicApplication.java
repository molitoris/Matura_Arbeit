/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basicapplication;

import java.util.Locale;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;

/**
 *
 * @author Rafael Müller_2
 */
public class BasicApplication
{

    public BasicApplication()
    {
	Canvas3D canvas = new Canvas3D(null);
	VirtualUniverse universe = new VirtualUniverse();
	
	Locale locale = new Locale(null);
	
	View view = new View();
	ViewPlatform viewPlatform = new ViewPlatform();
	PhysicalBody body = new PhysicalBody();
	PhysicalEnvironment environement = new PhysicalEnvironment();
	
	view.addCanvas3D(canvas);
	view.attachViewPlatform(viewPlatform);
	view.setPhysicalBody(body);
	view.setPhysicalEnvironment(environement);
	
    }
    
    public static void main(String[] args)
    {
	
    }
}

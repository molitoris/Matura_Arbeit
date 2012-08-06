/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ma_projekt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Rafael Müller_2
 */
public class HashMapDemo
{
    public static void main(String [] args)
    {
	HashMap hm  = new HashMap();
	hm.put("John Doe", new Double(3434.34));
	hm.put("Tom Smith", new Double(123.22));
	hm.put("Jane Baker", new Double(1378.00)); 
	hm.put("Todd Hall", new Double(99.22)); 
	hm.put("Ralph Smith", new Double(-19.08));
	
	Set set = hm.entrySet();
	Iterator i = set.iterator();
	
	while(i.hasNext())
	{
	    Map.Entry me = (Map.Entry)i.next();
	    System.out.print(me.getKey() + ": ");
	    System.out.println(me.getValue());
	}
	System.out.println();
	
	double balance = ((Double) hm.get("John Doe")).doubleValue();
	hm.put("John Doe", balance + 1000);
	System.out.println("John Doe's new balance: " + hm.get("John Doe"));
    }
}

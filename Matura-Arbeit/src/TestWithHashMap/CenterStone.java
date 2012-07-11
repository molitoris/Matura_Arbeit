/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TestWithHashMap;

import javax.vecmath.Color3f;

/**
 *
 * @author Rafael Müller_2
 */
public class CenterStone extends Stone
{
    Color3f black = new Color3f(0.0f,  0.0f, 0.0f);
    
    public CenterStone(float x, float y, float z, Color3f front)
    {
        super(x, y, z);
        
        
        Stone stone = new Stone(x, y, z, black, black, black, black, black, black);
        
        
    }
}

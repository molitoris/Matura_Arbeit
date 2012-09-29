/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package techcube;

/**
 *
 * @author Rafael Müller_2
 */
public class TechCube
{
    
    public static void main(String[] args) throws InterruptedException
    {
	String[] position   = new String[54];
	String[] side	    = new String[6];
	int j = 0;
	
	for(int i = 0; i < 9; i++)
	    position[i] = "[w]";	//white Face
	for(int i = 9; i < 18; i++)
	    position[i] = "[g]";	//green Face
	for(int i = 18; i < 27; i++)
	    position[i] = "[y]";	//yelllow Face
	for(int i = 27; i < 36; i++)
	    position[i] = "[b]";	//blue Face
	for(int i = 36; i < 45; i++)
	    position[i] = "[r]";
	for(int i = 45; i < 54; i++)
	    position[i] = "[o]";

	
	side[0] = "Front (white)";
	side[1] = "Right (green)";
	side[2] = "Back (yellow)";
	side[3] = "Left (blue)";
	side[4] = "Top (red)";
	side[5] = "Bottom (orange)";
	
	drawCube(position, side, j);
	
	System.out.println();
	System.out.println("Grundaufstellung beendet");
	System.out.println();
	
	rotateSide(1);

    }

    private static void rotateSide(int side)
    {
	switch (side)
	{
	    case 1:
		System.out.println("Drehe Front:");
		
		break;
		
		    
	}
		
    }

    private static void drawCube(String[] side, String[] position, int j)
    {
	for(int i = 0; i < position.length; i++){
	    if(i % 9 == 0){
		System.out.println(side[j]);
		j++;
	    }		
	    if((i+1) % 3 == 0)
		System.out.println(position[i]);
	    else
		System.out.print(position[i]);
	}
    }


}

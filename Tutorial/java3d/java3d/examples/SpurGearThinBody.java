/*
 *	@(#)SpurGearThinBody.java 1.3 98/02/20 14:29:59
 *
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

import java.lang.Math.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class SpurGearThinBody extends SpurGear {
    
    /**
     * Construct a SpurGearThinBody;
     * @return a new spur gear that conforms to the input paramters
     * @param toothCount number of teeth
     * @param pitchCircleRadius radius at center of teeth
     * @param shaftRadius radius of hole at center
     * @param addendum distance from pitch circle to top of teeth
     * @param dedendum distance from pitch circle to root of teeth
     * @param gearThickness  thickness of the gear
     */
    public SpurGearThinBody(int toothCount, float pitchCircleRadius,
			    float shaftRadius, float addendum, float dedendum,
			    float gearThickness) {
	this(toothCount, pitchCircleRadius, shaftRadius,
	     addendum, dedendum, gearThickness, gearThickness, 0.25f, null);
    }

    /**
     * Construct a SpurGearThinBody;
     * @return a new spur gear that conforms to the input paramters
     * @param toothCount number of teeth
     * @param pitchCircleRadius radius at center of teeth
     * @param shaftRadius radius of hole at center
     * @param addendum distance from pitch circle to top of teeth
     * @param dedendum distance from pitch circle to root of teeth
     * @param gearThickness  thickness of the gear
     * @param look the gear's appearance
     */
    public SpurGearThinBody(int toothCount, float pitchCircleRadius,
			    float shaftRadius, float addendum, float dedendum,
			    float gearThickness,
		    Appearance look) {
	this(toothCount, pitchCircleRadius, shaftRadius,
	     addendum, dedendum, gearThickness, gearThickness, 0.25f, look);
    }

    /**
     * Construct a SpurGearThinBody;
     * @return a new spur gear that conforms to the input paramters
     * @param toothCount number of teeth
     * @param pitchCircleRadius radius at center of teeth
     * @param shaftRadius radius of hole at center
     * @param addendum distance from pitch circle to top of teeth
     * @param dedendum distance from pitch circle to root of teeth
     * @param gearThickness thickness of the gear
     * @param toothTipThickness thickness of the tip of the tooth
     * @param look the gear's appearance
     */
    public SpurGearThinBody(int toothCount, float pitchCircleRadius,
			    float shaftRadius, float addendum, float dedendum,
			    float gearThickness, float toothTipThickness,
			    Appearance look) {
	this(toothCount, pitchCircleRadius, shaftRadius, addendum,
	     dedendum, gearThickness, toothTipThickness, 0.25f, look);
	}

    /**
     * Construct a SpurGearThinBody;
     * @return a new spur gear that conforms to the input paramters
     * @param toothCount number of teeth
     * @param pitchCircleRadius radius at center of teeth
     * @param shaftRadius radius of hole at center
     * @param addendum distance from pitch circle to top of teeth
     * @param dedendum distance from pitch circle to root of teeth
     * @param gearThickness thickness of the gear
     * @param toothTipThickness thickness of the tip of the tooth
     * @param toothToValleyRatio ratio of tooth valley to circular pitch
     * (must be <= .25) 
     * @param look the gear's appearance object
     */
    public SpurGearThinBody(int toothCount, float pitchCircleRadius,
			    float shaftRadius, float addendum, float dedendum,
			    float gearThickness, float toothTipThickness,
			    float toothToValleyAngleRatio, Appearance look) { 

	this(toothCount, pitchCircleRadius, shaftRadius, addendum,
	     dedendum, gearThickness, toothTipThickness, 0.25f, look,
	     0.6f * gearThickness, 0.75f * (pitchCircleRadius - shaftRadius));
    }

    /**
     * Construct a SpurGearThinBody;
     * @return a new spur gear that conforms to the input paramters
     * @param toothCount number of teeth
     * @param pitchCircleRadius radius at center of teeth
     * @param shaftRadius radius of hole at center
     * @param addendum distance from pitch circle to top of teeth
     * @param dedendum distance from pitch circle to root of teeth
     * @param gearThickness thickness of the gear
     * @param toothTipThickness thickness of the tip of the tooth
     * @param toothToValleyRatio ratio of tooth valley to circular pitch
     * (must be <= .25)
     * @param look the gear's appearance object
     * @param bodyThickness the thickness of the gear body
     * @param crossSectionWidth the width of the depressed portion of the
     * gear's body
     */
    public SpurGearThinBody(int toothCount, float pitchCircleRadius,
			    float shaftRadius, float addendum, float dedendum,
			    float gearThickness, float toothTipThickness,
			    float toothToValleyAngleRatio, Appearance look,
			    float bodyThickness, float crossSectionWidth) {

	super(toothCount, pitchCircleRadius, addendum, dedendum,
	      toothToValleyAngleRatio);

	float diskCrossSectionWidth =
	    (rootRadius - shaftRadius - crossSectionWidth)/ 2.0f;
	float outerShaftRadius = shaftRadius + diskCrossSectionWidth;
	float innerToothRadius = rootRadius - diskCrossSectionWidth;

	// Generate the gear's body disks, first by the shaft, then in
	// the body and, lastly, by the teeth
	addBodyDisks(shaftRadius, outerShaftRadius,
		     gearThickness, look);
	addBodyDisks(innerToothRadius, rootRadius,
		     gearThickness, look);
	addBodyDisks(outerShaftRadius, innerToothRadius,
		     bodyThickness, look);

	// Generate the gear's "shaft" equivalents the two at the teeth
	// and the two at the shaft
	addCylinderSkins(innerToothRadius, gearThickness, InwardNormals, look);
	addCylinderSkins(outerShaftRadius, gearThickness, OutwardNormals, look);
	
	// Generate the gear's interior shaft
	addCylinderSkins(shaftRadius, gearThickness, InwardNormals, look);

	// Generate the gear's teeth
	addTeeth(pitchCircleRadius, rootRadius,
		 outsideRadius, gearThickness, toothTipThickness,
		 toothToValleyAngleRatio, look);
    }

}

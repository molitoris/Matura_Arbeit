/*
 *	@(#)SpurGear.java 1.12 98/02/20 14:29:58
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

public class SpurGear extends Gear {
    
    float toothTopAngleIncrement;
    float toothDeclineAngleIncrement;

    float rootRadius;
    float outsideRadius;

    //The angle subtended by the ascending or descending portion of a tooth
    float circularToothEdgeAngle;
    // The angle subtended by a flat (either a tooth top or a valley
    // between teeth
    float circularToothFlatAngle;

    /**
     * internal constructor for SpurGear, used by subclasses to establish
     * SpurGear's required state
     * @return a new spur gear that contains sufficient information to
     * continue building
     * @param toothCount number of teeth
     * @param pitchCircleRadius radius at center of teeth
     * @param addendum distance from pitch circle to top of teeth
     * @param dedendum distance from pitch circle to root of teeth
     * @param toothToValleyAngleRatio the ratio of the angle subtended by the
     * tooth to the angle subtended by the valley (must be <= .25) 
     */
    SpurGear(int toothCount, float pitchCircleRadius,
	     float addendum, float dedendum, float toothToValleyAngleRatio) {
	
	super(toothCount);
	
	// The angle about Z subtended by one tooth and its associated valley
	circularPitchAngle = (float)(2.0 * Math.PI / (double)toothCount);

	// The angle subtended by a flat (either a tooth top or a valley
	// between teeth
	circularToothFlatAngle = circularPitchAngle * toothToValleyAngleRatio;

	//The angle subtended by the ascending or descending portion of a tooth
	circularToothEdgeAngle = circularPitchAngle/2.0f -
	    circularToothFlatAngle;

	// Increment angles
	toothTopAngleIncrement = circularToothEdgeAngle;
	toothDeclineAngleIncrement
	    = toothTopAngleIncrement + circularToothFlatAngle;
	toothValleyAngleIncrement
	    = toothDeclineAngleIncrement + circularToothEdgeAngle;

	// Differential angles for offsetting to the center of tooth's top
	// and valley
	toothTopCenterAngle
	    = toothTopAngleIncrement + circularToothFlatAngle/2.0f;
	valleyCenterAngle
	    = toothValleyAngleIncrement +  circularToothFlatAngle/2.0f;

	// Gear start differential angle. All gears are constructed with the
	// center of a tooth at Z-axis angle = 0.
	gearStartAngle = -1.0 * toothTopCenterAngle;

	// The radial distance to the root and top of the teeth, respectively
	rootRadius = pitchCircleRadius - dedendum;
	outsideRadius = pitchCircleRadius + addendum;
    
	// Allow this object to spin. etc.
	this.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    }

    /**
     * Construct a SpurGear;
     * @return a new spur gear that conforms to the input paramters
     * @param toothCount number of teeth
     * @param pitchCircleRadius radius at center of teeth
     * @param shaftRadius radius of hole at center
     * @param addendum distance from pitch circle to top of teeth
     * @param dedendum distance from pitch circle to root of teeth
     * @param gearThickness  thickness of the gear
     */
    public SpurGear(int toothCount, float pitchCircleRadius, float shaftRadius,
		    float addendum, float dedendum, float gearThickness) {
	this(toothCount, pitchCircleRadius, shaftRadius, addendum, dedendum,
	     gearThickness, gearThickness, 0.25f, null);
    }

    /**
     * Construct a SpurGear;
     * @return a new spur gear that conforms to the input paramters
     * @param toothCount number of teeth
     * @param pitchCircleRadius radius at center of teeth
     * @param shaftRadius radius of hole at center
     * @param addendum distance from pitch circle to top of teeth
     * @param dedendum distance from pitch circle to root of teeth
     * @param gearThickness  thickness of the gear
     * @param look the gear's appearance
     */
    public SpurGear(int toothCount, float pitchCircleRadius, float shaftRadius,
		    float addendum, float dedendum, float gearThickness,
		    Appearance look) {
	this(toothCount, pitchCircleRadius, shaftRadius, addendum, dedendum,
	     gearThickness, gearThickness, 0.25f, look);
    }

    /**
     * Construct a SpurGear;
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
    public SpurGear(int toothCount, float pitchCircleRadius, float shaftRadius,
		    float addendum, float dedendum, float gearThickness,
		    float toothTipThickness, Appearance look) {
	this(toothCount, pitchCircleRadius, shaftRadius, addendum, dedendum,
	     gearThickness, toothTipThickness, 0.25f, look);
	}

    /**
     * Construct a SpurGear;
     * @return a new spur gear that conforms to the input paramters
     * @param toothCount number of teeth
     * @param pitchCircleRadius radius at center of teeth
     * @param shaftRadius radius of hole at center
     * @param addendum distance from pitch circle to top of teeth
     * @param dedendum distance from pitch circle to root of teeth
     * @param gearThickness thickness of the gear
     * @param toothTipThickness thickness of the tip of the tooth
     * @param toothToValleyAngleRatio the ratio of the angle subtended by the
     * tooth to the angle subtended by the valley (must be <= .25) 
     * @param look the gear's appearance object
     */
    public SpurGear(int toothCount, float pitchCircleRadius, float shaftRadius,
		    float addendum, float dedendum, float gearThickness,
		    float toothTipThickness, float toothToValleyAngleRatio,
		    Appearance look) {

    this(toothCount, pitchCircleRadius, addendum, dedendum,
	 toothToValleyAngleRatio);

	// Generate the gear's body disks
	addBodyDisks(shaftRadius, rootRadius, gearThickness, look);
	
	// Generate the gear's interior shaft
	addCylinderSkins(shaftRadius, gearThickness, InwardNormals, look);

	// Generate the gear's teeth
	addTeeth(pitchCircleRadius, rootRadius,
		 outsideRadius, gearThickness, toothTipThickness,
		 toothToValleyAngleRatio, look);
    }

    /**
     * Construct a SpurGear's teeth by adding the teeth shape nodes
     * @param pitchCircleRadius radius at center of teeth
     * @param rootRadius distance from pitch circle to top of teeth
     * @param outsideRadius distance from pitch circle to root of teeth
     * @param gearThickness thickness of the gear
     * @param toothTipThickness thickness of the tip of the tooth
     * @param toothToValleyAngleRatio the ratio of the angle subtended by the
     * tooth to the angle subtended by the valley (must be <= .25) 
     * @param look the gear's appearance object
     */
    void addTeeth(float pitchCircleRadius, float rootRadius,
		  float outsideRadius, float gearThickness,
		  float toothTipThickness, float toothToValleyAngleRatio,
		  Appearance look) {
	int index;
	Shape3D newShape;
	
	// Temporaries that store start angle for each portion of tooth facet
	double toothStartAngle, toothTopStartAngle,
	    toothDeclineStartAngle, toothValleyStartAngle,
	    nextToothStartAngle;

	// The x and y coordinates at each point of a facet and at each
	// point on the gear: at the shaft, the root of the teeth, and
	// the outer point of the teeth
	float xRoot0, yRoot0;
	float xOuter1, yOuter1;
	float xOuter2, yOuter2;
	float xRoot3, yRoot3;
	float xRoot4, yRoot4;

	// The z coordinates for the gear
	final float frontZ = -0.5f * gearThickness;
	final float rearZ = 0.5f * gearThickness;

	// The z coordinates for the tooth tip of the gear
	final float toothTipFrontZ = -0.5f * toothTipThickness;
	final float toothTipRearZ = 0.5f * toothTipThickness;

	int toothFacetVertexCount;		// #(vertices) per tooth facet
	int toothFacetCount;			// #(facets) per tooth
	int toothFaceTotalVertexCount;          // #(vertices) in all teeth
	int toothFaceStripCount[] = new int[toothCount];
						// per tooth vertex count
	int topVertexCount;			// #(vertices) for teeth tops
	int topStripCount[] = new int[1];	// #(vertices) in strip/strip

	// Front and rear facing normals for the teeth faces
	Vector3f frontToothNormal = new Vector3f(0.0f, 0.0f, -1.0f);
	Vector3f rearToothNormal = new Vector3f(0.0f, 0.0f, 1.0f);

	// Normals for teeth tops up incline, tooth top, and down incline
	Vector3f leftNormal = new Vector3f(-1.0f, 0.0f, 0.0f);
	Vector3f rightNormal = new Vector3f(1.0f, 0.0f, 0.0f);
	Vector3f outNormal = new Vector3f(1.0f, 0.0f, 0.0f);
	Vector3f inNormal = new Vector3f(-1.0f, 0.0f, 0.0f);

	// Temporary variables for storing coordinates and vectors 
	Point3f coordinate = new Point3f(0.0f, 0.0f, 0.0f);
	Point3f tempCoordinate1 = new Point3f(0.0f, 0.0f, 0.0f);
	Point3f tempCoordinate2 = new Point3f(0.0f, 0.0f, 0.0f);
	Point3f tempCoordinate3 = new Point3f(0.0f, 0.0f, 0.0f);
	Vector3f tempVector1 = new Vector3f(0.0f, 0.0f, 0.0f);
	Vector3f tempVector2 = new Vector3f(0.0f, 0.0f, 0.0f);

	/* Construct the gear's front facing teeth facets
	 *	   0______2
	 *         /     /\
	 *        /   /    \
	 *       /  /       \
	 *      //___________\
	 *     1              3
	 */
	toothFacetVertexCount = 4;
	toothFaceTotalVertexCount = toothFacetVertexCount * toothCount;
	for(int i = 0; i < toothCount; i++)
	    toothFaceStripCount[i] = toothFacetVertexCount;

	TriangleStripArray frontGearTeeth
	    = new TriangleStripArray(toothFaceTotalVertexCount,
				     GeometryArray.COORDINATES
				     | GeometryArray.NORMALS,
				     toothFaceStripCount);

	for(int count = 0; count < toothCount; count++) {
	    index = count * toothFacetVertexCount;

	    toothStartAngle
		= gearStartAngle + circularPitchAngle * (double)count;
	    toothTopStartAngle = toothStartAngle + toothTopAngleIncrement;
	    toothDeclineStartAngle
		= toothStartAngle + toothDeclineAngleIncrement;
	    toothValleyStartAngle
		= toothStartAngle + toothValleyAngleIncrement;

	    xRoot0 = rootRadius * (float)Math.cos(toothStartAngle);
	    yRoot0 = rootRadius * (float)Math.sin(toothStartAngle);
	    xOuter1 = outsideRadius * (float)Math.cos(toothTopStartAngle);
	    yOuter1 = outsideRadius * (float)Math.sin(toothTopStartAngle);
	    xOuter2 = outsideRadius * (float)Math.cos(toothDeclineStartAngle);
	    yOuter2 = outsideRadius * (float)Math.sin(toothDeclineStartAngle);
	    xRoot3 = rootRadius * (float)Math.cos(toothValleyStartAngle);
	    yRoot3 = rootRadius * (float)Math.sin(toothValleyStartAngle);
	
	    tempCoordinate1.set(xRoot0, yRoot0, frontZ);
	    tempCoordinate2.set(xRoot3, yRoot3, frontZ);
	    tempVector1.sub(tempCoordinate2, tempCoordinate1);

	    tempCoordinate2.set(xOuter1, yOuter1, toothTipFrontZ);
	    tempVector2.sub(tempCoordinate2, tempCoordinate1);

	    frontToothNormal.cross(tempVector1, tempVector2);
	    frontToothNormal.normalize();

	    coordinate.set(xOuter1, yOuter1, toothTipFrontZ);
	    frontGearTeeth.setCoordinate(index, coordinate);
	    frontGearTeeth.setNormal(index, frontToothNormal);

	    coordinate.set(xRoot0, yRoot0, frontZ);
	    frontGearTeeth.setCoordinate(index + 1, coordinate);
	    frontGearTeeth.setNormal(index + 1, frontToothNormal);

	    coordinate.set(xOuter2, yOuter2, toothTipFrontZ);
	    frontGearTeeth.setCoordinate(index + 2, coordinate);
	    frontGearTeeth.setNormal(index + 2, frontToothNormal);

	    coordinate.set(xRoot3, yRoot3, frontZ);
	    frontGearTeeth.setCoordinate(index + 3, coordinate);
	    frontGearTeeth.setNormal(index + 3, frontToothNormal);
	}
	newShape = new Shape3D(frontGearTeeth, look);
	this.addChild(newShape);

	/* Construct the gear's rear facing teeth facets (Using Quads)
	 *	   1______2
	 *         /      \
	 *        /        \
	 *       /          \
	 *      /____________\
	 *     0              3
	 */
	toothFacetVertexCount = 4;
	toothFaceTotalVertexCount = toothFacetVertexCount * toothCount;

	QuadArray rearGearTeeth
	    = new QuadArray(toothCount * toothFacetVertexCount,
			    GeometryArray.COORDINATES
			    | GeometryArray.NORMALS);

	for(int count = 0; count < toothCount; count++) {

	    index = count * toothFacetVertexCount;
	    toothStartAngle =
		gearStartAngle + circularPitchAngle * (double)count;
	    toothTopStartAngle = toothStartAngle + toothTopAngleIncrement;
	    toothDeclineStartAngle
		= toothStartAngle + toothDeclineAngleIncrement;
	    toothValleyStartAngle = toothStartAngle + toothValleyAngleIncrement;
	    
	    xRoot0 = rootRadius * (float)Math.cos(toothStartAngle);
	    yRoot0 = rootRadius * (float)Math.sin(toothStartAngle);
	    xOuter1 = outsideRadius * (float)Math.cos(toothTopStartAngle);
	    yOuter1 = outsideRadius * (float)Math.sin(toothTopStartAngle);
	    xOuter2 = outsideRadius * (float)Math.cos(toothDeclineStartAngle);
	    yOuter2 = outsideRadius * (float)Math.sin(toothDeclineStartAngle);
	    xRoot3 = rootRadius * (float)Math.cos(toothValleyStartAngle);
	    yRoot3 = rootRadius * (float)Math.sin(toothValleyStartAngle);

	    tempCoordinate1.set(xRoot0, yRoot0, rearZ);
	    tempCoordinate2.set(xRoot3, yRoot3, rearZ);
	    tempVector1.sub(tempCoordinate2, tempCoordinate1);
	    tempCoordinate2.set(xOuter1, yOuter1, toothTipRearZ);
	    tempVector2.sub(tempCoordinate2, tempCoordinate1);
	    rearToothNormal.cross(tempVector2, tempVector1);
	    rearToothNormal.normalize();

	    coordinate.set(xRoot0, yRoot0, rearZ);
	    rearGearTeeth.setCoordinate(index, coordinate);
	    rearGearTeeth.setNormal(index, rearToothNormal);

	    coordinate.set(xOuter1, yOuter1, toothTipRearZ);
	    rearGearTeeth.setCoordinate(index + 1, coordinate);
	    rearGearTeeth.setNormal(index + 1, rearToothNormal);

	    coordinate.set(xOuter2, yOuter2, toothTipRearZ);
	    rearGearTeeth.setCoordinate(index + 2, coordinate);
	    rearGearTeeth.setNormal(index + 2, rearToothNormal);

	    coordinate.set(xRoot3, yRoot3, rearZ);
	    rearGearTeeth.setCoordinate(index + 3, coordinate);
	    rearGearTeeth.setNormal(index + 3, rearToothNormal);

	}
	newShape = new Shape3D(rearGearTeeth, look);
	this.addChild(newShape);

	/*
	 * Construct the gear's top teeth faces   (As seen from above)
	 *    Root0    Outer1    Outer2    Root3    Root4 (RearZ)
	 *	0_______3 2_______5 4_______7 6_______9
	 *      |0     3| |4     7| |8    11| |12   15|
	 *      |       | |       | |       | |       |
	 *      |       | |       | |       | |       |
	 *      |1_____2| |5_____6| |9____10| |13___14|
	 *      1       2 3       4 5       6 7       8
	 *    Root0    Outer1    Outer2    Root3    Root4 (FrontZ)
	 *
	 * Quad 0123 uses a left normal
	 * Quad 2345 uses an out normal
	 * Quad 4567 uses a right normal
	 * Quad 6789 uses an out normal
	 */
	topVertexCount = 8 * toothCount + 2;
	topStripCount[0] = topVertexCount;

	toothFacetVertexCount = 4;
	toothFacetCount = 4;

	QuadArray topGearTeeth
	    = new QuadArray(toothCount * toothFacetVertexCount
			    * toothFacetCount,
			    GeometryArray.COORDINATES
			    | GeometryArray.NORMALS);

	for(int count = 0; count < toothCount; count++) {
	    index = count * toothFacetCount * toothFacetVertexCount;
	    toothStartAngle = gearStartAngle +
		circularPitchAngle * (double)count;
	    toothTopStartAngle = toothStartAngle + toothTopAngleIncrement;
	    toothDeclineStartAngle
		= toothStartAngle + toothDeclineAngleIncrement;
	    toothValleyStartAngle
		= toothStartAngle + toothValleyAngleIncrement;
	    nextToothStartAngle = toothStartAngle + circularPitchAngle;

	    xRoot0 = rootRadius * (float)Math.cos(toothStartAngle);
	    yRoot0 = rootRadius * (float)Math.sin(toothStartAngle);
	    xOuter1 = outsideRadius * (float)Math.cos(toothTopStartAngle);
	    yOuter1 = outsideRadius * (float)Math.sin(toothTopStartAngle);
	    xOuter2 = outsideRadius * (float)Math.cos(toothDeclineStartAngle);
	    yOuter2 = outsideRadius * (float)Math.sin(toothDeclineStartAngle);
	    xRoot3 = rootRadius * (float)Math.cos(toothValleyStartAngle);
	    yRoot3 = rootRadius * (float)Math.sin(toothValleyStartAngle);
	    xRoot4 = rootRadius * (float)Math.cos(nextToothStartAngle);
	    yRoot4 = rootRadius * (float)Math.sin(nextToothStartAngle);

	    // Compute normal for quad 1
	    tempCoordinate1.set(xRoot0, yRoot0, frontZ);
	    tempCoordinate2.set(xOuter1, yOuter1, toothTipFrontZ);
	    tempVector1.sub(tempCoordinate2, tempCoordinate1);
	    leftNormal.cross(frontNormal, tempVector1);
	    leftNormal.normalize();
	    
	    // Coordinate labeled 0 in the quad
	    coordinate.set(xRoot0, yRoot0, rearZ);
	    topGearTeeth.setCoordinate(index, coordinate);
	    topGearTeeth.setNormal(index, leftNormal);

	    // Coordinate labeled 1 in the quad
	    coordinate.set(tempCoordinate1);
	    topGearTeeth.setCoordinate(index + 1, coordinate);
	    topGearTeeth.setNormal(index + 1, leftNormal);

	    // Coordinate labeled 2 in the quad
	    topGearTeeth.setCoordinate(index + 2, tempCoordinate2);
	    topGearTeeth.setNormal(index + 2, leftNormal);
	    topGearTeeth.setCoordinate(index + 5, tempCoordinate2);

	    // Coordinate labeled 3 in the quad
	    coordinate.set(xOuter1, yOuter1, toothTipRearZ);
	    topGearTeeth.setCoordinate(index + 3, coordinate);
	    topGearTeeth.setNormal(index + 3, leftNormal);
	    topGearTeeth.setCoordinate(index + 4, coordinate);

	    // Compute normal for quad 2
	    tempCoordinate1.set(xOuter1, yOuter1, toothTipFrontZ);
	    tempCoordinate2.set(xOuter2, yOuter2, toothTipFrontZ);
	    tempVector1.sub(tempCoordinate2, tempCoordinate1);
	    outNormal.cross(frontNormal, tempVector1);
	    outNormal.normalize();

	    topGearTeeth.setNormal(index + 4, outNormal);
	    topGearTeeth.setNormal(index + 5, outNormal);

	    // Coordinate labeled 4 in the quad
	    topGearTeeth.setCoordinate(index + 6, tempCoordinate2);
	    topGearTeeth.setNormal(index + 6, outNormal);
	    topGearTeeth.setCoordinate(index + 9, tempCoordinate2);

	    // Coordinate labeled 5 in the quad
	    coordinate.set(xOuter2, yOuter2, toothTipRearZ);
	    topGearTeeth.setCoordinate(index + 7, coordinate);
	    topGearTeeth.setNormal(index + 7, outNormal);
	    topGearTeeth.setCoordinate(index + 8, coordinate);

	    // Compute normal for quad 3
	    tempCoordinate1.set(xOuter2, yOuter2, toothTipFrontZ);
	    tempCoordinate2.set(xRoot3, yRoot3, frontZ);
	    tempVector1.sub(tempCoordinate2, tempCoordinate1);
	    rightNormal.cross(frontNormal, tempVector1);
	    rightNormal.normalize();

	    topGearTeeth.setNormal(index + 8, rightNormal);
	    topGearTeeth.setNormal(index + 9, rightNormal);

	    // Coordinate labeled 7 in the quad
	    topGearTeeth.setCoordinate(index + 10, tempCoordinate2);
	    topGearTeeth.setNormal(index + 10, rightNormal);
	    topGearTeeth.setCoordinate(index + 13, tempCoordinate2);
	
	    // Coordinate labeled 6 in the quad
	    coordinate.set(xRoot3, yRoot3, rearZ);
	    topGearTeeth.setCoordinate(index + 11, coordinate);
	    topGearTeeth.setNormal(index + 11, rightNormal);
	    topGearTeeth.setCoordinate(index + 12, coordinate);

	    // Compute normal for quad 4
	    tempCoordinate1.set(xRoot3, yRoot3, frontZ);
	    tempCoordinate2.set(xRoot4, yRoot4, frontZ);
	    tempVector1.sub(tempCoordinate2, tempCoordinate1);
	    outNormal.cross(frontNormal, tempVector1);
	    outNormal.normalize();

	    topGearTeeth.setNormal(index + 12, outNormal);
	    topGearTeeth.setNormal(index + 13, outNormal);

	    // Coordinate labeled 9 in the quad
	    topGearTeeth.setCoordinate(index + 14, tempCoordinate2);
	    topGearTeeth.setNormal(index + 14, outNormal);

	    // Coordinate labeled 8 in the quad
	    coordinate.set(xRoot4, yRoot4, rearZ);
	    topGearTeeth.setCoordinate(index + 15, coordinate);
	    topGearTeeth.setNormal(index + 15, outNormal);

	    // Prepare for the loop by computing the new normal
	    toothTopStartAngle
		= nextToothStartAngle + toothTopAngleIncrement;
	    xOuter1 = outsideRadius * (float)Math.cos(toothTopStartAngle);
	    yOuter1 = outsideRadius * (float)Math.sin(toothTopStartAngle);

	    tempCoordinate1.set(xRoot4, yRoot4, toothTipFrontZ);
	    tempCoordinate2.set(xOuter1, yOuter1, toothTipFrontZ);
	    tempVector1.sub(tempCoordinate2, tempCoordinate1);
	    leftNormal.cross(frontNormal, tempVector1);
	    leftNormal.normalize();
	}
	newShape = new Shape3D(topGearTeeth, look);
	this.addChild(newShape);
    }

}

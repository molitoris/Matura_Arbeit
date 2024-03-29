/*
 *	@(#)Shaft.java 1.3 98/02/20 14:29:58
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

public class Shaft extends javax.media.j3d.TransformGroup {
    
    /**
     * Construct a Shaft;
     * @return a new shaft that with the specified radius centered about
     * the origin an laying in the XY plane and of a specified length
     * extending in the Z dimension
     * @param radius radius of shaft
     * @param length shaft length shaft extends from -length/2 to length/2 in
     * the Z dimension
     * @param segmentCount number of segments for the shaft face
     * @param look the Appearance to associate with this shaft
     */
    public Shaft(float radius, float length, int segmentCount,
		 Appearance look) {
	// The direction of the ray from the shaft's center
	float xDirection, yDirection;
	float xShaft, yShaft;

	// The z coordinates for the shaft's faces (never change)
	float frontZ = -0.5f * length;
	float rearZ = 0.5f * length;

	int shaftFaceVertexCount;	  // #(vertices) per shaft face
	int shaftFaceTotalVertexCount;	  // total #(vertices) in all teeth
	int shaftFaceStripCount[] = new int[1]; // per shaft vertex count
	int shaftVertexCount;	  // #(vertices) for shaft
	int shaftStripCount[] = new int[1]; // #(vertices) in strip/strip

	// Front and rear facing normals for the shaft's faces
	Vector3f frontNormal = new Vector3f(0.0f, 0.0f, -1.0f);
	Vector3f rearNormal = new Vector3f(0.0f, 0.0f, 1.0f);
	// Outward facing normal
	Vector3f outNormal = new Vector3f(1.0f, 0.0f, 0.0f);

	// Temporary variables for storing coordinates and vectors 
	Point3f coordinate = new Point3f(0.0f, 0.0f, 0.0f);
	Shape3D newShape;
	
	// The angle subtended by a single segment
	double segmentAngle = 2.0 * Math.PI/segmentCount;
	double tempAngle;

	// Allow this object to spin. etc.
	this.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

	/* for the forward facing fan:
	 *               ___3___
	 *             -    |    -
	 *          /       |       \
	 *       4/\        |        /\2
	 *     /     \      |      /     \
	 *   /         \    |    /         \
	 *  :            \  |  /            :
	 * |--------------- *----------------|
	 * 5                0                1
	 *
	 * for backward facing fan exchange 1 with 5; 2 with 4, etc.
	 */

	// Construct the shaft's front and rear face
	shaftFaceVertexCount = segmentCount + 2;
	shaftFaceStripCount[0] = shaftFaceVertexCount;

	TriangleFanArray frontShaftFace
	    = new TriangleFanArray(shaftFaceVertexCount,
				     GeometryArray.COORDINATES
				     | GeometryArray.NORMALS,
				     shaftFaceStripCount);

	TriangleFanArray rearShaftFace
	    = new TriangleFanArray(shaftFaceVertexCount,
				     GeometryArray.COORDINATES
				     | GeometryArray.NORMALS,
				     shaftFaceStripCount);

	coordinate.set(0.0f, 0.0f, frontZ);
	frontShaftFace.setCoordinate(0, coordinate);
	frontShaftFace.setNormal(0, frontNormal);

	coordinate.set(0.0f, 0.0f, rearZ);
	rearShaftFace.setCoordinate(0, coordinate);
	rearShaftFace.setNormal(0, rearNormal);

	for(int index = 1; index < segmentCount+2; index++) {

	    tempAngle = segmentAngle * -(double)index;
	    coordinate.set(radius * (float)Math.cos(tempAngle),
			   radius * (float)Math.sin(tempAngle),
			   frontZ);
	    frontShaftFace.setCoordinate(index, coordinate);
	    frontShaftFace.setNormal(index, frontNormal);

	    tempAngle = -tempAngle;
	    coordinate.set(radius * (float)Math.cos(tempAngle),
			   radius * (float)Math.sin(tempAngle),
			   rearZ);
	    rearShaftFace.setCoordinate(index, coordinate);
	    rearShaftFace.setNormal(index, rearNormal);
	}
	newShape = new Shape3D(frontShaftFace, look);
	this.addChild(newShape);
	newShape = new Shape3D(rearShaftFace, look);
	this.addChild(newShape);

	// Construct shaft's outer skin (the cylinder body)
	shaftVertexCount = 2 * segmentCount + 2;
	shaftStripCount[0] = shaftVertexCount;

	TriangleStripArray shaft
	    = new TriangleStripArray(shaftVertexCount,
				     GeometryArray.COORDINATES
				     | GeometryArray.NORMALS,
				     shaftStripCount);

	outNormal.set(1.0f, 0.0f, 0.0f);

	coordinate.set(radius, 0.0f, rearZ);
	shaft.setCoordinate(0, coordinate);
	shaft.setNormal(0, outNormal);

	coordinate.set(radius, 0.0f, frontZ);
	shaft.setCoordinate(1, coordinate);
	shaft.setNormal(1, outNormal);

	for(int count = 0; count < segmentCount; count++) {
	    int index = 2 + count * 2;

	    tempAngle = segmentAngle * (double)(count + 1);
	    xDirection = (float)Math.cos(tempAngle);
	    yDirection = (float)Math.sin(tempAngle);
	    xShaft = radius * xDirection;
	    yShaft = radius * yDirection;
	    outNormal.set(xDirection, yDirection, 0.0f);

	    coordinate.set(xShaft, yShaft, rearZ);
	    shaft.setCoordinate(index, coordinate);
	    shaft.setNormal(index, outNormal);
	    
	    coordinate.set(xShaft, yShaft, frontZ);
	    shaft.setCoordinate(index + 1, coordinate);
	    shaft.setNormal(index + 1, outNormal);
	}
	newShape = new Shape3D(shaft, look);
	this.addChild(newShape);
    }
}


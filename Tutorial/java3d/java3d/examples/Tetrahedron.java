/*
 *	@(#)Tetrahedron.java 1.6 98/02/20 14:29:41
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

import javax.media.j3d.*;
import javax.vecmath.*;

public class Tetrahedron extends Shape3D {
    private static final float sqrt3 = (float) Math.sqrt(3.0);
    private static final float sqrt3_3 = sqrt3 / 3.0f;
    private static final float sqrt24_3 = (float) Math.sqrt(24.0) / 3.0f;

    private static final float ycenter = 0.5f * sqrt24_3;
    private static final float zcenter = -sqrt3_3;

    private static final Point3f p1 = new Point3f(-1.0f, -ycenter, -zcenter);
    private static final Point3f p2 = new Point3f(1.0f, -ycenter, -zcenter);
    private static final Point3f p3 =
	new Point3f(0.0f, -ycenter, -sqrt3 - zcenter);
    private static final Point3f p4 =
	new Point3f(0.0f, sqrt24_3 - ycenter, 0.0f);

    private static final Point3f[] verts = {
	p1, p2, p4,	// front face
	p1, p4, p3,	// left, back face
	p2, p3, p4,	// right, back face
	p1, p3, p2,	// bottom face
    };

    private Point2f texCoord[] = {
        new Point2f(0.0f, 0.0f),
	new Point2f(1.0f, 0.0f),
        new Point2f(0.5f, sqrt3 / 2.0f),
    };

    public Tetrahedron() {
	int i;

	TriangleArray tetra = new TriangleArray(12, TriangleArray.COORDINATES |
		TriangleArray.NORMALS | TriangleArray.TEXTURE_COORDINATE_2);

	tetra.setCoordinates(0, verts);
        for (i = 0; i < 12; i++) {
            tetra.setTextureCoordinate(i, texCoord[i%3]);
        }

	int face;
	Vector3f normal = new Vector3f();
	Vector3f v1 = new Vector3f();
	Vector3f v2 = new Vector3f();
	Point3f [] pts = new Point3f[3];
	for (i = 0; i < 3; i++) pts[i] = new Point3f();

	for (face = 0; face < 4; face++) {
	    tetra.getCoordinates(face*3, pts);
	    v1.sub(pts[1], pts[0]);
	    v2.sub(pts[2], pts[0]);
	    normal.cross(v1, v2);
	    normal.normalize();
	    for (i = 0; i < 3; i++) {
		tetra.setNormal((face * 3 + i), normal);
	    }
	}
	this.setGeometry(tetra);
	this.setAppearance(new Appearance());
    }
}

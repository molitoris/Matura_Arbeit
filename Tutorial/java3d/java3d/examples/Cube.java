/*
 *	@(#)Cube.java 1.3 98/02/20 14:30:08
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

import java.applet.Applet;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class Cube extends Object {

   private Shape3D shape3D;

   private static final float[] verts = {
   // Front Face
      1.0f, -1.0f,  1.0f,     1.0f,  1.0f,  1.0f,
     -1.0f,  1.0f,  1.0f,    -1.0f, -1.0f,  1.0f,
   // Back Face
     -1.0f, -1.0f, -1.0f,    -1.0f,  1.0f, -1.0f,
      1.0f,  1.0f, -1.0f,     1.0f, -1.0f, -1.0f,
   // Right Face
      1.0f, -1.0f, -1.0f,     1.0f,  1.0f, -1.0f,
      1.0f,  1.0f,  1.0f,     1.0f, -1.0f,  1.0f,
   // Left Face
     -1.0f, -1.0f,  1.0f,    -1.0f,  1.0f,  1.0f,
     -1.0f,  1.0f, -1.0f,    -1.0f, -1.0f, -1.0f,
   // Top Face
      1.0f,  1.0f,  1.0f,     1.0f,  1.0f, -1.0f,
     -1.0f,  1.0f, -1.0f,    -1.0f,  1.0f,  1.0f,
   // Bottom Face
     -1.0f, -1.0f,  1.0f,    -1.0f, -1.0f, -1.0f,
      1.0f, -1.0f, -1.0f,     1.0f, -1.0f,  1.0f,
   };

   private static final float[] normals = {
   // Front Face
      0.0f,  0.0f,  1.0f,     0.0f,  0.0f,  1.0f,
      0.0f,  0.0f,  1.0f,     0.0f,  0.0f,  1.0f,
   // Back Face
      0.0f,  0.0f, -1.0f,     0.0f,  0.0f, -1.0f,
      0.0f,  0.0f, -1.0f,     0.0f,  0.0f, -1.0f,
   // Right Face
      1.0f,  0.0f,  0.0f,     1.0f,  0.0f,  0.0f,
      1.0f,  0.0f,  0.0f,     1.0f,  0.0f,  0.0f,
   // Left Face
     -1.0f,  0.0f,  0.0f,    -1.0f,  0.0f,  0.0f,
     -1.0f,  0.0f,  0.0f,    -1.0f,  0.0f,  0.0f,
   // Top Face
      0.0f,  1.0f,  0.0f,     0.0f,  1.0f,  0.0f,
      0.0f,  1.0f,  0.0f,     0.0f,  1.0f,  0.0f,
   // Bottom Face
      0.0f, -1.0f,  0.0f,     0.0f, -1.0f,  0.0f,
      0.0f, -1.0f,  0.0f,     0.0f, -1.0f,  0.0f,
   };

   private static final float[] textCoords = {
   // Front Face
      1.0f,  0.0f,            1.0f,  1.0f,
      0.0f,  1.0f,            0.0f,  0.0f,
   // Back Face
      1.0f,  0.0f,            1.0f,  1.0f,
      0.0f,  1.0f,            0.0f,  0.0f,
   // Right Face
      1.0f,  0.0f,            1.0f,  1.0f,
      0.0f,  1.0f,            0.0f,  0.0f,
   // Left Face
      1.0f,  0.0f,            1.0f,  1.0f,
      0.0f,  1.0f,            0.0f,  0.0f,
   // Top Face
      1.0f,  0.0f,            1.0f,  1.0f,
      0.0f,  1.0f,            0.0f,  0.0f,
   // Bottom Face
      1.0f,  0.0f,            1.0f,  1.0f,
      0.0f,  1.0f,            0.0f,  0.0f
   };

   public Cube(Appearance appearance) {

      QuadArray quadArray = new QuadArray(24, QuadArray.COORDINATES | 
                                              QuadArray.NORMALS |
                                              QuadArray.TEXTURE_COORDINATE_2);
      quadArray.setCoordinates(0, verts);
      quadArray.setNormals(0, normals);
      quadArray.setTextureCoordinates(0, textCoords);

      shape3D = new Shape3D(quadArray, appearance);
   }

   public Shape3D getChild() {
      return shape3D;
   }
}

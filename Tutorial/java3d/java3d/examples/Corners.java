/*
 *	@(#)Corners.java 1.3 98/02/20 14:30:07
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

public class Corners extends Object {

   private Group group;
   private Shape3D shape1;
   private Shape3D shape2;
   private Shape3D shape3;
   private Shape3D shape4;
   private Shape3D shape5;
   private Shape3D shape6;
   private Shape3D shape7;
   private Shape3D shape8;

   private static final float[] verts1 = {
   // Front Face
     -1.05f,  0.45f,  1.05f,  -0.45f,  0.45f,  1.05f,
     -0.45f,  1.05f,  1.05f,  -1.05f,  1.05f,  1.05f,
   // Back Face
     -1.05f,  0.45f,  0.45f,  -1.05f,  1.05f,  0.45f,
     -0.45f,  1.05f,  0.45f,  -0.45f,  0.45f,  0.45f,
   // Right Face
     -0.45f,  0.45f,  1.05f,  -0.45f,  0.45f,  0.45f,
     -0.45f,  1.05f,  0.45f,  -0.45f,  1.05f,  1.05f,
   // Left Face
     -1.05f,  0.45f,  0.45f,  -1.05f,  0.45f,  1.05f,
     -1.05f,  1.05f,  1.05f,  -1.05f,  1.05f,  0.45f,
   // Top Face
     -1.05f,  1.05f,  1.05f,  -0.45f,  1.05f,  1.05f,
     -0.45f,  1.05f,  0.45f,  -1.05f,  1.05f,  0.45f,
   // Bottom Face
     -1.05f,  0.45f,  0.45f,  -0.45f,  0.45f,  0.45f,
     -0.45f,  0.45f,  1.05f,  -1.05f,  0.45f,  1.05f,
   };

   private static final float[] verts2 = new float[72];
   private static final float[] verts3 = new float[72];
   private static final float[] verts4 = new float[72];
   private static final float[] verts5 = new float[72];
   private static final float[] verts6 = new float[72];
   private static final float[] verts7 = new float[72];
   private static final float[] verts8 = new float[72];

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

   public Corners(Appearance appearance) {

      int i;
      for (i=0; i<72; i+=3) {
         verts2[i]   = verts1[i] + 1.5f;
         verts2[i+1] = verts1[i+1];
         verts2[i+2] = verts1[i+2];
      }

      for (i=0; i<72; i+=3) {
         verts3[i]   = verts2[i];
         verts3[i+1] = verts2[i+1] - 1.5f;
         verts3[i+2] = verts2[i+2];
      }

      for (i=0; i<72; i+=3) {
         verts4[i]   = verts1[i];
         verts4[i+1] = verts1[i+1] - 1.5f;
         verts4[i+2] = verts1[i+2];
      }

      for (i=0; i<72; i+=3) {
         verts5[i]   = verts1[i];
         verts5[i+1] = verts1[i+1];
         verts5[i+2] = verts1[i+2]- 1.5f;

         verts6[i]   = verts2[i];
         verts6[i+1] = verts2[i+1];
         verts6[i+2] = verts2[i+2] - 1.5f;

         verts7[i]   = verts3[i];
         verts7[i+1] = verts3[i+1];
         verts7[i+2] = verts3[i+2] - 1.5f;

         verts8[i]   = verts4[i];
         verts8[i+1] = verts4[i+1];
         verts8[i+2] = verts4[i+2] - 1.5f;
      }

      QuadArray quadArray1 = new QuadArray(24, QuadArray.COORDINATES | 
                                           QuadArray.NORMALS         |
                                           QuadArray.TEXTURE_COORDINATE_2);
      quadArray1.setCoordinates(0, verts1);
      quadArray1.setNormals(0, normals);
      quadArray1.setTextureCoordinates(0, textCoords);

      QuadArray quadArray2 = new QuadArray(24, QuadArray.COORDINATES | 
                                           QuadArray.NORMALS         |
                                           QuadArray.TEXTURE_COORDINATE_2);
      quadArray2.setCoordinates(0, verts2);
      quadArray2.setNormals(0, normals);
      quadArray2.setTextureCoordinates(0, textCoords);

      QuadArray quadArray3 = new QuadArray(24, QuadArray.COORDINATES | 
                                           QuadArray.NORMALS         |
                                           QuadArray.TEXTURE_COORDINATE_2);
      quadArray3.setCoordinates(0, verts3);
      quadArray3.setNormals(0, normals);
      quadArray3.setTextureCoordinates(0, textCoords);

      QuadArray quadArray4 = new QuadArray(24, QuadArray.COORDINATES | 
                                           QuadArray.NORMALS         |
                                           QuadArray.TEXTURE_COORDINATE_2);
      quadArray4.setCoordinates(0, verts4);
      quadArray4.setNormals(0, normals);
      quadArray4.setTextureCoordinates(0, textCoords);

      QuadArray quadArray5 = new QuadArray(24, QuadArray.COORDINATES | 
                                           QuadArray.NORMALS         |
                                           QuadArray.TEXTURE_COORDINATE_2);
      quadArray5.setCoordinates(0, verts5);
      quadArray5.setNormals(0, normals);
      quadArray5.setTextureCoordinates(0, textCoords);

      QuadArray quadArray6 = new QuadArray(24, QuadArray.COORDINATES | 
                                           QuadArray.NORMALS         |
                                           QuadArray.TEXTURE_COORDINATE_2);
      quadArray6.setCoordinates(0, verts6);
      quadArray6.setNormals(0, normals);
      quadArray6.setTextureCoordinates(0, textCoords);

      QuadArray quadArray7 = new QuadArray(24, QuadArray.COORDINATES | 
                                           QuadArray.NORMALS         |
                                           QuadArray.TEXTURE_COORDINATE_2);
      quadArray7.setCoordinates(0, verts7);
      quadArray7.setNormals(0, normals);
      quadArray7.setTextureCoordinates(0, textCoords);

      QuadArray quadArray8 = new QuadArray(24, QuadArray.COORDINATES | 
                                           QuadArray.NORMALS         |
                                           QuadArray.TEXTURE_COORDINATE_2);
      quadArray8.setCoordinates(0, verts8);
      quadArray8.setNormals(0, normals);
      quadArray8.setTextureCoordinates(0, textCoords);

      shape1 = new Shape3D(quadArray1, appearance);
      shape2 = new Shape3D(quadArray2, appearance);
      shape3 = new Shape3D(quadArray3, appearance);
      shape4 = new Shape3D(quadArray4, appearance);
      shape5 = new Shape3D(quadArray5, appearance);
      shape6 = new Shape3D(quadArray6, appearance);
      shape7 = new Shape3D(quadArray7, appearance);
      shape8 = new Shape3D(quadArray8, appearance);

      group = new Group();
      group.addChild(shape1);
      group.addChild(shape2);
      group.addChild(shape3);
      group.addChild(shape4);
      group.addChild(shape5);
      group.addChild(shape6);
      group.addChild(shape7);
      group.addChild(shape8);
   }

   public Group getChild() {
      return group;
   }
}

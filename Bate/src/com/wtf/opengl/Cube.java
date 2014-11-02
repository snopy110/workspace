/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wtf.opengl;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;



   
public class Cube
{
	
    public Cube()
    {
        int one = 0x02500;
        int vertices[] = {
        		
                // TOP
                -one, -one, one, one, -one, one,
                -one, one, one, one, one, one,
                // BOTTOM
                -one, -one, -one, -one, one, -one,
                one, -one, -one, one, one, -one,
                // BACK
                -one, -one, one, -one, one, one,
                -one, -one, -one, -one, one, -one,
                // FRONT
                one, -one, -one, one, one, -one,
                one, -one, one, one, one, one,
                // LEFT
                -one, one, one, one, one, one,
                -one, one, -one, one, one, -one,
                // RIGHT
                -one, -one, one, -one, -one, -one,
                one, -one, one, one, -one, -one,



        };


        
    	float spriteTexcoords[]  = {
    			
                // FRONT
                0, 1, 1, 1, 0, 0, 1, 0,
                // BACK
                1, 1, 1, 0, 0, 1, 0, 0,
                // LEFT
                1, 1, 1, 0, 0, 1, 0, 0,
                // RIGHT
                1, 1, 1, 0, 0, 1, 0, 0,
                // TOP
                1, 0, 0, 0, 1, 1, 0, 1,
                // BOTTOM
                0, 0, 0, 1, 1, 0, 1, 1, 

 			
    		};

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(spriteTexcoords.length*4);
        tbb.order(ByteOrder.nativeOrder());
        mTexBuffer = tbb.asFloatBuffer();
        mTexBuffer.position(0);
       
        
        for(int i=0; i<48; i++){
        	mTexBuffer.put(spriteTexcoords[i]);
        }
        
        mTexBuffer.position(0);
        
        
    }

    
    public void draw(GL10 gl){    	
    	//Main.debug("draw");
    	
    	gl.glFrontFace(GL10.GL_CCW);
    	
    	//������Ȳ���    3D ͼ��һ�㶼����
    	gl.glEnable(GL10.GL_DEPTH_TEST);
    	
        //���ö�����������
        gl.glEnableClientState(gl.GL_VERTEX_ARRAY);              

    	//Ԥ���� ������������
        gl.glVertexPointer(3, gl.GL_FIXED, 0, mVertexBuffer);

        //�� ��  �� �� �� Զ
        //gl.glOrthof(-1.0f, 1.0f, -1.5f, -1.5f, -1.0f, 1.0f);
        
        
        //����
        gl.glScalef(0.65f, 0.65f, 0.65f);
        
       
        //��������
        gl.glEnable(GL10.GL_TEXTURE_2D);              
        

        
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
        
        
        gl.glBindTexture(GL10.GL_TEXTURE_2D, Cube.mTextureID_front);
        gl.glDrawArrays(gl.GL_TRIANGLE_STRIP, 0, 4);  
        
        gl.glBindTexture(GL10.GL_TEXTURE_2D, Cube.mTextureID__back);
        gl.glDrawArrays(gl.GL_TRIANGLE_STRIP, 4, 4);

        //gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, Cube.mTextureID_left);
        gl.glDrawArrays(gl.GL_TRIANGLE_STRIP, 8, 4);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, Cube.mTextureID_right);
        gl.glDrawArrays(gl.GL_TRIANGLE_STRIP, 12, 4);

        
        //gl.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, Cube.mTextureID_top);
        gl.glDrawArrays(gl.GL_TRIANGLE_STRIP, 16, 4);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, Cube.mTextureID_bottom);
        gl.glDrawArrays(gl.GL_TRIANGLE_STRIP, 20, 4);
        
        
    }
   
    private IntBuffer   mVertexBuffer;
    private FloatBuffer mTexBuffer;
    public static int mTextureID_front;
    public static int mTextureID__back;
    public static int mTextureID_left;
    public static int mTextureID_right;
    public static int mTextureID_top;
    public static int mTextureID_bottom;
    
    

    
}

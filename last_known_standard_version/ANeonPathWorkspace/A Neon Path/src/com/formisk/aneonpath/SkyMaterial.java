package com.formisk.aneonpath;

import java.nio.FloatBuffer;

import android.graphics.Color;
import android.opengl.GLES20;
import android.os.SystemClock;
import android.util.Log;
import rajawali.materials.AMaterial;

public class SkyMaterial extends ANeonPathMaterial {
	public ANeonPathWallpaper mWallpaper = null;
	public int skyColor;
	public int pathColorLoc;
	
	protected static final String mVShader = 
			"uniform mat4 uMVPMatrix;\n" +
			"uniform float time;\n" +
			"uniform vec3 pathColor;\n" +

			"attribute vec4 aPosition;\n" +
			"attribute vec2 aTextureCoord;\n" +
			"attribute vec4 aColor;\n" +

			"varying vec2 vTextureCoord;\n" +
			"varying vec2 vTextureCoordTime;\n" +

			"void main() {\n" +
			"	gl_Position = uMVPMatrix * aPosition;\n" +
			"	vTextureCoord = aTextureCoord;\n" +
			"	vTextureCoordTime = aTextureCoord;\n" +
			"	vTextureCoordTime.y = vTextureCoordTime.y+time*4.0;\n" +
			"}\n";
		
		protected static final String mFShader = 
			"precision highp float;\n" +

			"uniform sampler2D uDiffuseTexture;\n" +
			"uniform vec3 pathColor;\n" +
			"uniform float time;\n" +

			"varying vec2 vTextureCoord;\n" +
			"varying vec2 vTextureCoordTime;\n" +

			"void main() {\n" +
			"   float gradientColor = texture2D(uDiffuseTexture, vTextureCoord).r*texture2D(uDiffuseTexture, vTextureCoordTime).g*1.4;\n" +
			"	vec3 color = pathColor*gradientColor;\n" +
			"   gl_FragColor = vec4(color.x, color.y, color.z, 1.0);\n" + 
			"}\n";
		
	public SkyMaterial(int skyColor) {
		super(mVShader, mFShader, true);
		this.pathColorLoc = GLES20.glGetUniformLocation ( mProgram, "pathColor" );
		this.skyColor = skyColor;
		this.updateUniform();
	}
	
	public void printDebug() {
		FloatBuffer fb = FloatBuffer.allocate(3);
		GLES20.glGetUniformfv(mProgram, timeLoc, fb);
		Log.d("Sky Time", ""+fb.get(0));
	}

	public void updateUniform() {
		super.updateUniform();
		GLES20.glUseProgram(mProgram); 
		GLES20.glUniform3f ( this.pathColorLoc, Color.red(this.skyColor)/255.0f, Color.green(this.skyColor)/255.0f, Color.blue(this.skyColor)/255.0f);
		//printDebug();
	}
}



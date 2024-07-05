package com.formisk.aneonpath;

import java.nio.FloatBuffer;

import android.graphics.Color;
import android.opengl.GLES20;
import android.os.SystemClock;
import android.util.Log;
import rajawali.materials.AMaterial;

public class NeonPathMaterial extends ANeonPathMaterial {
	public ANeonPathWallpaper mWallpaper = null;
	public int color;
	public int pathColorLoc;
	
	public static final String mVShader = 
			"uniform mat4 uMVPMatrix;\n" +
			"uniform float time;\n" +
			"uniform vec3 pathColor;\n" +

			"attribute vec4 aPosition;\n" +
			"attribute vec2 aTextureCoord;\n" +
			"attribute vec4 aColor;\n" +

			"varying vec2 vTextureCoord;\n" +
			"varying vec4 vColor;\n" +
			"varying float vDistance;\n" +
			"varying float vDistanceNorm;\n" +

			"void main() {\n" +
			"	gl_Position = uMVPMatrix * aPosition;\n" +
			"	vColor = aColor;\n" +
			"	vTextureCoord = aTextureCoord;\n" +
			//"   vDistance = smoothstep(1.0, 0.0, distance(gl_Position.xyz, vec3(0.0,0.0,0.0))*0.05);\n" +
			"   vDistanceNorm = distance(vec3(0.0,0.0,0.0), gl_Position.xyz);\n" +
			"   vDistance = min(1.0, 16.0-vDistanceNorm);\n" +
			"   vDistanceNorm = vDistanceNorm/16.0;\n" +
			"}\n";
		
		public static final String mFShaderFlat = 
			"precision highp float;\n" +

			"uniform sampler2D uDiffuseTexture;\n" +
			"uniform vec3 pathColor;\n" +

			"varying vec2 vTextureCoord;\n" +
			"varying vec4 vColor;\n" +
			"varying float vDistance;\n" +

			"void main() {\n" +
			"   float gradientColor = texture2D(uDiffuseTexture, vTextureCoord).r;\n" +
			"	vec3 color = pathColor*gradientColor*vDistance;\n" +
			"   gl_FragColor = vec4(color.x, color.y, color.z, 1.0);\n" + 
			"}\n";
		public static final String mFShaderRippleBand = 
				"precision highp float;\n" +

				"uniform sampler2D uDiffuseTexture;\n" +
				"uniform vec3 pathColor;\n" +

				"varying vec2 vTextureCoord;\n" +
				"varying vec4 vColor;\n" +
				"varying float vDistance;\n" +

				"void main() {\n" +
				"   float gradientColor = texture2D(uDiffuseTexture, vTextureCoord).b;\n" +
				"	vec3 color = pathColor*gradientColor*vDistance;\n" +
				"   gl_FragColor = vec4(color.x, color.y, color.z, 1.0);\n" + 
				"}\n";
		public static final String mFShaderRope = 
				"precision highp float;\n" +

				"uniform sampler2D uDiffuseTexture;\n" +
				"uniform vec3 pathColor;\n" +

				"varying vec2 vTextureCoord;\n" +
				"varying vec4 vColor;\n" +
				"varying float vDistance;\n" +
				"varying float vDistanceNorm;\n" +

				"void main() {\n" +
				"   float gradientColor = texture2D(uDiffuseTexture, vec2(vDistanceNorm, vTextureCoord.y)).b;\n" +
				"	vec3 color = pathColor*gradientColor*vDistance;\n" +
				"   gl_FragColor = vec4(color.x, color.y, color.z, 1.0);\n" + 
				"}\n";
		
	public NeonPathMaterial(int color, String fshader) {
		super(mVShader, fshader, false);
		this.pathColorLoc = GLES20.glGetUniformLocation ( mProgram, "pathColor" );
		this.color = color;
		this.updateUniform();
	}
	
	public void printDebug() {
		FloatBuffer fb = FloatBuffer.allocate(3);
		GLES20.glGetUniformfv(mProgram, pathColorLoc, fb);
		Log.d("pathColorLoc:" + mProgram + ":" + pathColorLoc, ""+fb.get(0)+" "+fb.get(1)+" "+fb.get(2));
		Log.d("pathColor", Color.red(this.color)+" "+Color.green(this.color)+" "+Color.blue(this.color));
	}

	public void updateUniform() {
		GLES20.glUseProgram(mProgram); 
		GLES20.glUniform3f ( this.pathColorLoc, Color.red(this.color)/255.0f, Color.green(this.color)/255.0f, Color.blue(this.color)/255.0f);
		//printDebug();
	}
}



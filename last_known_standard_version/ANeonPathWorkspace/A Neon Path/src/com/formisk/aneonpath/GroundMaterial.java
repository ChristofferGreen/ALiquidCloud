package com.formisk.aneonpath;

import android.graphics.Color;
import android.opengl.GLES20;
import android.os.SystemClock;
import rajawali.materials.AMaterial;

public class GroundMaterial extends ANeonPathMaterial {
	public ANeonPathWallpaper mWallpaper = null;
	public int groundColor;
	public int groundColorLoc;
	
	public static final String mVShader =
			"precision highp float;\n" +
			"uniform mat4 uMVPMatrix;\n" +
			"uniform float time;\n" +

			"attribute vec4 aPosition;\n" +
			"attribute vec2 aTextureCoord;\n" +
			"attribute vec4 aColor;\n" +

			"varying vec2 vTextureCoord;\n" +
			"varying vec4 vColor;\n" +
			"varying float vDistance;\n" + 

			"void main() {\n" +
			"	gl_Position = uMVPMatrix * aPosition;\n" +
			"	vColor = aColor;\n" +
			//"   vDist = (1.0/distance(vec3(0.0, 0.0, 0.0), gl_Position.xyz))*10.0-0.5;\n" +
			"   vDistance = clamp(20.0-distance(vec3(0.0,0.0,0.0), gl_Position.xyz), 0.0, 1.0);\n" +
			"	vTextureCoord = aTextureCoord;\n" +
			"	vTextureCoord = vTextureCoord*vec2(10.0, 10.0);\n" +
			"	vTextureCoord.x = vTextureCoord.x;\n" +
			"}\n";
		
		public static final String mFShaderR = 
			"precision highp float;\n" +

			"varying vec2 vTextureCoord;\n" +
			"varying float vDistance;\n" + 
			"varying vec4 vColor;\n" +
			"uniform sampler2D uDiffuseTexture;\n" +
			"uniform vec3 groundColor;\n" +

			"void main() {\n" +
			"	vec3 col = texture2D(uDiffuseTexture, vTextureCoord).r*vDistance*groundColor.xyz*2.0;\n" +
			"   gl_FragColor = vec4(col.x, col.y, col.z, col.x+0.5);\n" + 
			"}\n";
		public static final String mFShaderG = 
				"precision highp float;\n" +

				"varying vec2 vTextureCoord;\n" +
				"varying float vDistance;\n" + 
				"varying vec4 vColor;\n" +
				"uniform sampler2D uDiffuseTexture;\n" +
				"uniform vec3 groundColor;\n" +

				"void main() {\n" +
				"	vec3 col = texture2D(uDiffuseTexture, vTextureCoord).g*vDistance*groundColor.xyz*2.0;\n" +
				"   gl_FragColor = vec4(col.x, col.y, col.z, col.x+0.5);\n" + 
				"}\n";
		public static final String mFShaderB = 
				"precision highp float;\n" +

				"varying vec2 vTextureCoord;\n" +
				"varying float vDistance;\n" + 
				"varying vec4 vColor;\n" +
				"uniform sampler2D uDiffuseTexture;\n" +
				"uniform vec3 groundColor;\n" +

				"void main() {\n" +
				"	vec3 col = texture2D(uDiffuseTexture, vTextureCoord).b*vDistance*groundColor.xyz*2.0;\n" +
				"   gl_FragColor = vec4(col.x, col.y, col.z, col.x+0.5);\n" + 
				"}\n";
		
	public GroundMaterial(int groundColor, String fshader) {
		super(mVShader, fshader, false);
		this.groundColor = groundColor;
		this.groundColorLoc = GLES20.glGetUniformLocation ( mProgram, "groundColor" );
		this.updateUniform();
	}
	
	public void updateUniform() {
		super.updateUniform();
		GLES20.glUseProgram(mProgram); 
		GLES20.glUniform3f ( this.groundColorLoc, Color.red(this.groundColor)/255.0f, Color.green(this.groundColor)/255.0f, Color.blue(this.groundColor)/255.0f);
		//printDebug();
	}
}


package com.formisk.aneonpath;

import android.graphics.Color;
import android.opengl.GLES20;
import rajawali.BaseObject3D;
import rajawali.parser.ObjParser;

public class NorthernLightsMaterial extends ANeonPathMaterial {
	public int color;
	public int northernLightsColorLoc;
	
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
			"	vTextureCoord = aTextureCoord;\n" +
			"}\n";
		
		public static final String mFShader = 
			"precision highp float;\n" +

			"varying vec2 vTextureCoord;\n" +
			"varying vec4 vColor;\n" +
			"uniform sampler2D uDiffuseTexture;\n" +
			"uniform vec3 northernLightsColor;\n" +
			"uniform float time;\n" +

			"void main() {\n" +
			"	float perlin = texture2D(uDiffuseTexture, vec2(vTextureCoord.x, vTextureCoord.y+time*5.0)).r;\n" +
			"	float mask = texture2D(uDiffuseTexture, vTextureCoord).g;\n" +
			"	float flow = texture2D(uDiffuseTexture, vTextureCoord+(perlin*0.1)).b;\n" +
			"	float col = perlin*mask*flow;\n" +
			"   gl_FragColor = vec4(northernLightsColor.x*col, northernLightsColor.y*col, northernLightsColor.z*col, 1.0)*3.0;\n" + 
			"}\n";	
	
	public NorthernLightsMaterial(int color) {
		super(mVShader, mFShader, false);
		this.northernLightsColorLoc = GLES20.glGetUniformLocation ( mProgram, "northernLightsColor" );
		this.color = color;
		this.updateUniform();
	}
	
	public void updateUniform() {
		GLES20.glUseProgram(mProgram); 
		GLES20.glUniform3f ( this.northernLightsColorLoc, Color.red(this.color)/255.0f, Color.green(this.color)/255.0f, Color.blue(this.color)/255.0f);
		super.updateUniform();
		//printDebug();
	}
}

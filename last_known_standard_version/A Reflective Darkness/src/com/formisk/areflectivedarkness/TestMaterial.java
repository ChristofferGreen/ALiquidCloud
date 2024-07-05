package com.formisk.areflectivedarkness;

public class TestMaterial extends ALiquidCloudMaterial {
	
	protected static final String mVShader = 
			"uniform mat4 uMVPMatrix;\n" +
			"attribute vec4 aPosition;\n" +
			"attribute vec2 aTextureCoord;\n" +
			"varying vec2 vTextureCoord;\n" +
			"varying vec2 vTextureCoordNormal;\n" +
			"uniform float u_time;\n" +
			"uniform float u_time2;\n" +
			"uniform float zoom;\n" +

			"void main() {\n" +
			"	gl_Position = uMVPMatrix * aPosition;\n" +
			"	vTextureCoord = aTextureCoord/(zoom*0.066);\n" +
			"}\n";	
	
	protected static final String mFShader = 
			"precision mediump float;\n" +
			"varying vec2 vTextureCoord;\n" +
			"uniform sampler2D uTexture0;\n" +
			"uniform sampler2D uTexture1;\n" +
			"uniform float u_time;\n" +
			"uniform float u_time2;\n" +
			"float fbm(vec2 p) {\n" +
			"  return texture2D(uTexture0, p).x;\n" +
			"}\n" +
			"float fbmSmooth(vec2 p) {\n" +
			"  return texture2D(uTexture1, p).x;\n" +
			"}\n" +
			"float pattern( in vec2 pNormal, out vec2 q, out vec2 r ) {\n" +
			"  q.x = fbmSmooth( pNormal );\n" + 									// LEVEL 1
			"  q.y = fbmSmooth( pNormal + vec2(5.0,1.0) );\n" +						// LEVEL 1
			"  r.x = fbmSmooth( pNormal + ( q * 0.5 ) + u_time*10.0 );\n" +			// LEVEL 1
			"  return fbm( pNormal/2.0 + 0.15*r.x );\n" +							// LEVEL 2
			"}\n" +
			"void main() {\n" +
			"   vec2 q, r;\n" +
			"   float col = pattern(vTextureCoord + u_time*1.5, q, r);\n" +
			"   float qlen = length(q);\n" +
			"   col=(col*0.001)+col*qlen*2.0;\n" +
			"	gl_FragColor = vec4(col, col, col, 1.0);\n" +
			"}\n";	
	
	public TestMaterial() {
		super(mVShader, mFShader);
	}
}

package com.formisk.aliquidcloud;

public class CanonicalMaterial extends ALiquidCloudMaterial {
	protected static final String mFShader = 
			"precision highp float;\n" +
			"varying vec2 vTextureCoord;\n" +
			"varying vec2 vTextureCoordPUtime5;\n" +
			"uniform sampler2D uTexture0;\n" +
			"uniform sampler2D uTexture1;\n" +
			"uniform float qmul;\n" +
			"uniform float rmul;\n" +
			
			"vec3 pattern( out vec2 q, out vec2 r ) {\n" +
			"  q.x =    texture2D( uTexture0, vTextureCoord + vec2( 0.0, 0.0) ).z;\n" +
			"  q.y =    texture2D( uTexture0, vTextureCoord + vec2( 5.2, 1.3) ).z;\n" +
			"  r.x =    texture2D( uTexture0, vTextureCoordPUtime5 + q*qmul + vec2( 1.7,9.2 ) ).y;\n" +
			"  r.x =    texture2D( uTexture0, vTextureCoordPUtime5 + q*qmul + vec2( 8.3,2.8 ) ).y;\n" +
			"  return vec3(texture2D( uTexture0, vTextureCoordPUtime5 + r*rmul ).x);\n" +
			"}\n" +
			
			"void main() {\n" +
			"	vec2 q, r;\n" +
			"   vec3 f = pattern(q, r);\n" +
			"   float ql = length(q);\n" +
			"   float rl = length(r);\n" +
			"   vec3 gradcol1 = texture2D( uTexture1, vec2(f.x, 0.0) ).xyz;\n" +
			"   vec3 gradcol2 = texture2D( uTexture1, vec2(f.x, 0.5) ).xyz;\n" +
			"   vec3 gradcol3 = texture2D( uTexture1, vec2(f.x, 1.0) ).xyz;\n" +
			"   vec3 col=mix(gradcol1, gradcol2, ql);\n" +
			"        col=mix(col, gradcol3, r.y);\n" +
			"	gl_FragColor = vec4(col.x, col.y, col.z, 1.0);\n" +
			//"   col=col+r.x*q.x+r.y*q.y;\n" +
			//"	gl_FragColor = vec4(col*r.x, col*r.y*q.x, col*q.y, 1.0);\n" +
			"}\n";	

	
	public CanonicalMaterial() {
		super(mVShader, mFShader);
		mQMul = 1.0f;
		mRMul = 0.5f;
	}
}
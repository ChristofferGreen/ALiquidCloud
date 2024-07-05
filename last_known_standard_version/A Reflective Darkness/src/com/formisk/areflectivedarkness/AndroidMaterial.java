package com.formisk.areflectivedarkness;

public class AndroidMaterial extends ALiquidCloudMaterial {
	protected static final String mFShader = 
			"precision mediump float;\n" +
			"varying vec2 vTextureCoord;\n" +
			"uniform sampler2D uTexture0;\n" +
			"uniform float u_time;\n" + 
			"float fbm(vec2 p) {\n" +
			"  return texture2D(uTexture0, p).x;\n" +
			"}\n" +
			"float pattern( in vec2 p, out vec2 q, out vec2 r ) {\n" +
			"  vec2 p5 = p*5.0;\n" + 
			"  q.x = fbm( p5 );\n" +
			"  q.y = fbm( p5 + vec2(5.0,1.0) );\n" +
			"  vec2 pq01t = p + ( q * 0.1 ) + u_time;\n" + 
			"  r.x = fbm( pq01t );\n" +
			"  return fbm( p5 + 0.15*r.x );\n" +
			"}\n" +
			"void main() {\n" +
			"   vec2 q, r;\n" +
			"   float col = pattern(vTextureCoord, q, r);\n" +
			"   float qlen = length(q);\n" +
			"	gl_FragColor = vec4(0.0, col*r.x*qlen*2.0, 0.0, 1.0);\n" +
			"}\n";		
	
	public AndroidMaterial() {
		super(mVShader, mFShader);
	}
}

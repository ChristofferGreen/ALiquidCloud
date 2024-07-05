package com.formisk.aliquidcloud;

public class RainbowMaterial extends ALiquidCloudMaterial {

	protected static final String mFShader = 
			"void main() {\n" +
			"	float q, r;\n" +
			"   float col = pattern(q, r);\n" +
			"   vec3 sky = vec3(col);\n" +
			"   sky = vec3(0.4, 0.0, 1.0);\n" +
			"   sky.x = sky.x*q;\n" +
			"   sky.y = sky.y+length(vec2(q,r));\n" +
			"   sky.z = sky.z*r;\n" +
			"	gl_FragColor = vec4(sky.x, sky.y, sky.z, 1.0);\n" +
			"}\n";	
	
	public RainbowMaterial(String fs) {
		super(mVShader, fs+mFShader);
	}
	
}

package com.formisk.aliquidcloud;

public class SkyMaterial extends ALiquidCloudMaterial {

	protected static final String mFShader = 
			"void main() {\n" +
			"	float q, r;\n" +
			"   float col = pattern(q, r);\n" +
			"   vec3 sky = vec3(col);\n" +
			"   sky = vec3(0.4, 0.3, 1.0);\n" +
			"   sky = sky-q+r;\n" +
			"	gl_FragColor = vec4(sky.x, sky.y, sky.z, 1.0);\n" +
			"}\n";	
	
	public SkyMaterial(String fs) {
		super(mVShader, fs+mFShader);
	}
	
}

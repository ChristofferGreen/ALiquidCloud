package com.formisk.aliquidcloud;

public class PurpleMaterial extends ALiquidCloudMaterial {

	protected static final String mFShader = 
			"void main() {\n" +
			"	float q, r;\n" +
			"   float col = pattern(q, r);\n" +
			"   col=(col*r)*0.8+(r*q+r*q)*0.5;\n" +
			"	gl_FragColor = vec4(col, col*r, col*q, 1.0);\n" +
			"}\n";	
	
	public PurpleMaterial(String fs) {
		super(mVShader, fs+mFShader);
	}
	
}

package com.formisk.aliquidcloud; 

public class BlackWhiteMaterial extends ALiquidCloudMaterial {
	protected static final String mFShader = 
			"void main() {\n" +
			"	float q, r;\n" +
			"   float col = pattern(q, r);\n" +
			"   col=(col*r)*0.8+(r*q+r*q)*0.5;\n" +
			"	gl_FragColor = vec4(col, col, col, 1.0);\n" +
			"}\n";	

	
	public BlackWhiteMaterial(String fs) {
		super(mVShader, fs+mFShader);
		mQMul = 0.8f;
		mRMul = 0.2f;
	}
}

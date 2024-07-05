package com.formisk.aliquidcloud;

public class MetallicWaveMaterial extends ALiquidCloudMaterial {
	protected static final String mFShader = 
			"void main() {\n" +
			"	float q, r;\n" +
			"   float col = pattern(q, r);\n" +
			"	gl_FragColor = vec4(col*q*r, col*r, col, 1.0);\n" +
			"}\n";	
	
	public MetallicWaveMaterial(String fs) {
		super(mVShader, fs+mFShader);
		mQMul = 2.0f;
		mRMul = 0.5f;
	}
}

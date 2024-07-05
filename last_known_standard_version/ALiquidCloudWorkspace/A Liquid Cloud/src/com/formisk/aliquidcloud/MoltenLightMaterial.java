package com.formisk.aliquidcloud;

public class MoltenLightMaterial extends ALiquidCloudMaterial {
	protected static final String mFShader = 
			"void main() {\n" +
			"	float q, r;\n" +
			"   float col = pattern(q, r);\n" +
			"   col=col*(q*2.0);\n" +
			"	gl_FragColor = vec4(col, col*r, col*(q*r), 1.0);\n" +
			"}\n";	
	
	public MoltenLightMaterial(String fs) {
		super(mVShader, fs+mFShader);
		mQMul = 1.5f;
		mRMul = 0.5f;
	}
}

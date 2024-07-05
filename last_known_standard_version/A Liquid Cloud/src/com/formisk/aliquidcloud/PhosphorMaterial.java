package com.formisk.aliquidcloud;

public class PhosphorMaterial extends ALiquidCloudMaterial {
	protected static final String mFShader = 
			"void main() {\n" +
			"	float q, r;\n" +
			"   float col = pattern(q, r);\n" +
			"   float red = col*q+r*0.3;\n" +
			"   float green = col*q+r*0.3;\n" +
			"   float blue = col*r+q*0.3;\n" +
			"	gl_FragColor = vec4(red, green, blue, 1.0);\n" +
			"}\n";	
	
	public PhosphorMaterial(String fs) {
		super(mVShader, fs+mFShader);
		mQMul = 0.7f;
		mRMul = 0.3f;
	}

}

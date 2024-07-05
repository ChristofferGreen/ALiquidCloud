package com.formisk.aliquidcloud; 

public class CustomMaterial1 extends ALiquidCloudMaterial {
	protected static final String mFShader = 
			"void main() {\n" +
				"	float q, r;\n" +
				"   float col = pattern(q, r);\n" +
				"   col=(col*r)*0.8+(r*q+r*q)*0.5;\n" +
				"   vec3 gradCol = getGradient1(col);\n" +
				"	gl_FragColor = vec4(gradCol.x, gradCol.y, gradCol.z, 1.0);\n" +
			"}\n";	


	public CustomMaterial1(String fs) {
		super(mVShader, fs+mFShader);
		mQMul = 0.8f;
		mRMul = 0.2f;
	}
}

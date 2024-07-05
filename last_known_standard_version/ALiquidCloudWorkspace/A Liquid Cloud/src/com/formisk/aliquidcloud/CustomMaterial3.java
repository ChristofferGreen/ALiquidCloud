package com.formisk.aliquidcloud; 

public class CustomMaterial3 extends ALiquidCloudMaterial {
	protected static final String mFShader = 
			"void main() {\n" +
			"	float q, r;\n" +
			"   float col = pattern(q, r);\n" +

			"   vec3 mixCol2 = mix(getGradient1(col), getGradient2(col), q);\n" +
			"   vec3 mixCol3 = mix(mixCol2, getGradient3(col), r);\n" +

			"	gl_FragColor = vec4(mixCol3.x, mixCol3.y, mixCol3.z, 1.0);\n" +
			"}\n";	

	
	public CustomMaterial3(String fs) {
		super(mVShader, fs+mFShader);
		mQMul = 0.8f;
		mRMul = 0.2f;
	}
}

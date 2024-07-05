package com.formisk.aliquidcloud; 

public class SimpleCustomAdditiveMaterial extends ALiquidCloudMaterial {
	protected static final String mFShader = 
			"void main() {\n" +
			"	float q, r;\n" +
			"   float col = pattern(q, r);\n" +
			"   vec3 mix = col*(gradColor1 + gradColor2*r + gradColor3*q);\n" +
			"	gl_FragColor = vec4(mix.x, mix.y, mix.z, 1.0);\n" +
			"}\n";	

	
	public SimpleCustomAdditiveMaterial(String fs) {
		super(mVShader, fs+mFShader);
		mQMul = 0.8f;
		mRMul = 0.2f;
	}
}

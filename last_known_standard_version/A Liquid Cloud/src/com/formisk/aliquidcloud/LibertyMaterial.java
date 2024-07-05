package com.formisk.aliquidcloud;

public class LibertyMaterial extends ALiquidCloudMaterial {

	protected static final String mFShader = 
			//"   col=col*q+r*q+r*q;\n" +
			"void main() {\n" +
			"	float q, r;\n" +
			"   float col = pattern(q, r);\n" +
			"   col=col+r*q+r*q;\n" +
			"	gl_FragColor = vec4(col*r, col*r*q, col*q, 1.0);\n" +
			"}\n";	
	
	public LibertyMaterial(String fs) {
		super(mVShader, fs+mFShader);
	}
	
}

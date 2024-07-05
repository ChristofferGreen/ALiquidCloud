package com.formisk.areflectivedarkness;

public class PenguinMaterial extends ALiquidCloudMaterial {
	protected static final String mFShader = 
			mFShaderBase +
			"   col=col*q.y;\n" +
			"   float yellow=(1.0-r)*q.x*q.y;\n" +
			"	gl_FragColor = vec4(col+yellow, (col+yellow)*0.8, yellow, 1.0);\n" +
			"}\n";	
	
	public PenguinMaterial() {
		super(mVShader, mFShader);
	}

}

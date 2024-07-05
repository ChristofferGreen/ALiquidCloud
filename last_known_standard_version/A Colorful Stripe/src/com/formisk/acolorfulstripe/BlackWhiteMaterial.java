package com.formisk.acolorfulstripe; 

public class BlackWhiteMaterial extends AColorfulStripeMaterial {
	protected static final String mFShader = 
			mFShaderBase +
			"void main() {\n" +
			"	float q, r;\n" +
			"   float col = pattern(q, r);\n" +
			"   vec3 gradCol1 = getGradient1(col);\n" +
			"   vec3 gradCol2 = getGradient2(col);\n" +
			"   vec3 mixCol = mix(gradCol1, gradCol2, r);\n" +
			"   vec3 baseMixCol = mixCol/10.0;\n" +
			"   baseMixCol = (baseMixCol-mod(baseMixCol, 0.01));\n" +
			"   baseMixCol = baseMixCol*10.0;\n" +
			"   mixCol = (mixCol*0.5) + (baseMixCol*0.5);\n" +
			"	gl_FragColor = vec4(mixCol.x, mixCol.y, mixCol.z, 1.0);\n" +
			"}\n";

	
	public BlackWhiteMaterial() {
		super(mVShader, mFShader);
		mQMul = 0.8f;
		mRMul = 0.2f;
	}
}

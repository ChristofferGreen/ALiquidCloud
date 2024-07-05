package com.formisk.aneonpath;

import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import rajawali.BaseObject3D;
import rajawali.materials.AMaterial;
import rajawali.materials.SimpleMaterial;
import rajawali.parser.ObjParser;

public class NeonPath extends BaseObject3D {
	private NeonPathCreator neonPathCreator = null;
	private float partMoved = 0.0f;
	
	public NeonPath(NeonPathMaterial neonPathMaterial, NeonPathPartCreator neonPathPartCreator, long seed, boolean inverted) {
		super();
		this.neonPathCreator = new NeonPathCreator(neonPathMaterial, neonPathPartCreator, seed, inverted);
		for(BaseObject3D object : this.neonPathCreator.createInitialPath())
			this.addChild(object);
	}
	
	public void drawNextFrame(float speed) {
		for(int i = 0; i < this.getNumChildren(); i++) {
			BaseObject3D part = this.getChildAt(i); 
			part.setZ(part.getZ()-speed);
		}
		this.partMoved += speed;
		if(this.partMoved >= NeonPathCreator.partSize) {
			this.partMoved = this.partMoved-NeonPathCreator.partSize;
			this.removeChild(this.getChildAt(0));
			BaseObject3D part = this.neonPathCreator.getNextPathPart();
			part.setZ(part.getZ()-this.partMoved);
			this.addChild(part);
		}
	}
}

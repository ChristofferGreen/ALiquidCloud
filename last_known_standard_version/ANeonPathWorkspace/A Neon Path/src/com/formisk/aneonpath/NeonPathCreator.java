package com.formisk.aneonpath;

import java.util.LinkedList;
import java.util.List;

import rajawali.BaseObject3D;
import rajawali.materials.AMaterial;

public class NeonPathCreator {
	public static float partSize = 2.0f;
	private boolean inverted = false;
	private float targetX = 0;
	private int targetParts = 10;
	private int previousPart = R.raw.straight;
	private int targetSubPathParts = 7;
	private java.util.Random randomNumberGenerator;
	private float currentHorizontalPosition = 0.0f;
	private float calculatedTopPosition = 0.0f;
	private NeonPathPartCreator neonPathPartCreator = null;
	private AMaterial neonPathMaterial = null;
	private int addedParts = 0;
	
	public NeonPathCreator(AMaterial neonPathMaterial, NeonPathPartCreator neonPathPartCreator, long seed, boolean inverted) {
		this.neonPathMaterial = neonPathMaterial;
		this.neonPathPartCreator = neonPathPartCreator;
		this.randomNumberGenerator = new java.util.Random(seed);
		this.inverted = inverted;
		this.calculatedTopPosition = this.targetParts*this.partSize;
	}
	
	public List<BaseObject3D> createInitialPath() {
		List<BaseObject3D> pathPartsList = new LinkedList<BaseObject3D>();
		this.setNewTarget();
		for(int i = 0; i < this.targetParts; i++) {
			for(BaseObject3D part : pathPartsList)
				part.setZ(part.getZ()-this.partSize);
			BaseObject3D part = this.getNextPathPart();
			pathPartsList.add(part);
		}

		return pathPartsList;
	}
	
	public BaseObject3D getNextPathPart() {
		int nextPartId = -1;
		if(this.targetX < this.currentHorizontalPosition)
			nextPartId = this.neonPathPartCreator.getNextPathPartIdFromPrevious(this.previousPart, 0);
		else if(this.targetX == this.currentHorizontalPosition)
			nextPartId = this.neonPathPartCreator.getNextPathPartIdFromPrevious(this.previousPart, 1);
		else if(this.targetX > this.currentHorizontalPosition)
			nextPartId = this.neonPathPartCreator.getNextPathPartIdFromPrevious(this.previousPart, 2);

		
		BaseObject3D part = this.neonPathPartCreator.getClonedPathPart(nextPartId, this.neonPathMaterial);
		part.setPosition(this.currentHorizontalPosition, 0.0f, this.calculatedTopPosition);
		if(this.inverted)
			part.setScale(1.0f, -1.0f, 1.0f);
		
		if(this.addedParts >= this.targetSubPathParts) {
			this.setNewTarget();
			this.addedParts = 0;
		}
		
		this.currentHorizontalPosition += this.neonPathPartCreator.getHorizontalMove(nextPartId);
		
		this.previousPart = nextPartId; 
		this.addedParts++;
		return part;
	}	
	
	public static long getNewSeed() {
		return (long)(Math.random() * 20000);
	}

	private void setNewTarget() {
		float Min = -6.0f;
		float Max = 6.0f;
		float random = Min + (float)(this.randomNumberGenerator.nextFloat() * ((Max - Min) + 1.0f));
		this.targetX = random;
	}
}

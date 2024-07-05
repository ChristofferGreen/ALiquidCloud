package com.formisk.aneonpath;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import rajawali.BaseObject3D;
import rajawali.materials.AMaterial;
import rajawali.materials.SimpleMaterial;
import rajawali.parser.ObjParser;

public class NeonPathPartCreator {
	public ANeonPathRenderer renderer = null;
	public Map<Integer, Map<Integer, Integer> > directionMap;
	private Map<Integer, Float> xMoveMap;
	private Map<Integer, BaseObject3D> partsMap;
	
	public NeonPathPartCreator(ANeonPathRenderer renderer) {
		this.renderer = renderer;
		this.partsMap = new LinkedHashMap<Integer, BaseObject3D>();
		this.renderer.addChild(this.parseObject(R.raw.turn_right));
		this.renderer.addChild(this.parseObject(R.raw.turn_left));
		this.renderer.addChild(this.parseObject(R.raw.straight));
		this.renderer.addChild(this.parseObject(R.raw.diagonal_right));
		this.renderer.addChild(this.parseObject(R.raw.diagonal_left));
		this.renderer.addChild(this.parseObject(R.raw.right_diagonal_straight));
		this.renderer.addChild(this.parseObject(R.raw.left_diagonal_straight));
		
		this.directionMap = new LinkedHashMap<Integer, Map<Integer, Integer> >();
		this.directionMap.put(R.raw.straight, new LinkedHashMap<Integer, Integer>());
		this.directionMap.get(R.raw.straight).put(0, R.raw.turn_right);
		this.directionMap.get(R.raw.straight).put(1, R.raw.straight);
		this.directionMap.get(R.raw.straight).put(2, R.raw.turn_left);
		this.directionMap.put(R.raw.turn_right, new LinkedHashMap<Integer, Integer>());
		this.directionMap.get(R.raw.turn_right).put(0, R.raw.right_diagonal_straight);
		this.directionMap.get(R.raw.turn_right).put(1, R.raw.right_diagonal_straight);
		this.directionMap.get(R.raw.turn_right).put(2, R.raw.diagonal_right);
		this.directionMap.put(R.raw.turn_left, new LinkedHashMap<Integer, Integer>());
		this.directionMap.get(R.raw.turn_left).put(0, R.raw.diagonal_left);
		this.directionMap.get(R.raw.turn_left).put(1, R.raw.left_diagonal_straight);
		this.directionMap.get(R.raw.turn_left).put(2, R.raw.left_diagonal_straight);
		this.directionMap.put(R.raw.diagonal_right, new LinkedHashMap<Integer, Integer>());
		this.directionMap.get(R.raw.diagonal_right).put(0, R.raw.right_diagonal_straight);
		this.directionMap.get(R.raw.diagonal_right).put(1, R.raw.right_diagonal_straight);
		this.directionMap.get(R.raw.diagonal_right).put(2, R.raw.diagonal_right);
		this.directionMap.put(R.raw.diagonal_left, new LinkedHashMap<Integer, Integer>());
		this.directionMap.get(R.raw.diagonal_left).put(0, R.raw.diagonal_left);
		this.directionMap.get(R.raw.diagonal_left).put(1, R.raw.left_diagonal_straight);
		this.directionMap.get(R.raw.diagonal_left).put(2, R.raw.left_diagonal_straight);
		this.directionMap.put(R.raw.right_diagonal_straight, new LinkedHashMap<Integer, Integer>());
		this.directionMap.get(R.raw.right_diagonal_straight).put(0, R.raw.turn_left);
		this.directionMap.get(R.raw.right_diagonal_straight).put(1, R.raw.straight);
		this.directionMap.get(R.raw.right_diagonal_straight).put(2, R.raw.turn_right);
		this.directionMap.put(R.raw.left_diagonal_straight, new LinkedHashMap<Integer, Integer>());
		this.directionMap.get(R.raw.left_diagonal_straight).put(0, R.raw.turn_left);
		this.directionMap.get(R.raw.left_diagonal_straight).put(1, R.raw.straight);
		this.directionMap.get(R.raw.left_diagonal_straight).put(2, R.raw.turn_right);
		
		this.xMoveMap = new LinkedHashMap<Integer, Float>();
		this.xMoveMap.put(R.raw.straight, 0.0f);
		this.xMoveMap.put(R.raw.turn_right, 2.0f);
		this.xMoveMap.put(R.raw.turn_left, -2.0f);
		this.xMoveMap.put(R.raw.diagonal_right, 2.0f);
		this.xMoveMap.put(R.raw.diagonal_left, -2.0f);
		this.xMoveMap.put(R.raw.right_diagonal_straight, 0.0f);
		this.xMoveMap.put(R.raw.left_diagonal_straight, 0.0f);
	}
	
	private BaseObject3D parseObject(int objectId) {
		ObjParser parser = new ObjParser(this.renderer.getContext().getResources(), this.renderer.getTextureManager(), objectId);
		parser.parse();
		BaseObject3D object = parser.getParsedObject();
		object.setMaterial(new SimpleMaterial());
		object.setZ(-10.0f);
		this.partsMap.put(objectId, object);
		return object;
	}
	
	public float getHorizontalMove(int partId) {
		return this.xMoveMap.get(partId);
	}
	
	public int getNextPathPartIdFromPrevious(int previousPartId, int direction) {
		return this.directionMap.get(previousPartId).get(direction);
	}
	
	public BaseObject3D getClonedPathPart(int partId, AMaterial pathMaterial) {
		BaseObject3D part = this.partsMap.get(partId).clone();
		//BaseObject3D part = this.parseObject(partId);
		part.setTransparent(true);
		part.setDoubleSided(true);
		part.setMaterial(pathMaterial);
		part.setBlendingEnabled(true);
		part.setBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_COLOR);
		//part.setAdditive(true);
		part.setRotation(0.0f, 180.0f, 0.0f);
		return part;
	}
}

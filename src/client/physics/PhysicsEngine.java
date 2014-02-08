package client.physics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import shared.Box;
import shared.Vec3f;

public class PhysicsEngine {
	
	public static void init() { instance = new PhysicsEngine(); }
	public static PhysicsEngine get() { return instance; }
	
	public void updatePhysics() {
		//Update all physics models
		List<PhysicsModel> checkCollisions = new LinkedList();
		for(PhysicsModel mdl : models) {
			if(mdl.isDynamic()) {
				mdl.onUpdate(1.f);
				
				//This force will get mixed in on the next update
				mdl.applyForce(gravity);
				mdl.setOnSurface(false);	//Used to ensure gravity force counteracted only once
				
				//This object should have collisions checked
				checkCollisions.add(mdl);
			} else {
				for(PhysicsModel dmdl : checkCollisions) {
					applyCollisions(mdl, dmdl);
				}
			}
		}
		
		//Perform collisions
		for(PhysicsModel mdl1 : checkCollisions) {
			for(PhysicsModel mdl2 : models) {
				if(mdl1.equals(mdl2)) {
					break;
				}
				applyCollisions(mdl1, mdl2);
			}
		}
	}
	
	public boolean add(PhysicsModel mdl) {
		return models.add(mdl);
	}
	
	public boolean remove(PhysicsModel mdl) {
		return models.remove(mdl);
	}

	
	private PhysicsEngine() {
	}
	
	private void applyCollisions(PhysicsModel mdl1, PhysicsModel mdl2) {
		CollisionModel cmdl1 = mdl1.getCollision();
		CollisionModel cmdl2 = mdl2.getCollision();
		switch(cmdl1.type()) {
		case CAPSULE:
			switch(cmdl2.type()) {
			case HMAP:
				capsuleOnHmapCollision(mdl1, mdl2);
				break;
			case CAPSULE:
				break;
			default:
				break;
			}
			break;
		case HMAP:
			switch(cmdl2.type()) {
			case CAPSULE:
				capsuleOnHmapCollision(mdl2, mdl1);
				break;
			case SPHERE:
				sphereOnHmapCollision(mdl2, mdl1);
				break;
			default:
				break;
			}
			break;
		case SPHERE:
			switch(cmdl2.type()) {
			case HMAP:
				sphereOnHmapCollision(mdl1, mdl2);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
	}
	
	private void capsuleOnCapsuleCollision(PhysicsModel mdl1, PhysicsModel mdl2) {
		CapsuleCollisionModel cmdl1 = (CapsuleCollisionModel) mdl1.getCollision();
		CapsuleCollisionModel cmdl2 = (CapsuleCollisionModel) mdl2.getCollision();
		
	}
	
	private void capsuleOnHmapCollision(PhysicsModel capsMdl, PhysicsModel hmapMdl) {
		CapsuleCollisionModel capsCmdl = (CapsuleCollisionModel) capsMdl.getCollision();
		HmapCollisionModel hmapCmdl = (HmapCollisionModel) hmapMdl.getCollision();
		
		//check if there is a collision
		Vec3f end1 = capsCmdl.getEnd1();
		Vec3f end2 = capsCmdl.getEnd2();
		
		Box bounds = hmapCmdl.getBounds();
		float radius = capsCmdl.getRadius();
		Vec3f radAdd = new Vec3f(radius, radius, radius);
		bounds.min.sub(radAdd);
		bounds.max.add(radAdd);
		
		if(!bounds.pointIsInside(end1) &&
				!bounds.pointIsInside(end2)) {
			return;	//Assume that no capsule is going to be larger than a heightmap collision box
		}

		float y1 = hmapCmdl.getHeightAt(end1);
		float y2 = hmapCmdl.getHeightAt(end2);
		if(y1 < (end1.y() - radius) && y2 < (end2.y()) - radius) {
			//Neither point is below the heightmap
			return;
		}
		
		float ydiff1 = y1 - (end1.y() - radius);
		float ydiff2 = y2 - (end2.y() - radius);
		float maxDiff = (ydiff1 > ydiff2) ? ydiff1 : ydiff2;
		capsMdl.moveBy(new Vec3f(0.f, maxDiff, 0.f));
		if(capsMdl.onSurface()) {
			capsMdl.applyForce(normForce);	//For now, just stop them from falling
			capsMdl.setOnSurface(true);
		}
	}
	
	private void sphereOnHmapCollision(PhysicsModel sphereMdl, PhysicsModel hmapMdl) {
		SphereCollisionModel scmdl = (SphereCollisionModel)sphereMdl.getCollision();
		HmapCollisionModel hcmdl = (HmapCollisionModel)hmapMdl.getCollision();
		Vec3f center = scmdl.getCenter();
		
		Box bounds = hcmdl.getBounds();
		float radius = scmdl.getRadius();
		Vec3f radAdd = new Vec3f(radius, radius, radius);
		bounds.min.sub(radAdd);
		bounds.max.add(radAdd);

		//Check for a collision, part 1
		if(!bounds.pointIsInside(center)) {
			return;
		}
		
		//Check for a collision, part 2
		float y = hcmdl.getHeightAt(center);
		if(y < (center.y() - radius)) {
			return;	//The point is not below the heightmap
		}
		
		//We have a collision, now do something about it
		float ydiff = y - (center.y() - radius);
		sphereMdl.moveBy(new Vec3f(0.f, ydiff, 0.f));
		if(!sphereMdl.onSurface()) {
			Vec3f normal = hcmdl.getNormalAt(center);
			normal.normalizeTo(gravityMagnitude);
			//System.out.println("Normal = (" + normal.x() + "," + normal.y() + "," + normal.z() + ")");
			sphereMdl.applyForce(normal);	//For now, just stop them from falling
			sphereMdl.setOnSurface(true);
		}
	}
	
	private List<PhysicsModel> models = new ArrayList();
	private static PhysicsEngine instance;
	private static final float gravityMagnitude = 0.1f;
	private static final Vec3f gravity = new Vec3f(0.f, -gravityMagnitude, 0.f);
	private static final Vec3f normForce = new Vec3f(0.f, gravityMagnitude, 0.f);
}

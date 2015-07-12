package handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameContactListener implements ContactListener{

	private int contactsWithGround;

	//called when two fixtures start to collide
	@Override
	public void beginContact(Contact c) {
		Fixture a = c.getFixtureA();
		Fixture b = c.getFixtureB();
		
		if(a.getUserData() != null && a.getUserData().equals("foot")){
			contactsWithGround++;
		}
		if(b.getUserData() != null && b.getUserData().equals("foot")){
			contactsWithGround++;
		}
	}

	//called when two fixture no longer collide
	@Override
	public void endContact(Contact c) {
		Fixture a = c.getFixtureA();
		Fixture b = c.getFixtureB();
		
		if(a.getUserData() != null && a.getUserData().equals("foot")){
			contactsWithGround--;
		}
		if(b.getUserData() != null && b.getUserData().equals("foot")){
			contactsWithGround--;
		}
	}

	public boolean isPlayerOnGround(){
		return contactsWithGround>0;
	}
	
	//collision detection
	//preSolve
	//collision handling
	//postSolve
	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}

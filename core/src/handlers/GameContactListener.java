package handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameContactListener implements ContactListener{

	private int contactsWithGround,contactsWithRoof;
	private boolean isFalling;
	
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
		if(a.getUserData() != null && a.getUserData().equals("head")){
			contactsWithRoof++;
		}
		if(b.getUserData() != null && b.getUserData().equals("head")){
			contactsWithRoof++;
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
		if(a.getUserData() != null && a.getUserData().equals("head")){
			contactsWithRoof--;
		}
		if(b.getUserData() != null && b.getUserData().equals("head")){
			contactsWithRoof--;
		}
	}

	public boolean isPlayerOnGround(){
		if(contactsWithGround>0){
			setFalling(false);
			return true;
		}
		else return false;
	}
	
	public boolean playerHitRoof(){
		if(contactsWithRoof>0){
			setFalling(true);
			return true;
		}
		else return false;
	}
	
	public void setFalling(boolean isFalling){
		this.isFalling = isFalling;
	}
	
	public boolean isFalling(){
		return isFalling;
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

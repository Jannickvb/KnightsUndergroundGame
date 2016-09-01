package entities;

import main.Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;



public class Player extends Entity{

	private BodyDef bdef;
	private FixtureDef fdef;
	
	private Fixture bodyFix;
	private Fixture footFix;
	private Fixture headSensor;
	
	public Player(World world,float x,float y) {
		super(world,x,y);
		this.bdef  = new BodyDef();
		this.fdef  = new FixtureDef();
		
		//Create player body
		createPlayerBody();
		
		//Create sensors
		fdef.isSensor = true;
		createFootSensor();
		createHeadSensor();
		
		//Draw player
//		Texture tex = Game.res.getTexture("");
//		TextureRegion[] sprites = TextureRegion.split(tex,32,32)[0];
		
//		setAnimation(sprites, 1 / 12f);
	}
	
	private void createPlayerBody(){
		bdef.position.set(x / PPM, y / PPM);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		body.setSleepingAllowed(false);
		PolygonShape playerShape = new PolygonShape();
		playerShape.setAsBox(5 / PPM, 12 / PPM);
		fdef.shape = playerShape;
		fdef.filter.categoryBits = BIT_PLAYER;
		fdef.filter.maskBits = BIT_GROUND;
		bodyFix = body.createFixture(fdef);
		bodyFix.setUserData("player");
	}
	
	private void createFootSensor(){
		CircleShape footShape = new CircleShape();
		footShape.setRadius(4.5f / PPM);
		footShape.setPosition(new Vector2(0, -12f / PPM));
		fdef.shape = footShape;
		footFix = body.createFixture(fdef);
		footFix.setUserData("foot");
	}
	
	private void createHeadSensor(){
		CircleShape headShape = new CircleShape();
		headShape.setRadius(4.5f / PPM);
		headShape.setPosition(new Vector2(0, 12 / PPM));
		fdef.shape = headShape;
		headSensor = body.createFixture(fdef);
		headSensor.setUserData("head");
	}
	
	public Fixture getBodyFixture(){
		return bodyFix;
	}
	
	public Fixture getFootFixture(){
		return footFix;
	}
	
}

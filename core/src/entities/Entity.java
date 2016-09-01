package entities;

import handlers.Animation;
import interfaces.B2DVars;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;


public abstract class Entity implements B2DVars{
	
	protected Body body;
	protected World world;
	protected Animation animation;
	
	protected float width,height,x,y;
	
	public Entity(World world,float x, float y){
		this.world = world;
		this.x = x;
		this.y = y;
		animation = new Animation();		
	}
	
	public void setAnimation(TextureRegion[] reg, float delay){
		animation.setFrames(reg, delay);
		width = reg[0].getRegionWidth();
		height = reg[0].getRegionHeight();
	}
	
	public void update(float dt){
		animation.update(dt);
	}
	
	public void render(SpriteBatch sb){
		sb.begin();
		sb.draw(animation.getFrame(),
				body.getPosition().x * PPM - width/2,
				body.getPosition().y * PPM - height/2);
		sb.end();
	}
	
	public Body getBody(){
		return body;
	}
	
	public Vector2 getPosition(){
		return body.getPosition();
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
}

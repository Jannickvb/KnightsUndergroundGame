package states;

import handlers.GameContactListener;
import handlers.GameInput;
import handlers.GameStateManager;
import interfaces.B2DVars;
import main.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import entities.Player;

public class Play extends GameState implements B2DVars {

	private World world;
	private Box2DDebugRenderer b2dr;
	private OrthographicCamera b2dCam;
	private boolean debug = false;
	
	private GameContactListener cl;

	private TiledMap tilemap;
	private float tileSize;
	private OrthogonalTiledMapRenderer tmr;

	private BodyDef bdef;
	private FixtureDef fdef;
	
	private Player player;
	
	public Play(GameStateManager gsm) {
		super(gsm);

		bdef  = new BodyDef();
		fdef  = new FixtureDef();
		
		world = new World(new Vector2(0, -9.81f), true);
		cl = new GameContactListener();
		world.setContactListener(cl);
		b2dr = new Box2DDebugRenderer();

		b2dCam = new OrthographicCamera();
		b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);

		player = new Player(world,600f,1700f);
		
		createTiles();
	}

	Vector2 vel;
	Vector2 pos;

	@Override
	public void update(float dt) {
		vel = player.getBody().getLinearVelocity();
		pos = player.getBody().getPosition();
				
		if(Math.abs(vel.x) > MAX_WALK_VELOCITY) {			
			vel.x = Math.signum(vel.x) * MAX_WALK_VELOCITY;
			player.getBody().setLinearVelocity(vel.x, vel.y);
		}	
		
		
		handleInput();

		world.step(dt, 6, 2);
	}


	@Override
	public void render() {
		// clear screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// set cam position
		b2dCam.position.set(new Vector2(player.getBody().getPosition().x,player.getBody().getPosition().y), 0);
		b2dCam.update();
		
		cam.position.set(player.getBody().getPosition().x*PPM,
				player.getBody().getPosition().y*PPM,0);
		cam.update();
		
		// render tilemap
		tmr.setView(cam);
		tmr.render();
		
		// draw player
		sb.setProjectionMatrix(cam.combined);
		
		// draw hud
//		sb.setProjectionMatrix(hudCam.combined);
		
		// draw world
		if(debug){
			b2dr.render(world, b2dCam.combined);
		}
				
	}

	@Override
	public void dispose() {

	}

	final static float MAX_WALK_VELOCITY = 240f/PPM;
	final static float MAX_RUN_VELOCITY = 380f/PPM;
	final static float MAX_JUMP_VELOCITY = 350f/PPM;
	@Override
	public void handleInput() {
		if(!GameInput.isDown(GameInput.VK_SPACE)){
			if(!cl.isPlayerOnGround()){
				cl.setFalling(true);
			}
		}
		if (GameInput.isDown(GameInput.VK_SPACE)) {
			cl.playerHitRoof();
			if (cl.isPlayerOnGround()) {
				player.getBody().applyLinearImpulse(0, 70f/PPM, pos.x, pos.y,true);
			}
			if(!cl.isPlayerOnGround()){
				if(vel.y >= MAX_JUMP_VELOCITY)
					cl.setFalling(true);
				
				if(!cl.isFalling()){
					player.getBody().applyLinearImpulse(0, 50f/PPM, pos.x, pos.y,true);
				}
			}
		}
//		if (GameInput.isDown(GameInput.VK_LEFT)) {
//			player.getBody().applyForceToCenter(-100/PPM, 0, true);
//		}
//		if (GameInput.isDown(GameInput.VK_RIGHT)) {
//			player.getBody().applyForceToCenter(100/PPM, 0, true);
//		}
			
		// disable friction while jumping
		if(!cl.isPlayerOnGround()) {			
			player.getBodyFixture().setFriction(0f);
			player.getFootFixture().setFriction(0f);			
		} else {
			if(!GameInput.isDown(GameInput.VK_LEFT) && !GameInput.isDown(GameInput.VK_RIGHT)) {
				player.getBody().applyLinearImpulse(-vel.x, 0, pos.x, pos.y, true);
			}
			else {
				player.getBodyFixture().setFriction(0.2f);
				player.getFootFixture().setFriction(0.2f);
			}
		}		
		if(GameInput.isDown(GameInput.VK_SHIFT))
		{
			// apply left impulse, but only if max run velocity is not reached yet
			if(GameInput.isDown(GameInput.VK_LEFT) && vel.x > -MAX_RUN_VELOCITY) {
				player.getBody().applyLinearImpulse(-100f/PPM, 0, pos.x, pos.y,true);
			}

			// apply right impulse, but only if max run velocity is not reached yet
			if(GameInput.isDown(GameInput.VK_RIGHT) && vel.x < MAX_RUN_VELOCITY) {
				player.getBody().applyLinearImpulse(100f/PPM, 0, pos.x, pos.y,true);
			}
		}else{
			// apply left impulse, but only if max velocity is not reached yet
			if(GameInput.isDown(GameInput.VK_LEFT) && vel.x > -MAX_WALK_VELOCITY) {
				player.getBody().applyLinearImpulse(-70f/PPM, 0, pos.x, pos.y,true);
			}

			// apply right impulse, but only if max velocity is not reached yet
			if(GameInput.isDown(GameInput.VK_RIGHT) && vel.x < MAX_WALK_VELOCITY) {
				player.getBody().applyLinearImpulse(70f/PPM, 0, pos.x, pos.y,true);
			}
		}
			
	}

	public void createPlayer(Body body){
	
	}
	
	public void createTiles(){
		// load tile map
		tilemap = new TmxMapLoader().load("maps/test.tmx");
		tmr = new OrthogonalTiledMapRenderer(tilemap);
		
		//square blocks
		TiledMapTileLayer layer = (TiledMapTileLayer) tilemap.getLayers().get(
				"solid");
		tileSize = layer.getTileWidth();
		createLayer(layer,BIT_GROUND,0);
		
		//topleftslope
		layer = (TiledMapTileLayer) tilemap.getLayers().get(
				"topleftslope");
		createLayer(layer,BIT_GROUND,1);
		
		//toprightslope
		layer = (TiledMapTileLayer) tilemap.getLayers().get(
				"toprightslope");
		createLayer(layer,BIT_GROUND,2);
		
		//botleftslope
		layer = (TiledMapTileLayer) tilemap.getLayers().get(
				"botleftslope");
		createLayer(layer,BIT_GROUND,3);
		
		//botrightslope
		layer = (TiledMapTileLayer) tilemap.getLayers().get(
				"botrightslope");
		createLayer(layer,BIT_GROUND,4);
	}
	
	public void createLayer(TiledMapTileLayer layer, short bits,int type){
		
		bdef = new BodyDef();
		fdef = new FixtureDef();
		
		for (int row = 0; row < layer.getHeight(); row++) {
			for (int col = 0; col < layer.getWidth(); col++) {
				// get cell
				Cell cell = layer.getCell(col, row);

				if (cell == null)
					continue;
				if (cell.getTile() == null)
					continue;

				bdef.type = BodyType.StaticBody;
				bdef.position.set(
						(col + 0.5f) * tileSize / PPM,
						(row + 0.5f) * tileSize / PPM
				);
				
				ChainShape cs = new ChainShape();
				Vector2[] v = null;
				switch(type){
				case 0:
					v = new Vector2[5];
					v[0] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					v[1] = new Vector2(-tileSize /2 /PPM,tileSize/2/PPM);
					v[2] = new Vector2(tileSize /2 /PPM,tileSize/2/PPM);
					v[3] = new Vector2(tileSize /2 /PPM,-tileSize/2/PPM);
					v[4] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					break;
				case 1:
					v = new Vector2[4];
					v[0] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					v[1] = new Vector2(-tileSize /2 /PPM,tileSize/2/PPM);
					v[2] = new Vector2(tileSize /2 /PPM,tileSize/2/PPM);
					v[3] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					break;
				case 2:
					v = new Vector2[4];
					v[0] = new Vector2(-tileSize /2 /PPM,tileSize/2/PPM);
					v[1] = new Vector2(tileSize /2 /PPM,tileSize/2/PPM);
					v[2] = new Vector2(tileSize /2 /PPM,-tileSize/2/PPM);
					v[3] = new Vector2(-tileSize /2 /PPM,tileSize/2/PPM);
					break;
				case 3:
					v = new Vector2[4];
					v[0] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					v[1] = new Vector2(-tileSize /2 /PPM,tileSize/2/PPM);
					v[2] = new Vector2(tileSize /2 /PPM,-tileSize/2/PPM);
					v[3] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					break;
				case 4:
					v = new Vector2[4];
					v[0] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					v[1] = new Vector2(tileSize /2 /PPM,tileSize/2/PPM);
					v[2] = new Vector2(tileSize /2 /PPM,-tileSize/2/PPM);
					v[3] = new Vector2(-tileSize /2 /PPM,-tileSize/2/PPM);
					break;
				}
				if(type>=0&&type<=4)
					cs.createChain(v);
				
				fdef.shape = cs;
				fdef.filter.categoryBits = BIT_GROUND;
				fdef.filter.maskBits = BIT_PLAYER;
				fdef.isSensor = false;
				world.createBody(bdef).createFixture(fdef);
			}
		}
	}
}

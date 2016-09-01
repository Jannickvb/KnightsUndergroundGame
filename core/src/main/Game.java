package main;

import handlers.Content;
import handlers.GameInput;
import handlers.GameInputProcessor;
import handlers.GameStateManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game implements ApplicationListener {
	public static final int V_HEIGHT = 320, V_WIDTH = 480, SCALE = 2;
	public static final String TITLE = "Knights Underground";

	public static final float STEP = 1 / 60f;
	private float accum;

	private SpriteBatch sb;
	private OrthographicCamera cam,
							   hudCam;
	private GameStateManager gsm;
	public static Content res;
	
	public void create() {
		Gdx.input.setInputProcessor(new GameInputProcessor());
		Gdx.gl.glClearColor(255, 255, 255, 1);
		res = new Content();
		
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);

		gsm = new GameStateManager(this);
	}

	public void render() {
		accum += Gdx.graphics.getDeltaTime();
		while (accum >= STEP) {
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();
			GameInput.update();
		}
	}

	public void dispose() {

	}

	public void resize(int width, int height) {
	}

	public void pause() {
	}

	public void resume() {
	}

	public SpriteBatch getSpriteBatch() {
		return sb;
	}

	public OrthographicCamera getCamera() {
		return cam;
	}

	public OrthographicCamera getHudCamera() {
		return hudCam;
	}

}

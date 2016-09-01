package handlers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter{
	
	public boolean keyDown(int k){
		if(k == Keys.LEFT){
			GameInput.setKey(GameInput.VK_LEFT, true);
		}
		if(k == Keys.UP){
			GameInput.setKey(GameInput.VK_UP, true);
		}
		if(k == Keys.RIGHT){
			GameInput.setKey(GameInput.VK_RIGHT, true);
		}
		if(k == Keys.DOWN){
			GameInput.setKey(GameInput.VK_DOWN, true);
		}
		if(k == Keys.SPACE){
			GameInput.setKey(GameInput.VK_SPACE, true);
		}
		if(k == Keys.SHIFT_LEFT || k == Keys.SHIFT_RIGHT){
			GameInput.setKey(GameInput.VK_SHIFT, true);
		}
		return true;
	}

	public boolean keyUp(int k){
		if(k == Keys.LEFT){
			GameInput.setKey(GameInput.VK_LEFT, false);
		}
		if(k == Keys.UP){
			GameInput.setKey(GameInput.VK_UP, false);
		}
		if(k == Keys.RIGHT){
			GameInput.setKey(GameInput.VK_RIGHT, false);
		}
		if(k == Keys.DOWN){
			GameInput.setKey(GameInput.VK_DOWN, false);
		}
		if(k == Keys.SPACE){
			GameInput.setKey(GameInput.VK_SPACE, false);
		}
		if(k == Keys.SHIFT_LEFT || k == Keys.SHIFT_RIGHT){
			GameInput.setKey(GameInput.VK_SHIFT, false);
		}
		return true;
	}
}

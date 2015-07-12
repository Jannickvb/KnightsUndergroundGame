package handlers;

public class GameInput {

	public static boolean[] keys;
	public static boolean[] pkeys;
	
	public static final int NUM_KEYS = 5,
							VK_LEFT = 0,
							VK_UP = 1,
							VK_RIGHT = 2,
							VK_DOWN = 3,
							VK_SPACE = 4;
	
	static{
		keys = new boolean[NUM_KEYS];
		pkeys = new boolean[NUM_KEYS];
	}
	
	public static void update() {
		for(int i = 0; i < NUM_KEYS;i++){
			pkeys[i] = keys[i];
		}
	}
	
	public static boolean isDown(int i){
		return keys[i];
	}
	
	public static boolean isPressed(int i){
		return keys[i] && !pkeys[i];
	}
	
	public static void setKey(int i, boolean b){
		keys[i] = b;
	}
}

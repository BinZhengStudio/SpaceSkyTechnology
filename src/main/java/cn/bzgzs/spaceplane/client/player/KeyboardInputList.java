package cn.bzgzs.spaceplane.client.player;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class KeyboardInputList {
	private static final List<KeyMapping> LIST = new ArrayList<>();
	private static final String PLANE_CONTROL = "plane_control";
	public static final KeyMapping ENGINE_ON = registerUniversal("engine_on", GLFW.GLFW_KEY_V, PLANE_CONTROL);
	public static final KeyMapping LANDING_GEAR = registerUniversal("landing_gear", GLFW.GLFW_KEY_Z, PLANE_CONTROL);
	public static final KeyMapping PLANE_SPEED_UP = registerUniversal("plane_speed_up", GLFW.GLFW_KEY_SPACE, PLANE_CONTROL);
	public static final KeyMapping PLANE_LOOK_UP = registerUniversal("plane_look_up", GLFW.GLFW_KEY_W, PLANE_CONTROL);
	public static final KeyMapping PLANE_LOOK_DOWN = registerUniversal("plane_look_down", GLFW.GLFW_KEY_S, PLANE_CONTROL);
	public static final KeyMapping PLANE_LEFT_ROLL = registerUniversal("plane_left_roll", GLFW.GLFW_KEY_A, PLANE_CONTROL);
	public static final KeyMapping PLANE_RIGHT_ROLL = registerUniversal("plane_right_roll", GLFW.GLFW_KEY_D, PLANE_CONTROL);
	public static final KeyMapping PLANE_CLIMB_UP = registerUniversal("plane_climb_up", GLFW.GLFW_KEY_UP, PLANE_CONTROL);
	public static final KeyMapping PLANE_DECLINE = registerUniversal("plane_decline", GLFW.GLFW_KEY_DOWN, PLANE_CONTROL);
	public static final KeyMapping PLANE_LEFT = registerUniversal("plane_left", GLFW.GLFW_KEY_LEFT, PLANE_CONTROL);
	public static final KeyMapping PLANE_RIGHT = registerUniversal("plane_right", GLFW.GLFW_KEY_RIGHT, PLANE_CONTROL);
	public static final KeyMapping LAUNCH_MISSILE = registerUniversal("launch_missile", GLFW.GLFW_KEY_ENTER, PLANE_CONTROL);
	public static final KeyMapping INTERCEPTOR_MISSILE = registerUniversal("interceptor_missile", GLFW.GLFW_KEY_RIGHT_SHIFT, PLANE_CONTROL);
	public static final KeyMapping LAUNCH_CANNONBALL = registerUniversal("launch_cannonball", GLFW.GLFW_KEY_R, PLANE_CONTROL);
	public static final KeyMapping SEPARATE_TRACTOR = registerUniversal("separate_tractor", GLFW.GLFW_KEY_G, PLANE_CONTROL);

	public static void register() {
		for (KeyMapping keyMapping : LIST) {
			ClientRegistry.registerKeyBinding(keyMapping);
		}
	}

	private static KeyMapping registerUniversal(String name, final int keyCode, String category) {
		return register(name, KeyConflictContext.UNIVERSAL, KeyModifier.NONE, InputConstants.Type.KEYSYM, keyCode, category);
	}

	private static KeyMapping register(String name, IKeyConflictContext context, KeyModifier modifier, InputConstants.Type type, final int keyCode, String category) {
		KeyMapping keyMapping = new KeyMapping("key.spaceplane." + name, context, modifier, type, keyCode, "key.category.spaceplane." + category);
		LIST.add(keyMapping);
		return keyMapping;
	}
}

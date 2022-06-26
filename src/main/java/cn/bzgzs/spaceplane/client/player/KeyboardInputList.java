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
	public static final KeyMapping ENGINE_ON = registerInGame("engine_on", GLFW.GLFW_KEY_Z, PLANE_CONTROL);
	public static final KeyMapping LANDING_GEAR = registerInGame("landing_gear", GLFW.GLFW_KEY_X, PLANE_CONTROL);
	public static final KeyMapping CLIMB_UP = registerInGame("climb_up", GLFW.GLFW_KEY_UP, PLANE_CONTROL);
	public static final KeyMapping DECLINE = registerInGame("decline", GLFW.GLFW_KEY_DOWN, PLANE_CONTROL);
	public static final KeyMapping LEFT = registerInGame("left", GLFW.GLFW_KEY_LEFT, PLANE_CONTROL);
	public static final KeyMapping RIGHT = registerInGame("right", GLFW.GLFW_KEY_RIGHT, PLANE_CONTROL);

	public static void register() {
		for (KeyMapping keyMapping : LIST) {
			ClientRegistry.registerKeyBinding(keyMapping);
		}
	}

	private static KeyMapping registerInGame(String name, final int keyCode, String category) {
		return register(name, KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, keyCode, category);
	}

	private static KeyMapping register(String name, IKeyConflictContext context, KeyModifier modifier, InputConstants.Type type, final int keyCode, String category) {
		KeyMapping keyMapping = new KeyMapping("key.spaceplane." + name, context, modifier, type, keyCode, "key.category.spaceplane." + category);
		LIST.add(keyMapping);
		return keyMapping;
	}
}

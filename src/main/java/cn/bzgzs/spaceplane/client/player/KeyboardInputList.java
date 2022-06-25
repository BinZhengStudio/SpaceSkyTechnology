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
	public static final KeyMapping ENGINE_ON = register("engine_on", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, PLANE_CONTROL);
	public static final KeyMapping CLIMB_UP = register("climb_up", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UP, PLANE_CONTROL);
	public static final KeyMapping DECLINE = register("decline", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_DOWN, PLANE_CONTROL);
	public static final KeyMapping LEFT = register("left", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT, PLANE_CONTROL);
	public static final KeyMapping RIGHT = register("right", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT, PLANE_CONTROL);
	public static void register() {
		for (KeyMapping keyMapping : LIST) {
			ClientRegistry.registerKeyBinding(keyMapping);
		}
	}

	private static KeyMapping register(String name, IKeyConflictContext context, KeyModifier modifier, InputConstants.Type type, final int keyCode, String category) {
		KeyMapping keyMapping = new KeyMapping("key.spaceplane." + name, context, modifier, type, keyCode, "key.category.spaceplane." + category);
		LIST.add(keyMapping);
		return keyMapping;
	}
}

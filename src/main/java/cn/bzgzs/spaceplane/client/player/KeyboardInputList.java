package cn.bzgzs.spaceplane.client.player;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class KeyboardInputList {
	public static final KeyMapping ENGINE_ON = new KeyMapping("key.spaceplane.engine_on", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, "key.category.spaceplane.plane_control");
	public static final KeyMapping CLIMB_UP = new KeyMapping("key.spaceplane.climb_up", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UP, "key.category.spaceplane.plane_control");
	public static final KeyMapping DECLINE = new KeyMapping("key.spaceplane.decline", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_DOWN, "key.category.spaceplane.plane_control");
	public static final KeyMapping LEFT = new KeyMapping("key.spaceplane.left", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT, "key.category.spaceplane.plane_control");
	public static final KeyMapping RIGHT = new KeyMapping("key.spaceplane.right", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT, "key.category.spaceplane.plane_control");
	public static void register() {
		ClientRegistry.registerKeyBinding(ENGINE_ON);
		ClientRegistry.registerKeyBinding(CLIMB_UP);
		ClientRegistry.registerKeyBinding(DECLINE);
	}
}

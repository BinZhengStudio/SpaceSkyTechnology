package cn.bzgzs.spaceplane.client.player;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

public class KeyboardInputList {
	public static final KeyMapping CLIMB_UP = new KeyMapping("key.spaceplane.climb_up", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UP, "key.spaceplane.category.plane_control");

	public static void register() {
		ClientRegistry.registerKeyBinding(CLIMB_UP);
	}
}

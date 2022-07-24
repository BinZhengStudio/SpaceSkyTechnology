package cn.bzgzs.spaceplane.sounds;

import cn.bzgzs.spaceplane.SpacePlane;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundEventList {
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SpacePlane.MODID);
	public static final RegistryObject<SoundEvent> PLANE_ENGINE = SOUND_EVENTS.register("plane_engine", () -> new SoundEvent(new ResourceLocation(SpacePlane.MODID, "plane_engine")));
}

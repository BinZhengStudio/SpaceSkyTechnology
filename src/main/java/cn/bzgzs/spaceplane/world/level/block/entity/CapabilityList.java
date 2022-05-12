package cn.bzgzs.spaceplane.world.level.block.entity;

import cn.bzgzs.spaceplane.energy.IMechanicalTransmission;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapabilityList {
	public static final Capability<IMechanicalTransmission> MECHANICAL_TRANSMISSION = CapabilityManager.get(new CapabilityToken<>(){});

	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event) {
		event.register(IMechanicalTransmission.class);
	}
}

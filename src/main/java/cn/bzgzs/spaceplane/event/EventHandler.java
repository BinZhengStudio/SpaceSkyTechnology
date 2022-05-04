package cn.bzgzs.spaceplane.event;

import cn.bzgzs.spaceplane.data.worldgen.placement.OrePlacementList;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventHandler {
	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacementList.ORE_RHENIUM);
		event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacementList.ORE_NICKEL);
	}
}

package cn.bzgzs.spaceplane.data.worldgen.features;

import cn.bzgzs.spaceplane.world.level.block.BlockList;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

import java.util.List;

public class OreFeatureList {
	public static final List<OreConfiguration.TargetBlockState> ORE_RHENIUM_TARGET_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, BlockList.RHENIUM_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, BlockList.DEEPSLATE_RHENIUM_ORE.get().defaultBlockState()));
	public static final List<OreConfiguration.TargetBlockState> ORE_NICKEL_TARGET_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, BlockList.NICKEL_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, BlockList.DEEPSLATE_NICKEL_ORE.get().defaultBlockState()));

	public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_RHENIUM = FeatureUtils.register("ore_rhenium", Feature.ORE, new OreConfiguration(ORE_RHENIUM_TARGET_LIST, 3, 1.0F));
	public static final Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_NICKEL = FeatureUtils.register("ore_nickel", Feature.ORE, new OreConfiguration(ORE_NICKEL_TARGET_LIST, 4));
}

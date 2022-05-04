package cn.bzgzs.spaceplane.data.worldgen.placement;

import cn.bzgzs.spaceplane.data.worldgen.features.OreFeatureList;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class OrePlacementList {
	public static final Holder<PlacedFeature> ORE_RHENIUM = PlacementUtils.register("ore_rhenium", OreFeatureList.ORE_RHENIUM, commonOrePlacement(15, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80))));
	public static final Holder<PlacedFeature> ORE_NICKEL = PlacementUtils.register("ore_nickel", OreFeatureList.ORE_NICKEL, commonOrePlacement(50, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))));

	private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
		return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
	}

	private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
		return orePlacement(CountPlacement.of(p_195344_), p_195345_);
	}

	private static List<PlacementModifier> rareOrePlacement(int p_195350_, PlacementModifier p_195351_) {
		return orePlacement(RarityFilter.onAverageOnceEvery(p_195350_), p_195351_);
	}
}

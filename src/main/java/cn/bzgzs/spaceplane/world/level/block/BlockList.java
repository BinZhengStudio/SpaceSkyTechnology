package cn.bzgzs.spaceplane.world.level.block;

import cn.bzgzs.spaceplane.SpacePlane;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockList {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SpacePlane.MODID);
	// vvvbbbcz: 方块注册
	public static final RegistryObject<Block> RHENIUM_ORE = BLOCKS.register("rhenium_ore", () -> new OreBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
	public static final RegistryObject<Block> DEEPSLATE_RHENIUM_ORE = BLOCKS.register("deepslate_rhenium_ore", () -> new OreBlock(Block.Properties.copy(RHENIUM_ORE.get()).color(MaterialColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));
	public static final RegistryObject<Block> RHENIUM_BLOCK = BLOCKS.register("rhenium_block", () -> new Block(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
	public static final RegistryObject<Block> NICKEL_ORE = BLOCKS.register("nickel_ore", () -> new OreBlock(Block.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
	public static final RegistryObject<Block> DEEPSLATE_NICKEL_ORE = BLOCKS.register("deepslate_nickel_ore", () -> new OreBlock(Block.Properties.copy(NICKEL_ORE.get()).color(MaterialColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));
	public static final RegistryObject<Block> NICKEL_BLOCK = BLOCKS.register("nickel_block", () -> new Block(Block.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));
	public static final RegistryObject<Block> ALLOY_FURNACE = BLOCKS.register("alloy_furnace", AlloyFurnaceBlock::new);
	public static final RegistryObject<Block> FLUID_PIPE = BLOCKS.register("fluid_pipe", FluidPipeBlock::new);
}

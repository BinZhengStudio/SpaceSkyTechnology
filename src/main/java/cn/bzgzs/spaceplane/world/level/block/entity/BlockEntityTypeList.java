package cn.bzgzs.spaceplane.world.level.block.entity;

import cn.bzgzs.spaceplane.SpacePlane;
import cn.bzgzs.spaceplane.world.level.block.BlockList;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityTypeList {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, SpacePlane.MODID);

	public static final RegistryObject<BlockEntityType<AlloyFurnaceBlockEntity>> ALLOY_FURNACE = BLOCK_ENTITY_TYPE.register("alloy_furnace", () -> BlockEntityType.Builder.of(AlloyFurnaceBlockEntity::new, BlockList.ALLOY_FURNACE.get()).build(null));
	public static final RegistryObject<BlockEntityType<IronFluidPipeBlockEntity>> IRON_FLUID_PIPE = BLOCK_ENTITY_TYPE.register("iron_fluid_pipe", () -> BlockEntityType.Builder.of(IronFluidPipeBlockEntity::new, BlockList.FLUID_PIPE.get()).build(null));
}

package cn.bzgzs.spaceplane.world.entity;

import cn.bzgzs.spaceplane.SpacePlane;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeList {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, SpacePlane.MODID);

	public static final RegistryObject<EntityType<J20PlaneEntity>> J20_PLANE = ENTITY_TYPES.register("j20_plane", () -> EntityType.Builder.of(J20PlaneEntity::new, MobCategory.MISC).sized(5, 5).build("j20_plane"));
}

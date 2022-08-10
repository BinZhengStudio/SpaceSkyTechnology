package cn.bzgzs.spaceplane.world.entity;

import cn.bzgzs.spaceplane.SpacePlane;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeList {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, SpacePlane.MODID);

	public static final RegistryObject<EntityType<TestPlaneEntity>> TEST = ENTITY_TYPES.register("test", () -> EntityType.Builder.of(TestPlaneEntity::new, MobCategory.MISC).sized(4.25F, 4.25F).build("test"));
	public static final RegistryObject<EntityType<CannonballEntity>> CANNONBALL = ENTITY_TYPES.register("cannonball", () -> EntityType.Builder.<CannonballEntity>of(CannonballEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).build("cannonball"));
}

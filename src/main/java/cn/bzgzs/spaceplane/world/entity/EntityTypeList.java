package cn.bzgzs.spaceplane.world.entity;

import cn.bzgzs.spaceplane.SpacePlane;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityTypeList {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, SpacePlane.MODID);

	public static final RegistryObject<EntityType<BZ20PlaneEntity>> BZ_20_PLANE = ENTITY_TYPES.register("bz_20_plane", () -> EntityType.Builder.of(BZ20PlaneEntity::new, MobCategory.MISC).sized(5, 5).build("bz_20_plane"));
	public static final RegistryObject<EntityType<TestPlaneEntity>> TEST = ENTITY_TYPES.register("test", () -> EntityType.Builder.of(TestPlaneEntity::new, MobCategory.MISC).sized(5, 5).build("test"));
	public static final RegistryObject<EntityType<TestPlane2>> TEST2 = ENTITY_TYPES.register("test2", () -> EntityType.Builder.of(TestPlane2::new, MobCategory.MISC).sized(5, 5).build("test2"));
}

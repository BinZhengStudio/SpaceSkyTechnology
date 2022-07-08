package cn.bzgzs.spaceplane.world.item;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public enum ModItemTiers implements Tier {

    RHENIUM(5,3000,13.0F,10.0F,17,() -> {return Ingredient.of(ItemList.RHENIUM_INGOT.get());}),
    NICKEL(2,1033,7.0F,8.0F,10,() -> {return Ingredient.of(ItemList.NICKEL_INGOT.get());});

    private final int uses;
    private final float speed;
    private final float attackDamageBonus;
    private final int level;
    private final int enchantmentvalue;
    private final Supplier<Ingredient> repairIngredient;

    ModItemTiers(int level,int uses,float speed,float attackDamageBonus,int enchantmentvalue,@Nonnull Supplier<Ingredient> repairIngredient){
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.attackDamageBonus = attackDamageBonus;
        this.enchantmentvalue = enchantmentvalue;
        this.repairIngredient = repairIngredient;
    }

    public int getUses(){
        return this.uses;
    }

    public float getSpeed(){
        return this.speed;
    }

    public float getAttackDamageBonus(){
        return this.attackDamageBonus;
    }

    public int getLevel(){
        return this.level;
    }

    public int getEnchantmentValue(){
        return this.enchantmentvalue;
    }

    @Nonnull
    public Ingredient getRepairIngredient(){
        return this.repairIngredient.get();
    }

}

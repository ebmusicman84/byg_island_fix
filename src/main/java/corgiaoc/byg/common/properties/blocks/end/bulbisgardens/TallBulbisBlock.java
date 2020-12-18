package corgiaoc.byg.common.properties.blocks.end.bulbisgardens;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class TallBulbisBlock extends TallFlowerBlock {
    public TallBulbisBlock(Properties builder) {
        super(builder);
    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
            if (entityIn instanceof LivingEntity && !EnchantmentHelper.hasSoulSpeed((LivingEntity)entityIn)) {
                LivingEntity livingentity = (LivingEntity)entityIn;
                    livingentity.addPotionEffect(new EffectInstance(Effects.LEVITATION, 160, 0, false, false));
            }
    }
}

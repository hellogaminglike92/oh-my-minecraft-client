package com.plusls.ommc.mixin.feature.preventExplodingBed;

import com.plusls.ommc.config.Configs;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {
    @Inject(method = "interactBlock",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/util/ActionResult;isAccepted()Z",
                    ordinal = 0),
            cancellable = true)
    private void preventExplodingBed(ClientPlayerEntity player, ClientWorld world, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        if (!Configs.FeatureToggle.PREVENT_EXPLODING_BED.getBooleanValue()) {
            return;
        }
        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.getBlock() instanceof BedBlock && !world.getDimension().isBedWorking()) {
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}

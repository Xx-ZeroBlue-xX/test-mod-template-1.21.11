package net.zeroblue.testmod.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.zeroblue.testmod.logic.FishingController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {
    @Shadow
    private boolean caughtFish;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        FishingBobberEntity bobber = (FishingBobberEntity) (Object) this;
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player != null && bobber.getOwner() == client.player) {
            if (this.caughtFish) {
                FishingController.triggerCatch();
            }
        }
    }
}

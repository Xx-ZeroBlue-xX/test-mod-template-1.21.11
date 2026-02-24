package net.zeroblue.testmod.logic;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.Hand;
import net.zeroblue.testmod.config.ModConfig;

public class FishingController {
    public enum State {
        IDLE, FISHING, REELING, ATTACKING, RECASTING
    }

    private static State currentState = State.IDLE;
    private static int timer = 0;
    private static int attackClicksLeft = 0;
    private static int originalSlot = 0;

    private static float initialYaw = 0;
    private static float initialPitch = 0;
    private static int afkTimer = 0;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(FishingController::onTick);
    }

    private static void onTick(MinecraftClient client) {
        if (client.player == null || !ModConfig.INSTANCE.enabled) {
            currentState = State.IDLE;
            return;
        }

        if (initialYaw == 0 && initialPitch == 0) {
            initialYaw = client.player.getYaw();
            initialPitch = client.player.getPitch();
        }

        handleAntiAfk(client);

        switch (currentState) {
            case IDLE -> {
                if (client.player.getMainHandStack().getItem() instanceof net.minecraft.item.FishingRodItem) {
                    currentState = State.FISHING;
                }
            }
            case FISHING -> {
                FishingBobberEntity bobber = client.player.fishHook;
                if (bobber != null) {
                    // We'll use a mixin later to set a flag, or check velocity here
                    // For now, let's assume a Mixin will call a method to trigger reeling
                }
            }
            case REELING -> {
                client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
                currentState = State.ATTACKING;
                timer = 10; // Wait 10 ticks for mob to spawn/be catchable
                attackClicksLeft = ModConfig.INSTANCE.clickCount;
                originalSlot = client.player.getInventory().selectedSlot;
            }
            case ATTACKING -> {
                if (timer > 0) {
                    timer--;
                    return;
                }
                
                if (attackClicksLeft > 0) {
                    client.player.getInventory().selectedSlot = ModConfig.INSTANCE.weaponSlot;
                    client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
                    attackClicksLeft--;
                    timer = ModConfig.INSTANCE.clickDelay;
                } else {
                    client.player.getInventory().selectedSlot = originalSlot;
                    currentState = State.RECASTING;
                    timer = 10;
                }
            }
            case RECASTING -> {
                if (timer > 0) {
                    timer--;
                    return;
                }
                client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
                currentState = State.FISHING;
            }
        }
    }

    private static void handleAntiAfk(MinecraftClient client) {
        if (!ModConfig.INSTANCE.antiAfk) return;

        afkTimer++;
        if (afkTimer > 40) { // Every 2 seconds
            float range = ModConfig.INSTANCE.antiAfkRange;
            float dx = (float) (Math.random() * range * 2 - range);
            float dy = (float) (Math.random() * range * 2 - range);
            
            // Keep it around the initial look position
            client.player.setYaw(initialYaw + dx);
            client.player.setPitch(initialPitch + dy);
            afkTimer = 0;
        }
    }

    public static void triggerCatch() {
        if (currentState == State.FISHING) {
            currentState = State.REELING;
        }
    }
}

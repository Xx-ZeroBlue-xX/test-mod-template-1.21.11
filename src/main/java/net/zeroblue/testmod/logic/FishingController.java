package net.zeroblue.testmod.logic;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.zeroblue.testmod.config.ModConfig;

import java.util.Random;

public class FishingController {
    public enum State {
        IDLE, FISHING, REELING, ATTACKING, RECASTING
    }

    private static State currentState = State.IDLE;
    private static int timer = 0;
    private static int attackClicksLeft = 0;
    private static int originalSlot = 0;
    private static final Random random = new Random();

    private static float initialYaw = 0;
    private static float initialPitch = 0;
    private static int afkTimer = 0;
    private static int failSafeTimer = 0;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(FishingController::onTick);
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> handleChatMessage(message));
    }

    private static void handleChatMessage(Text message) {
        if (!ModConfig.INSTANCE.enabled) return;
        String text = message.getString();
        
        for (ModConfig.MobSetting mob : ModConfig.INSTANCE.mobSettings) {
            if (text.contains(mob.message)) {
                attackClicksLeft = mob.clicks;
                // If we catch a mob, we definitely want to transition to attacking
                if (currentState == State.FISHING || currentState == State.REELING) {
                    currentState = State.REELING;
                }
                return;
            }
        }
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
                if (bobber == null) {
                    failSafeTimer++;
                    if (failSafeTimer > 100) { // 5 seconds of no bobber
                        currentState = State.RECASTING;
                        timer = getRandomDelay(10, 20);
                        failSafeTimer = 0;
                    }
                } else {
                    failSafeTimer = 0;
                }
            }
            case REELING -> {
                client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
                currentState = State.ATTACKING;
                timer = getRandomDelay(15, 25); // Wait for mob to spawn
                if (attackClicksLeft == 0) attackClicksLeft = ModConfig.INSTANCE.defaultClickCount;
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
                    timer = getRandomDelay(ModConfig.INSTANCE.clickDelayMin, ModConfig.INSTANCE.clickDelayMax);
                } else {
                    client.player.getInventory().selectedSlot = originalSlot;
                    currentState = State.RECASTING;
                    timer = getRandomDelay(15, 30);
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

    private static int getRandomDelay(int min, int max) {
        if (min >= max) return min;
        return min + random.nextInt(max - min + 1);
    }

    private static void handleAntiAfk(MinecraftClient client) {
        if (!ModConfig.INSTANCE.antiAfk) return;

        afkTimer++;
        if (afkTimer > 40 + random.nextInt(40)) { // 2-4 seconds
            float range = ModConfig.INSTANCE.antiAfkRange;
            float dx = (random.nextFloat() * range * 2 - range);
            float dy = (random.nextFloat() * range * 2 - range);
            
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

package net.zeroblue.testmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.zeroblue.testmod.logic.FishingController;
import net.zeroblue.testmod.screen.FishingConfigScreen;
import org.lwjgl.glfw.GLFW;

public class TestModClient implements ClientModInitializer {
    private static KeyBinding configKey;

    @Override
    public void onInitializeClient() {
        FishingController.init();

        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.test-mod.open_config",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            "category.test-mod.fishing"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKey.wasPressed()) {
                client.setScreen(new FishingConfigScreen());
            }
        });
    }
}

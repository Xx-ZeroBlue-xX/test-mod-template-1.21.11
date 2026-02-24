package net.zeroblue.testmod.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.zeroblue.testmod.config.ModConfig;

public class FishingConfigScreen extends Screen {
    public FishingConfigScreen() {
        super(Text.literal("Auto-Fishing Config"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Toggle Enabled
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Enabled: " + ModConfig.INSTANCE.enabled),
            button -> {
                ModConfig.INSTANCE.enabled = !ModConfig.INSTANCE.enabled;
                button.setMessage(Text.literal("Enabled: " + ModConfig.INSTANCE.enabled));
            })
            .dimensions(centerX - 100, centerY - 70, 200, 20)
            .build());

        // Weapon Slot
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Weapon Slot: " + (ModConfig.INSTANCE.weaponSlot + 1)),
            button -> {
                ModConfig.INSTANCE.weaponSlot = (ModConfig.INSTANCE.weaponSlot + 1) % 9;
                button.setMessage(Text.literal("Weapon Slot: " + (ModConfig.INSTANCE.weaponSlot + 1)));
            })
            .dimensions(centerX - 100, centerY - 45, 200, 20)
            .build());

        // Mob Settings Sub-menu
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Mob Specific Settings..."),
            button -> this.client.setScreen(new MobSettingsScreen(this)))
            .dimensions(centerX - 100, centerY - 20, 200, 20)
            .build());

        // Anti-AFK
        this.addDrawableChild(ButtonWidget.builder(
            Text.literal("Anti-AFK: " + ModConfig.INSTANCE.antiAfk),
            button -> {
                ModConfig.INSTANCE.antiAfk = !ModConfig.INSTANCE.antiAfk;
                button.setMessage(Text.literal("Anti-AFK: " + ModConfig.INSTANCE.antiAfk));
            })
            .dimensions(centerX - 100, centerY + 5, 200, 20)
            .build());

        // Close
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> this.close())
            .dimensions(centerX - 100, centerY + 45, 200, 20)
            .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
    }
}

package net.zeroblue.testmod.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.zeroblue.testmod.config.ModConfig;

import java.util.ArrayList;
import java.util.List;

public class MobSettingsScreen extends Screen {
    private final Screen parent;
    private int scrollOffset = 0;
    private final List<ButtonWidget> mobButtons = new ArrayList<>();

    public MobSettingsScreen(Screen parent) {
        super(Text.literal("Mob Specific Clicks"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        refreshButtons();

        // Back button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Back"), button -> this.client.setScreen(parent))
            .dimensions(this.width / 2 - 100, this.height - 30, 200, 20)
            .build());

        // Simple scroll buttons
        this.addDrawableChild(ButtonWidget.builder(Text.literal("^"), button -> {
            if (scrollOffset > 0) { scrollOffset--; refreshButtons(); }
        })
        .dimensions(this.width / 2 + 110, 50, 20, 20)
        .build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("v"), button -> {
            if (scrollOffset < ModConfig.INSTANCE.mobSettings.size() - 5) { scrollOffset++; refreshButtons(); }
        })
        .dimensions(this.width / 2 + 110, 150, 20, 20)
        .build());
    }

    private void refreshButtons() {
        this.mobButtons.forEach(this::remove);
        this.mobButtons.clear();

        int startY = 50;
        int visibleCount = 6;
        for (int i = 0; i < visibleCount && (i + scrollOffset) < ModConfig.INSTANCE.mobSettings.size(); i++) {
            ModConfig.MobSetting mob = ModConfig.INSTANCE.mobSettings.get(i + scrollOffset);
            ButtonWidget btn = ButtonWidget.builder(
                Text.literal(mob.name + ": " + mob.clicks + " clicks"),
                button -> {
                    mob.clicks = (mob.clicks % 50) + 1;
                    button.setMessage(Text.literal(mob.name + ": " + mob.clicks + " clicks"));
                })
                .dimensions(this.width / 2 - 100, startY + (i * 25), 200, 20)
                .build();
            this.mobButtons.add(btn);
            this.addDrawableChild(btn);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
    }
}

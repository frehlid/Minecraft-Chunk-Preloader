package com.lodchunkloader;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;

public class ChunkLoaderScreen extends Screen {
    private  final Screen parent;
    private final GameOptions settngs;
    private TextFieldWidget radiusField;

    public ChunkLoaderScreen(Screen parent, GameOptions gameOptions) {
        super(Text.literal("Settings"));
        this.parent = parent;
        this.settngs = gameOptions;
    }

    protected void init()
    {
        this.addDrawableChild(new ButtonWidget.Builder(Text.literal("Toggle Movement"), btn -> ToggleMovement()).build());
        this.addDrawableChild(radiusField = new TextFieldWidget(textRenderer, 0, 50, 100, 25, Text.literal("Radius")));
        ButtonWidget bw = new ButtonWidget.Builder(Text.literal("Apply"), btn -> ApplySettings()).build();
        bw.setPosition(this.width / 4, this.height/4);
        this.addDrawableChild(bw);
    }
    private void ApplySettings()
    {
        try {
            int newRadius = Integer.parseInt(radiusField.getText());
            LODChunkLoaderClient.RADIUS = newRadius;
        }catch (NumberFormatException e) {

        }

    }
    public void ToggleMovement()
    {
        if (!LODChunkLoaderClient.shouldBeMoving) {
            LODChunkLoaderClient.shouldStartMoving = true;
        } else {
            LODChunkLoaderClient.shouldBeMoving = false;
        }

    }


}

package com.lodchunkloader;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;

public class ChunkLoaderScreen extends Screen {
    private  final Screen parent;
    private final GameOptions settngs;
    public ChunkLoaderScreen(Screen parent, GameOptions gameOptions) {
        super(Text.literal("Settings"));
        this.parent = parent;
        this.settngs = gameOptions;
    }

    protected void init()
    {
        this.addDrawableChild(new ButtonWidget.Builder(Text.literal("Toggle Movement"), btn -> ToggleMovement()).build());
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

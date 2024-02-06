package com.lodchunkloader.mixin.client;

import com.lodchunkloader.ChunkLoaderScreen;
import com.lodchunkloader.LODChunkLoader;
import com.lodchunkloader.LODChunkLoaderClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class ExampleClientMixin extends Screen {
    protected ExampleClientMixin(Text title) {
        super(title);
    }


    @Inject(at = @At("HEAD"), method = "initWidgets")
    private void initWidgets(CallbackInfo ci)
    {
        this.addDrawableChild(new ButtonWidget.Builder(Text.literal("Start Moving"),
                btn -> this.client.setScreen(new ChunkLoaderScreen(this, this.client.options))).build());

    }


}
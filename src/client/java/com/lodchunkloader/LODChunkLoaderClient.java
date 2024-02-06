package com.lodchunkloader;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;

public class LODChunkLoaderClient implements ClientModInitializer {
	int HEIGHT = 200;
	int RADIUS = 1000;

	public static boolean shouldStartMoving = false;
	public static boolean shouldBeMoving = false;

	ChunkMovement movement;

	private int tickCounter = 0;
	private final int DELAY_TICKS = 20;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register( client ->
		{
			if (client.player == null) return;


			if (shouldStartMoving)
			{
				startSpiralMovement(client);
			}

			if (shouldBeMoving)
			{
				if (tickCounter >= DELAY_TICKS)
				{
					movement.movePlayerInSpiral(client);

					if (client.player.getPos().x > RADIUS){
						shouldBeMoving = false;
					}

					tickCounter = 0;
				}
				tickCounter++;
			}
		});

		// Register the commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			// Command to start moving
			dispatcher.register(CommandManager.literal("startMoving")
					.requires(source -> source.hasPermissionLevel(2)) // Require OP level 2 permission
					.then(argument("value", IntegerArgumentType.integer())
							.executes(context -> {
								final int value = IntegerArgumentType.getInteger(context, "value");
								RADIUS = value;
								shouldStartMoving = true;
								context.getSource().sendFeedback(Text.literal("Starting spiral movement."), false);
								return 1; // Command was executed successfully
							}))
			);

			// Command to stop moving
			dispatcher.register(CommandManager.literal("stopMoving")
					.requires(source -> source.hasPermissionLevel(2)) // Require OP level 2 permission
					.executes(context -> {
						shouldBeMoving = false;
						context.getSource().sendFeedback(Text.literal("Stopping spiral movement."), false);
						return 1; // Command was executed successfully
					})
			);
		});
	}

	private void startSpiralMovement(MinecraftClient client)
	{
		shouldStartMoving = false;
		client.player.setPos(0, HEIGHT, 0);
		movement = new ChunkMovement();
		shouldBeMoving = true;
	}

	public class SpiralMovement
	{
		private double theta = 0;
		private final double a = 0;
		private final double b = 0.1;

		public void movePlayerInSpiral(MinecraftClient client)
		{
			theta += Math.PI / 16;

			double r = a + b * theta;
			double x = r * Math.cos(theta);
			double z = r * Math.sin(theta);

			client.player.setPos(x, HEIGHT, z);
		}
	}

	public class ChunkMovement {
		private int currentStep = 0; // Current step in the spiral
		private int lengthOfSide = 1; // Initial length of the side in the spiral
		private int currentSideLength = 0; // Current length of the side being traversed
		private int direction = 0; // Direction of movement: 0 = right, 1 = down, 2 = left, 3 = up
		private int[] position = {0, 0}; // Current chunk position (x, z)
		private final int CHUNK_SIZE = 16 * 8; // Size of the chunk

		public void movePlayerInSpiral(MinecraftClient client) {
			if (currentSideLength < lengthOfSide) {
				currentSideLength++;
			} else {
				currentSideLength = 1;
				if (direction == 1 || direction == 3) {
					lengthOfSide++; // Increase the side length after completing a vertical move
				}
				direction = (direction + 1) % 4; // Change direction
			}

			switch (direction) {
				case 0: // Move right
					position[0]++;
					break;
				case 1: // Move down
					position[1]++;
					break;
				case 2: // Move left
					position[0]--;
					break;
				case 3: // Move up
					position[1]--;
					break;
			}

			// Calculate the world coordinates of the center of the current chunk
			double centerX = position[0] * CHUNK_SIZE + 7.5;
			double centerZ = position[1] * CHUNK_SIZE + 7.5;

			// Teleport the player to the center of the current chunk
			client.player.setPos(centerX, HEIGHT, centerZ);

			currentStep++; // Move to the next step in the spiral
		}
	}




}
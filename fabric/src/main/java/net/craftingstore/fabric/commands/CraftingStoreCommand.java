package net.craftingstore.fabric.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.craftingstore.fabric.CraftingStoreFabric;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.craftingstore.fabric.utils.Permissions;

import java.util.concurrent.ExecutionException;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CraftingStoreCommand {

    private CraftingStoreFabric instance;
    public CraftingStoreCommand(CraftingStoreFabric instance) {

        this.instance = instance;

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    literal("craftingstore")
                            .requires(Permissions.require("craftingstore.admin"))
                            .then(
                                    literal("api")
                                            .then(argument("api-key", StringArgumentType.string())
                                                    .executes(this::setApiKey)
                                            )
                            )
                            .then(
                                    literal("reload")
                                            .executes(this::reloadConfig)
                            )
            );
        });
    }

    private int reloadConfig(CommandContext<ServerCommandSource> ctx) {
        instance.getCraftingStore().reload();
        ctx.getSource().sendMessage(Text.literal("CraftingStore is reloading..."));
        return 1;
    }


    private int setApiKey(CommandContext<ServerCommandSource> ctx) {
        String apiKey = StringArgumentType.getString(ctx, "api-key");
        instance.setApiKey(apiKey);
        instance.config.save();
        try {
            if (instance.getCraftingStore().reload().get()) {
                ctx.getSource().sendMessage(Text.literal("The new API key has been set in the config, and the plugin has been reloaded."));
            } else {
                ctx.getSource().sendMessage(Text.literal("The API key is invalid. The plugin will not work until you set a valid API key."));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
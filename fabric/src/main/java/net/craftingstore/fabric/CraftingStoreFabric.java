package net.craftingstore.fabric;

import net.craftingstore.core.CraftingStore;
import net.craftingstore.fabric.commands.CraftingStoreCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.craftingstore.fabric.utils.Config;

public class CraftingStoreFabric implements ModInitializer {

    private CraftingStore craftingStore;
    private MinecraftServer server;
    public Config config = new Config();

    @Override
    public void onInitialize() {
        this.craftingStore = new CraftingStore(new CraftingStoreFabricImpl(this));
        new CraftingStoreCommand(this);

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            this.server = server;
        });
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    public CraftingStore getCraftingStore() {
        return this.craftingStore;
    }

    public void setApiKey(String apiKey) {
        this.config.setApiKey(apiKey);
    }
}

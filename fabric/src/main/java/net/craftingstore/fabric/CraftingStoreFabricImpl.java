package net.craftingstore.fabric;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.craftingstore.core.CraftingStorePlugin;
import net.craftingstore.core.PluginConfiguration;
import net.craftingstore.core.logging.CraftingStoreLogger;
import net.craftingstore.core.logging.impl.JavaLogger;
import net.craftingstore.core.models.donation.Donation;
import net.craftingstore.fabric.utils.MultiThreading;
import net.minecraft.server.command.ServerCommandSource;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class CraftingStoreFabricImpl implements CraftingStorePlugin {

    private final CraftingStoreFabric fabricPlugin;
    private final JavaLogger logger;
    private final PluginConfiguration pluginConfiguration;

    CraftingStoreFabricImpl(CraftingStoreFabric fabricPlugin) {
        this.fabricPlugin = fabricPlugin;
        this.logger = new JavaLogger(LoggerFactory.getLogger("CraftingStore"));
        this.pluginConfiguration = new FabricPluginConfiguration(fabricPlugin);
    }

    @Override
    public boolean executeDonation(Donation donation) {
        final var id = String.format("%s/%s", donation.getPaymentId(), donation.getCommandId());

        this.getLogger().info(String.format("Donation (%s) > Package: %s, PlayerName: %s, PlayerUUID: %s",
                id, donation.getPackage().getName(), donation.getPlayer().getUsername(), donation.getPlayer().getUUID()));
        this.getLogger().info(String.format("Donation (%s) > Command: %s", id, donation.getCommand()));

        if (donation.getPlayer().isRequiredOnline()) {
            if (fabricPlugin.getServer().getPlayerManager().getPlayerList().stream().noneMatch(player -> player.getUuid().equals(donation.getPlayer().getUUID()))) {
                this.getLogger().info(String.format("Donation (%s) > Player not online!!", id));
                return false;
            }
        }

        var success = new AtomicBoolean(false);
        var finished = new AtomicBoolean(false);

        fabricPlugin.getServer().execute(() -> {
            CommandDispatcher<ServerCommandSource> dispatcher = fabricPlugin.getServer().getCommandManager().getDispatcher();
            try {
                dispatcher.execute(donation.getCommand(), fabricPlugin.getServer().getCommandSource());
                success.set(true);
            } catch (CommandSyntaxException e) {
                this.getLogger().error(String.format("Donation (%s) > Running command for Package: %s, Player: %s, Command: %s",
                        id, donation.getPackage().getName(), donation.getPlayer().getUsername(), donation.getCommand()), e);
                success.set(false);
            } finally {
                finished.set(true);
            }
        });

        while (!finished.get()) {
            Thread.onSpinWait();
        }

        this.getLogger().info(String.format("Donation (%s) > Result: %s", id, success.get()));
        return success.get();
    }

    @Override
    public CraftingStoreLogger getLogger() {
        return this.logger;
    }

    @Override
    public void registerRunnable(Runnable runnable, int i, int i1) {
        MultiThreading.schedule(runnable, i, i1, TimeUnit.SECONDS);
    }

    @Override
    public void runAsyncTask(Runnable runnable) {
        MultiThreading.runAsync(runnable);
    }

    @Override
    public String getToken() {
        return fabricPlugin.config.getApiKey();
    }

    @Override
    public PluginConfiguration getConfiguration() {
        return this.pluginConfiguration;
    }
}

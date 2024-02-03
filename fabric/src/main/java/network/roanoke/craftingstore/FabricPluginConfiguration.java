package network.roanoke.craftingstore;

import net.craftingstore.core.PluginConfiguration;

public class FabricPluginConfiguration implements PluginConfiguration {

    private CraftingStoreFabric plugin;

    FabricPluginConfiguration(CraftingStoreFabric plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "Fabric";
    }

    @Override
    public String[] getMainCommands() {
        return new String[]{"craftingstore", "cs"};
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getPlatform() {
        return "Fabric";
    }

    @Override
    public boolean isBuyCommandEnabled() {
        return false;
    }

    @Override
    public int getTimeBetweenCommands() {
        return 0;
    }

    @Override
    public String getNotEnoughBalanceMessage() {
        return "Not enough balance.";
    }

    @Override
    public boolean isUsingAlternativeApi() {
        return false;
    }
}

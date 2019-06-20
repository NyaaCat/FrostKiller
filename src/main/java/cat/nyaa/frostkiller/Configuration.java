package cat.nyaa.frostkiller;

import cat.nyaa.nyaacore.configuration.PluginConfigure;
import org.bukkit.plugin.java.JavaPlugin;

public class Configuration extends PluginConfigure {
    @Serializable
    public boolean enabled = false;

    @Serializable(name = "remove_item")
    public boolean removeItem = false;

    @Serializable(name = "remove_enchant")
    public boolean removeEnchant = false;

    @Serializable
    public String language;

    @Override
    protected JavaPlugin getPlugin() {
        return FrostKillerPlugin.plugin;
    }
}

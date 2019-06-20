package cat.nyaa.frostkiller;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class FrostKillerPlugin extends JavaPlugin {
    public static FrostKillerPlugin plugin;
    Configuration configuration;
    FrostKillerCommand command;
    FrostKillerEvent event;
    I18n i18n;

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        configuration = new Configuration();
        configuration.load();
        i18n = new I18n(configuration.language);
        command = new FrostKillerCommand(this, i18n);
        event = new FrostKillerEvent();
        Objects.requireNonNull(getCommand("frostkiller")).setExecutor(command);
        getServer().getPluginManager().registerEvents(event, this);
    }

    @Override
    public void onDisable() {
        configuration.save();
    }

    public void reload(){
        configuration.load();
        i18n.setLanguage(configuration.language);
        i18n.load();
    }
}

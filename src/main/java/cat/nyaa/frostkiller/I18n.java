package cat.nyaa.frostkiller;

import cat.nyaa.nyaacore.LanguageRepository;
import org.bukkit.plugin.java.JavaPlugin;

public class I18n extends LanguageRepository {
    private static I18n instance;
    private String language;

    I18n(String language){
        this.language = language;
        instance = this;
    }

    public static String format(String format, Object ... para) {
        return instance.getFormatted(format, para);
    }

    @Override
    protected JavaPlugin getPlugin() {
        return FrostKillerPlugin.plugin;
    }

    @Override
    protected String getLanguage() {
        return instance.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}

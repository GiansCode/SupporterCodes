package io.alerium.supportercodes;

import io.alerium.supportercodes.information.InformationStorage;
import org.bukkit.plugin.java.JavaPlugin;

public final class SupporterCodesPlugin extends JavaPlugin {

    private final InformationStorage informationStorage = new InformationStorage(this);

    @Override
    public void onEnable() {
        saveDefaultConfig();

        saveResource("hikari.properties", false);

        informationStorage.initialize();
    }

    @Override
    public void onDisable() {
        informationStorage.saveData();
    }

}

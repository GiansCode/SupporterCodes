package io.alerium.supportercodes;

import io.alerium.supportercodes.database.Connection;
import io.alerium.supportercodes.database.InformationHandler;
import io.alerium.supportercodes.database.SetupDatabase;
import io.alerium.supportercodes.factory.MenuFactory;
import io.alerium.supportercodes.listener.PlayerJoinListener;
import io.alerium.supportercodes.storage.InformationStorage;
import org.bukkit.plugin.java.JavaPlugin;

public final class SupporterCodesPlugin extends JavaPlugin {

    private final CommandHandler commandHandler = new CommandHandler(this);
    private final PlayerJoinListener joinListener = new PlayerJoinListener(this);

    private final InformationHandler informationHandler = new InformationHandler(this);

    private final InformationStorage informationStorage = new InformationStorage();
    private final MenuFactory menuFactory = new MenuFactory();
    private Connection connection;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("hikari.properties", false);

        this.connection = new Connection(this);
        informationHandler.initialize();

        new SetupDatabase(this);
        commandHandler.setup();
        menuFactory.updateMenu(this);

        new Placeholders(this).register();
        informationHandler.startUpdater();

        getServer().getPluginManager().registerEvents(joinListener, this);
    }

    @Override
    public void onDisable() {
        reloadConfig();

        informationHandler.save();
    }

    public InformationStorage getInformationStorage() {
        return this.informationStorage;
    }

    public MenuFactory getMenuFactory() {
        return this.menuFactory;
    }

    public void updateMenuFactory() {
        this.menuFactory.updateMenu(this);
    }

    public Connection getDatabaseConnection() {
        return this.connection;
    }

    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

}

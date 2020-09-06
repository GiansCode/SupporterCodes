package io.alerium.supportercodes;

import io.alerium.supportercodes.database.Connection;
import io.alerium.supportercodes.database.InformationHandler;
import io.alerium.supportercodes.database.SetupDatabase;
import io.alerium.supportercodes.listener.PlayerJoinListener;
import io.alerium.supportercodes.storage.InformationStorage;
import org.bukkit.plugin.java.JavaPlugin;

public final class SupporterCodesPlugin extends JavaPlugin {

    private final CommandHandler commandHandler = new CommandHandler(this);
    private final PlayerJoinListener joinListener = new PlayerJoinListener(this);

    private final InformationHandler informationHandler = new InformationHandler(this);

    private final InformationStorage informationStorage = new InformationStorage();
    private Connection connection;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("hikari.properties", false);

        this.connection = new Connection(this);
        informationHandler.initialize();

        new SetupDatabase(this);
        commandHandler.setup();

        informationHandler.startUpdater();

        getServer().getPluginManager().registerEvents(joinListener, this);
        new Placeholders(this).register();
    }

    @Override
    public void onDisable() {
        reloadConfig();

        informationHandler.save();
    }

    public InformationStorage getInformationStorage() {
        return this.informationStorage;
    }

    public Connection getDatabaseConnection() {
        return this.connection;
    }

    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    public InformationHandler getInformationHandler() { return this.informationHandler; }
}

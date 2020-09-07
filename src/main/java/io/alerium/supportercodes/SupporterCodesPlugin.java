package io.alerium.supportercodes;

import io.alerium.supportercodes.command.SupportCommand;
import io.alerium.supportercodes.command.SupportHandle;
import io.alerium.supportercodes.command.SupportStop;
import io.alerium.supportercodes.command.admin.PluginReload;
import io.alerium.supportercodes.information.InformationHandler;
import io.alerium.supportercodes.information.InformationStorage;
import io.alerium.supportercodes.listener.PlayerJoinListener;
import io.alerium.supportercodes.message.MessageStorage;
import me.mattstudios.mf.base.CommandBase;
import me.mattstudios.mf.base.CommandManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class SupporterCodesPlugin extends JavaPlugin {

    private final MessageStorage messageStorage = new MessageStorage();
    private final InformationStorage informationStorage = new InformationStorage(this);

    private CommandManager commandManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        saveResource("hikari.properties", false);

        messageStorage.load(this);

        informationStorage.initialize();

        commandManager = new CommandManager(this);
        registerCommands(
                commandManager,
                new SupportCommand(this),
                new SupportHandle(this),
                new SupportStop(this),
                new PluginReload(this)
        );

        registerListeners(
                new PlayerJoinListener(this)
        );
    }

    @Override
    public void onDisable() {
        reloadConfig();

        informationStorage.saveData();
    }

    public InformationHandler getInformationHandler() {
        return this.informationStorage.getInformationHandler();
    }

    public MessageStorage getMessageStorage() {
        return this.messageStorage;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    private void registerListeners(final Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands(final CommandManager manager, final CommandBase... commands) {
        Arrays.stream(commands).forEach(manager::register);
    }
}

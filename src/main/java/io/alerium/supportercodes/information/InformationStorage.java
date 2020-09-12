package io.alerium.supportercodes.information;

import io.alerium.supportercodes.Identifier;
import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.database.ConnectionProvider;
import io.alerium.supportercodes.information.wrapper.CreatorWrapper;
import io.alerium.supportercodes.information.wrapper.InformationWrapper;
import io.alerium.supportercodes.information.wrapper.SupporterWrapper;
import io.alerium.supportercodes.util.Statement;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public final class InformationStorage {

    private final InformationHandler informationHandler = new InformationHandler(this);

    private final Map<UUID, InformationWrapper> information = new HashMap<>();
    private final Set<InformationWrapper> informationRemoval = new HashSet<>();

    private final SupporterCodesPlugin plugin;
    private final long saveDelay;
    private ConnectionProvider connectionProvider;

    public InformationStorage(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;

        this.saveDelay = plugin.getConfig().getLong(Identifier.SAVE_DELAY_PATH);
    }

    public void initialize() {
        try {
            this.connectionProvider = new ConnectionProvider(plugin);

            this.connectionProvider.setupDatabase();
        } catch (final Exception ex) {
            plugin.getLogger().log(Level.WARNING, "Database connection was null, failed to initialize plugin!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        CompletableFuture.supplyAsync(() -> {

            final java.sql.Connection conn = connectionProvider.getConnection();
            final String databaseName = connectionProvider.getDatabaseName();

            if (conn == null) {
                plugin.getLogger().log(Level.WARNING, "Database connection was null, failed to save data!");
                return null;
            }

            try {
                final ResultSet creatorResult = conn.prepareStatement(
                        String.format(
                                Statement.SELECT_ALL_FROM_TABLE,
                                databaseName, Identifier.CREATOR_TABLE
                        )
                ).executeQuery();

                while (creatorResult.next()) {
                    final UUID creatorUUID = UUID.fromString(creatorResult.getString("uuid"));
                    final long supporters = creatorResult.getLong("supporters");
                    final long supportCodeUses = creatorResult.getLong("support_code_uses");

                    final OfflinePlayer player = Bukkit.getOfflinePlayer(creatorUUID);
                    final CreatorWrapper wrapper = new CreatorWrapper(player);

                    wrapper.setSupporters(supporters);
                    wrapper.setSupportCodeUses(supportCodeUses);

                    information.put(creatorUUID, wrapper);
                }

                final ResultSet supporterResult = conn.prepareStatement(
                        String.format(
                                Statement.SELECT_ALL_FROM_TABLE,
                                databaseName, Identifier.SUPPORTER_TABLE
                        )
                ).executeQuery();

                while (supporterResult.next()) {
                    final UUID supporterUUID = UUID.fromString(supporterResult.getString("uuid"));
                    final String supportedCreatorUUID = supporterResult.getString("supported_creator");
                    final long supporterSince = supporterResult.getLong("supporter_since");

                    final OfflinePlayer player = Bukkit.getOfflinePlayer(supporterUUID);
                    final SupporterWrapper wrapper = new SupporterWrapper(player);

                    wrapper.setSupportedCreatorID(supportedCreatorUUID == null ? "none" : supportedCreatorUUID);
                    wrapper.setSupporterSince(supporterSince);

                    information.put(supporterUUID, wrapper);
                }

                conn.close();
            } catch (final SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });

        initializeRunnable();
    }

    private void initializeRunnable() {
        new BukkitRunnable() {

            @Override
            public void run() {
                CompletableFuture.supplyAsync(() -> {
                    saveData();
                    return null;
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
            }
        }.runTaskTimer(plugin, saveDelay, saveDelay);
    }

    public void saveData() {
        if (connectionProvider == null) {
            plugin.getLogger().log(Level.WARNING, "Connection Provider was null, failed to save data!");
            return;
        }

        final java.sql.Connection conn = connectionProvider.getConnection();

        if (conn == null) {
            plugin.getLogger().log(Level.WARNING, "Tried saving data, but database connection was unreachable!");
            return;
        }

        try {
            for (final InformationWrapper removal : informationRemoval) {
                removal.removeData(connectionProvider);
            }

            informationRemoval.clear();

            for (final UUID identifier : information.keySet()) {
                information.get(identifier).queryData(connectionProvider);
            }

            conn.close();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }

    public InformationHandler getInformationHandler() {
        return this.informationHandler;
    }

    Set<InformationWrapper> getInformationRemoval() {
        return this.informationRemoval;
    }

    void setInformationData(final UUID identifier, final InformationWrapper object) {
        this.information.put(identifier, object);
    }

    void removeInformationData(final UUID identifier) {
        this.information.remove(identifier);
    }

    InformationWrapper getInformationData(final UUID identifier) {
        return this.information.get(identifier);
    }

    public Map<UUID, InformationWrapper> getInformation() {
        return this.information;
    }
}

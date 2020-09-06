package io.alerium.supportercodes.database;

import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.object.Creator;
import io.alerium.supportercodes.object.Supporter;
import io.alerium.supportercodes.storage.InformationStorage;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class InformationHandler {

    private final SupporterCodesPlugin plugin;
    private final Logger logger;
    private String databaseName;
    private long updateDelay;

    public InformationHandler(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    public void initialize() {
        this.databaseName = plugin.getDatabaseConnection().getDatabaseName();

        this.updateDelay = plugin.getConfig().getLong("database.updateDelay") * 20;
    }

    private Connection openConnection() {
        return plugin.getDatabaseConnection().getConnection();
    }

    private void updateCreator(final Connection connection, final Creator creator) {
        try {
            connection.prepareStatement(
                    String.format(
                            "UPDATE %s.creators SET supporters=%d, support_code_uses=%d WHERE uuid='%s';",
                            databaseName, creator.getSupporters(), creator.getSupportCodeUses(), creator.getId()
                            )
            ).executeUpdate();
        } catch (final SQLException ex) {
            logger.log(Level.WARNING, "An exception occurred while saving Creator '" + creator.getId() + "'", ex);
        }
    }

    private void removeCreator(final Connection connection, final UUID uuid) {
        try {
            connection.prepareStatement(
                    String.format(
                            "DELETE FROM %s.creators WHERE uuid='%s';",
                            databaseName, uuid
                    )
            ).executeUpdate();
        } catch (final SQLException ex) {
            logger.log(Level.WARNING, "An exception occurred while removing Creator '" + uuid + "'", ex);
        }
    }

    public void setSupporter(final Supporter supporter) {
        final Connection connection = plugin.getDatabaseConnection().getConnection();
        if (connection == null) {
            logger.log(Level.WARNING, "Database connection was null while trying to set Supporter '" + supporter.getId() + "'");
            return;
        }

        CompletableFuture.supplyAsync(() -> {
            try {
                connection.prepareStatement(
                        String.format(
                                "INSERT INTO %s.supporters (uuid) VALUES ('%s');",
                                databaseName, supporter.getId()
                        )
                ).executeUpdate();
            } catch (final SQLException ex) {
                logger.log(Level.WARNING, "An exception occurred while setting Supporter '" + supporter.getId() + "'", ex);
            }
            return null;
        }).exceptionally(ex -> {
            logger.log(Level.WARNING, "An exception occurred while setting Supporter '" + supporter.getId() + "'", ex);
            return null;
        });
    }

    private void updateSupporter(final Connection connection, final Supporter supporter) {
        final Creator supportedCreator = plugin.getInformationStorage().getCreator(UUID.fromString(supporter.getSupporting()));
        try {
            connection.prepareStatement(
                    String.format(
                            "UPDATE %s.supporters SET supported_creator='%s', supporter_since=%d WHERE uuid='%s';",
                            databaseName, supportedCreator == null ? null : supportedCreator.getId(), supporter.getSupportingSince(), supporter.getId()
                    )
            ).executeUpdate();

            connection.prepareStatement(
                    String.format(
                            "UPDATE %s.creators SET supporters=%d WHERE uuid='%s';",
                            databaseName, supportedCreator == null ? null : supportedCreator.getSupporters(), supportedCreator == null ? null : supportedCreator.getId()
                    )
            ).executeUpdate();
        } catch (final SQLException ex) {
            logger.log(Level.WARNING, "An exception occurred while updating Supporter '" + supporter.getId() + "'", ex);
        }
    }

    public void startUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                save();
            }
        }.runTaskTimer(plugin, updateDelay, updateDelay);
    }

    public void save() {
        CompletableFuture.supplyAsync(() -> {
            try {
                final Connection connection = openConnection();

                final InformationStorage storage = plugin.getInformationStorage();
                final Iterator<UUID> removalIterator = storage.getRemoval().iterator();
                while (removalIterator.hasNext()) {
                    final UUID removable = removalIterator.next();

                    removeCreator(connection, removable);
                }

                storage.getRemoval().clear();

                for (final UUID uuid : storage.getCreators().keySet()) {
                    updateCreator(connection, storage.getCreator(uuid));
                }

                for (final UUID uuid : storage.getSupporters().keySet()) {
                    updateSupporter(connection, storage.getSupporter(uuid));
                }

                connection.close();
            } catch (final SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }).exceptionally(ex -> {
            plugin.getLogger().log(Level.WARNING, "An exception occurred while saving data!", ex);
            return null;
        });
    }

}

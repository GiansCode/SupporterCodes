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

public final class InformationHandler {

    private final SupporterCodesPlugin plugin;
    private String databaseName;
    private long updateDelay;

    public InformationHandler(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        this.databaseName = plugin.getDatabaseConnection().getDatabaseName();

        this.updateDelay = plugin.getConfig().getLong("database.updateDelay") * 20;
    }

    private Connection openConnection() {
        return plugin.getDatabaseConnection().getConnection();
    }

    private void setCreator(final Connection connection, final Creator creator) {
        try {
            connection.prepareStatement(
                    String.format(
                            "INSERT INTO %s.creators (uuid, supporters, support_code_uses) VALUES ('%s', %d, %d);",
                            databaseName, creator.getId(), creator.getSupporters(), creator.getSupportCodeUses()
                    )
            ).executeUpdate();
        } catch (final SQLException ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
        }
    }

    private void setSupporter(final Connection connection, final Supporter supporter) {
        final Creator supportedCreator = plugin.getInformationStorage().getCreator(supporter.getSupporting());

        try {
            connection.prepareStatement(
                    String.format(
                            "INSERT INTO %s.supporters (uuid, supported_creator, supporter_since) VALUES ('%s', '%s', %d);",
                            databaseName, supporter.getId(), supportedCreator.getId(), supporter.getSupportingSince()
                    )
            ).executeUpdate();

            connection.prepareStatement(
                    String.format(
                            "UPDATE %s.supporters SET supported_creator='%s', supporter_since=%d WHERE uuid='%s';",
                            databaseName, supportedCreator.getId(), supporter.getSupportingSince(), supporter.getId()
                    )
            ).executeUpdate();

            connection.prepareStatement(
                    String.format(
                            "UPDATE %s.creators SET supporters=%d WHERE uuid='%s';",
                            databaseName, supportedCreator.getSupporters(), supportedCreator.getId()
                    )
            ).executeUpdate();
        } catch (final SQLException ex) {
            ex.printStackTrace();
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
            final Connection connection = openConnection();

            final InformationStorage storage = plugin.getInformationStorage();
            final Iterator<UUID> removalIterator = storage.getRemoval().iterator();
            while (removalIterator.hasNext()) {
                final UUID removable = removalIterator.next();

                removeCreator(connection, removable);
            }

            for (final UUID uuid : storage.getCreators().keySet()) {
                setCreator(connection, storage.getCreator(uuid));
            }

            for (final UUID uuid : storage.getSupporters().keySet()) {
                setSupporter(connection, storage.getSupporter(uuid));
            }

            try {
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

package io.alerium.supportercodes.database;

import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.object.Creator;
import io.alerium.supportercodes.object.Supporter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SetupDatabase {

    private final SupporterCodesPlugin plugin;
    private final Logger logger;
    private final String databaseName;
    private final List<UUID> creators;

    public SetupDatabase(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.creators = getCreators();
        this.databaseName = plugin.getDatabaseConnection().getDatabaseName();

        if (this.databaseName == null) {
            plugin.getLogger().log(Level.WARNING, "Failed to setup database, as it's name is null!");
            return;
        }

        CompletableFuture.supplyAsync(() -> {
            createDatabaseAndTables();
            loadCreators();
            loadSupporters();
            return null;
        }).exceptionally(ex -> {
            logger.log(Level.WARNING, "An exception occurred while setting-up the database!", ex);
            return null;
        });
    }

    private Connection openConnection() {
        return plugin.getDatabaseConnection().getConnection();
    }

    private void createDatabaseAndTables() {
        final Connection connection = openConnection();

        try {
            /*
            -- -----------------------------------------------------
            -- Database `databaseName`
            -- -----------------------------------------------------
             */
            connection.prepareStatement(
                    "CREATE DATABASE IF NOT EXISTS `" + databaseName + "`;"
            ).executeUpdate();

            /*
            -- -----------------------------------------------------
            -- Table `databaseName`.`creators`
            -- -----------------------------------------------------
             */
            connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `" + databaseName + "`.`creators` ( "
                            + "`uuid` VARCHAR(45) NOT NULL, "
                            + "`supporters` LONG NULL, "
                            + "`support_code_uses` LONG NULL, "
                            + "PRIMARY KEY (`uuid`)) "
                            + "ENGINE = InnoDB;"
            ).executeUpdate();

            /*
            -- -----------------------------------------------------
            -- Table `databaseName`.`supporters`
            -- -----------------------------------------------------
             */
            connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `" + databaseName + "`.`supporters` ( "
                            + "`uuid` VARCHAR(45) NOT NULL, "
                            + "`supported_creator` VARCHAR(45) NULL, "
                            + "`supporter_since` LONG NULL, "
                            + "PRIMARY KEY (`uuid`)) "
                            + "ENGINE = InnoDB;"
            ).executeUpdate();

            connection.close();
        } catch (final SQLException ex) {
            logger.log(Level.WARNING, "An exception occurred while creating tables!", ex);
        }
    }

    private void loadCreators() {
        final Connection connection = openConnection();

        try {
            final ResultSet resultSet = connection.prepareStatement(
                    "SELECT * FROM creators"
            ).executeQuery();

            while (resultSet.next()) {
                final UUID creatorUUID = UUID.fromString(resultSet.getString("uuid"));
                if (!creators.contains(creatorUUID)) {
                    plugin.getInformationStorage().getRemoval().add(creatorUUID);
                    continue;
                }

                final long supporters = resultSet.getLong("supporters");
                final long supportCodeUses = resultSet.getLong("support_code_uses");

                final Creator creator = new Creator(creatorUUID, supporters, supportCodeUses);
                plugin.getInformationStorage().setCreator(creatorUUID, creator);
            }

            connection.close();
        } catch (final SQLException ex) {
            logger.log(Level.WARNING, "An exception occurred while loading creators!", ex);
        }
    }

    private void loadSupporters() {
        final Connection connection = openConnection();

        try {
            final ResultSet resultSet = connection.prepareStatement(
                    "SELECT * FROM supporters"
            ).executeQuery();

            while (resultSet.next()) {
                final UUID supporterUUID = UUID.fromString(resultSet.getString("uuid"));
                final UUID supportedCreatorUUID = UUID.fromString(resultSet.getString("supported_creator"));
                final Long supporterSince = resultSet.getLong("supporter_since");

                final Supporter supporter = new Supporter(supporterUUID);
                supporter.setSupporting(supportedCreatorUUID);
                supporter.setSupporterSince(supporterSince);

                plugin.getInformationStorage().setSupporter(supporterUUID, supporter);
            }

            connection.close();
        } catch (final SQLException ex) {
            logger.log(Level.WARNING, "An exception occurred while loading supporters!", ex);
        }
    }

    private List<UUID> getCreators() {
        final List<String> input = plugin.getConfig().getStringList("creators");
        final List<UUID> result = new ArrayList<>();

        for (final String creatorString : input) {
            final String[] infoString = creatorString.split(";");
            final UUID uuid = UUID.fromString(infoString[0]);

            result.add(uuid);
        }

        return result;
    }
}

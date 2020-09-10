package io.alerium.supportercodes.database;

import com.zaxxer.hikari.HikariDataSource;
import io.alerium.supportercodes.SupporterCodesPlugin;
import io.alerium.supportercodes.util.Statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public final class ConnectionProvider extends DatabaseFactory {

    private final HikariDataSource dataSource;
    private final SupporterCodesPlugin plugin;

    public ConnectionProvider(final SupporterCodesPlugin plugin) {
        this.dataSource = configureDataSource(plugin);
        this.plugin = plugin;
    }

    @Override
    public java.sql.Connection getConnection() {
        java.sql.Connection connection = null;

        try {
            connection = dataSource.getConnection();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }

        return connection;
    }

    @Override
    public String getDatabaseName() {
        final Properties properties = readPropertiesFile(plugin.getDataFolder() + "/hikari.properties");
        return properties.getProperty("dataSource.databaseName");
    }

    public void setupDatabase() {
        final Connection connection = getConnection();

        try {
            /*
            -- -----------------------------------------------------
            -- Database `databaseName`
            -- -----------------------------------------------------
             */
            connection.prepareStatement(
                    String.format(
                            Statement.SETUP_DATABASE,
                            getDatabaseName()
                    )
            ).executeUpdate();

            /*
            -- -----------------------------------------------------
            -- Table `databaseName`.`creators`
            -- -----------------------------------------------------
             */
            connection.prepareStatement(
                    String.format(
                            Statement.SETUP_CREATOR_TABLE,
                            getDatabaseName()
                    )
            ).executeUpdate();

            /*
            -- -----------------------------------------------------
            -- Table `databaseName`.`supporters`
            -- -----------------------------------------------------
             */
            connection.prepareStatement(
                    String.format(
                            Statement.SETUP_SUPPORTER_TABLE,
                            getDatabaseName()
                    )
            ).executeUpdate();

            connection.close();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }
}

package io.alerium.supportercodes.database;

import com.zaxxer.hikari.HikariDataSource;
import io.alerium.supportercodes.SupporterCodesPlugin;

import java.sql.SQLException;
import java.util.Properties;

public final class Connection extends DatabaseFactory {

    private final HikariDataSource dataSource;
    private final SupporterCodesPlugin plugin;

    public Connection(final SupporterCodesPlugin plugin) {
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

        if (connection == null) {
            throw new RuntimeException("Database connection is null!");
        }

        return connection;
    }

    @Override
    public String getDatabaseName() {
        final Properties properties = readPropertiesFile(plugin.getDataFolder() + "/hikari.properties");
        return properties.getProperty("dataSource.databaseName");
    }
}

package io.alerium.supportercodes.database;

import io.alerium.supportercodes.SupporterCodesPlugin;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public abstract class DatabaseFactory {

    public abstract Connection getConnection() throws SQLException;

    HikariDataSource configureDataSource(final SupporterCodesPlugin plugin) {
        final HikariConfig config = new HikariConfig(
            plugin.getDataFolder() + "/hikari.properties"
        );
        return new HikariDataSource(config);
    }

    Properties readPropertiesFile(final String fileName) {
        FileInputStream inputStream = null;
        Properties properties;

        try {
            try {
                inputStream = new FileInputStream(fileName);
            } catch (final FileNotFoundException ex) {
                ex.printStackTrace();
            }
            properties = new Properties();
            try {
                properties.load(inputStream);
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                inputStream.close();
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }

        return properties;
    }
}

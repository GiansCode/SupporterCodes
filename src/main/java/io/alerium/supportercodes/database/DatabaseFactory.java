package io.alerium.supportercodes.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.alerium.supportercodes.SupporterCodesPlugin;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

public abstract class DatabaseFactory {

    public abstract Connection getConnection();

    public abstract String getDatabaseName();

    HikariDataSource configureDataSource(final SupporterCodesPlugin plugin) {
        final HikariConfig config = new HikariConfig(
                plugin.getDataFolder() + "/hikari.properties"
        );

        return new HikariDataSource(config);
    }

    Properties readPropertiesFile(final String fileName) {
        FileInputStream inputStream = null;
        final Properties properties = new Properties();

        try {
            try {
                inputStream = new FileInputStream(fileName);
                properties.load(inputStream);
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }

        return properties;
    }

}

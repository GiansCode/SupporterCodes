package io.alerium.supportercodes.command.sub;

import io.alerium.supportercodes.SupporterCodesPlugin;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@Command("db-info")
public final class DBInfo extends CommandBase {

    private final SupporterCodesPlugin plugin;

    public DBInfo(final SupporterCodesPlugin plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onCommand(final CommandSender sender) {
        final Connection connection = plugin.getDatabaseConnection().getConnection();
        final String databaseName = plugin.getDatabaseConnection().getDatabaseName();
        if (connection == null) {
            return;
        }

        try {
            final ResultSet supportersResult = connection.prepareStatement(
                    "SELECT * FROM " + databaseName + ".supporters;"
            ).executeQuery();

            while (supportersResult.next()) {
                System.out.println("Supporter: " + supportersResult.getString("uuid"));
            }

            final ResultSet creatorsResult = connection.prepareStatement(
                    "SELECT * FROM " + databaseName + ".creators;"
            ).executeQuery();

            while (creatorsResult.next()) {
                System.out.println("Creator: " + creatorsResult.getString("uuid"));
            }

            connection.close();
        } catch (final SQLException ex) {
            ex.printStackTrace();
        }
    }
}

package io.alerium.supportercodes.information.wrapper;

import io.alerium.supportercodes.Identifier;
import io.alerium.supportercodes.database.ConnectionProvider;
import io.alerium.supportercodes.util.Statement;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.util.UUID;

public final class SupporterWrapper implements InformationWrapper {

    private final UUID userID;

    private String supportedCreatorID = "none";
    private long supporterSince = 0;

    public SupporterWrapper(final OfflinePlayer player) {
        this.userID = player.getUniqueId();
    }

    public String getSupportedCreatorID() {
        return this.supportedCreatorID;
    }

    public void setSupportedCreatorID(final String value) {
        this.supportedCreatorID = value;
    }

    public long getSupporterSince() {
        return this.supporterSince;
    }

    public void setSupporterSince(final long value) {
        this.supporterSince = value;
    }

    @Override
    public UUID getUserID() {
        return userID;
    }

    @Override
    public void queryData(final ConnectionProvider connectionProvider) throws SQLException {
        final java.sql.Connection conn = connectionProvider.getConnection();

        conn.prepareStatement(
                String.format(
                        Statement.UPDATE_SUPPORTER_UUID_DATA,
                        connectionProvider.getDatabaseName(), Identifier.SUPPORTER_TABLE, userID,
                        supportedCreatorID.equalsIgnoreCase("none") ? null : supportedCreatorID, supporterSince,
                        supportedCreatorID, supporterSince
                )
        ).executeUpdate();

        conn.close();
    }

    @Override
    public void removeData(final ConnectionProvider connectionProvider) throws SQLException {
        final java.sql.Connection conn = connectionProvider.getConnection();

        conn.prepareStatement(
                String.format(
                        Statement.DELETE_UUID_DATA,
                        connectionProvider.getDatabaseName(), Identifier.SUPPORTER_TABLE, userID
                )
        ).executeUpdate();

        conn.close();
    }

}

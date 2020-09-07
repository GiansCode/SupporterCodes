package io.alerium.supportercodes.information.wrapper;

import io.alerium.supportercodes.Identifier;
import io.alerium.supportercodes.database.Connection;
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

    public void setSupportedCreatorID(final String value) {
        this.supportedCreatorID = value;
    }

    public void setSupporterSince(final long value) {
        this.supporterSince = value;
    }

    public String getSupportedCreatorID() {
        return this.supportedCreatorID;
    }

    public long getSupporterSince() {
        return this.supporterSince;
    }

    @Override
    public UUID getUserID() {
        return userID;
    }

    @Override
    public void queryData(final Connection connection) throws SQLException {
        final java.sql.Connection conn = connection.getConnection();

        conn.prepareStatement(
                String.format(
                        "INSERT INTO %s.%s (uuid, supported_creator, supporter_since) VALUES (" +
                                "'%s', '%s', %d" +
                        ") ON DUPLICATE KEY UPDATE supported_creator='%s', supporter_since=%d;",
                        connection.getDatabaseName(), Identifier.SUPPORTER_TABLE, userID, supportedCreatorID, supporterSince,
                        supportedCreatorID, supporterSince
                )
        ).executeUpdate();
    }

    @Override
    public void removeData(final Connection connection) throws SQLException {
        final java.sql.Connection conn = connection.getConnection();

        conn.prepareStatement(
                String.format(
                        "DELETE FROM %s.%s WHERE uuid='%s';",
                        connection.getDatabaseName(), Identifier.SUPPORTER_TABLE, userID
                )
        ).executeUpdate();
    }

}

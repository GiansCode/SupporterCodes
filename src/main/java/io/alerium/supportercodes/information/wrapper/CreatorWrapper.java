package io.alerium.supportercodes.information.wrapper;

import io.alerium.supportercodes.Identifier;
import io.alerium.supportercodes.database.ConnectionProvider;
import io.alerium.supportercodes.util.Statement;
import org.bukkit.OfflinePlayer;

import java.sql.SQLException;
import java.util.UUID;

public final class CreatorWrapper implements InformationWrapper {

    private final UUID userID;

    private long supporters = 0;
    private long supportCodeUses = 0;

    public CreatorWrapper(final OfflinePlayer player) {
        this.userID = player.getUniqueId();
    }

    public long getSupporters() {
        return this.supporters;
    }

    public void setSupporters(final long value) {
        this.supporters = this.supporters + value;
    }

    public long getSupportCodeUses() {
        return this.supportCodeUses;
    }

    public void setSupportCodeUses(final long value) {
        this.supportCodeUses = this.supportCodeUses + value;
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
                        Statement.UPDATE_CREATOR_UUID_DATA,
                        connectionProvider.getDatabaseName(), Identifier.CREATOR_TABLE, userID, supporters, supportCodeUses,
                        supporters, supportCodeUses
                )
        ).executeUpdate();
    }

    @Override
    public void removeData(final ConnectionProvider connectionProvider) throws SQLException {
        final java.sql.Connection conn = connectionProvider.getConnection();

        conn.prepareStatement(
                String.format(
                        Statement.DELETE_UUID_DATA,
                        connectionProvider.getDatabaseName(), Identifier.CREATOR_TABLE, userID
                )
        ).executeUpdate();
    }

}

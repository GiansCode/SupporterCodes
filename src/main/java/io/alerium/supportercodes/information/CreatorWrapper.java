package io.alerium.supportercodes.information;

import io.alerium.supportercodes.Identifier;
import io.alerium.supportercodes.database.Connection;
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

    public void setSupporters(final long value) {
        this.supporters = this.supporters + value;
    }

    public void setSupportCodeUses(final long value) {
        this.supportCodeUses = this.supportCodeUses + value;
    }

    public long getSupporters() {
        return this.supporters;
    }

    public long getSupportCodeUses() {
        return this.supportCodeUses;
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
                        "INSERT INTO %s.%s (uuid, supporters, support_code_uses) VALUES (" +
                                "'%s', %d, %d" +
                        ") ON DUPLICATE KEY UPDATE supporters=%d, support_code_uses=%d;",
                        connection.getDatabaseName(), Identifier.CREATOR_TABLE, userID, supporters, supportCodeUses,
                        supporters, supportCodeUses
                )
        ).executeUpdate();
    }

}

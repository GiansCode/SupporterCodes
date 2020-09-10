package io.alerium.supportercodes.information.wrapper;

import io.alerium.supportercodes.database.ConnectionProvider;

import java.sql.SQLException;
import java.util.UUID;

public interface InformationWrapper {

    UUID getUserID();

    void queryData(final ConnectionProvider connectionProvider) throws SQLException;

    void removeData(final ConnectionProvider connectionProvider) throws SQLException;
}

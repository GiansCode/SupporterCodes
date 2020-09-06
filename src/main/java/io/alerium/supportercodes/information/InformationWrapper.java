package io.alerium.supportercodes.information;

import io.alerium.supportercodes.database.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public interface InformationWrapper {

    UUID getUserID();

    void queryData(final Connection connection) throws SQLException;

}

package com.utfpr.distributed.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDataAccess {

    private static final Connection con = DBConnection.getConnection();
    private final String table;

    protected BaseDataAccess(String table) {

        this.table = table;
    }

    public ResultSet read(String id) {

        final String QUERY = "SELECT * FROM " + this.table + " WHERE id = ?;";

        try (PreparedStatement pst = con.prepareStatement(QUERY)) {

            pst.setString(1, id);

            return pst.executeQuery();

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }

    public static boolean isResultSetEmpty(ResultSet rs) throws SQLException {
        return (!rs.isBeforeFirst() && rs.getRow() == 0);
    }

    public static Connection getCon() {

        return con;
    }

    public String getTable() {

        return this.table;
    }
}

package com.utfpr.distributed.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/distributed";
    private static final String USER = "nauder";
    private static final String PASSWORD = "505450";
    private static Connection con;

    public static void connect() {
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close() {
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseMetaData getMetadata() {

        DatabaseMetaData dbmd;

        try {
            dbmd = con.getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dbmd;
    }

    public static List<String> getColumns(String table) {

        final List<String> columns = new ArrayList<>();

        try {

            final ResultSet rs = getMetadata().getColumns(null, null, table, null);

            while (rs.next()) {
                columns.add(rs.getString("COLUMN_NAME"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return columns;
    }

    public static Connection getConnection() {
        return con;
    }
}

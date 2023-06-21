package com.utfpr.distributed.database;

import com.utfpr.distributed.model.User;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class UserDataAccess extends BaseDataAccess {

    public UserDataAccess() {
        super("user");
    }

    public static void create(JSONObject user) {

        final String QUERY = "INSERT INTO public.user (nome, email, senha) VALUES (?, ?, ?);";

        try (PreparedStatement pst = getCon().prepareStatement(QUERY)) {

            pst.setString(1, (String) user.query("/nome"));
            pst.setString(2, (String) user.query("/email"));
            pst.setString(3, (String) user.query("/senha"));
            pst.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static void update(JSONObject user) {

        final String QUERY = "UPDATE public.user SET nome = ?, email = ?, senha = ? WHERE id_user = ?;";

        try (PreparedStatement pst = getCon().prepareStatement(QUERY)) {

            pst.setString(1, (String) user.query("/nome"));
            pst.setString(2, (String) user.query("/email"));
            pst.setString(3, (String) user.query("/senha"));
            pst.setInt(4, (Integer) user.query("/id"));
            pst.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }

    }

    public static User getLogin(String email, String senha) {

        final String QUERY = "SELECT * FROM public.user WHERE email = ? AND senha = ?";

        try (PreparedStatement pst = getCon().prepareStatement(QUERY)) {

            pst.setString(1, email);
            pst.setString(2, senha);

            final ResultSet rs = pst.executeQuery();

            if (isResultSetEmpty(rs)) {

                return null;
            } else {

                while (rs.next()) {
                    return new User(
                            rs.getInt("id_user"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha"));
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }

    public static boolean findEmail(String email) {

        final String QUERY = "SELECT EXISTS (SELECT 1 FROM public.user WHERE email = ?);";

        try (PreparedStatement pst = getCon().prepareStatement(QUERY)) {

            pst.setString(1, email);

            final ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                return rs.getBoolean("exists");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;

    }

    public static boolean findEmailBesidesCurrent(String email, Integer id) {

        final String QUERY = "SELECT EXISTS (SELECT 1 FROM public.user WHERE email = ? AND id_user <> ?);";

        try (PreparedStatement pst = getCon().prepareStatement(QUERY)) {

            pst.setString(1, email);
            pst.setInt(2, id);

            final ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                return rs.getBoolean("exists");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;

    }

    public static boolean validatePassword(Integer id, String senha) {

        final String QUERY = "SELECT EXISTS (SELECT 1 FROM public.user WHERE id_user = ? AND senha = ?);";

        try (PreparedStatement pst = getCon().prepareStatement(QUERY)) {

            pst.setInt(1, id);
            pst.setString(2, senha);

            final ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                return rs.getBoolean("exists");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return false;

    }

    public static void delete(Integer id) {

        final String QUERY = "DELETE FROM public.user WHERE id_user = ?;";

        try (PreparedStatement pst = getCon().prepareStatement(QUERY)) {

            pst.setInt(1, id);

            pst.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public static User getById(Integer id) {

        final String QUERY = "SELECT * FROM public.user WHERE id_user = ?;";

        try (PreparedStatement pst = getCon().prepareStatement(QUERY)) {

            pst.setInt(1, id);

            final ResultSet rs = pst.executeQuery();

            if (isResultSetEmpty(rs)) {

                return null;
            } else {

                while (rs.next()) {
                    return new User(
                            rs.getInt("id_user"),
                            rs.getString("nome"),
                            rs.getString("email"),
                            rs.getString("senha")
                    );
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }
}

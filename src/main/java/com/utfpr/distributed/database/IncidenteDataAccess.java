package com.utfpr.distributed.database;

import com.utfpr.distributed.model.Incidente;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class IncidenteDataAccess extends BaseDataAccess {

    public IncidenteDataAccess() {
        super("incidente");
    }

    public static boolean create(JSONObject incidente) {

        boolean success = true;
        final String QUERY =
                "INSERT INTO public.incidente (id_user, data, hora, estado, cidade, bairro, rua, tipo_incidente) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement pst = getCon().prepareStatement(QUERY)) {

            pst.setInt(1, (Integer) incidente.query("/id"));
            pst.setString(2, (String) incidente.query("/data"));
            pst.setString(3, (String) incidente.query("/hora"));
            pst.setString(4, (String) incidente.query("/estado"));
            pst.setString(5, (String) incidente.query("/cidade"));
            pst.setString(6, (String) incidente.query("/bairro"));
            pst.setString(7, (String) incidente.query("/rua"));
            pst.setInt(8, (Integer) incidente.query("/tipo_incidente"));
            pst.execute();

        } catch (SQLException e) {

            e.printStackTrace();
            success = false;
        }

        return success;
    }

    public static List<Incidente> get(String data, String estado, String cidade) {

        final List<Incidente> incidentes = new ArrayList<>();
        final String QUERY =
                "SELECT * FROM public.incidente i " +
                "WHERE i.data = ? " +
                "AND i.estado = ? " +
                "AND i.cidade = ? " +
                "ORDER BY data DESC;";

        try (PreparedStatement pst = getCon().prepareStatement(QUERY)) {

            pst.setString(1, data);
            pst.setString(2, estado);
            pst.setString(3, cidade);

            final ResultSet rs = pst.executeQuery();

            if (isResultSetEmpty(rs)) {

                return incidentes;
            } else {

                while(rs.next()) {
                    incidentes.add(new Incidente(
                            rs.getInt("id_incidente"),
                            rs.getString("data"),
                            rs.getString("hora"),
                            rs.getString("estado").trim(),
                            rs.getString("cidade").trim(),
                            rs.getString("bairro").trim(),
                            rs.getString("rua").trim(),
                            rs.getInt("tipo_incidente")
                    ));
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return incidentes;
    }

    public static List<Incidente> getByUser(Integer id) {

        final List<Incidente> incidentes = new ArrayList<>();
        final String QUERY =
                "SELECT * FROM public.incidente i " +
                "WHERE i.id_user = ? " +
                "ORDER BY data DESC;";

        try (PreparedStatement pst = getCon().prepareStatement(QUERY)) {

            pst.setInt(1, id);

            final ResultSet rs = pst.executeQuery();

            if (isResultSetEmpty(rs)) {

                return incidentes;
            } else {

                while(rs.next()) {
                    incidentes.add(new Incidente(
                            rs.getInt("id_incidente"),
                            rs.getString("data"),
                            rs.getString("hora"),
                            rs.getString("estado").trim(),
                            rs.getString("cidade").trim(),
                            rs.getString("bairro").trim(),
                            rs.getString("rua").trim(),
                            rs.getInt("tipo_incidente")
                    ));
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return incidentes;
    }

    public static void delete(Integer id) {

        final String QUERY = "DELETE FROM public.incidente WHERE id_incidente = ?;";

        try (PreparedStatement pst = getCon().prepareStatement(QUERY)) {

            pst.setInt(1, id);

            pst.execute();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}

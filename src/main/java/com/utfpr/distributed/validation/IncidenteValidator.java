package com.utfpr.distributed.validation;

import org.json.JSONObject;

public interface IncidenteValidator {

    static boolean checkUploadFields(JSONObject json) {

        return json != null && json.has("data") && json.has("hora") && json.has("estado")
                && json.has("cidade") && json.has("bairro") && json.has("rua")
                && json.has("tipo_incidente") && json.has("token") && json.has("id");
    }

    static boolean checkGetFields(JSONObject json) {

        return json != null && json.has("data") && json.has("estado") && json.has("cidade");
    }

    static boolean checkData(String data) {

        return data != null && data.matches("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$");
    }

    static boolean checkHora(String hora) {

        return hora != null && hora.matches("^([01][0-9]|2[0-3]):([0-5][0-9])$");
    }

    static boolean checkEstado(String estado) {

        return estado != null && estado.length() == 2 && estado.toUpperCase().equals(estado);
    }

    static boolean checkLocal(String local) {

        return local != null && !local.isEmpty() && local.length() <= 50 && local.toUpperCase().equals(local);
    }
}

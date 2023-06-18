package com.utfpr.distributed.service;

import com.utfpr.distributed.database.IncidenteDataAccess;
import com.utfpr.distributed.util.ClientSession;
import com.utfpr.distributed.util.token.TokenManager;
import com.utfpr.distributed.validation.IncidenteValidator;
import org.json.JSONObject;

import java.util.Map;

public final class IncidenteService {

    private static final String[] KEYS = { "operacao", "status" };

    public static String requestCreate(Map<String, Object> input) {
        final JSONObject requestObject = new JSONObject();

        requestObject.put(KEYS[0], input.get("operacao"));
        requestObject.put("data", input.get("data"));
        requestObject.put("hora", input.get("hora"));
        requestObject.put("estado", input.get("estado"));
        requestObject.put("bairro", input.get("bairro"));
        requestObject.put("rua", input.get("rua"));
        requestObject.put("cidade", input.get("cidade"));
        requestObject.put("tipo_incidente", input.get("tipo"));
        requestObject.put("id", ClientSession.getId());
        requestObject.put("token", ClientSession.getToken());

        return requestObject.toString();
    }

    public static String requestGet(Map<String, Object> input) {
        final JSONObject requestObject = new JSONObject();

        requestObject.put(KEYS[0], input.get("operacao"));
        requestObject.put("data", input.get("data"));
        requestObject.put("estado", input.get("estado"));
        requestObject.put("cidade", input.get("cidade"));

        return requestObject.toString();
    }

    public static String requestGetByUser(Map<String, Object> input) {

        final JSONObject requestObject = new JSONObject();

        requestObject.put(KEYS[0], input.get("operacao"));
        requestObject.put("token", ClientSession.getToken());
        requestObject.put("id", ClientSession.getId());

        return requestObject.toString();
    }

    public static String requestDelete(Map<String, Object> input) {

        final JSONObject requestObject = new JSONObject();

        requestObject.put(KEYS[0], input.get("operacao"));
        requestObject.put("id", ClientSession.getId());
        requestObject.put("token", ClientSession.getToken());
        requestObject.put("id_incidente", input.get("id_incidente"));

        return requestObject.toString();
    }

    public static String create(JSONObject json) {
        final JSONObject responseObject = new JSONObject();
        responseObject.put(KEYS[0], 7);

        if(!IncidenteValidator.checkUploadFields(json)) {
            responseObject.put(KEYS[1], "Campos obrigatórios faltantes.");
        } else if(!IncidenteValidator.checkData(json.getString("data"))) {
            responseObject.put(KEYS[1], "Formato de data incorreto.");
        } else if(!IncidenteValidator.checkHora(json.getString("hora"))) {
            responseObject.put(KEYS[1], "Formato de hora incorreto.");
        }  else if(!IncidenteValidator.checkEstado(json.getString("estado"))) {
            responseObject.put(KEYS[1], "Formato de estado incorreto.");
        } else if(!IncidenteValidator.checkLocal(json.getString("cidade"))) {
            responseObject.put(KEYS[1], "Formato de cidade incorreto.");
        } else if(!IncidenteValidator.checkLocal(json.getString("bairro"))) {
            responseObject.put(KEYS[1], "Formato de bairro incorreto.");
        } else if(!IncidenteValidator.checkLocal(json.getString("rua"))) {
            responseObject.put(KEYS[1], "Formato de rua incorreto.");
        } else if(json.getInt("tipo_incidente") < 0 || json.getInt("tipo_incidente") > 10) {
            responseObject.put(KEYS[1], "Tipo de incidente desconhecido.");
        } else if (TokenManager.getToken((Integer) json.query("/id")) == null) {
            responseObject.put(KEYS[1], "id incorreto ou token não gerado");
        } else if (!TokenManager.getToken((Integer) json.query("/id")).equals(json.query("/token"))) {
            responseObject.put(KEYS[1], "token incorreto");
        } else if(IncidenteDataAccess.create(json)) {
            responseObject.put(KEYS[1], "OK");
        }  else {
            responseObject.put(KEYS[1], "Erro Genérico");
        }

        return responseObject.toString();
    }

    public static String get(JSONObject json) {
        final JSONObject responseObject = new JSONObject();

        responseObject.put(KEYS[0], 4);

        if(!IncidenteValidator.checkGetFields(json)) {
            responseObject.put(KEYS[1], "Campos obrigatórios faltantes.");
        } else if(!IncidenteValidator.checkEstado(json.getString("estado"))) {
            responseObject.put(KEYS[1], "Formato de estado incorreto.");
        } else if(!IncidenteValidator.checkData(json.getString("data"))) {
            responseObject.put(KEYS[1], "Formato de data incorreto.");
        } else if(!IncidenteValidator.checkLocal(json.getString("cidade"))) {
            responseObject.put(KEYS[1], "Formato de cidade incorreto.");
        } else {
            responseObject.put(KEYS[1], "OK");
            responseObject.put("incidentes", IncidenteDataAccess.get(
                    json.getString("data"),
                    json.getString("estado"),
                    json.getString("cidade")
            ));
        }

        return responseObject.toString();
    }

    public static String getByUser(JSONObject json) {
        final JSONObject responseObject = new JSONObject();

        responseObject.put(KEYS[0], 5);

        if (TokenManager.getToken((Integer) json.query("/id")) == null) {
            responseObject.put(KEYS[1], "id incorreto ou token não gerado");
        } else if (!TokenManager.getToken((Integer) json.query("/id")).equals(json.query("/token"))) {
            responseObject.put(KEYS[1], "token incorreto");
        } else {
            responseObject.put(KEYS[1], "OK");
            responseObject.put("incidentes", IncidenteDataAccess.getByUser(json.getInt("id")));
        }

        return responseObject.toString();
    }

    public static String delete(JSONObject json) {
        final JSONObject responseObject = new JSONObject();

        responseObject.put(KEYS[0], 6);

        if (TokenManager.getToken((Integer) json.query("/id")) == null) {
            responseObject.put(KEYS[1], "id incorreto ou token não gerado");
        } else if (!TokenManager.getToken((Integer) json.query("/id")).equals(json.query("/token"))) {
            responseObject.put(KEYS[1], "token incorreto");
        } else {
            responseObject.put(KEYS[1], "OK");
            IncidenteDataAccess.delete(json.getInt("id_incidente"));
        }

        return responseObject.toString();
    }
}

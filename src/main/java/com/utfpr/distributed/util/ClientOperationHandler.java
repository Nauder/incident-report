package com.utfpr.distributed.util;

import com.utfpr.distributed.service.IncidenteService;
import com.utfpr.distributed.service.UserService;
import org.json.JSONObject;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ClientOperationHandler {

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    private ClientOperationHandler() {
    }

    public static String request(Map<String, Object> input) {

        String request = "";

        switch ((Integer) input.get("operacao")) {
            case 1 -> request = UserService.requestCreate(input);
            case 2 -> request = UserService.requestLogin(input);
            case 3 -> request = UserService.requestUpdate(input);
            case 4 -> request = IncidenteService.requestGet(input);
            case 5 -> request = IncidenteService.requestGetByUser(input);
            case 6 -> request = IncidenteService.requestDelete(input);
            case 7 -> request = IncidenteService.requestCreate(input);
            case 8 -> request = UserService.requestDelete();
            case 9 -> request = UserService.requestLogout();
        }

        return request;
    }

    public static JSONObject handleResponse(String response) {
        final JSONObject responseInformation = new JSONObject();

        if (response != null) {
            final JSONObject json = new JSONObject(response);

            if ("OK".equals(json.query("/status"))) {
                switch ((Integer) json.query("/operacao")) {
                    case 2 -> {
                        ClientSession.setId((Integer) json.query("/id"));
                        ClientSession.setToken((String) json.query("/token"));
                        ClientSession.setNome((String) json.query("/nome"));
                    }
                    case 4, 5 -> responseInformation.put("incidentes", json.query("/incidentes"));
                    case 3, 8, 9 -> ClientSession.setId(-1);
                }
                responseInformation.put("status", "OK");
            } else {
                responseInformation.put("status", json.query("/status"));
                LOGGER.log(Level.SEVERE, "{0}", json.query("/status"));
            }
        } else {

            responseInformation.put("status", "Resposta do servidor foi null");
            LOGGER.log(Level.SEVERE, "{0}", "Resposta do servidor foi null");
        }

        return responseInformation;
    }
}

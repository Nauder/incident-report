package com.utfpr.distributed.util;

import com.utfpr.distributed.exception.SocketExceptionBuilder;
import com.utfpr.distributed.service.IncidenteService;
import com.utfpr.distributed.service.UserService;
import org.json.JSONObject;

public final class Gateway {

    private Gateway() {
    }

    public static String chooseOperation(String message) {

        String response;

        if (message != null) {

            final JSONObject json = new JSONObject(message);

            switch ((Integer) json.query("/operacao")) {
                case 1 -> response = UserService.create(json);
                case 2 -> response = UserService.login(json);
                case 3 -> response = UserService.update(json);
                case 4 -> response = IncidenteService.get(json);
                case 5 -> response = IncidenteService.getByUser(json);
                case 6 -> response = IncidenteService.delete(json);
                case 7 -> response = IncidenteService.create(json);
                case 8 -> response = UserService.delete(json);
                case 9 -> response = UserService.logout(json);
                default -> response = SocketExceptionBuilder.buildUnknownOperationException(
                        (Integer) json.query("/operacao"));
            }

        } else {

            response = SocketExceptionBuilder.buildNullMessageException();
        }

        return response;

    }
}

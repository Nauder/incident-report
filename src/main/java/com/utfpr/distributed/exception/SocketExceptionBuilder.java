package com.utfpr.distributed.exception;

import org.json.JSONObject;

public interface SocketExceptionBuilder {

    static String buildUnknownOperationException(int operation) {

        final JSONObject json = new JSONObject();

        json.put("status", "Operação desconhecida");
        json.put("operacao", operation);

        return json.toString();
    }

    static String buildNullMessageException() {

        final JSONObject json = new JSONObject();

        json.put("status", "Mensagem foi null");

        return json.toString();
    }
}

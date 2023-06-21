package com.utfpr.distributed.util.token;

import com.utfpr.distributed.controller.ServerController;

public final class ServerTokenManager extends TokenManager {

    private ServerTokenManager() {
    }

    public static String generateToken(Integer id) {

        final String token = new RandomString(20).nextString();

        getTokens().put(id, token);
        ServerController.refreshLogins();

        return token;
    }

    public static boolean validateToken(Integer id, String token) {

        return getTokens().get(id).equals(token);
    }

}

package com.utfpr.distributed.util.token;

public final class ServerTokenManager extends TokenManager {

    private ServerTokenManager() {
    }

    public static String generateToken(Integer id) {

        final String token = new RandomString(20).nextString();

        getTokens().put(id, token);

        return token;
    }

    public static boolean validateToken(Integer id, String token) {

        return getTokens().get(id).equals(token);
    }

}

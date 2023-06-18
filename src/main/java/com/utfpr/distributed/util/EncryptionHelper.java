package com.utfpr.distributed.util;

public final class EncryptionHelper {

    private EncryptionHelper() {
    }

    public static String encrypt(String string) {

        final StringBuilder ciphertext = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);
            char encryptedChar = (char) ((character + 2) % 256);
            ciphertext.append(encryptedChar);
        }

        return ciphertext.toString();
    }

    public static String decrypt(String string) {

        final StringBuilder plaintext = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);
            char decryptedChar = (char) ((character - 2 + 256) % 256);
            plaintext.append(decryptedChar);
        }

        return plaintext.toString();
    }
}

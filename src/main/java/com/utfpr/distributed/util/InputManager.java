package com.utfpr.distributed.util;

import java.util.Scanner;

public interface InputManager {

    static Scanner in = new Scanner(System.in);

    public static String getString() {

        return in.nextLine();
    }

    public static int getInt() {

        final int input = in.nextInt();
        in.nextLine();

        return input;
    }
}

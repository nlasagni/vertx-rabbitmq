package com.wedriveu.rd;

/**
 * Created by nicolalasagni on 29/07/2017.
 */
public class Log {

    private static final String ANSI_RED = "\u001B[31m";

    static void info(String tag, String message) {
        System.out.println("[" + tag + "] " + message);
    }

    static void error(String tag, String message, Throwable e) {
        System.out.println(ANSI_RED + "[" + tag + "] " + message + ANSI_RED);
        e.printStackTrace();
    }

}

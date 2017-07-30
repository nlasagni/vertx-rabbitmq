package com.vertx.rabbitmq.util;

/**
 * Created by nicolalasagni on 29/07/2017.
 */
public class Log {

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void info(String tag, String message) {
        System.out.println("[" + tag + "] " + message);
    }

    public static void error(String tag, String message, Throwable e) {
        System.out.println(ANSI_RED + "[" + tag + "] " + message + ANSI_RESET);
        e.printStackTrace();
    }

}

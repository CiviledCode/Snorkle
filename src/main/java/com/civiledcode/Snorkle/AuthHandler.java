package com.civiledcode.Snorkle;

import java.util.concurrent.BlockingQueue;

public class AuthHandler {

    public static final int FAILURE = 0;
    public static final int SUCCESS = 1;
    public static final int BAN = 2;
    public static final int CUSTOM = 3;

    public void handleResponse(String response) {
        System.out.println(ConsoleColor.RED + "ERROR: No custom handle defined in AuthHandler! Please override handleCustom and try again");
        System.exit(0);
    }

    public void handleCustom() {
        System.out.println(ConsoleColor.RED + "ERROR: No custom handle defined in AuthHandler! Please override handleCustom and try again");
        System.exit(0);
    }

    public void run(BlockingQueue<String> queue, SnorkleInstance instance) {
        try {
            String message;
            while (!(message = queue.take()).equalsIgnoreCase("EXIT")) {
                handleResponse(message);
            }
        } catch (Exception ignored) { }
    }

}

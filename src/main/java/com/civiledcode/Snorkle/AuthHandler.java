package com.civiledcode.Snorkle;

import java.util.concurrent.BlockingQueue;

public class AuthHandler {

    public static final int FAILURE = 0;
    public static final int SUCCESS = 1;
    public static final int BAN = 2;
    public static final int CUSTOM = 3;

    public int handleResponse(String response) {
        System.out.println(ConsoleColor.RED + "ERROR: No custom handle defined in AuthHandler! Please override handleCustom and try again");
        System.exit(0);
        return FAILURE;
    }

    public void handleCustom(String response, String details) {
        System.out.println(ConsoleColor.RED + "ERROR: No custom handle defined in AuthHandler! Please override handleCustom and try again");
        System.exit(0);
    }

    public void run(BlockingQueue<String> queue, SnorkleInstance instance) {
        try {
            String message;
            while (!(message = queue.take()).equalsIgnoreCase("EXIT")) {
                //The format is botid~user:password~response
                String[] args = message.split("~");

                int response = handleResponse(args[2]);

                switch (response) {
                    case FAILURE:
                        break;
                    case SUCCESS:
                        //Save the combo to hits.txt
                        break;
                    case BAN:
                        instance.proxyNeeded.add(Integer.parseInt(args[0]));
                        instance.needRetry.add(args[1]);
                        break;
                    case CUSTOM:
                        handleCustom(args[2], args[0]);
                        break;
                }
            }
        } catch (Exception e) {

        }
    }

}

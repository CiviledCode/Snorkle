package com.civiledcode.Snorkle;

public class AuthHandler {
    public static final int FAILURE = 0;
    public static final int SUCCESS = 1;
    public static final int BAN = 2;
    public static final int CUSTOM = 3;
    public AuthHandler() {

    }

    public void handleResponse(String response) {
        System.out.println(ConsoleColor.RED + "ERROR: No custom handle defined in AuthHandler! Please override handleCustom and try again");
        System.exit(0);
    }

    public void handleCustom() {
        System.out.println(ConsoleColor.RED + "ERROR: No custom handle defined in AuthHandler! Please override handleCustom and try again");
        System.exit(0);
    }
}

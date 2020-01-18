package com.civiledcode.Snorkle;

import java.io.File;

public class SnorkleInstance {
    String instanceTitle;
    boolean useProxies = false;
    boolean useCaptcha = false;

    int botCount = 0;

    File wordlist;

    AuthHandler handler;

    public SnorkleInstance(String title) {
        this.instanceTitle = title;
    }

    public boolean useProxies(boolean bool) {
       return this.useProxies = bool;
    }

    /**
     * Try a new client. This method should be overwritten
     * @param user Username of account
     * @param pass Password of account
     * @return String used for response parsing in the PARSE thread
     */
    public String newClient(String user, String pass) {
        System.out.println(ConsoleColor.RED + "ERROR: The 'newClient' method must be overwritten to continue!");
        System.exit(0);
        return "ERROR";
    }

    public boolean usesCaptcha(boolean bool) {
        return this.useCaptcha = bool;
    }

    public void setBotCount(int amount) {
        this.botCount = amount;
    }

    public int getBotCount() {
        return botCount;
    }

    public void setWordlist(File file) {
        this.wordlist = file;
    }

    public void setAuthHandler(AuthHandler handler) {
        this.handler = handler;
    }

    public AuthHandler getHandler() {
        return handler;
    }
}

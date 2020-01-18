package com.civiledcode.Snorkle;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public class SnorkleInstance {
    //TODO: Add proxies
    String instanceTitle;
    boolean useProxies = false;
    boolean useCaptcha = false;
    boolean shouldStart = false;

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

    public void setWordlist(File file) {
        this.wordlist = file;
    }

    public void setAuthHandler(AuthHandler handler) {
        this.handler = handler;
    }

    public AuthHandler getHandler() {
        return handler;
    }

    public void start(SnorkleInstance instance) {
        instance.shouldStart = true;
        Runnable target;
        Thread startChecker = new Thread(new Runnable() {
            @Override
            public void run(){
                Snorkle.runInstance(instance, getBotAmount());
            }
        }, instanceTitle + "Checker");

        startChecker.start();
    }

    public void stop() {
        shouldStart = false;
    }

    public int getBotAmount() {
        return 100;
    }
}

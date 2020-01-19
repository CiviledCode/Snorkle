package com.civiledcode.Snorkle;

import java.io.File;
import java.util.concurrent.*;

public class SnorkleInstance {

    //TODO: Add proxies
    String instanceTitle;

    boolean useProxies = false;

    boolean useCaptcha = false;

    boolean shouldStart = false;

    int maxBotAmount = 100;

    int botCount = 0;

    File wordList;

    AuthHandler handler;

    ArrayBlockingQueue<String> queue;

    public SnorkleInstance(String title) {
        this.instanceTitle = title;
        try {
            queue = new ArrayBlockingQueue<>(Snorkle.parseList(wordList).length);
        } catch(Exception e) {
            System.out.println(ConsoleColor.RED + "ERROR: An unexpected error occurred whilst loading the word list!");
            System.exit(0);
        }
        SnorkleInstance instance = this;
        new Thread(() -> instance.getHandler().run(queue, instance), instanceTitle + "AuthHandler").start();
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
        this.wordList = file;
    }

    public void setAuthHandler(AuthHandler handler) {
        this.handler = handler;
    }

    public AuthHandler getHandler() {
        return handler;
    }

    public void start(SnorkleInstance instance, BlockingQueue<String> queue) {
        instance.shouldStart = true;
        for(int i = 0; i < botCount; i++) {
            new Thread(() -> Snorkle.runInstance(instance, getMaxBotAmount(), queue), instanceTitle + "CheckerBot" + i).start();
        }
    }

    public void stop() {
        shouldStart = false;
    }

    public int getMaxBotAmount() {
        return maxBotAmount;
    }

}

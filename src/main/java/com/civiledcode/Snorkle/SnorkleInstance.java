package com.civiledcode.Snorkle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class SnorkleInstance {

    //TODO: Add proxy support

    String instanceTitle;

    boolean useProxies = false;

    boolean useCaptcha = false;

    boolean shouldStart = false;

    int getMaximumBots = 100;

    File wordList;

    String[] words;

    String[] proxies;

    AuthHandler handler = new AuthHandler();

    ArrayList<String> retries = new ArrayList<>();

    HashSet<String> proxiesUsed = new HashSet<>();

    ArrayBlockingQueue<String> queue;

    public SnorkleInstance(String title, File wordList, File proxyFile) throws IOException {
        this.instanceTitle = title;
        this.wordList = wordList;
        if (!wordList.exists()) {
            wordList.createNewFile();
        }
        if (!proxyFile.exists()) {
            proxyFile.createNewFile();
        }
        words = Snorkle.parseList(wordList);
        try {
            queue = new ArrayBlockingQueue<>(Snorkle.parseList(wordList).length);
        } catch(Exception e) {
            System.out.println(ConsoleColor.RED + "ERROR: An unexpected error occurred whilst loading the word list!");
            System.exit(0);
        }
        SnorkleInstance instance = this;
        new Thread(() -> instance.getHandler().run(queue, instance), instanceTitle + "AuthHandler").start();
        shouldStart = true;
    }

    /**
     * Try a new client. This method should be overwritten
     * @param user Username of account
     * @param pass Password of account
     * @return String used for response parsing in the PARSE thread
     */
    public abstract String newClient(String user, String pass);

    public String getProxy() {
        return "";
    }

    public boolean usesCaptcha(boolean bool) {
        return this.useCaptcha = bool;
    }

    public void setWordList(File file) {
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
        for (int i = 0; i < getMaxBotAmount(); i++) {
            final int finalIndex = i;
            new Thread(() -> {
                Snorkle.runInstance(instance, finalIndex, queue);
            }, instanceTitle + "CheckerBot" + i).start();
        }
    }

    public boolean sendToAuth(String response, String user) {
        try {
            if (response.contains("400")) {
                retries.add(user);
                return true;
            } else {
                queue.put(response);
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void stop() {
        shouldStart = false;
    }

    public int getMaxBotAmount() {
        return getMaximumBots;
    }

}
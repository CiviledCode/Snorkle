package com.civiledcode.Snorkle;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class SnorkleInstance {

    String instanceTitle;

    boolean useCaptcha = false;

    boolean shouldStart;

    int getMaximumBots = 100;

    File wordList;

    File proxiesFile;

    AuthHandler handler = new AuthHandler();

    ArrayBlockingQueue<String> queue;

    HashMap<String, String> proxyAssigned = new HashMap<>();

    String[] words;

    String[] proxies;

    Random random = new Random();

    public SnorkleInstance(String title, File wordList, File proxies) throws IOException {
        this.instanceTitle = title;
        this.wordList = wordList;
        this.proxiesFile = proxies;
        if (!wordList.exists()) {
            wordList.createNewFile();
        }
        if (!proxies.exists()) {
            proxies.createNewFile();
        }
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
    public abstract String newClient(String user, String pass, Proxy proxy);

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
        try {
            words = Snorkle.parseList(instance.wordList);
            proxies = Snorkle.parseList(instance.proxiesFile);
        } catch (Exception e) {
            System.out.println(ConsoleColor.RED + "ERROR: An unexpected error occurred whilst loading the wordlist or proxies!");
            System.exit(0);
            return;
        }
        instance.shouldStart = true;
        String proxyDetails = findProxy(proxies, random);
        String ip = proxyDetails.split(":")[0];
        int port = Integer.parseInt(proxyDetails.split(":")[1]);
        InetSocketAddress address = new InetSocketAddress(ip, port);
        Proxy.Type type = detectProxyType(address);
        Proxy proxy = new Proxy(type, address);
        for (int i = 0; i < getMaxBotAmount(); i++) {
            final int finalIndex = i;
            new Thread(() -> {
                proxyAssigned.put(Thread.currentThread().getName(), proxyDetails);
                Snorkle.runInstance(instance, finalIndex, queue, words, proxy);
            }, instanceTitle + "CheckerBot" + i).start();
        }
    }

    public void finishedUsingProxy(String threadName) {
        proxyAssigned.remove(threadName);
    }

    public String findProxy(String[] proxies, Random random) {
        int index = random.nextInt(proxies.length);
        String selectedProxy = proxies[index];
        if (proxyAssigned.containsValue(selectedProxy)) {
            return findProxy(proxies, random);
        } else {
            return selectedProxy;
        }
    }

    private static Proxy.Type detectProxyType(InetSocketAddress proxyAddress) {
        try {
            URL url = new URL("http://www.google.com");
            List<Proxy.Type> proxyTypesToTry = Arrays.asList(Proxy.Type.SOCKS, Proxy.Type.HTTP);
            for (Proxy.Type proxyType : proxyTypesToTry) {
                Proxy proxy = new Proxy(proxyType, proxyAddress);
                URLConnection connection;
                connection = url.openConnection(proxy);
                connection.getContent();
                return(proxyType);
            }
        } catch (IOException ignored) { }
        return(null);
    }

    public void stop() {
        shouldStart = false;
    }

    public int getMaxBotAmount() {
        return getMaximumBots;
    }

}

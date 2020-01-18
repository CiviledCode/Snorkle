package com.civiledcode.Snorkle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Snorkle {
    public static void runInstance(SnorkleInstance instance, int botnumber) {
        String[] words = null;
        try {
            words = parseList(instance.wordlist);
        } catch(Exception e) {
            System.out.println(ConsoleColor.RED + "ERROR: An unexpected error occurred whilst loading the word list!");
            System.exit(0);
            return;
        }
        int index = 0;

        try {
            //Create a blocking queue to send to the newClient method
            //We use this to pass information to the responseParsing thread
            BlockingQueue<String> queue = new ArrayBlockingQueue<String>(words.length);
            while (instance.shouldStart) {
                    //It's okay to run :)
                    String[] user = words[botnumber + (index * instance.getBotAmount())].split(":");

                    //Push the response to the responseHandler where we can depict what to do
                    queue.put(instance.newClient(user[0], user[1]));

                    //Increase the index and bot count for the instance
                index++;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }

    public static String[] parseList(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] list = new String[br.lines().toArray().length];
        try {
            for(int index = 0; index < list.length; index++) {
                list[index] = br.readLine();
            }
        } finally {
            br.close();
        }
        return list;
    }
}

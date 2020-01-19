package com.civiledcode.Snorkle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class Snorkle {
    //TODO: Work on retrying if auth failed due to banned IP or bad request
    public static void runInstance(SnorkleInstance instance, int botnumber, BlockingQueue<String> queue) {
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
            while (instance.shouldStart) {
                    int ind = index * instance.getBotAmount() + botnumber;

                    //If we have hit the end of the road
                    if(ind >= words.length)
                        break;

                    //It's okay to run :)
                    String[] user = words[ind].split(":");

                    //Push the response to the responseHandler where we can depict what to do
                    queue.put(instance.newClient(user[0], user[1]));

                    //Increase the index and bot count for the instance
                index++;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
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

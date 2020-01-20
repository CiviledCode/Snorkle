package com.civiledcode.Snorkle;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

public class Snorkle {

    //TODO: Work on retrying if auth failed due to banned IP or bad request

    public static void runInstance(SnorkleInstance instance, int botNumber, BlockingQueue<String> queue) {
        String[] words;
        try {
            words = parseList(instance.wordList);
        } catch (Exception e) {
            System.out.println(ConsoleColor.RED + "ERROR: An unexpected error occurred whilst loading the word list!");
            System.exit(0);
            return;
        }
        int index = 0;

        try {
            // Create a blocking queue to send to the newClient method
            // We use this to pass information to the responseParsing thread
            while (instance.shouldStart) {
                int ind = index * instance.getMaxBotAmount() + botNumber;

                // If we have hit the end of the road
                if(ind >= words.length) break;

                // It's okay to run :)
                String[] user = words[ind].split(":");

                // Push the response to the responseHandler where we can depict what to do
                queue.put(instance.newClient(user[0], user[1]));

                System.out.println(Thread.currentThread().getName());
                // Increase the index and bot count for the instance
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] parseList(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        Path path = Paths.get(file.getPath());
        int lineCount = (int) Files.lines(path).count();
        String[] list = new String[lineCount];
        try {
            for (int index = 0; index < list.length; index++) {
                String line = br.readLine();
                list[index] = line;
            }
        } finally {
            br.close();
        }
        return list;
    }

}

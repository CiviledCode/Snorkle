package com.civiledcode.Snorkle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Snorkle {
    public static boolean runInstances = false;
    public static void runInstance(SnorkleInstance instance, int botcount) {
        while(runInstances) {
            if(instance.getBotCount() == botcount) {
                continue;
            } else {
            }
        }
    }

    public static void main(String[] args) {

    }

    public static void stopInstances() {
        runInstances = false;
    }

    public static void startInstances() {
        runInstances = true;
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

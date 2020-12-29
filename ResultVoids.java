package com.ice.mci_mod;

import java.io.*;
import java.util.ArrayList;

public class ResultVoids {
    public static void ChatResult(String worldName, ArrayList<ArrayList<String>> Chats) {
        try {
            File file = new File("C:\\minecraft_mci_files\\" + worldName + "_ChatsResult.csv");
            FileWriter filewriter = new FileWriter(file);
            PrintWriter p = new PrintWriter(new BufferedWriter(filewriter));

            for (int i = 0; i < Chats.size(); i++) {
                p.print(Chats.get(i).get(0));
                p.print(",");
                p.print(Chats.get(i).get(1));
                p.println();    // 改行
            }
            p.close();
            filewriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void ItemResult(String worldName, ArrayList<ArrayList<String>> Items) {
        try {
            File file = new File("C:\\minecraft_mci_files\\" + worldName + "_ItemsResult.csv");
            FileWriter filewriter = new FileWriter(file);
            PrintWriter p = new PrintWriter(new BufferedWriter(filewriter));

            for (int i = 0; i < Items.size(); i++) {
                p.print(Items.get(i).get(0));
                p.print(",");
                p.print(Items.get(i).get(1));
                p.println();    // 改行
            }
            p.close();
            filewriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void BlockResult(String worldName, ArrayList<ArrayList<String>> Blocks) {
        try {
            File file = new File("C:\\minecraft_mci_files\\" + worldName + "_BlocksResult.csv");
            FileWriter filewriter = new FileWriter(file);
            PrintWriter p = new PrintWriter(new BufferedWriter(filewriter));

            for (int i = 0; i < Blocks.size(); i++) {
                p.print(Blocks.get(i).get(0));
                p.print(",");
                p.print(Blocks.get(i).get(1));
                p.println();    // 改行
            }
            p.close();
            filewriter.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
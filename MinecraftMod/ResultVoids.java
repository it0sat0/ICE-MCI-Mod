package com.ice.mci_mod;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ResultVoids {
    public static void ChatResult(String worldName, ArrayList<ArrayList<String>> Chats) {
        Date nowDate = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String formatNowDate = sdf1.format(nowDate);

        try {
            File file = new File("C:\\minecraft_mci_files\\"  + formatNowDate+ "_" + worldName + "_ChatsResult.csv");
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

    public static void TimeCountResult(String worldName, int TimeCounter[]){
        Date nowDate = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String formatNowDate = sdf1.format(nowDate);

        try{
            File file = new File("C:\\minecraft_mci_files\\"+ formatNowDate+ "_" + worldName + "_StopTimeResult.csv");
            FileWriter filewriter = new FileWriter(file);
            PrintWriter p = new PrintWriter(new BufferedWriter(filewriter));
            p.print(worldName);
            for(int j=0; j<4; j++){
                p.print(",");
                p.print(TimeCounter[j]);
            }
            p.println();    // 改行
            p.close();
            filewriter.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public static void ItemResult(String worldName, ArrayList<ArrayList<String>> Items) {
        Date nowDate = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String formatNowDate = sdf1.format(nowDate);

        try {
            File file = new File("C:\\minecraft_mci_files\\" + formatNowDate+ "_" + worldName + "_ItemsResult.csv");
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
        Date nowDate = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String formatNowDate = sdf1.format(nowDate);

        try {
            File file = new File("C:\\minecraft_mci_files\\" + formatNowDate+ "_" + worldName + "_BlocksResult.csv");
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
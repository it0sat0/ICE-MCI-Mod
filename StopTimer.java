package com.ice.mci_mod;

import java.io.*;


public class StopTimer {
    //配列で、各レベルの選択
    public static String LevNum[] = {"1", "2", "6", "8", "11", "00"};
    //各領域にいた時間の格納
    public static int TimeCounter[] = {0,0,0,0};
    //public static int TimeCounter[][] = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
    public static void StopTimerVoid(String level, double x, double z, double LowLevel[][][], double HighLevel[][][]) {
        int Lev = LevNum.length-1;

        for (int i = 0; i < LevNum.length; i++) {
            if (level.equals("level" + LevNum[i])) {
                Lev = i;
                break;
            }
        }

        if (Lev == LevNum.length - 1) {
            System.out.println("NONONONONO");
        } else if (Lev < 2) {     //level1 or 2
            for(int i = 0; i < 2; i++){
                if(LowLevel[Lev][i][0]-5.0 <= x && x <= LowLevel[Lev][i][0]+5.0){
                    if(LowLevel[Lev][i][1]-5 <= z && z <= LowLevel[Lev][i][1]+5){
                        TimeCounter[i]++;
                    }
                }
            }
        } else {      //level6,8,11
            for(int i = 0; i < 4; i++){
                if(HighLevel[Lev][i][0]-5 <= x && x <= HighLevel[Lev][i][0]+5){
                    if(HighLevel[Lev][i][1]-5 <= z && z <= HighLevel[Lev][i][1]+5) {
                        TimeCounter[i]++;
                    }
                }
            }
        }
    }
    public static void StopTimerResult(String worldName) {
        try{
            File file = new File("C:\\minecraft_mci_files\\"+ worldName + "_StopTimeResult.csv");
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
}

package com.ice.mci_mod;

import java.io.*;

public class OpenChest {
    //配列で、各レベルの選択
    public static String LevNum[] = {"1", "2", "6", "8", "11", "00"};

    public static int OpenChestPosition(String level, double x, double z, double LowLevel[][][], double HighLevel[][][]) {
        int Lev = LevNum.length - 1;

        System.out.println("Level=" + level);
        System.out.println("x=" + x);
        System.out.println("z=" + z);


        for (int i = 0; i < LevNum.length; i++) {
            if (level.equals("level" + LevNum[i])) {
                Lev = i;
                break;
            }
        }
        System.out.println("Lev=" + Lev);
        if (Lev == LevNum.length - 1) {
            System.out.println("NONONONONO");
        } else if (Lev < 2) {     //level1 or 2
            for(int i = 0; i < 2; i++){
                if (LowLevel[Lev][i][0] - 5.0 <= x && x <= LowLevel[Lev][i][0] + 5.0) {
                    if (LowLevel[Lev][i][1] - 5 <= z && z <= LowLevel[Lev][i][1] + 5) {
                        return i;
                    }
                }
            }
        } else {      //level6,8,11
            System.out.println("1");
            for (int i = 0; i < 4; i++) {
                System.out.println("2");
                if (HighLevel[Lev-2][i][0] - 5 <= x && x <= HighLevel[Lev-2][i][0] + 5) {
                    System.out.println("3");
                    if (HighLevel[Lev-2][i][1] - 5 <= z && z <= HighLevel[Lev-2][i][1] + 5) {
                        System.out.println("4");
                        return i;
                    }
                }
            }
        }
        return 5;
    }
}

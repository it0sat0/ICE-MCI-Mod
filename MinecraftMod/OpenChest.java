package com.ice.mci_mod;

import java.io.*;

public class OpenChest {
    //配列で、各レベルの選択
    public static String LevNum[] = {"1", "2", "6", "8", "11", "00"};
    public static int ReturnInfo[] = {LevNum.length - 1,5};

    public static int[] OpenChestPosition(double x, double z, double ChestPos[][][], boolean LC) {

        if (LC == true) {
            for (int j = 0; j < 5; j++) {
                for (int i = 0; i < 4; i++) {
                    if (ChestPos[j][i][0] - 5.0 <= x && x <= ChestPos[j][i][0] + 5.0) {
                        if (ChestPos[j][i][1] - 5.0 <= z && z <= ChestPos[j][i][1] + 5.0) {
                            ReturnInfo[0] = j;
                            ReturnInfo[1] = i;
                        }
                    }
                }
            }
        }
        return ReturnInfo;
    }
}

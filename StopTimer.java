package com.ice.mci_mod;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.Collectors;
import java.util.Timer;
import java.util.TimerTask;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
                if(HighLevel[Lev-2][i][0]-5 <= x && x <= HighLevel[Lev-2][i][0]+5){
                    if(HighLevel[Lev-2][i][1]-5 <= z && z <= HighLevel[Lev-2][i][1]+5) {
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

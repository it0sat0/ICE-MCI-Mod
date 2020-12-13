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

// The value here should match an entry in the META-INF/mods.toml file
@Mod("mci_mod")
public class ExampleMod {
    //public static final String MOD_ID = "mci_mod"; // 追記 書いたらバグる
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public ExampleMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }


    //チェストの位置
    //level1 and 2
    public static double LowLevel[][][] = {{{-132,-631},{-103,-678}},{{-494,719},{-515,643}}};
    //level6,8 and 11
    public static double HighLevel[][][] = {{{116,58},{115,42},{143,-13},{85,-6}},{{19,129},{38,93},{-17,111},{3,57}},{{352,20},{315,-57},{313,-4},{348,-41}}};

    //プレイ開始後の処理
    int count = 0;
    double disX, disZ;
    double dis = 0.0;
    SimpleDateFormat sdf;
    ArrayList<ArrayList<Double>> PosLists = new ArrayList<ArrayList<Double>>(); //Set position
    ArrayList<String> TS = new ArrayList<String>(); //Set TimeStamp
    ArrayList<Double> Dis = new ArrayList<Double>();    //Set Distance
    ArrayList<ArrayList<Float>> PitchYaws = new ArrayList<ArrayList<Float>>();  //Set Pitch and Yaw
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        String worldName = player.world.getWorldInfo().getWorldName();  //get World Name

        //本当は書く必要ないけど、disのために書くしか案がおもいつかなかったところ。残念無念。
        Calendar cl = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        TS.add(sdf.format(cl.getTime()));
        ArrayList<Double> PosList = new ArrayList<Double>();    //add Position
        PosList.add(player.getPosX());
        PosList.add(player.getPosY());
        PosList.add(player.getPosZ());
        PosLists.add(PosList);
        Dis.add(0.0);
        ArrayList<Float> PitchYaw = new ArrayList<Float>();
        PitchYaw.add(player.getPitchYaw().x); //get Pitch
        PitchYaw.add(player.getPitchYaw().y); //get Yaw
        PitchYaws.add(PitchYaw);

        TimerTask task = new TimerTask() {
            public void run() {
                //System.out.println(count+ "-----------------------");
                if(count == 3){
                    player.sendMessage(new StringTextComponent("/time set noon"));
                }
                count +=1;
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Calendar cl = Calendar.getInstance();
                TS.add(sdf.format(cl.getTime()));
                //System.out.println(sdf.format(cl.getTime()));
                ArrayList<Double> PosList = new ArrayList<Double>();
                PosList.add(player.getPosX());
                PosList.add(player.getPosY());
                PosList.add(player.getPosZ());
                PosLists.add(PosList);
                disX = PosLists.get(count).get(0)-PosLists.get(count-1).get(0);
                disZ = PosLists.get(count).get(2)-PosLists.get(count-1).get(2);
                dis += Math.sqrt(disX*disX + disZ*disZ);
                Dis.add(dis);
                ArrayList<Float> PitchYaw = new ArrayList<Float>();
                PitchYaw.add(player.getPitchYaw().x); //get Pitch
                PitchYaw.add(player.getPitchYaw().y); //get Yaw
                PitchYaws.add(PitchYaw);
                StopTimer.StopTimerVoid(worldName,player.getPosX(),player.getPosZ(), LowLevel, HighLevel);
            }
        };
        Timer looptimer = new Timer();
        looptimer.schedule(task, 0, 1000);
    }

    //チャットに入力された情報を取得
    ArrayList<ArrayList<String>>Chats = new ArrayList<ArrayList<String>>();
    @SubscribeEvent
    public void onPlayerMessage(ServerChatEvent chatEvent){
        Calendar cl = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        ArrayList<String> Chat = new ArrayList<String>();
        Chat.add(sdf.format(cl.getTime()));
        Chat.add(chatEvent.getMessage());
        Chats.add(Chat);
        System.out.println(chatEvent.getMessage());
    }

    //アイテムをクリックした際に情報を取得
    ArrayList<ArrayList<String>>Items = new ArrayList<ArrayList<String>>();
    @SubscribeEvent
    public void onPlayerLeftClickItem(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        if(!(player.inventory.getItemStack().getItem().toString().equals("air"))){
            Calendar cl = Calendar.getInstance();
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            ArrayList<String> Item = new ArrayList<String>();
            Item.add(sdf.format(cl.getTime()));
            Item.add(player.inventory.getItemStack().getItem().toString());
            Items.add(Item);
        }
    }

    //ブロックをクリックした際に情報を取得
    ArrayList<ArrayList<String>>BlockLists = new ArrayList<ArrayList<String>>();
    int ChestNo = 0; //次に開けるべきチェストの番号-1
    int OpenChestNo; //開けたチェストの番号
    int BeforeChestNo = 0; //ひとつ前に開けたチェストの番号
    int TimeCount = 0;
    boolean CC = true;
    boolean memory = true;
    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        String BlockName = event.getUseBlock().toString();
        memory = !memory;
        if(memory == true && BlockName.equals("DEFAULT")) {
            String worldName = player.world.getWorldInfo().getWorldName();  //get World Name
            Calendar cl = Calendar.getInstance();
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            OpenChestNo = OpenChest.OpenChestPosition(worldName, player.getPosX(), player.getPosZ(), LowLevel, HighLevel);
            ArrayList<String> BlockList = new ArrayList<String>();
            BlockList.add(sdf.format(cl.getTime()));
            BlockList.add(BlockName);
            BlockList.add(String.valueOf(OpenChestNo));
            BlockLists.add(BlockList);

            System.out.println("----------------------------");
            System.out.println("OpenChestNo=" + OpenChestNo);
            System.out.println("BeforeChesNo=" + BeforeChestNo);
            System.out.println("ChestNo=" + ChestNo);

            if (OpenChestNo == 0) {

            } else if (OpenChestNo == 5) {

            }else if(ChestNo+1 == OpenChestNo && CC == true){
                player.sendMessage(new StringTextComponent("正しい順にチェストを開けています"));
                BeforeChestNo = ChestNo;
                ChestNo++;
                CC = false;
                TimeCount = 0;
            } else if (ChestNo == OpenChestNo && CC == true) {
                if (BeforeChestNo == ChestNo) {
                    player.sendMessage(new StringTextComponent("すでに開けたチェストです"));
                } else {
                    BeforeChestNo = ChestNo;
                }
                CC = false;
                TimeCount = 0;
            } else if (ChestNo <= OpenChestNo && CC == true) {
                player.sendMessage(new StringTextComponent("すでに開けたチェストです"));
                CC = false;
                TimeCount = 0;
            } else if (OpenChestNo <= ChestNo && CC == true) {
                player.sendMessage(new StringTextComponent("次に開けるチェストはここではありません！"));
                CC = false;
                TimeCount = 0;
            }

            TimerTask task = new TimerTask() {
                public void run() {
                    if (TimeCount == 1) {
                        CC = true;
                    }
                    TimeCount++;
                }
            };
            Timer looptimer = new Timer();
            looptimer.schedule(task, 0, 1000);
        }
    }

    //ファイル書き出し
    @SubscribeEvent
    public void onPlayerCrafted(PlayerEvent.ItemCraftedEvent event) {
        PlayerEntity player = event.getPlayer();
        String worldName = player.world.getWorldInfo().getWorldName();
        try{
            if(worldName.equals("level1")) {
                File newdir = new File("C:\\minecraft_mci_files");
                newdir.mkdir();
            }
            File file = new File("C:\\minecraft_mci_files\\" + worldName + "_MovementResult.csv");
            FileWriter filewriter = new FileWriter(file);
            PrintWriter p = new PrintWriter(new BufferedWriter(filewriter));
            for(int i=0; i<count; i++){//Time,x,y,z,dis,pitch,yaw
                p.print(TS.get(i));
                for(int j=0; j < 3; j++){
                    p.print(",");
                    p.print(PosLists.get(i).get(j));
                }
                p.print(",");
                p.print(Dis.get(i));
                p.print(",");
                p.print(PitchYaws.get(i).get(0));   //Pitch
                p.print(",");
                p.print(PitchYaws.get(i).get(1));   //Yaw
                p.println();    // 改行
            }
            p.close();
            filewriter.close();
        }catch(IOException e){
            System.out.println(e);
        }
        StopTimer.StopTimerResult(worldName);
        ResultVoids.ChatResult(worldName, Chats);
        ResultVoids.ItemResult(worldName, Items);
        ResultVoids.BlockResult(worldName, BlockLists);
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
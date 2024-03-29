package com.ice.mci_mod;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.Collectors;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
//import java.awt.*;
//import java.awt.event.InputEvent;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
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

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

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

        event.getCommandDispatcher().register(
                literal("join").then(argument("channel", string()).executes(c -> {
                    String channel = getString(c, "channel");
                    TwitchPlays.join(channel);
                    event.getServer().sendMessage(new StringTextComponent("Joined channel " + channel));
                    return 1;
                }))
        );
    }



    //チェストの位置
    public static double ChestPosotionXZ[][][] = {{{-132,-631},{-103,-678},{0,0},{0,0}}, //SHQ模倣のlevel1~5
                                                {{-494,719},{-515,643},{0,0},{0,0}},
                                                {{116,58},{115,41},{143,-14},{85.5,-6.5}},
                                                {{19,129},{38,92},{-17,112},{4,56}},
                                                {{352,20},{315,-59},{314,-5},{349,-43}},
                                                {{-131.5,108.5},{-111.5,82.5},{0,0},{0,0}}, //オリジナルのlevel1~5
                                                {{-195.5,20.5},{-208.5,-18.5},{0,0},{0,0}},
                                                {{100.5,-214.5},{108.5,-221.5},{86.5,-257.5},{129.5,-256.5}},
                                                {{-279.5,248.5},{-286.5,224.5},{-329.5,235.5},{-289.5,200.5}},
                                                {{376.5,-8.5},{382.5,35.5},{355.5,-4.5},{349.5,41.5}}};

    //プレイ開始後の処理
    int count = 0;
    double disX, disZ;
    double dis = 0.0;
    String NowWorldName;
    SimpleDateFormat sdf;
    ArrayList<ArrayList<Double>> PosLists = new ArrayList<ArrayList<Double>>(); //Set position
    ArrayList<String> TS = new ArrayList<String>(); //Set TimeStamp
    ArrayList<Double> Dis = new ArrayList<Double>();    //Set Distance
    ArrayList<ArrayList<Float>> PitchYaws = new ArrayList<ArrayList<Float>>();  //Set Pitch and Yaw


    int TimeCounter[] = {0,0,0,0};
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        //ここ～～～～！！！！！！！
        TwitchPlays.join("it0sat0"); //ここー！！！！！　ぜったいへんこうするように！！！！！！
        System.out.println("OK1");
        //event.getPlayer().sendMessage(new StringTextComponent("ゲーム開始"));
        PlayerEntity player = event.getPlayer();
        String worldName = player.world.getWorldInfo().getWorldName();  //get World Name
        NowWorldName =worldName;

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
                count +=1;
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Calendar cl = Calendar.getInstance();
                TS.add(sdf.format(cl.getTime()));
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
            }

        };
        Timer looptimer = new Timer();
        looptimer.schedule(task, 0, 1000);

        TwitchPlays.setTargetPlayer(event.getPlayer());
    }

    //キーボード入力判定
    int ChestNo = 0; //次に開けるべきチェストの番号-1
    int OpenChestNo; //開けたチェストの番号
    int BeforeChestNo = 0; //ひとつ前に開けたチェストの番号
    boolean Chast = false;
    //チェスト開いた時判定が保存されなくなってるで
    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event){
        if(event.getKey()==69){ //Eかな？
            Chast = true;
        }
    }

    //チェスト順のコメント出力
    boolean LC = true;
    @SubscribeEvent
    public void onSendChat(PlayerEvent event){
        if(Chast == true) {
            PlayerEntity player = event.getPlayer();
            int ChestInfo[] = OpenChest.OpenChestPosition(player.getPosX(), player.getPosZ(), ExampleMod.ChestPosotionXZ, true); //座標だけ見てどこのワールドか見るようになってるわ
            //OpenChestNo = OpenChest.ReturnInfo[1];
            OpenChestNo = ChestInfo[1];
            if (OpenChestNo == 0 || OpenChestNo == 5) {

            } else if (ChestNo + 1 == OpenChestNo) {
                player.sendMessage(new StringTextComponent("正しい順にチェストを開けています"));
                BeforeChestNo = ChestNo;
                ChestNo++;
                LC = false;
            } else if (ChestNo == OpenChestNo) {
                if (BeforeChestNo == ChestNo) {
                    player.sendMessage(new StringTextComponent("正しい順にチェストを開けています"));
                } else {
                    BeforeChestNo = ChestNo;
                }
                LC = false;
            } else if (ChestNo <= OpenChestNo) {
                player.sendMessage(new StringTextComponent("次に開けるチェストはここではありません！"));
                LC = false;
            } else if (OpenChestNo <= ChestNo) {
                player.sendMessage(new StringTextComponent("このチェストはすでに開けられてます"));
                LC = false;
            }
            Chast = false;
        }
    }


    //アイテムをクリックした際に情報を取得
    ArrayList<ArrayList<String>>Items = new ArrayList<ArrayList<String>>();
    @SubscribeEvent
    public void onPlayerLeftClickItem(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
            if (!(player.inventory.getItemStack().getItem().toString().equals("air"))) {
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
    //boolean memory = true;
    @SubscribeEvent
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        String BlockName = event.getUseBlock().toString();
        String worldName = player.world.getWorldInfo().getWorldName();  //get World Name
        Calendar cl = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        ArrayList<String> BlockList = new ArrayList<String>();
        BlockList.add(sdf.format(cl.getTime()));
        BlockList.add(BlockName);
        BlockLists.add(BlockList);
    }

    //ファイル書き出し
    @SubscribeEvent
    public void onPlayerCrafted(PlayerEvent.ItemCraftedEvent event) {
        PlayerEntity player = event.getPlayer();
        String worldName = player.world.getWorldInfo().getWorldName();

        if(worldName.equals("Olevel5")){
            TwitchPlays.EndStage(5);
        }else{
            TwitchPlays.EndStage(0);
        }

        try{
            if(worldName.equals("level1")) {
                File newdir = new File("C:\\minecraft_mci_files");
                newdir.mkdir();
            }
            Date nowDate = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String formatNowDate = sdf1.format(nowDate);
            File file = new File("C:\\minecraft_mci_files\\" + formatNowDate+ "_" + worldName + "_MovementResult.csv");
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
        ResultVoids.ItemResult(worldName, Items);
        ResultVoids.BlockResult(worldName, BlockLists);
    }

    @SubscribeEvent
    public void onPlayerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent event){
        count = 0;
        dis = 0.0;
        PosLists.clear(); //Set position
        TS.clear(); //Set TimeStamp
        Dis.clear();    //Set Distance
        PitchYaws.clear();  //Set Pitch and Yaw
        //Chats.clear();
        Items.clear();
        BlockLists.clear();
        ChestNo = 0; //次に開けるべきチェストの番号-1
        BeforeChestNo = 0; //ひとつ前に開けたチェストの番号
        LC = true;
        Chast = false;
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
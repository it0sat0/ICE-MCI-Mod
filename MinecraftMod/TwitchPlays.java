package com.ice.mci_mod;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import com.ice.mci_mod.ExampleMod.*;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class TwitchPlays {
    private static final Logger LOGGER = LogManager.getLogger();

    private static PlayerEntity targetPlayer;

    //チェストの位置
    public static double CrPosotionXZ[][][] = {{{-105,-679},{0,0},{0,0},{0,0}},
                                                {{-517,642},{0,0},{0,0},{0,0}},
                                                {{116,58},{0,0},{0,0},{0,0}},
                                                {{3,56},{0,0},{0,0},{0,0}},
                                                {{347,-43},{0,0},{0,0},{0,0}}};
    public static double PositionY[][] = {{67.5,67.5,0,0},{66.5,66.5,0,0},{68.5,68.5,69.5,68.5},{0,64.5,64.5,66.5},{71.5,68.5,70.5,71.5}};
    public static double dis, vectx, vectz,X,Z;

    public static void join(String channel) {
        OAuth2Credential oAuth2Credential = new OAuth2Credential("twitch", "");
        TwitchClient client = TwitchClientBuilder.builder()
                .withEnableChat(true)
                .withChatAccount(oAuth2Credential)
                .build();

        client.getChat().joinChannel(channel);

        if(Objects.nonNull(targetPlayer)) {
            targetPlayer.sendMessage(new StringTextComponent("Joined channel : " + channel));
        }

        client.getEventManager().onEvent(ChannelMessageEvent.class, event -> {
            LOGGER.debug("[" + event.getChannel().getName() + "] " + event.getUser().getName() + ": " + event.getMessage());
            playerCommand(event.getMessage());
        });
    }


    public static void playerCommand(String message) {
        if (Objects.isNull(TwitchPlays.targetPlayer)) {
            return;
        }
        Vec3d move_short = new Vec3d(targetPlayer.getPosX(), targetPlayer.getPosY(), targetPlayer.getPosZ());
        /*
        switch (message) {
            case "w":
                move_short = movePlayer(0);
                break;
            case "a":
                move_short = movePlayer(-90);
                break;
            case "s":
                move_short = movePlayer(180);
                break;
            case "d":
                move_short = movePlayer(90);
                break;
            default:
                break;
        }
         */

        Vec3d move_long = new Vec3d(targetPlayer.getPosX(), targetPlayer.getPosY(), targetPlayer.getPosZ());
        /*
        boolean walk = false;
        switch (message) {
            case "W":
                walk = true;
                move_long = movePlayer(0);
                break;
            case "A":
                walk = true;
                move_long = movePlayer(-90);
                break;
            case "S":
                walk = true;
                move_long = movePlayer(180);
                break;
            case "D":
                walk = true;
                move_long = movePlayer(90);
                break;
            case "st":
                walk = false;
            default:
                break;
        }
        */

        Vec2f rotate = new Vec2f(targetPlayer.getPitchYaw().x,targetPlayer.getPitchYaw().y);

        int ChestNo[];
        switch (message) {
            /*
            case "vw":
                rotate = new Vec2f(rotate.x - 5f, rotate.y);
                break;
            case "va":
                rotate = new Vec2f(rotate.x, rotate.y - 5f);
                break;
            case "vs":
                rotate = new Vec2f(rotate.x + 5f, rotate.y);
                break;
            case "vd":
                rotate = new Vec2f(rotate.x, rotate.y + 5f);
                break;
             */
            case "op":   //Chest Open
                System.out.println("---------InToCommandOP--------------");
                ChestNo = OpenChest.OpenChestPosition(targetPlayer.getPosX(), targetPlayer.getPosZ(), ExampleMod.ChestPosotionXZ, true);
                LookAt(ExampleMod.ChestPosotionXZ,ChestNo[0],ChestNo[1]);
                rotate = new Vec2f((float)vectx, (float) vectz);
                targetPlayer.setPositionAndRotation(targetPlayer.getPosX(), targetPlayer.getPosY(), targetPlayer.getPosZ(), rotate.y, rotate.x);
                break;

            case "cr":   // Open
                System.out.println("---------InToCommandOPCR--------------");
                ChestNo = OpenChest.OpenChestPosition(targetPlayer.getPosX(), targetPlayer.getPosZ(), ExampleMod.ChestPosotionXZ, true);
                LookAt(CrPosotionXZ,ChestNo[0],0);
                rotate = new Vec2f((float)vectx, (float) vectz);
                targetPlayer.setPositionAndRotation(targetPlayer.getPosX(), targetPlayer.getPosY(), targetPlayer.getPosZ(), rotate.y, rotate.x);
                break;

            default:
        }

        targetPlayer.setPositionAndRotation(targetPlayer.getPosX(), targetPlayer.getPosY(), targetPlayer.getPosZ(), rotate.y, rotate.x);
        targetPlayer.setPositionAndUpdate(move_short.x, move_short.y, move_short.z);
    }

    public static Vec3d movePlayer(int dig) {
        Vec3d lookVec = targetPlayer.getLookVec();

        LOGGER.debug("position: (" + targetPlayer.getPosX() + ", " + targetPlayer.getPosY() + ", " + targetPlayer.getPosZ() + ")");
        LOGGER.debug("lookVec: (" + lookVec.x + ", " + lookVec.y + ", " + lookVec.z + ")");

        double vecX = lookVec.x * Math.cos(Math.toRadians(dig)) - lookVec.z * Math.sin(Math.toRadians(dig));
        double vecZ = lookVec.x * Math.sin(Math.toRadians(dig)) + lookVec.z * Math.cos(Math.toRadians(dig));
        Vec3d newPosition = new Vec3d(targetPlayer.getPosX() + vecX, targetPlayer.getPosY(), targetPlayer.getPosZ() + vecZ);
        Block frontBlock = targetPlayer.world.getBlockState(new BlockPos(newPosition)).getBlock();
        // 先方が空気ブロックのときはジャンプしない
        if (frontBlock instanceof AirBlock) { //人間っていう種族の中の大学生なのかどうなのか。ブロックの中でのAirBlockなのかどうなのかを判定している。instanceofはinstanceofやろ
            return newPosition;
        }
        return new Vec3d(newPosition.x, newPosition.y + 1, newPosition.z);
    }

    public static void LookAt(double Pos[][][], int Level,int No){
        X = Pos[Level][No][0];
        Z = Pos[Level][No][1];
        dis = Math.sqrt(Math.pow(X-targetPlayer.getPosX(),2)+Math.pow(Z-targetPlayer.getPosZ(),2));
        vectx = Math.toDegrees(Math.atan2(PositionY[Level][No]-targetPlayer.getPosYEye(),dis));
        vectz = Math.toDegrees(Math.atan2(Z-targetPlayer.getPosZ(),X-targetPlayer.getPosX()));
        System.out.println("Twi Level:" + (Level+1));
        System.out.println("Twi No:" + No);
        System.out.println("vectx:"+ vectx);
        System.out.println("vectz:"+ vectz);

        switch (Level) {
            case 0: //level1
                if (X > targetPlayer.getPosX() && Z > targetPlayer.getPosZ()) {
                    //3
                    vectz = 1.44 * vectz - 122;
                } else if (X < targetPlayer.getPosX() && Z < targetPlayer.getPosZ()) {
                    //1
                    vectz = vectz * 0.564 + 223;
                } else if (X > targetPlayer.getPosX()) {
                    //4
                    vectz = 0.908 * vectz - 114;
                } else {
                    //2
                    vectz = vectz * 1.68 - 143;
                }
                break;
            case 1: //level2
                if (X > targetPlayer.getPosX() && Z > targetPlayer.getPosZ()) {
                    //3
                    System.out.println("3333333333333333333");
                    vectz = 1.22*vectz -105;
                } else if (X < targetPlayer.getPosX() && Z < targetPlayer.getPosZ()) {
                    //1
                    System.out.println("11111111111111111111111");
                    vectz = 0.761*vectz +239;
                } else if (X > targetPlayer.getPosX()) {
                    //4
                    System.out.println("444444444444444444444");
                    vectz = 1.04*vectz -97.8;
                } else {
                    //2
                    System.out.println("2222222222222222222");
                    vectz = 0.558*vectz -3.44;
                }

                break;
            case 2: //level3
                if (X > targetPlayer.getPosX() && Z > targetPlayer.getPosZ()) {
                    //3
                    vectz = 0.891*vectz -94.1;
                    System.out.println("3333333333333333333");
                } else if (X < targetPlayer.getPosX() && Z < targetPlayer.getPosZ()) {
                    //1
                    vectz = 0.998*vectz +276;
                    System.out.println("111111111111111111111");
                } else if (X > targetPlayer.getPosX()) {
                    //4
                    vectz = 0.699*vectz -105;
                    System.out.println("44444444444444444444");
                } else {
                    //2
                    vectz = 1.01*vectz -95.6;
                    System.out.println("2222222222222222222");
                }

                break;
            case 3: //level4
                if (X > targetPlayer.getPosX() && Z > targetPlayer.getPosZ()) {
                    //3
                    System.out.println("3333333333333333333");
                    vectz = 0.723*vectz -76.8;
                } else if (X < targetPlayer.getPosX() && Z < targetPlayer.getPosZ()) {
                    //1
                    System.out.println("111111111111111111111");
                    vectz = 1.08*vectz +285;
                } else if (X > targetPlayer.getPosX()) {
                    //4
                    System.out.println("44444444444444444444");
                    vectz = 0.986*vectz -82;
                } else {
                    //2
                    vectz = 0.822*vectz -75.6;
                    System.out.println("2222222222222222222");
                }

                break;
            case 4: //level5
                if (X > targetPlayer.getPosX() && Z > targetPlayer.getPosZ()) {
                    //3
                    System.out.println("3333333333333333333");
                    vectz = 0.563*vectz -77.5;
                } else if (X < targetPlayer.getPosX() && Z < targetPlayer.getPosZ()) {
                    //1
                    System.out.println("111111111111111111111");
                    vectz = 0.868*vectz +265;
                } else if (X > targetPlayer.getPosX()) {
                    //4
                    System.out.println("44444444444444444444");
                    vectz = 0.685*vectz -107;
                } else {
                    //2
                    System.out.println("2222222222222222222");
                    vectz = 1.24*vectz -126;
                }
                break;
            default:
        }
        vectx = (-0.319)*vectx + 13.8;
    }

    public static void setTargetPlayer(PlayerEntity targetPlayer) {
        TwitchPlays.targetPlayer = targetPlayer;
    }
}

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
    //level1 and 2
    public static double LowLevel[][][] = {{{-132,-631},{-103,-678}},{{-494,719},{-515,643}}};
    //level6,8 and 11
    public static double HighLevel[][][] = {{{116,58},{115,42},{143,-13},{85,-6}},{{19,129},{38,93},{-17,111},{3,57}},{{352,20},{315,-57},{313,-4},{348,-41}}};


    public static void join(String channel) {
        OAuth2Credential oAuth2Credential = new OAuth2Credential("twitch", "oauth:");
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

        Vec2f rotate = new Vec2f(targetPlayer.getPitchYaw().x, targetPlayer.getPitchYaw().y);
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
            case "op":
                float addAng;
                if(LowLevel[0][1][0] > targetPlayer.getPosX() && LowLevel[0][1][1] > targetPlayer.getPosZ()){
                    addAng = -45;
                }else if(LowLevel[0][1][0] < targetPlayer.getPosX() && LowLevel[0][1][1] < targetPlayer.getPosZ()) {
                    addAng =  135;
                }else if(LowLevel[0][1][0] > targetPlayer.getPosX()){
                    addAng = -135;
                }else {
                    addAng = 45;
                }
                double vect=((LowLevel[0][1][1]-targetPlayer.getPosZ())*Math.sin(90))/((LowLevel[0][1][0]-targetPlayer.getPosX())+ Math.cos(90)*(LowLevel[0][1][1]-targetPlayer.getPosZ()));
                rotate = new Vec2f(rotate.x, (float) vect+addAng);
            default:
        }


        targetPlayer.setPositionAndRotation(move_short.x, move_short.y, move_short.z, rotate.y, rotate.x);
        targetPlayer.setPositionAndUpdate(move_short.x, move_short.y, move_short.z);
        //targetPlayer.setPositionAndUpdate(move_long.x, move_long.y, move_long.z);
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

    public static void setTargetPlayer(PlayerEntity targetPlayer) {
        TwitchPlays.targetPlayer = targetPlayer;
    }
}

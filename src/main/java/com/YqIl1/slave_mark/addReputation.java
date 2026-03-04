package com.YqIl1.slave_mark;

import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public class addReputation {
    public static void addReputation(Villager villager, Player player, Boolean positive) {
        try {
            Field gossipsField = Villager.class.getDeclaredField("gossips");
            gossipsField.setAccessible(true);
            GossipContainer gossips = (GossipContainer) gossipsField.get(villager);
            Method addMethod = GossipContainer.class.getDeclaredMethod("add", UUID.class, GossipType.class, int.class);
            addMethod.setAccessible(true);
            if (positive) {
                addMethod.invoke(gossips, player.getUUID(), GossipType.MAJOR_POSITIVE, ModConfig.COMMON.tradeExtraPrice.get());
            } else {
                addMethod.invoke(gossips, player.getUUID(), GossipType.MAJOR_NEGATIVE, ModConfig.COMMON.tradeExtraPrice.get());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

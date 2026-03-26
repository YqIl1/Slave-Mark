package com.YqIl1.slave_mark.item.minecraft.items;

import com.YqIl1.slave_mark.ModConfig;
import com.YqIl1.slave_mark.annotation.SlaveMarkItems;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@SlaveMarkItems(id="procrastination")
public class ProcrastinationItem extends Item {
    public ProcrastinationItem() {
        this(new Item.Properties().stacksTo(1).setNoRepair().durability(10));
    }
    public ProcrastinationItem(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("item.slave_mark.procrastination.desc1").withStyle(ChatFormatting.WHITE));
        tooltip.add(Component.translatable("item.slave_mark.procrastination.desc2").withStyle(ChatFormatting.YELLOW));
    }

    public static class EventHandler{

        private final Map<UUID, Stack<EntityType>> playerMobs = new HashMap<>();
        /*
        when the player hit a mob with this stick
        the mob is recorded into a stack correlating to the player's uuid
        after config time, it will be spawned again(10 mins for now)
        */
        @SubscribeEvent
        public void onHitMob(AttackEntityEvent event){
            if(event.getTarget() instanceof Player)return;
            Player player = (Player) event.getEntity();
            Level level = player.level();
            if (level.isClientSide)return;
            UUID playerUUID = player.getUUID();
            Entity target = event.getTarget();
            EntityType targetType = target.getType();
            ItemStack itemStack = player.getMainHandItem();
            itemStack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            Stack<EntityType> stack = playerMobs.computeIfAbsent(playerUUID, k -> new Stack<>());
            if(stack.isEmpty()){
                long currentTime = level.getGameTime();
                long delayTicks = ModConfig.COMMON.timeToProcrastinate.get()*20;
                player.getPersistentData().putLong("delayed_action_time", currentTime + delayTicks);
            }
            target.kill();
            stack.push(targetType);
        }
        @SubscribeEvent
        public void onTimePast(TickEvent.PlayerTickEvent event){
            Player player = event.player;
            if (event.phase != TickEvent.Phase.END) return; // 只执行一次
            if (player.level().isClientSide) return; // 只在服务端处理
            UUID playerId = player.getUUID();
            Stack<EntityType> stack = playerMobs.get(playerId);
            CompoundTag data = player.getPersistentData();
            if (data.contains("delayed_action_time")) {
                long triggerTime = data.getLong("delayed_action_time");
                if (player.level().getGameTime() >= triggerTime) {

                    stack.pop().spawn((ServerLevel) player.level(), new BlockPos((int)player.getX(), (int)player.getY()+1, (int)player.getZ()), MobSpawnType.COMMAND);
                    if(stack.isEmpty()){
                        playerMobs.remove(playerId);
                        data.remove("delayed_action_time");
                    }

                }
            }
        }
    }
}


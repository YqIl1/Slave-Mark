package com.YqIl1.slave_mark.item.minecraft.curios;

import com.YqIl1.slave_mark.ModConfig;
import com.YqIl1.slave_mark.annotation.SlaveMarkItems;
import com.YqIl1.slave_mark.item.BaseBlessingItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

@SlaveMarkItems(id = "wilderness")
public class WildernessItem extends BaseBlessingItem {
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        double attackSpeed = ModConfig.COMMON.attackSpeedBonus.get();
        tooltip.add(Component.translatable("item.slave_mark.wilderness.desc1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.wilderness.desc2").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.wilderness.desc3").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.wilderness.desc4").withStyle(ChatFormatting.YELLOW));
    }
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;
        if (!isWearing(player, "wilderness")) return;
        // 每20 tick (1秒) 恢复一次
        if (player.tickCount % 20 != 0) return;
        double speed = player.getAttributeValue(Attributes.MOVEMENT_SPEED);
        double baseSpeed = 0.1; // 原版玩家基础速度
        float maxHeal =  (float) (player.getAttributeValue(Attributes.MAX_HEALTH)*ModConfig.COMMON.movementHealMax.get());
        float healAmount = (float) (player.getMaxHealth() * ModConfig.COMMON.movementHealPercent.get()*baseSpeed);
        player.heal(Math.min(healAmount, maxHeal));
    }
}

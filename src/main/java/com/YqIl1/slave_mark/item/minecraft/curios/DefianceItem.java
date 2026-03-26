package com.YqIl1.slave_mark.item.minecraft.curios;

import com.YqIl1.slave_mark.ModConfig;
import com.YqIl1.slave_mark.annotation.SlaveMarkItems;
import com.YqIl1.slave_mark.item.BaseBlessingItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
@SlaveMarkItems(id="defiance")
public class DefianceItem extends BaseBlessingItem {
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        double attackSpeed = ModConfig.COMMON.attackSpeedBonus.get();
        tooltip.add(Component.translatable("item.slave_mark.defiance.desc1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.defiance.desc2").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.defiance.desc3").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.defiance.desc4").withStyle(ChatFormatting.YELLOW));
    }
    public static class EventHandler {
        @SubscribeEvent
        public void onLivingHurt(LivingHurtEvent event) {
            if (event.getSource().getEntity() instanceof Player player && isWearing(player,"defiance")) {
                LivingEntity target = event.getEntity();
                if (!target.getMobType().equals(MobType.UNDEAD) && !target.getMobType().equals(MobType.ARTHROPOD)) {
                    event.setAmount((float) (event.getAmount() * (1+ ModConfig.COMMON.damageMultiplier.get())));
                }
            }
        }
    }
}
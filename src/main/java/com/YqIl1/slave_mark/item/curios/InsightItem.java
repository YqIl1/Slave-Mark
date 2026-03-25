package com.YqIl1.slave_mark.item.curios;

import com.YqIl1.slave_mark.ModConfig;
import com.YqIl1.slave_mark.annotation.SlaveMarkItems;
import com.YqIl1.slave_mark.item.BaseBlessingItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

@SlaveMarkItems(id="insight")
public class InsightItem extends BaseBlessingItem {
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        double attackSpeed = ModConfig.COMMON.attackSpeedBonus.get();
        tooltip.add(Component.translatable("item.slave_mark.insight.desc1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.insight.desc2").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.insight.desc3").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.insight.desc4").withStyle(ChatFormatting.YELLOW));
    }
    public static class EventHandler {
        @SubscribeEvent
        public void onEnchantmentLevelSet(EnchantmentLevelSetEvent event) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            for (Player player : level.players()) {
                if (player.containerMenu instanceof EnchantmentMenu menu) {
                    BlockPos menuPos = getEnchantmentTablePos(menu);
                    if (menuPos != null && menuPos.equals(pos) && isWearing(player,"insight")) {
                        // 将附魔等级增加100
                        event.setEnchantLevel(event.getEnchantLevel() + ModConfig.COMMON.enchantmentLevelIncrement.get());
                        break;
                    }
                }
            }
        }

        private BlockPos getEnchantmentTablePos(EnchantmentMenu menu) {
            try {
                Field field = EnchantmentMenu.class.getDeclaredField("access");
                field.setAccessible(true);
                ContainerLevelAccess access = (ContainerLevelAccess) field.get(menu);
                return access.evaluate((lvl, p) -> p).orElse(null);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
package com.YqIl1.slave_mark.item.minecraft.curios;

import com.YqIl1.slave_mark.*;
import com.YqIl1.slave_mark.annotation.SlaveMarkItems;
import com.YqIl1.slave_mark.item.BaseBlessingItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.TradeWithVillagerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
@SlaveMarkItems(id = "heroism")
public class HeroismItem extends BaseBlessingItem {
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        double attackSpeed = ModConfig.COMMON.attackSpeedBonus.get();
        tooltip.add(Component.translatable("item.slave_mark.heroism.desc1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.heroism.desc2").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.heroism.desc3").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.heroism.desc4").withStyle(ChatFormatting.YELLOW));
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;
        player.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 100, 99, false, false, true));
    }

    public static class EventHandler {
        @SubscribeEvent
        public void onVillagerTrade(TradeWithVillagerEvent event) {
            AbstractVillager villager = event.getAbstractVillager();
            Player player = event.getEntity();
            if (isWearing(player,"heroism")) {
                MerchantOffer offer = event.getMerchantOffer();
                addReputation.addReputation((Villager) villager, player,true);
                offer.resetUses(); // 重置已使用次数
            }
        }

    }
}
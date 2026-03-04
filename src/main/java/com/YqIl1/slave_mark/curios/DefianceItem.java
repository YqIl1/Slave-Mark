package com.YqIl1.slave_mark.curios;

import com.YqIl1.slave_mark.ModConfig;
import com.YqIl1.slave_mark.ModItems;
import com.YqIl1.slave_mark.UniqueCurioHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.UUID;

public class DefianceItem extends Item implements ICurioItem {
    public DefianceItem(Properties properties) {
        super(properties.stacksTo(1));
        MinecraftForge.EVENT_BUS.register(new EventHandler());

    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        double attackSpeed = ModConfig.COMMON.attackSpeedBonus.get();
        tooltip.add(Component.translatable("item.slave_mark.defiance.desc1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.defiance.desc2").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.defiance.desc3").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.defiance.desc4").withStyle(ChatFormatting.YELLOW));
    }
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.entity() instanceof Player player &&
                UniqueCurioHelper.canEquipUnique(player, stack);
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
        // 添加槽位修饰符：佩戴此物品时增加 1 个 blessing_slot
        CuriosApi.addSlotModifier(map, "blessing_slot", uuid, 1, AttributeModifier.Operation.ADDITION);
        // 可以添加其他属性（如攻击速度+1）...
        return map;
    }

    public static class EventHandler {
        @SubscribeEvent
        public void onLivingHurt(LivingHurtEvent event) {
            if (event.getSource().getEntity() instanceof Player player && isWearingDefiance(player)) {
                LivingEntity target = event.getEntity();
                if (!target.getMobType().equals(MobType.UNDEAD) && !target.getMobType().equals(MobType.ARTHROPOD)) {
                    event.setAmount((float) (event.getAmount() * (1+ ModConfig.COMMON.damageMultiplier.get())));
                }
            }
        }

        private boolean isWearingDefiance(Player player) {
            return CuriosApi.getCuriosHelper().findFirstCurio(player, ModItems.DEFIANCE.get()).isPresent();
        }
    }
}
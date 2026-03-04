package com.YqIl1.slave_mark.curios;

import com.YqIl1.slave_mark.ModConfig;
import com.YqIl1.slave_mark.ModItems;
import com.YqIl1.slave_mark.UniqueCurioHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class InsightItem extends Item implements ICurioItem {
    public InsightItem(Properties properties) {
        super(properties.stacksTo(1));
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.entity() instanceof Player player &&
                UniqueCurioHelper.canEquipUnique(player, stack);
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        double attackSpeed = ModConfig.COMMON.attackSpeedBonus.get();
        tooltip.add(Component.translatable("item.slave_mark.insight.desc1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.insight.desc2").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.insight.desc3").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.insight.desc4").withStyle(ChatFormatting.YELLOW));
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
        public void onEnchantmentLevelSet(EnchantmentLevelSetEvent event) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            for (Player player : level.players()) {
                if (player.containerMenu instanceof EnchantmentMenu menu) {
                    BlockPos menuPos = getEnchantmentTablePos(menu);
                    if (menuPos != null && menuPos.equals(pos) && isWearingInsight(player)) {
                        // 将附魔等级增加100
                        event.setEnchantLevel(event.getEnchantLevel() + ModConfig.COMMON.enchantmentLevelIncrement.get());
                        break;
                    }
                }
            }
        }

        private boolean isWearingInsight(Player player) {
            return CuriosApi.getCuriosHelper().findFirstCurio(player, ModItems.INSIGHT.get()).isPresent();
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
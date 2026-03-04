package com.YqIl1.slave_mark.curios;
import com.YqIl1.slave_mark.ModConfig;
import com.YqIl1.slave_mark.SlaveMark;
import com.YqIl1.slave_mark.UniqueCurioHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ibm.icu.impl.locale.XCldrStub;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.UUID;

public class FreedomItem extends net.minecraft.world.item.Item implements ICurioItem {

    private static final UUID ATTACK_SPEED_BOOST_UUID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1");

    public FreedomItem(Properties properties) {
        super(properties.stacksTo(1));

    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        double attackSpeed = ModConfig.COMMON.attackSpeedBonus.get();
        tooltip.add(Component.translatable("item.slave_mark.freedom.desc1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.freedom.desc2").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.freedom.desc3").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.freedom.desc4").withStyle(ChatFormatting.YELLOW));
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

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;
        // 攻击速度+1 (加法操作)
        var instance = player.getAttribute(Attributes.ATTACK_SPEED);
        if (instance != null && instance.getModifier(ATTACK_SPEED_BOOST_UUID) == null) {
            instance.addTransientModifier(new AttributeModifier(ATTACK_SPEED_BOOST_UUID, "Freedom boost", ModConfig.COMMON.attackSpeedBonus.get(), AttributeModifier.Operation.ADDITION));
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;
        var instance = player.getAttribute(Attributes.ATTACK_SPEED);
        if (instance != null) {
            instance.removeModifier(ATTACK_SPEED_BOOST_UUID);
        }
    }
}

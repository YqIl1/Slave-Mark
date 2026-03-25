package com.YqIl1.slave_mark.item;

import com.YqIl1.slave_mark.SlaveMark;
import com.YqIl1.slave_mark.UniqueCurioHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public abstract class BaseBlessingItem extends Item implements ICurioItem {
    public BaseBlessingItem(){
        this(new Item.Properties().stacksTo(1).fireResistant());
    }
    public BaseBlessingItem(Item.Properties properties) {
        super(properties);
    }
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.entity() instanceof Player player &&
                UniqueCurioHelper.canEquipUnique(player, stack);
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
        CuriosApi.addSlotModifier(map, "blessing_slot", uuid, 1, AttributeModifier.Operation.ADDITION);
        return map;
    }
    public static boolean isWearing(Player player, String curio) {
        return CuriosApi.getCuriosHelper().findFirstCurio(player, ForgeRegistries.ITEMS.getValue(new ResourceLocation(SlaveMark.MODID, curio))).isPresent();
    }
}

package com.YqIl1.slave_mark;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;

public class UniqueCurioHelper {
    public static boolean canEquipUnique(Player player, ItemStack stack) {
        // 获取当前物品的注册名（如 "slave_mark:freedom"）
        ResourceLocation currentItemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (currentItemId == null) return true; // 如果物品未注册，默认允许

        return CuriosApi.getCuriosHelper().getCuriosHandler(player).map(handler -> {
            // 遍历所有槽位类型（如 "charm", "ring", "blessing_slot" 等）
            for (Map.Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                ICurioStacksHandler stacksHandler = entry.getValue();
                // 遍历该类型下的每个槽位（因为有的类型可能有多个槽位）
                for (int i = 0; i < stacksHandler.getSlots(); i++) {
                    ItemStack itemInSlot = stacksHandler.getStacks().getStackInSlot(i);
                    if (!itemInSlot.isEmpty()) {
                        ResourceLocation itemInSlotId = ForgeRegistries.ITEMS.getKey(itemInSlot.getItem());
                        if (itemInSlotId != null && itemInSlotId.equals(currentItemId)) {
                            // 找到了一个相同的饰品，禁止再次佩戴
                            return false;
                        }
                    }
                }
            }
            // 没有找到相同的饰品，允许佩戴
            return true;
        }).orElse(true); // 如果获取处理器失败，默认允许佩戴
    }
}
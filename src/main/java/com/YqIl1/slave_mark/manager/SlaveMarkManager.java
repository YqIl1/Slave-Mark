package com.YqIl1.slave_mark.manager;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.apache.commons.logging.impl.WeakHashtable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;


import java.util.*;

public class SlaveMarkManager {
    private static final Map<UUID, Map<Class<?>, List<SlotResult>>> ABILITY_CACHE = new WeakHashMap<>();
    private static void refresh(LivingEntity entity) {
        Map<Class<?>, List<SlotResult>> playerAbilities = new HashMap<>();
        CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(handler -> {
            Map<String, ICurioStacksHandler> curios = handler.getCurios();
            for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                String identifier = entry.getKey();
                IItemHandlerModifiable stacks = entry.getValue().getStacks();

                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    if (stack.isEmpty()) continue;

                    Item item = stack.getItem();
                    SlotContext slotContext = new SlotContext(identifier, entity, i, false, true);
                    SlotResult result = new SlotResult(slotContext, stack);

                }
            }
        });
        ABILITY_CACHE.put(entity.getUUID(), playerAbilities);
    }
    public static void clear(UUID playerUUID) {
        ABILITY_CACHE.remove(playerUUID);
    }
    public static <T> List<SlotResult> getCurio(LivingEntity entity, Class<T> clazz) {
        return ABILITY_CACHE
                .getOrDefault(entity.getUUID(), Collections.emptyMap())
                .getOrDefault(clazz, Collections.emptyList());
    }
}

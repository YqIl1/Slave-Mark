package com.YqIl1.slave_mark;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import com.YqIl1.slave_mark.SlaveMark;
import com.YqIl1.slave_mark.ModItems;

public class ModCreativeTab {
    // 创建 CreativeModeTab 的延迟注册器
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SlaveMark.MODID);

    // 注册一个自定义标签页
    public static final RegistryObject<CreativeModeTab> SLAVE_MARK_TAB = CREATIVE_MODE_TABS.register("slave_mark_tab",
            () -> CreativeModeTab.builder()
                    // 设置标签页图标，这里用奴隶烙印作为图标
                    .icon(() -> new ItemStack(ModItems.SLAVE_MARK.get()))
                    // 设置显示名称（使用本地化键）
                    .title(Component.translatable("itemGroup." + SlaveMark.MODID))
                    // 设置标签页中显示的默认物品
                    .displayItems((parameters, output) -> {
                        // 添加你的所有饰品
                        output.accept(ModItems.SLAVE_MARK.get());      // 奴隶烙印
                        output.accept(ModItems.FREEDOM.get());         // 自由
                        output.accept(ModItems.WILDERNESS.get());      // 旷野
                        output.accept(ModItems.INSIGHT.get());         // 真知
                        output.accept(ModItems.WEALTH.get());       // 富足
                        output.accept(ModItems.VISION.get());          // 灼见
                        output.accept(ModItems.WELLBEING.get());          // 健康
                        output.accept(ModItems.DEFIANCE.get());        // 抗争
                        output.accept(ModItems.HEROISM.get());         // 英雄
                        output.accept(ModItems.WEALTH.get());          // 财富（时运/抢夺）
                        // 如果以后有更多物品，继续添加
                    })
                    .build());
}
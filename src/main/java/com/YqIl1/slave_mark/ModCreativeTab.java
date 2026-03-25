package com.YqIl1.slave_mark;

import com.YqIl1.slave_mark.register.SlaveMarkItemsRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ModCreativeTab {
    // 创建 CreativeModeTab 的延迟注册器
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SlaveMark.MODID);

    // 注册一个自定义标签页
    public static final RegistryObject<CreativeModeTab> SLAVE_MARK_TAB = CREATIVE_MODE_TABS.register(SlaveMark.MODID,
            () -> CreativeModeTab.builder()
                    // 设置标签页图标，这里用奴隶烙印作为图标
                    .icon(() -> new ItemStack(Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation(SlaveMark.MODID, "slave_mark")))))
                    // 设置显示名称（使用本地化键）
                    .title(Component.translatable("itemGroup." + SlaveMark.MODID))
                    // 设置标签页中显示的默认物品
                    .displayItems((parameters, output) -> {
                        // 添加你的所有饰品
                        for(RegistryObject<Item> item:SlaveMarkItemsRegister.ITEMS.getEntries()){
                            output.accept(item.get());
                        }
                    })
                    .build());
}
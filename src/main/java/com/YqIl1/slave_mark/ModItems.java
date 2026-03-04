package com.YqIl1.slave_mark;

import com.YqIl1.slave_mark.curios.*;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // 创建 DeferredRegister
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SlaveMark.MODID);

    // 注册奴隶烙印物品
    public static final RegistryObject<Item> SLAVE_MARK = ITEMS.register("slave_mark",
            () -> new SlaveMarkItem(new Item.Properties()));
    public static final RegistryObject<Item> FREEDOM = ITEMS.register("freedom",
            () -> new FreedomItem(new Item.Properties()));
    public static final RegistryObject<Item> WILDERNESS = ITEMS.register("wilderness",
            () -> new WildernessItem(new Item.Properties()));
    public static final RegistryObject<Item> INSIGHT = ITEMS.register("insight",
            () -> new InsightItem(new Item.Properties()));
    public static final RegistryObject<Item> WEALTH = ITEMS.register("wealth",
            () -> new WealthItem(new Item.Properties()));
    public static final RegistryObject<Item> VISION = ITEMS.register("vision",
            () -> new VisionItem(new Item.Properties()));
    public static final RegistryObject<Item> WELLBEING = ITEMS.register("wellbeing",
            () -> new Wellbeing(new Item.Properties()));
    public static final RegistryObject<Item> DEFIANCE = ITEMS.register("defiance",
            () -> new DefianceItem(new Item.Properties()));
    public static final RegistryObject<Item> HEROISM = ITEMS.register("heroism",
            () -> new HeroismItem(new Item.Properties()));

    //注册创造物品栏

}

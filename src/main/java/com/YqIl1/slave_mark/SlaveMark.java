package com.YqIl1.slave_mark;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SlaveMark.MODID)
public class SlaveMark {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "slave_mark";
    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent


    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public SlaveMark(FMLJavaModLoadingContext context) {

        IEventBus modEventBus = context.getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTab.CREATIVE_MODE_TABS.register(modEventBus);
        // Register the commonSetup method for modloading
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_SPEC);
        // Register the Deferred Register to the mod event bus so blocks get registered

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us

    }


    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
        }

    }

    public SlaveMark() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        // 其他注册...
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @Mod.EventBusSubscriber(modid = SlaveMark.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModSetup {
        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            event.enqueueWork(() -> {
                // 基础等级均为0（I级），叠加后等级提升
                SlaveMarkAPI.registerSicklyDebuff(new ResourceLocation("minecraft", "weakness"), 1200, 0);
                SlaveMarkAPI.registerSicklyDebuff(new ResourceLocation("minecraft", "slowness"), 1200, 0);
                SlaveMarkAPI.registerSicklyDebuff(new ResourceLocation("minecraft", "mining_fatigue"), 1200, 0);
                SlaveMarkAPI.registerSicklyDebuff(new ResourceLocation("minecraft", "hunger"), 1200, 0);
                SlaveMarkAPI.registerSicklyDebuff(new ResourceLocation("minecraft", "confusion"), 1200, 0);
                SlaveMarkAPI.registerSicklyDebuff(new ResourceLocation("minecraft", "blindness"), 1200, 0);
                SlaveMarkAPI.registerSicklyDebuff(new ResourceLocation("minecraft", "poison"), 1200, 0);
                SlaveMarkAPI.registerSicklyDebuff(new ResourceLocation("minecraft", "wither"), 1200, 0);
            });
        }
    }
}


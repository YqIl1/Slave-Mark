package com.YqIl1.slave_mark.curios;

import com.YqIl1.slave_mark.*;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.List;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.Level;


public class SlaveMarkItem extends Item implements ICurioItem {

    private static boolean hasCounterItem(Player player, Item item) {
        return CuriosApi.getCuriosHelper().findFirstCurio(player, item).isPresent();
    }
    static {
        System.out.println("=== SlaveMarkItem static init start ===");
        try {
            System.out.println("ModConfig.COMMON = " + ModConfig.COMMON);
        } catch (Throwable t) {
            System.err.println("!!! Exception in SlaveMarkItem static init:");
            t.printStackTrace();
        }
        System.out.println("=== SlaveMarkItem static init end ===");
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        // 添加描述行（灰色斜体）
        tooltip.add(Component.translatable("item.slave_mark.slave_mark.desc1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.slave_mark.desc2").withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("item.slave_mark.slave_mark.desc3").withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("item.slave_mark.slave_mark.desc4").withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("item.slave_mark.slave_mark.desc5").withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("item.slave_mark.slave_mark.desc6").withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("item.slave_mark.slave_mark.desc7").withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("item.slave_mark.slave_mark.desc8").withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("item.slave_mark.slave_mark.desc9").withStyle(ChatFormatting.RED));
        tooltip.add(Component.translatable("item.slave_mark.slave_mark.desc10").withStyle(ChatFormatting.GRAY));
    }
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.entity() instanceof Player player &&
                UniqueCurioHelper.canEquipUnique(player, stack);
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return slotContext.entity() instanceof Player player && player.isCreative();
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
        // 添加槽位修饰符：佩戴此物品时增加 1 个 blessing_slot
        CuriosApi.addSlotModifier(map, "blessing_slot", uuid, 1, AttributeModifier.Operation.ADDITION);
        // 可以添加其他属性（如攻击速度+1）...
        return map;
    }

    // 属性修饰符的固定UUID（确保不会重复）
    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID MOVEMENT_SPEED_UUID = UUID.fromString("33333333-3333-3333-3333-333333333333");


    public SlaveMarkItem(Properties properties) {
        super(properties.stacksTo(1)); // 不可堆叠
        // 注册事件处理器（静态内部类或外部类均可）
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    // ---------- 属性修改（佩戴/卸下时生效）----------
    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;

        // 攻击速度 -90%
        if(!hasCounterItem(player, ModItems.FREEDOM.get())){
            addModifier(player, Attributes.ATTACK_SPEED, ATTACK_SPEED_UUID, "Cursed attack speed",
                    ModConfig.COMMON.attackSpeedPenalty.get()-1, // 注意是负值
                    AttributeModifier.Operation.MULTIPLY_TOTAL);
        }else {
            removeModifier(player, Attributes.ATTACK_SPEED,ATTACK_SPEED_UUID);
        }

        // 挖掘效率 -90%
        //addModifier(player, Attributes.MINING_EFFICIENCY, MINING_SPEED_UUID, "Cursed mining speed", -0.9, AttributeModifier.Operation.MULTIPLY_TOTAL);
        // 移动速度 -90%
        //addModifier(player, Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_UUID, "Cursed movement speed", -0.9, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
    // 在 SlaveMarkItem 中添加方法
    private void applySicklyEffect(Player player) {
        if(hasCounterItem(player, ModItems.WELLBEING.get()))return;
        // 从 API 获取随机效果条目
        var entry = SlaveMarkAPI.getRandomSicklyDebuff();
        if (entry == null) return;

        // 获取效果实例
        MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(entry.getEffectId());
        if (effect == null) return;

        // 检查玩家当前是否已有此效果
        MobEffectInstance current = player.getEffect(effect);
        int newAmplifier;
        if (current == null) {
            newAmplifier = 1;
        } else {
            newAmplifier = Math.min(current.getAmplifier() + 1,
                    ModConfig.COMMON.sicklyMaxAmplifier.get());
        }
        player.addEffect(new MobEffectInstance(effect,
                ModConfig.COMMON.sicklyDuration.get(),
                newAmplifier, false, false, true));
    }


    private void addModifier(Player player, Attribute attribute, UUID uuid, String name,double amount, AttributeModifier.Operation operation) {
        var instance = player.getAttribute(attribute);
        if (instance != null && instance.getModifier(uuid) == null) {
            instance.addTransientModifier(new AttributeModifier(uuid, name, amount, operation));
        }
    }
    private void removeModifier(Player player, net.minecraft.world.entity.ai.attributes.Attribute attribute, UUID uuid) {
        var instance = player.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(uuid);
        }
    }
    // ---------- 每tick效果（药水效果、病弱计时、灾厄之兆、经验等级清零）----------
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;

        // 判断减速
        if(hasCounterItem(player, ModItems.FREEDOM.get())){
            removeModifier(player, Attributes.ATTACK_SPEED,ATTACK_SPEED_UUID);
        }
        // 每 20 tick 检测一次
        if (player.tickCount % 20 != 0) return;

        // 获取玩家周围 10 格内的所有存活实体
        List<LivingEntity> entities = player.level().getEntitiesOfClass(
                LivingEntity.class,
                player.getBoundingBox().inflate(10),
                entity -> entity.isAlive() && entity != player
        );

        // 判断其中是否存在敌对生物
        boolean hasHostile = entities.stream()
                .anyMatch(entity -> entity instanceof Monster); // 检查是否是怪物实例

        // 应用或移除效果
        if (hasHostile) {
            if (!hasCounterItem(player, ModItems.WILDERNESS.get())) {
                addModifier(player, Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_UUID, "Cursed movement speed", ModConfig.COMMON.attackSpeedPenalty.get()-1, AttributeModifier.Operation.MULTIPLY_TOTAL);
            }else{
                removeModifier(player, Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_UUID);
            }
        }else{
            removeModifier(player, Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_UUID);
        }


        // 饥困：饥饿5 + 反胃
        if (!hasCounterItem(player, ModItems.WEALTH.get())) {
            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 4, false, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0, false, false, true));
        }else{
            player.removeEffect(MobEffects.HUNGER);
            player.removeEffect(MobEffects.CONFUSION);
        }
        // 无根：最大等级灾厄之兆
        if (hasCounterItem(player, ModItems.HEROISM.get())) {
            player.removeEffect(MobEffects.BAD_OMEN);
        }else {
            player.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 100, 4, false, false, true));
        }
        // 病弱：每30秒随机一个debuff（使用玩家持久化数据计时）
        var data = player.getPersistentData();
        if (ModConfig.COMMON.enableSickly.get()) {
            long lastApply = data.getLong("slave_mark_sickly");
            long gameTime = player.level().getGameTime();

            if (gameTime - lastApply >= ModConfig.COMMON.sicklyInterval.get()) {
                if(!hasCounterItem(player, ModItems.WELLBEING.get())) {
                    applySicklyEffect(player);
                    data.putLong("slave_mark_sickly", gameTime);
                }
            }

        }
    }


    // ---------- 事件监听（经验获取、交易价格）----------
    public static class EventHandler {
        private BlockPos getEnchantmentTablePos(EnchantmentMenu menu) {
            try {
                // 获取私有的 access 字段
                Field field = EnchantmentMenu.class.getDeclaredField("access");
                field.setAccessible(true);
                ContainerLevelAccess access = (ContainerLevelAccess) field.get(menu);
                // 通过 access 获取附魔台位置
                return access.evaluate((level, pos) -> pos).orElse(null);
            } catch (Exception e) {
                // 打印异常以便调试
                e.printStackTrace();
                return null;
            }
        }
        @SubscribeEvent
        public void onEnchantmentLevelSet(EnchantmentLevelSetEvent event) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();

            for (Player player : level.players()) {
                if (player.containerMenu instanceof EnchantmentMenu menu) {
                    BlockPos menuPos = getEnchantmentTablePos(menu);
                    if (menuPos != null && menuPos.equals(pos)) {
                        if (isWearingCurse(player)) {
                            if (!hasCounterItem(player, ModItems.INSIGHT.get())) {
                                event.setEnchantLevel(ModConfig.COMMON.forceEnchantmentLevel.get());
                            }
                        }
                        break;
                    }
                }
            }
        }


        //声望



        private boolean isWearingCurse(Player player) {
            return CuriosApi.getCuriosHelper().findFirstCurio(player, ModItems.SLAVE_MARK.get()).isPresent();
        }
        @SubscribeEvent
        public void onXpPickup(PlayerXpEvent.PickupXp event) {
            Player player = event.getEntity();
            if (isWearingCurse(player)) {
                if (hasCounterItem(player, ModItems.VISION.get())) {
                    event.getOrb().value *= ModConfig.COMMON.xpIncrease.get();
                }else {
                    // 盲目：获取经验 -90%
                    event.getOrb().value = (int) (event.getOrb().value * ModConfig.COMMON.xpMultiplier.get());
                }
            }
        }
        @SubscribeEvent
        public void onBreakSpeed(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {
            Player player = event.getEntity();
            if (isWearingCurse(player)) {
                if (!hasCounterItem(player, ModItems.FREEDOM.get())) {
                    // 挖掘速度降低90%
                    event.setNewSpeed((float) (event.getOriginalSpeed() * ModConfig.COMMON.miningSpeedPenalty.get() - 1));
                }
            }
        }
        @SubscribeEvent
        public void onOpeningVillagerTrade(PlayerInteractEvent event) {
            Player player = event.getEntity();
            if (!isWearingCurse(player)) return;
            if (hasCounterItem(player, ModItems.VISION.get())) return;
            player.level().getEntitiesOfClass(Villager.class, player.getBoundingBox().inflate(16))
                    .forEach(villager -> addReputation.addReputation(villager, player, false));


        }

    }
}

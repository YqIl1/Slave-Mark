package com.YqIl1.slave_mark.curios;

import com.YqIl1.slave_mark.ModConfig;
import com.YqIl1.slave_mark.ModItems;
import com.YqIl1.slave_mark.UniqueCurioHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class WealthItem extends Item implements ICurioItem {
    private static final Random RANDOM = new Random();

    public WealthItem(Properties properties) {
        super(properties.stacksTo(1));
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        double attackSpeed = ModConfig.COMMON.attackSpeedBonus.get();
        tooltip.add(Component.translatable("item.slave_mark.wealth.desc1").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.wealth.desc2").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.wealth.desc3").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.slave_mark.wealth.desc4").withStyle(ChatFormatting.YELLOW));
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
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.entity() instanceof Player player &&
                UniqueCurioHelper.canEquipUnique(player, stack);
    }

    // 事件处理器内部类
    public static class EventHandler {
        private boolean isWearing(Player player) {
            return CuriosApi.getCuriosHelper().findFirstCurio(player, ModItems.WEALTH.get()).isPresent();
        }

        // 时运效果：破坏方块时额外掉落 1~3 个物品（针对可掉落物品的方块）
        @SubscribeEvent
        public void onBlockBreak(BlockEvent.BreakEvent event) {
            Player player = event.getPlayer();
            if (!isWearing(player)) return;

            // 获取方块状态和位置
            BlockState state = event.getState();
            Level level = (Level) event.getLevel();
            BlockPos pos = event.getPos();

            // 取消原版掉落（否则可能会掉落两次
            event.setCanceled(true);
            // 5. 计算基础掉落 (1次) 并生成

            if (level instanceof ServerLevel serverLevel) {
                level.removeBlock(pos, false);
                // 基础掉落 (使用 Block.getDrops，它会自动考虑工具的时运附魔)
                ItemStack fakeTool = player.getMainHandItem().copy();
                fakeTool.enchant(Enchantments.BLOCK_FORTUNE, 3);
                List<ItemStack> baseDrops = Block.getDrops(state, serverLevel, pos, null, player, fakeTool);
                for (ItemStack stack : baseDrops) {
                    Block.popResource(level, pos, stack.copy());
                }

                /*// 6. 额外进行 1-3 次时运判定
                Random random = new Random();
                int extraRolls = random.nextInt(3) + 1; // 1 到 3
                for (int i = 0; i < extraRolls; i++) {
                    // 再次调用 Block.getDrops 模拟一次独立的时运判定
                    List<ItemStack> extraDrops = Block.getDrops(state, serverLevel, pos, null, player, player.getMainHandItem());
                    for (ItemStack stack : extraDrops) {
                        Block.popResource(level, pos, stack.copy());
                    }
                }*/

                // 7. 处理经验掉落 (原版经验值，你可以根据需要调整)
                int exp = event.getExpToDrop();
                if (exp > 0) {
                    // 这里可以添加生成经验球的逻辑，例如：
                    //ExperienceOrb experienceOrb = new ExperienceOrb(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, exp);
                    //level.addFreshEntity(experienceOrb);
                }
            }
        }
        // 抢夺效果：击杀生物时额外掉落 1~3 个该生物的主要掉落物（如腐肉、骨头等）
        @SubscribeEvent
        public void onLivingDrops(LivingDropsEvent event) {
            // 1. 检查击杀者是否是玩家
            if (!(event.getSource().getEntity() instanceof Player player)) return;
            // 2. 检查玩家是否佩戴了 wealth 饰品
            if (!isWearing(player)) return;

            // 3. 获取被杀死的生物
            LivingEntity entity = event.getEntity();
            Level level = entity.level();
            if (level.isClientSide) return; // 只在服务端执行

            // 4. 取消原版掉落（因为我们手动处理）
            event.setCanceled(true);

            // 5. 构建战利品上下文（包含伤害来源、击杀者等）
            LootParams.Builder paramsBuilder = new LootParams.Builder((ServerLevel) level)
                    .withParameter(LootContextParams.ORIGIN, entity.position())
                    .withParameter(LootContextParams.THIS_ENTITY, entity)
                    .withParameter(LootContextParams.DAMAGE_SOURCE, event.getSource())
                    .withOptionalParameter(LootContextParams.KILLER_ENTITY, player)
                    .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, event.getSource().getDirectEntity());

            LootParams params = paramsBuilder.create(LootContextParamSets.ENTITY);

            // 6. 获取生物的战利品表
            ResourceLocation resourcelocation = entity.getLootTable();
            LootTable lootTable = entity.level().getServer().getLootData().getLootTable(resourcelocation);

            // 7. 进行 1–3 次额外抽取（包括基础的一次）
            Random random = new Random();
            int rolls = random.nextInt(3) + 1; // 1 到 3 次
            for (int i = 0; i < rolls; i++) {
                List<ItemStack> drops = lootTable.getRandomItems(params);
                for (ItemStack stack : drops) {
                    // 在生物死亡位置生成物品实体
                    entity.spawnAtLocation(stack.copy());
                }
            }
        }
    }
}
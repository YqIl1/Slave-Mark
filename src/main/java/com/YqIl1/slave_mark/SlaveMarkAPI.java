package com.YqIl1.slave_mark;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 奴隶烙印 API – 用于注册自定义的“病弱”效果
 */
public class SlaveMarkAPI {
    private static final List<DebuffEntry> SICKLY_DEBUFFS = new ArrayList<>();
    private static final Random RANDOM = new Random();

    // 初始化默认的 debuff（可选，也可以在这里添加默认效果）
    static {
        // 你可以选择将默认的 debuff 也通过 API 添加，或者直接在 SlaveMarkItem 中保留默认列表。
        // 为了统一，建议将所有 debuff 都通过 API 注册，包括默认的。
    }

    /**
     * 注册一个病弱效果
     * @param effectId 效果的注册名，例如 "minecraft:weakness"
     * @param duration 持续时间（tick）
     * @param amplifier 效果等级（0 = I级）
     */
    public static void registerSicklyDebuff(ResourceLocation effectId, int duration, int amplifier) {
        SICKLY_DEBUFFS.add(new DebuffEntry(effectId, duration, amplifier));
    }

    /**
     * 随机获取一个病弱效果（供内部使用）
     */
    public static DebuffEntry getRandomSicklyDebuff() {
        if (SICKLY_DEBUFFS.isEmpty()) {
            return null;
        }
        return SICKLY_DEBUFFS.get(RANDOM.nextInt(SICKLY_DEBUFFS.size()));
    }

    /**
     * 获取所有已注册的病弱效果（只读视图）
     */
    public static List<DebuffEntry> getAllSicklyDebuffs() {
        return Collections.unmodifiableList(SICKLY_DEBUFFS);
    }

    /**
     * 表示一个病弱效果的条目
     */
    public static class DebuffEntry {
        private final ResourceLocation effectId;
        private final int duration;
        private final int amplifier;

        public DebuffEntry(ResourceLocation effectId, int duration, int amplifier) {
            this.effectId = effectId;
            this.duration = duration;
            this.amplifier = amplifier;
        }

        public ResourceLocation getEffectId() { return effectId; }
        public int getDuration() { return duration; }
        public int getAmplifier() { return amplifier; }

        /**
         * 创建实际的 MobEffectInstance（需要确保效果已注册）
         */
        public MobEffectInstance createInstance() {
            MobEffect effect = ForgeRegistries.MOB_EFFECTS.getValue(effectId);
            if (effect == null) return null;
            return new MobEffectInstance(effect, duration, amplifier, false, false, true);
        }
    }
}
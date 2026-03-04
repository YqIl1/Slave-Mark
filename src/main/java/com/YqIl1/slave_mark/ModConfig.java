package com.YqIl1.slave_mark;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {
    // 公共配置（COMMON）实例
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        System.out.println("=== ModConfig static init ===");
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }

    public static class Common {
        // 攻击速度减成（百分比，0.9 = -90%）
        public final ForgeConfigSpec.DoubleValue attackSpeedPenalty;
        // 移动速度减成
        public final ForgeConfigSpec.DoubleValue movementSpeedPenalty;
        // 挖掘疲劳等级（代替挖掘效率减成）
        public final ForgeConfigSpec.DoubleValue miningSpeedPenalty;
        // 病弱效果间隔（tick）
        public final ForgeConfigSpec.IntValue sicklyInterval;
        // 病弱效果持续时间（tick）
        public final ForgeConfigSpec.IntValue sicklyDuration;
        // 病弱效果叠加上限（最大等级）
        public final ForgeConfigSpec.IntValue sicklyMaxAmplifier;
        // 经验获取减成（百分比，0.1 = 保留10%）
        public final ForgeConfigSpec.DoubleValue xpMultiplier;
        // 交易额外加价（specialPrice增加量）
        public final ForgeConfigSpec.IntValue tradeExtraPrice;
        // 是否强制经验等级为0
        public final ForgeConfigSpec.IntValue forceEnchantmentLevel;
        // 佩戴时是否每30秒触发病弱
        public final ForgeConfigSpec.BooleanValue enableSickly;
        //攻击速度加成
        public final ForgeConfigSpec.DoubleValue attackSpeedBonus;
        //移速回复数值
        public final ForgeConfigSpec.DoubleValue movementHealPercent;
        //移速回复上限
        public final ForgeConfigSpec.DoubleValue movementHealMax;
        //附魔等级增加
        public final ForgeConfigSpec.IntValue enchantmentLevelIncrement;
        //幸运增加
        public final ForgeConfigSpec.IntValue luckLevelIncrement;
        //经验获取增加
        public final ForgeConfigSpec.DoubleValue xpIncrease;
        //亡灵、节肢以外的生物造成伤害增加
        public final ForgeConfigSpec.DoubleValue damageMultiplier;
        //村庄英雄效果等级
        public final ForgeConfigSpec.DoubleValue heroLevelMultiplier;


        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("奴隶烙印模组配置").push("slave_mark");
            attackSpeedBonus = builder
                    .comment("攻击速度增加")
                    .defineInRange("attackSpeedBonus", 1, 0.0, 10);

            movementHealPercent = builder
                    .comment("移速回复比例 (0.2=移速*20%)")
                    .defineInRange("movementHealPercent", 0.2, 0.0, 1);

            movementHealMax = builder
                    .comment("移速回复上限")
                    .defineInRange("movementHealPercent", 0.2, 0.0, 1);

            enchantmentLevelIncrement = builder
                    .comment("附魔等级增加")
                    .defineInRange("enchantmentLevelIncrement", 100, 0, 255);

            luckLevelIncrement = builder
                    .comment("幸运增加")
                    .defineInRange("luckLevelIncrement", 3, 0, 255);

            xpIncrease = builder
                    .comment("经验获取增加 (3 = 300%)")
                    .defineInRange("xpIncrease", 3, 0.0, 255);

            damageMultiplier = builder
                    .comment("对亡灵、节肢以外的生物造成伤害增加 (1 = 100%)")
                    .defineInRange("damageMultiplier", 1.0, 0.0, 255);

            heroLevelMultiplier = builder
                    .comment("村庄英雄效果等级(0 = I级, 101 = C级)")
                    .defineInRange("heroLevelMultiplier", 101, 0.0, 255);

            attackSpeedPenalty = builder
                    .comment("攻击速度比例 (0.1 = 10%)")
                    .defineInRange("attackSpeedPenalty", 0.1, 0.0, 1.0);

            movementSpeedPenalty = builder
                    .comment("移动速度比例 (0.1 = 10%)")
                    .defineInRange("movementSpeedPenalty", 0.1, 0.0, 1.0);

            miningSpeedPenalty = builder
                    .comment("挖掘速度比例 (0.1 = 10%)")
                    .defineInRange("miningSpeedPenalty", 0.1, 0, 1);

            sicklyInterval = builder
                    .comment("病弱效果触发间隔（游戏刻，20刻=1秒）")
                    .defineInRange("sicklyInterval", 600, 1, 72000);

            sicklyDuration = builder
                    .comment("病弱效果持续时间（游戏刻）")
                    .defineInRange("sicklyDuration", 1200, 1, 72000);

            sicklyMaxAmplifier = builder
                    .comment("病弱效果最大等级 (0 = I级, 4 = V级)")
                    .defineInRange("sicklyMaxAmplifier", 4, 0, 4);

            xpMultiplier = builder
                    .comment("经验获取倍率 (0.1 = 保留10%)")
                    .defineInRange("xpMultiplier", 0.1, 0.0, 1.0);

            tradeExtraPrice = builder
                    .comment("交易额外加价 (specialPrice增加量)")
                    .defineInRange("tradeExtraPrice", 9999, 0, Integer.MAX_VALUE);

            forceEnchantmentLevel = builder
                    .comment("是否强制附魔等级为0")
                    .defineInRange("forceEnchantmentLevel", 0, 0,255);

            enableSickly = builder
                    .comment("是否启用病弱效果")
                    .define("enableSickly", true);

            builder.pop();
        }
    }
}
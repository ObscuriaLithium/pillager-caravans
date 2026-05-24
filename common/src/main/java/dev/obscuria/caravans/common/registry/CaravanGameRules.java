package dev.obscuria.caravans.common.registry;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import dev.obscuria.caravans.PillagerCaravans;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

public final class CaravanGameRules {

    public static final GameRule<Boolean> DO_CARAVAN_SPAWNING = registerBoolean("do_caravan_spawning", GameRuleCategory.SPAWNING, true);

    private static GameRule<Boolean> registerBoolean(String name, GameRuleCategory category, boolean defaultValue) {
        var gameRule = new GameRule<>(category, GameRuleType.BOOL, BoolArgumentType.bool(), GameRuleTypeVisitor::visitBoolean, Codec.BOOL, it -> it ? 1 : 0, defaultValue, FeatureFlags.DEFAULT_FLAGS);
        CaravanRegistries.REGISTRAR.register(Registries.GAME_RULE, PillagerCaravans.id(name), () -> gameRule);
        return gameRule;
    }

    public static void init() {}
}

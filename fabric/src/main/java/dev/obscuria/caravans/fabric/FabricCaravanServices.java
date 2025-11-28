package dev.obscuria.caravans.fabric;

import dev.obscuria.caravans.ICaravanServices;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public final class FabricCaravanServices implements ICaravanServices {

    @Override
    public GameRules.Key<GameRules.BooleanValue> registerGameRule(String name, GameRules.Category category, boolean value) {
        return GameRuleRegistry.register(name, category, GameRuleFactory.createBooleanRule(value));
    }
}

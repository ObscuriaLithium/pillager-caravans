package dev.obscuria.caravans.forge;

import dev.obscuria.caravans.ICaravanServices;
import net.minecraft.world.level.GameRules;

public final class ForgeCaravanServices implements ICaravanServices {

    @Override
    public GameRules.Key<GameRules.BooleanValue> registerGameRule(String name, GameRules.Category category, boolean value) {
        return GameRules.register(name, category, GameRules.BooleanValue.create(value));
    }
}

package dev.obscuria.caravans;

import net.minecraft.world.level.GameRules;

public interface ICaravanServices {

    GameRules.Key<GameRules.BooleanValue> registerGameRule(String name, GameRules.Category category, boolean value);
}

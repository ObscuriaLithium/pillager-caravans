package dev.obscuria.caravans.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.obscuria.caravans.content.caravan.CaravanSpawner;
import dev.obscuria.caravans.content.registry.CaravanRegistries;
import dev.obscuria.caravans.content.caravan.CaravanVariation;
import dev.obscuria.caravans.mixin.ResourceKeyArgumentAccessor;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

public final class CaravanCommand {

    private static final DynamicCommandExceptionType ERR_INVALID_VARIATION;

    public static void register(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext registryAccess,
            Commands.CommandSelection environment
    ) {
        dispatcher.register(Commands.literal("caravan").requires(it -> it.hasPermission(2))
                .then(Commands.literal("cooldown")
                        .then(Commands.literal("get")
                                .executes(it -> getCooldown(
                                        it.getSource())))
                        .then(Commands.literal("reset")
                                .executes(it -> resetCooldown(
                                        it.getSource())))
                        .then(Commands.literal("set")
                                .then(Commands.argument("ticks", IntegerArgumentType.integer(0))
                                        .executes(it -> setCooldown(
                                                it.getSource(),
                                                IntegerArgumentType.getInteger(it, "ticks"))))))
                .then(Commands.literal("spawn")
                        .then(Commands.argument("position", BlockPosArgument.blockPos())
                                .then(Commands.argument("variation", ResourceKeyArgument.key(CaravanRegistries.Keys.VARIATION))
                                        .executes(it -> spawn(
                                                it.getSource(),
                                                BlockPosArgument.getBlockPos(it, "position"),
                                                getCaravansVariation(it, "variation")))))));
    }

    public static int getCooldown(CommandSourceStack source) {
        final var ticks = CaravanSpawner.INSTANCE.cooldownTick;
        source.sendSuccess(() -> Component.translatable("commands.caravan.cooldown.get", ticks), false);
        return ticks;
    }

    public static int resetCooldown(CommandSourceStack source) {
        CaravanSpawner.INSTANCE.cooldownTick = 0;
        source.sendSuccess(() -> Component.translatable("commands.caravan.cooldown.reset"), true);
        return 1;
    }

    public static int setCooldown(CommandSourceStack source, int ticks) {
        CaravanSpawner.INSTANCE.cooldownTick = ticks;
        source.sendSuccess(() -> Component.translatable("commands.caravan.cooldown.set", ticks), true);
        return 1;
    }

    private static int spawn(CommandSourceStack source, BlockPos pos, Holder.Reference<CaravanVariation> variation) {
        final var id = variation.key().location();
        source.sendSuccess(() -> Component.translatable("commands.caravan.spawn.success", id.toString(), pos.getX(), pos.getY(), pos.getZ()), true);
        return variation.value().spawn(variation, source.getLevel(), pos);
    }

    private static Holder.Reference<CaravanVariation> getCaravansVariation(CommandContext<CommandSourceStack> context, String name) {
        return ResourceKeyArgumentAccessor.invokeResolveKey(context, name, CaravanRegistries.Keys.VARIATION, ERR_INVALID_VARIATION);
    }

    static {
        ERR_INVALID_VARIATION = new DynamicCommandExceptionType((it) -> Component.translatable("commands.caravan.spawn.invalid", it));
    }
}

package dev.obscuria.caravans.server;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.obscuria.caravans.common.CaravanSpawner;
import dev.obscuria.caravans.common.CaravanVariation;
import dev.obscuria.caravans.common.ILivingExtension;
import dev.obscuria.caravans.common.registry.CaravanRegistries;
import dev.obscuria.fragmentum.Fragmentum;
import lombok.experimental.UtilityClass;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public final class CaravanCommand {

    private static final DynamicCommandExceptionType ERR_INVALID_VARIATION;

    public static void register(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext registryAccess,
            Commands.CommandSelection environment
    ) {
        dispatcher.register(Commands.literal("caravan").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.literal("cooldown")
                        .then(Commands.literal("get")
                                .executes(context -> getCooldown(context.getSource())))
                        .then(Commands.literal("reset")
                                .executes(context -> resetCooldown(context.getSource())))
                        .then(Commands.literal("set")
                                .then(Commands.argument("ticks", IntegerArgumentType.integer(0))
                                        .executes(context -> {
                                            var ticks = IntegerArgumentType.getInteger(context, "ticks");
                                            return setCooldown(context.getSource(), ticks);
                                        }))))
                .then(Commands.literal("spawn")
                        .then(Commands.argument("position", BlockPosArgument.blockPos())
                                .then(Commands.argument("variation", ResourceKeyArgument.key(CaravanRegistries.Keys.VARIATION))
                                        .executes(context -> {
                                            var position = BlockPosArgument.getBlockPos(context, "position");
                                            var variation = getCaravansVariation(context, "variation");
                                            return spawn(context.getSource(), position, variation);
                                        }))))
                .then(Commands.literal("despawn")
                        .executes(context -> despawn(context.getSource()))));
    }

    public static int getCooldown(CommandSourceStack source) {
        final var ticks = CaravanSpawner.INSTANCE.cooldownTick;
        source.sendSuccess(() -> Component.translatableEscape("commands.caravan.cooldown.get", ticks), false);
        return ticks;
    }

    public static int resetCooldown(CommandSourceStack source) {
        CaravanSpawner.INSTANCE.cooldownTick = 0;
        source.sendSuccess(() -> Component.translatableEscape("commands.caravan.cooldown.reset"), true);
        return 1;
    }

    public static int setCooldown(CommandSourceStack source, int ticks) {
        CaravanSpawner.INSTANCE.cooldownTick = ticks;
        source.sendSuccess(() -> Component.translatableEscape("commands.caravan.cooldown.set", ticks), true);
        return 1;
    }

    private static int spawn(CommandSourceStack source, BlockPos pos, Holder.Reference<CaravanVariation> variation) {
        source.sendSuccess(() -> Component.translatableEscape("commands.caravan.spawn.success", variation.key().identifier(), pos.getX(), pos.getY(), pos.getZ()), true);
        try {
            return variation.value().spawn(variation, source.getLevel(), pos);
        } catch (Exception exception) {
            Fragmentum.LOG.error("Failed to spawn caravan", exception);
            return 0;
        }
    }

    private static int despawn(CommandSourceStack source) {
        var despawned = new AtomicInteger(0);
        var candidates = new HashSet<LivingEntity>();
        for (var entity : source.getLevel().getAllEntities()) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (!ILivingExtension.isCaravanMember(living)) continue;
            candidates.add(living);
        }
        for (var entity : candidates) {
            @Nullable var data = ILivingExtension.getCaravanData(entity);
            if (data == null) continue;
            if (!data.maybeDespawn(entity, true, false)) continue;
            despawned.incrementAndGet();
        }
        source.sendSuccess(() -> Component.translatableEscape("commands.caravan.despawn", despawned.get()), true);
        return despawned.get();
    }

    private static Holder.Reference<CaravanVariation> getCaravansVariation(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return ResourceKeyArgument.resolveKey(context, name, CaravanRegistries.Keys.VARIATION, ERR_INVALID_VARIATION);
    }

    static {
        ERR_INVALID_VARIATION = new DynamicCommandExceptionType((id) -> Component.translatableEscape("commands.caravan.spawn.invalid", id));
    }
}

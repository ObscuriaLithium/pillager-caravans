package dev.obscuria.caravans.server;

import com.mojang.brigadier.CommandDispatcher;
import dev.obscuria.caravans.registry.CaravanRegistries;
import dev.obscuria.caravans.world.caravans.CaravanVariation;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;

public final class CaravanCommand {

    public static void register(
            CommandDispatcher<CommandSourceStack> dispatcher,
            CommandBuildContext registryAccess,
            Commands.CommandSelection environment
    ) {
        dispatcher.register(Commands.literal("caravan")
                .then(Commands.literal("spawn")
                        .then(Commands.argument("position", BlockPosArgument.blockPos())
                                .then(Commands.argument("variation", ResourceArgument.resource(registryAccess, CaravanRegistries.Keys.VARIATION))
                                        .executes(it -> spawn(
                                                it.getSource(),
                                                BlockPosArgument.getBlockPos(it, "position"),
                                                ResourceArgument.getResource(it, "variation", CaravanRegistries.Keys.VARIATION)))))));
    }

    private static int spawn(CommandSourceStack source, BlockPos pos, Holder<CaravanVariation> variation) {
        return variation.value().spawn(source.getLevel(), pos);
    }
}

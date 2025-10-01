package dev.obscuria.caravans.world;

import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IWeighted {

    int weight();

    static <T extends IWeighted> @Nullable T pickRandom(List<T> elements, RandomSource random) {

        if (elements.isEmpty()) return null;
        var totalWeight = 0;
        for (T element : elements) {
            totalWeight += element.weight();
        }

        if (totalWeight <= 0) return null;
        var roll = random.nextInt(totalWeight);

        for (T element : elements) {
            roll -= element.weight();
            if (roll < 0) return element;
        }

        return null;
    }
}

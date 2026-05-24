package dev.obscuria.caravans.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.obscuria.caravans.PillagerCaravans;
import dev.obscuria.fragmentum.v2.api.common.Easing;
import dev.obscuria.fragmentum.v2.api.config.ConfigValue;
import lombok.experimental.UtilityClass;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public final class ConfigBuilder {

    public static Screen createConfigScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.literal("Pillager Caravans Options"))
                .category(buildGeneralCategory())
                .category(buildSpawnRatesCategory())
                .category(buildSpawnRulesCategory())
                .category(buildBehaviorCategory())
                .save(ConfigBuilder::saveAll)
                .build()
                .generateScreen(parent);
    }

    private static ConfigCategory buildGeneralCategory() {
        return category("general")
                .option(Opts.bool(CommonConfig.ENABLE_PILLAGER_CARAVANS))
                .option(Opts.bool(CommonConfig.ENABLE_ENCOUNTER_TOAST))
                .option(Opts.bool(CommonConfig.TOAST_LIGHT_MODE))
                .build();
    }

    private static ConfigCategory buildSpawnRatesCategory() {
        return category("spawn_rates")
                .option(Opts.intSlider(CommonConfig.WORLD_AGE_REQUIRED, 0, 24000 * 128, 24000, ValueFormat.TICKS))
                .option(Opts.intSlider(CommonConfig.SPAWN_ATTEMPT_COOLDOWN, 1200, 24000, 200, ValueFormat.TICKS))
                .option(Opts.doubleSlider(CommonConfig.SPAWN_COOLDOWN_MULTIPLIER, 0.1, 10.0, 0.1, ValueFormat.PERCENT))
                .group(group("small_caravans")
                        .option(Opts.intSlider(CommonConfig.SMALL_CARAVAN_SPAWN_WEIGHT, 0, 100, 1, ValueFormat.RAW_INT))
                        .option(Opts.intSlider(CommonConfig.SMALL_CARAVAN_SPAWN_COOLDOWN, 24000, 24000 * 128, 24000, ValueFormat.TICKS))
                        .option(Opts.intSlider(CommonConfig.SMALL_CARAVAN_DESPAWN_DELAY, 12000, 24000 * 16, 12000, ValueFormat.TICKS))
                        .build())
                .group(group("medium_caravans")
                        .option(Opts.intSlider(CommonConfig.MEDIUM_CARAVAN_SPAWN_WEIGHT, 0, 100, 1, ValueFormat.RAW_INT))
                        .option(Opts.intSlider(CommonConfig.MEDIUM_CARAVAN_SPAWN_COOLDOWN, 24000, 24000 * 128, 24000, ValueFormat.TICKS))
                        .option(Opts.intSlider(CommonConfig.MEDIUM_CARAVAN_DESPAWN_DELAY, 12000, 24000 * 16, 12000, ValueFormat.TICKS))
                        .build())
                .group(group("large_caravans")
                        .option(Opts.intSlider(CommonConfig.LARGE_CARAVAN_SPAWN_WEIGHT, 0, 100, 1, ValueFormat.RAW_INT))
                        .option(Opts.intSlider(CommonConfig.LARGE_CARAVAN_SPAWN_COOLDOWN, 24000, 24000 * 128, 24000, ValueFormat.TICKS))
                        .option(Opts.intSlider(CommonConfig.LARGE_CARAVAN_DESPAWN_DELAY, 12000, 24000 * 16, 12000, ValueFormat.TICKS))
                        .build())
                .build();
    }

    private static ConfigCategory buildSpawnRulesCategory() {
        return category("spawn_rules")
                .option(Opts.intSlider(CommonConfig.SPAWN_DISTANCE, 0, 256, 8, ValueFormat.BLOCKS))
                .option(Opts.intSlider(CommonConfig.SPAWN_DISTANCE_VARIANCE, 0, 256, 8, ValueFormat.BLOCKS))
                .option(Opts.intSlider(CommonConfig.MAX_BLOCK_LIGHT, 0, 15, 1, ValueFormat.RAW_INT))
                .option(Opts.intSlider(CommonConfig.VILLAGE_SAFE_ZONE, 0, 64, 1, ValueFormat.SECTIONS))
                .build();
    }

    private static ConfigCategory buildBehaviorCategory() {
        return category("behavior")
                .option(Opts.bool(CommonConfig.ALLOW_FOLLOWER_TELEPORT))
                .option(Opts.bool(CommonConfig.ALLOW_LEASHED_TELEPORT))
                .build();
    }

    private static void saveAll() {
        CommonConfig.ALL_VALUES.forEach(ConfigValue::save);
    }

    private static ConfigCategory.Builder category(String key) {
        return ConfigCategory.createBuilder()
                .name(translate("category.%s".formatted(key)));
    }

    private static OptionGroup.Builder group(String key) {
        return OptionGroup.createBuilder()
                .name(translate("group.%s".formatted(key)))
                .description(Opts.descriptionOf(translate("group.%s.desc".formatted(key))));
    }

    static MutableComponent translate(String key) {
        return Component.translatable("config.%s.%s".formatted(PillagerCaravans.MOD_ID, key));
    }

    static MutableComponent translate(String key, Object... args) {
        return Component.translatable("config.%s.%s".formatted(PillagerCaravans.MOD_ID, key), args);
    }

    @UtilityClass
    public static final class Opts {

        public static OptionDescription descriptionOf(Component text) {
            return OptionDescription.of(text.copy().withStyle(ChatFormatting.GRAY));
        }

        static <T> Option.Builder<T> base(ConfigValue<T> value) {
            return Option.<T>createBuilder()
                    .name(translate("option.%s".formatted(value.name())))
                    .description(descriptionOf(translate("option.%s.desc".formatted(value.name()))))
                    .binding(binding(value));
        }

        static Option<Boolean> bool(ConfigValue<Boolean> value) {
            return base(value).controller(TickBoxControllerBuilder::create).build();
        }

        static <T extends Enum<T>> Option<T> enumCycle(ConfigValue<T> value, Class<T> enumClass) {
            return base(value)
                    .controller(o -> EnumControllerBuilder.create(o).enumClass(enumClass))
                    .build();
        }

        static Option<Double> doubleSlider(ConfigValue<Double> value, double min, double max, double step, ValueFormat format) {
            return base(value)
                    .controller(o -> DoubleSliderControllerBuilder.create(o)
                            .range(min, max)
                            .step(step)
                            .formatValue(format.formatter))
                    .build();
        }

        static Option<Integer> intSlider(ConfigValue<Integer> value, int min, int max, int step, ValueFormat format) {
            return base(value)
                    .controller(o -> IntegerSliderControllerBuilder.create(o)
                            .range(min, max)
                            .step(step)
                            .formatValue(v -> format.formatter.format(v.doubleValue())))
                    .build();
        }

        static Option<String> string(ConfigValue<String> value) {
            return base(value).controller(StringControllerBuilder::create).build();
        }

        static Option<String> easing(ConfigValue<Easing> value) {
            return Option.<String>createBuilder()
                    .name(translate("option.%s".formatted(value.name())))
                    .description(descriptionOf(translate("option.%s.desc".formatted(value.name()))))
                    .binding(binding(value, Easing::name, Easing::valueOf))
                    .controller(o -> DropdownStringControllerBuilder.create(o)
                            .values(Arrays.stream(Easing.values()).map(Easing::name).toList()))
                    .build();
        }

        static ListOption<String> stringList(ConfigValue<List<? extends String>> value, String initialEntry) {
            return ListOption.<String>createBuilder()
                    .name(translate("option.%s".formatted(value.name())))
                    .description(descriptionOf(translate("option.%s.desc".formatted(value.name()))))
                    .binding(listBinding(value))
                    .controller(StringControllerBuilder::create)
                    .initial(initialEntry)
                    .build();
        }

        private static <T> Binding<T> binding(ConfigValue<T> value) {
            return Binding.generic(value.getDefault(), value::get, value::set);
        }

        private static <T, V> Binding<V> binding(ConfigValue<T> value, Function<T, V> encode, Function<V, T> decode) {
            return Binding.generic(
                    encode.apply(value.getDefault()),
                    () -> encode.apply(value.get()),
                    decoded -> value.set(decode.apply(decoded)));
        }

        private static <T> Binding<List<T>> listBinding(ConfigValue<List<? extends T>> value) {
            return Binding.generic(
                    new ArrayList<>(value.getDefault()),
                    () -> new ArrayList<>(value.get()),
                    updated -> value.set(new ArrayList<>(updated)));
        }
    }

    enum ValueFormat {
        RAW_INT(v -> Component.literal("%.0f".formatted(v))),
        RAW_DOUBLE(v -> Component.literal("%.1f".formatted(v))),
        TICKS(v -> translate("format.ticks", "%.0f".formatted(v), (v % 24000 == 0 ? "%.0f" : "%.1f").formatted(v / 24000.0))),
        PERCENT(v -> translate("format.percent", "%.0f".formatted(v * 100))),
        BLOCKS(v -> translate("format.blocks", v.intValue())),
        SECTIONS(v -> translate("format.sections", v.intValue()));

        final ValueFormatter<Double> formatter;

        ValueFormat(ValueFormatter<Double> formatter) {
            this.formatter = formatter;
        }
    }
}
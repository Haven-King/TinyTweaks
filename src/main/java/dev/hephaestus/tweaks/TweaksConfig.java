package dev.hephaestus.tweaks;

import dev.inkwell.conrad.api.Config;
import dev.inkwell.optionionated.api.data.DataType;
import dev.inkwell.optionionated.api.data.SaveType;
import dev.inkwell.optionionated.api.data.SyncType;
import dev.inkwell.optionionated.api.serialization.ConfigSerializer;
import dev.inkwell.optionionated.api.serialization.PropertiesSerializer;
import dev.inkwell.optionionated.api.util.Version;
import dev.inkwell.optionionated.api.value.ValueKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class TweaksConfig extends Config<Map<String, String>> {
    public static class Plants {
        public static final ValueKey<Boolean> BETTER_LILY_PADS = builder(true)
                .with(DataType.SYNC_TYPE, SyncType.INFO).build();

        public static final ValueKey<Boolean> FARMER_VILLAGER_AUTOMATION = value(true);

        public static class EasyHarvest {
            public static final ValueKey<Boolean> CROPS = value(true);
            public static final ValueKey<Boolean> SUGARCANE = value(true);
        }

        public static class AutoPlanting {
            public static final ValueKey<Boolean> ENABLED = value(true);
            public static final ValueKey<Integer> DELAY = builder(20).bounds(0, 100).build();
        }

        public static class Leaves {
            public static final ValueKey<Boolean> COLLISION = builder(false)
                    .with(DataType.SYNC_TYPE, SyncType.INFO).build();

            public static final ValueKey<Boolean> PERSISTENT_COLLISION = value(false);
            public static final ValueKey<Boolean> SLOW = value(true);
            public static final ValueKey<Double> SLOW_AMOUNT = builder(0.7D).bounds(0D, 1D).build();
            public static final ValueKey<Boolean> CLIMBING = value(true);
            public static final ValueKey<Double> CLIMBING_SPEED = builder(0.75).bounds(0D, 1D).build();
            public static final ValueKey<Integer> LEAF_DECAY_SPEED = builder(1).bounds(1, 100).build();
            public static final ValueKey<Boolean> SNOW_FALLS_THROUGH_LEAVES = value(true);
        }

        public static class Rejuvenation {
            public static final ValueKey<Boolean> ENABLED = value(true);
            public static final ValueKey<Double> GRASS_GROWTH_RATE = builder(0.0125D).bounds(0.001, 1D).build();
            public static final ValueKey<Double> FRIENDSHIP = builder(1.25D).bounds(0D, 10D).build();
            public static final ValueKey<Double> LONG_GRASS_RATIO = builder(0.01D).bounds(0D, 1D).build();
            public static final ValueKey<Boolean> DEAD_BUSHES_TO_SAPLINGS = value(true);
        }

        public static class NetherRejuvenation {
            public static final ValueKey<Boolean> ENABLED = value(true);
            public static final ValueKey<Double> ROOTS_GROWTH_RATE = builder(0.0125D).bounds(0.001, 1D).build();
            public static final ValueKey<Double> FRIENDSHIP = builder(1.25).bounds(0D, 10D).build();
            public static final ValueKey<Integer> SPROUT_ROOTS_RATIO = builder(75).bounds(0, 100).build();
            public static final ValueKey<Double> VINES_CHANCE = builder(0.01D).bounds(0D, 1D).build();
        }
    }

    public static class Animals {
        public static final ValueKey<Boolean> EAT_OFF_GROUND = value(true);
        public static final ValueKey<Boolean> EAT_CROPS = value(true);
    }

    public static class Misc {
        public static final ValueKey<Boolean> STURDY_LANTERNS = value(true);
        public static final ValueKey<Boolean> MOSSY_THINGS = value(true);
        public static final ValueKey<Boolean> BURNING_LOGS_DROP_CHARCOAL = value(true);
        public static final ValueKey<Boolean> DOUBLE_DOORS = value(true);
        public static final ValueKey<Boolean> BUBBLE_COLUMNS_FLOW = value(true);
        public static final ValueKey<Boolean> INFINITE_CAULDRONS = value(true);
        public static final ValueKey<Boolean> EASY_XP = value(true);
        public static final ValueKey<Boolean> ARMOR_STAND_SWAP = value(true);
        public static final ValueKey<Boolean> AUTOMATIC_DOORS = value(true);
        public static final ValueKey<Float> SOUL_FIRE_DAMAGE_MODIFIER = builder(1F).bounds(1F, 100F).build();

        public static class FLINT_AND_STEEL {
            public static final ValueKey<Boolean> ENABLED = value(true);
            public static final ValueKey<Integer> BURN_TIME = value(3);
        }
    }

    @Override
    public @NotNull ConfigSerializer<Map<String, String>> getSerializer() {
        return PropertiesSerializer.INSTANCE;
    }

    @Override
    public @NotNull SaveType getSaveType() {
        return SaveType.LEVEL;
    }

    @Override
    public @NotNull String getName() {
        return "config";
    }

    @Override
    public boolean upgrade(@Nullable Version from, Map<String, String> representation) {
        return true;
    }
}

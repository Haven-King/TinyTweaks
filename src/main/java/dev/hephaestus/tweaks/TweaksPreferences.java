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

public class TweaksPreferences extends Config<Map<String, String>> {
    public static final ValueKey<Boolean> PLANT_HITBOXES = value(true);
    public static final ValueKey<Boolean> BLUE_SOUL_FIRE = value(true);
    public static final ValueKey<Boolean> EASY_HARVEST_DROP_AS_ITEMS = builder(true)
            .with(DataType.SYNC_TYPE, SyncType.INFO).build();

    public static class Labels {
        public static final ValueKey<Boolean> ENABLED = value(true);
        public static final ValueKey<Long> SCALE = builder(100L).bounds(0L, 200L).build();
    }

    @Override
    public @NotNull ConfigSerializer<Map<String, String>> getSerializer() {
        return PropertiesSerializer.INSTANCE;
    }

    @Override
    public @NotNull SaveType getSaveType() {
        return SaveType.USER;
    }

    @Override
    public @NotNull String getName() {
        return "preferences";
    }

    @Override
    public boolean upgrade(@Nullable Version from, Map<String, String> representation) {
        return true;
    }
}

package dev.hephaestus.tweaks;

import net.fabricmc.fabric.api.config.v1.FabricDataTypes;
import net.fabricmc.fabric.api.config.v1.FabricSaveTypes;
import net.fabricmc.fabric.api.config.v1.SyncType;
import net.fabricmc.loader.api.config.data.SaveType;
import net.fabricmc.loader.api.config.entrypoint.Config;
import net.fabricmc.loader.api.config.serialization.ConfigSerializer;
import net.fabricmc.loader.api.config.serialization.PropertiesSerializer;
import net.fabricmc.loader.api.config.value.ValueKey;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TweaksPreferences extends Config<Map<String, String>> {
    public static final ValueKey<Boolean> PLANT_HITBOXES = value(true);
    public static final ValueKey<Boolean> BLUE_SOUL_FIRE = value(true);
    public static final ValueKey<Boolean> EASY_HARVEST_DROP_AS_ITEMS = builder(true)
            .with(FabricDataTypes.SYNC_TYPE, SyncType.INFO).build();

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
        return FabricSaveTypes.USER;
    }

    @Override
    public @NotNull String getName() {
        return "preferences";
    }
}

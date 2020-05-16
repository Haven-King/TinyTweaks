package dev.hephaestus.tweaks.mixin;

import dev.hephaestus.tweaks.Tweaks;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class VolatileMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        System.out.printf("mixinPackage: %s", mixinPackage);
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    private static boolean TEST = true;

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("AnvilContainer") && FabricLoader.getInstance().isModLoaded("strongandfairanvils")) {
            Tweaks.log("Strong and Fair Anvils is present; deferring rename cost adjustment to them.");
            return false;
        }

        boolean isDevelopmentEnvironment = FabricLoader.getInstance().isDevelopmentEnvironment();
        if (mixinClassName.contains("mixin.dev") && !isDevelopmentEnvironment)
            return false;
        else return !mixinClassName.contains("mixin.prod") || !isDevelopmentEnvironment;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}

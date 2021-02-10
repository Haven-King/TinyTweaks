package dev.hephaestus.tweaks.mixin.block;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LockableContainerBlockEntity.class)
public class AddContainerLabels implements BlockEntityClientSerializable {
    @Shadow private Text customName;

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        if (compoundTag.contains("customName", NbtType.STRING)) {
            this.customName = Text.Serializer.fromJson(compoundTag.getString("customName"));
        }
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        compoundTag.putString("customName", Text.Serializer.toJson(this.customName));

        return compoundTag;
    }
}

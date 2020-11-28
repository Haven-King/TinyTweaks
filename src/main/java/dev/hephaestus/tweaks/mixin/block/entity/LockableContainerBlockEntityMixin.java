package dev.hephaestus.tweaks.mixin.block.entity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LockableContainerBlockEntity.class)
public class LockableContainerBlockEntityMixin implements BlockEntityClientSerializable {
	@Shadow private Text customName;

	@Override
	public void fromClientTag(CompoundTag compoundTag) {
		if (compoundTag.contains("CustomName")) {
			this.customName = Text.Serializer.fromJson(compoundTag.getString("CustomName"));
		}
	}

	@Override
	public CompoundTag toClientTag(CompoundTag compoundTag) {
		if (this.customName != null) {
			compoundTag.putString("CustomName", Text.Serializer.toJson(this.customName));
		}

		return compoundTag;
	}
}

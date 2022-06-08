// TODO 提前加载全部方块实体
package net.leawind.infage.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.chunk.WorldChunk;

@Mixin(WorldChunk.class)
public class MixinWorldChunk {
	@Inject(at = @At("RETURN"), method = "()V")
	private void onTickBlockEntities(CallbackInfo info) {

	}

}

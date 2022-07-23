package net.leawind.infage.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.world.World;

@Mixin(World.class)
public interface WorldInvoker {
	@Invoker("isClient")
	public boolean invokeIsClient();
}

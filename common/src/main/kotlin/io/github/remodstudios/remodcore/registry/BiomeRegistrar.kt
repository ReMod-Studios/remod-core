package io.github.remodstudios.remodcore.registry

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome
import org.jetbrains.annotations.ApiStatus
import java.lang.AssertionError

/**
 * Why does this exist, you ask?
 *
 * In both Forge and Fabric, to add entries to built-in registries, you directly access them...
 *
 * *Except* for biomes, which Forge has a `ForgeRegistry` for.
 *
 * This means that the biome registration logic must be platform-dependent (assuming that Forge *requires*
 * biome registrations to go through its special registry, anyway.)
 */
@ApiStatus.Internal // use BiomeRegistryHelper like a normal person
object BiomeRegistrar {
    @Suppress("UNUSED_PARAMETER")
    @JvmStatic @ExpectPlatform
    fun add(id: Identifier, b: Biome): Unit = throw AssertionError("@ExpectPlatform didn't apply!")
}
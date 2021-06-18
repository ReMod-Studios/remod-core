package io.github.remodstudios.remodcore.registry

import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome

open class BiomeRegistryHelper(val modid: String) {
    fun add(id: String, biome: Biome): Biome
        = biome.also { BiomeRegistrar.add(Identifier(modid, id), biome) }

    open fun register() { }
}
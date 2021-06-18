package io.github.remodstudios.remodcore.registry

import net.minecraft.util.Identifier
import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.world.biome.Biome

object BiomeRegistrarImpl {
    @JvmStatic
    fun add(id: Identifier, b: Biome) {
        BuiltinRegistries.add(BuiltinRegistries.BIOME, id, b)
    }
}

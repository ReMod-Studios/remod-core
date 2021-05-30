package com.remodstudios.remodcore.registry

import me.shedaniel.architectury.registry.DeferredRegister
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome

open class BiomeRegistryHelper(registry: DeferredRegister<Biome>): RegistryHelper<Biome>(registry) {

    constructor(modid: String): this(DeferredRegister.create(modid, Registry.BIOME_KEY))

    fun add(
        id: String, b: Biome
    ): Biome
            = b.also { registry.register(id) { it } }
}
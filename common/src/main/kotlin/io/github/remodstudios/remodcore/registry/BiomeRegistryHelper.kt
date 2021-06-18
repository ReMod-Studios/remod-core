package io.github.remodstudios.remodcore.registry

import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.world.biome.Biome

open class BiomeRegistryHelper(modid: String): BuiltinRegistryHelper<Biome>(modid, BuiltinRegistries.BIOME) { }
package io.github.remodstudios.remodcore.registry

import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.feature.ConfiguredFeature

open class ConfiguredFeatureRegistryHelper(modid: String): BuiltinRegistryHelper<ConfiguredFeature<*, *>>(modid, BuiltinRegistries.CONFIGURED_FEATURE) { }
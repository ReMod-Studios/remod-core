package io.github.remodstudios.remodcore.registry

import net.minecraft.util.Identifier
import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.util.registry.Registry

open class BuiltinRegistryHelper<T>(val modid: String, val registry: Registry<T>) {
    open fun add(
        id: String, `object`: T
    ): T
        = `object`.also { BuiltinRegistries.add(registry, Identifier(modid, id), `object`) }

    open fun register() { }
}
package io.github.remodstudios.remodcore.registry

import dev.architectury.registry.registries.DeferredRegister

open class RegistryHelper<T>(val registry: DeferredRegister<T>) {
    open fun register() {
        registry.register()
    }
}
package com.remodstudios.remodcore.registry

import me.shedaniel.architectury.registry.DeferredRegister

open class RegistryHelper<T>(val registry: DeferredRegister<T>) {
    open fun register() {
        registry.register()
    }
}
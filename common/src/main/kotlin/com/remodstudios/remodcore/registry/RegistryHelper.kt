package com.remodstudios.remodcore.registry

import me.shedaniel.architectury.registry.DeferredRegister

open class RegistryHelper<T>(protected val registry: DeferredRegister<T>) {
    open fun register() {
        registry.register()
    }
}
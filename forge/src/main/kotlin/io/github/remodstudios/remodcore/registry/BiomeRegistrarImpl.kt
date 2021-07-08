package io.github.remodstudios.remodcore.registry

import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.eventbus.KotlinEventBus

object BiomeRegistrarImpl {
    private var deferred = true
    private val deferredList = ArrayList<Biome>()

    fun register(bus: KotlinEventBus) {
        bus.register(::onBiomesRegister)
    }

    private fun onBiomesRegister(event: RegistryEvent.Register<Biome>) {
        deferredList.forEach(event.registry::register)
        deferredList.clear()
        deferred = false
    }

    @JvmStatic
    fun add(id: Identifier, b: Biome) {
        // a bit funky, but it'll do I guess
        if (b.registryName != null)
            throw AssertionError("Biome has already been registered under different ID (\"${b.registryName}\")!")
        b.registryName = id
        if (deferred)
            deferredList += b
        else
            ForgeRegistries.BIOMES.register(b)
    }
}

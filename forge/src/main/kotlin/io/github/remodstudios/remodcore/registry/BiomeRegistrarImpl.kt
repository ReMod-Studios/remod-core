package io.github.remodstudios.remodcore.registry

import net.minecraft.util.Identifier
import net.minecraft.world.biome.Biome
import net.minecraftforge.registries.ForgeRegistries
import java.lang.AssertionError

object BiomeRegistrarImpl {
    @JvmStatic
    fun addImpl(id: Identifier, b: Biome) {
        // a bit funky, but it'll do I guess
        if (b.registryName != null)
            throw AssertionError("Biome has already been registered under different ID (\"${b.registryName}\")!")
        b.registryName = id
        ForgeRegistries.BIOMES.register(b)
    }
}
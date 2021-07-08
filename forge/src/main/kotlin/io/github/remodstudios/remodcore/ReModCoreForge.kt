package io.github.remodstudios.remodcore

import io.github.remodstudios.remodcore.registry.BiomeRegistrarImpl
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(ReModCore.ID)
object ReModCoreForge {
    init {
        BiomeRegistrarImpl.register(MOD_BUS)
    }
}

package io.github.remodstudios.remodcore.registry

import me.shedaniel.architectury.registry.BlockProperties
import me.shedaniel.architectury.registry.DeferredRegister
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.registry.Registry

open class BlockRegistryHelper(
    registry: DeferredRegister<Block>
): RegistryHelper<Block>(registry) {

    constructor(modid: String) : this(DeferredRegister.create(modid, Registry.BLOCK_KEY))

    inline fun <reified V: Block> add(
        id: String,
        v: V,
    ) = v.also { registry.register(id) { v } }

    inline fun <reified V: Block> addOfProp(
        id: String,
        prop: BlockProperties,
        toBlock: BlockProperties.() -> V,
    ) = add(id, prop.toBlock())

    inline fun <reified Original: Block, reified V: Block> addCopy(
        id: String,
        original: Original,
        factory: BlockProperties.() -> V,
    ) = addOfProp(id, BlockProperties.copy(original), factory)

    inline fun <reified Original: Block> addCopyWithInit(
        id: String,
        original: Original,
        factory: BlockProperties.() -> Unit,
    ): Block {
        val prop = BlockProperties.copy(original)
        prop.factory()
        return addOfProp(id, prop, ::Block)
    }

    inline fun <reified Original: Block> addCopy(
        id: String,
        original: Original
    ) = addCopy(id, original) { Block(this) }

    inline fun <reified V: Block> addOfMaterial(
        id: String,
        mat: Material,
        factory: BlockProperties.() -> V
    ) = addOfProp(id, BlockProperties.of(mat), factory)

    inline fun <reified V: Block> addWoodlike(
        id: String,
        factory: BlockProperties.() -> V
    ) = addOfMaterial(id, Material.WOOD) {
        strength(2.0F)
        sounds(BlockSoundGroup.WOOD)
        this.factory()
    }
}
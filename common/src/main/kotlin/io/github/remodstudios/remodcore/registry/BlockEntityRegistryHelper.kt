package io.github.remodstudios.remodcore.registry

import com.mojang.datafixers.types.Type
import dev.architectury.hooks.block.BlockEntityHooks
import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry

open class BlockEntityRegistryHelper(
    registry: DeferredRegister<BlockEntityType<*>>
): RegistryHelper<BlockEntityType<*>>(registry) {

    constructor(modid: String) : this(DeferredRegister.create(modid, Registry.BLOCK_ENTITY_TYPE_KEY))

    inline fun <reified V: BlockEntity> add(
        id: String,
        noinline v: (BlockPos, BlockState) -> V,
        vararg blocks: Block,
        dataFixType: Type<*>? = null
    ): BlockEntityType<V> {
        val type = BlockEntityHooks.builder(v, *blocks).build(dataFixType)
        registry.register(id) { type }
        return type
    }
}
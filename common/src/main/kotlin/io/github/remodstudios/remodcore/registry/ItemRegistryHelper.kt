package io.github.remodstudios.remodcore.registry

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry

open class ItemRegistryHelper(registry: DeferredRegister<Item>): RegistryHelper<Item>(registry) {
    constructor(modid: String) : this(DeferredRegister.create(modid, Registry.ITEM_KEY))

    open fun defaultSettings(): Item.Settings = Item.Settings()

    inline fun <reified I: Item> add(
        id: String, i: I
    ): I
            = i.also { registry.register(id) { it } }

    inline fun <reified I: Item> addWithFactory(
        id: String,
        factory: (Item.Settings) -> I
    ) = add(id, factory(defaultSettings()))

    inline fun add(
        id: String,
        init: (Item.Settings) -> Unit = {}
    ): Item {
        val settings = defaultSettings()
        init(settings)
        return add(id, Item(settings))
    }

    inline fun <reified B: Block> add(
        id: String,
        b: B,
        init: (Item.Settings) -> Unit = {}
    ): BlockItem {
        return addWithFactory(id) {
            init(it)
            BlockItem(b, it)
        }
    }
}
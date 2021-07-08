package io.github.remodstudios.remodcore

import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Position
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import kotlin.math.PI
import kotlin.math.sqrt

// Miscellaneous extensions

// Destructuring support
operator fun Position.component1(): Double = x
operator fun Position.component2(): Double = y
operator fun Position.component3(): Double = z

// Vector stuff
fun Entity.squaredDistanceTo(blockPos: BlockPos)
        = squaredDistanceTo(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble())

// MafsUtil:tm:
fun magnitude(x: Double, y: Double, z: Double)
        = sqrt(x*x + y*y + z*z).toFloat()

fun Float.toDeg(): Float = this * 180f / PI.toFloat()
fun Float.toRad(): Float = this * PI.toFloat() / 180f
fun Double.toDeg(): Double = this * 180.0 / PI
fun Double.toRad(): Double = this * PI / 180.0

inline fun Boolean.ifTrueThenAlso(also: () -> Unit): Boolean {
    if (this) also()
    return this
}

inline operator fun MatrixStack.invoke(block: (MatrixStack) -> Unit) {
    push()
    block(this)
    pop()
}

operator fun Vec3d.unaryMinus(): Vec3d = this.negate()

operator fun Vec3d.plus(by: Vec3d): Vec3d = this.add(by)
operator fun Vec3d.minus(by: Vec3d): Vec3d = this.subtract(by)
operator fun Vec3d.times(by: Double): Vec3d = this.multiply(by)
operator fun Vec3d.div(by: Double): Vec3d = this.multiply(1 / by)

fun PlayerEntity.addStatusEffect(type: StatusEffect,
                                   duration: Int = 0,
                                   amplifier: Int = 0,
                                   ambient: Boolean = false,
                                   showParticles: Boolean = true,
                                   showIcon: Boolean = true,
                                   hiddenEffect: StatusEffectInstance? = null)
{
    addStatusEffect(StatusEffectInstance(type, duration, amplifier, ambient, showParticles, showIcon, hiddenEffect))
}

@Deprecated(message = "Renamed to addStatusEffect",
    replaceWith = ReplaceWith(expression = "addStatusEffect(type, duration, amplifier, ambient, showParticles, showIcon, hiddenEffect)"))
fun PlayerEntity.applyStatusEffect(type: StatusEffect,
                                   duration: Int = 0,
                                   amplifier: Int = 0,
                                   ambient: Boolean = false,
                                   showParticles: Boolean = true,
                                   showIcon: Boolean = true,
                                   hiddenEffect: StatusEffectInstance? = null)
    = addStatusEffect(type, duration, amplifier, ambient, showParticles, showIcon, hiddenEffect)

@JvmInline
value class Color(val value: Int) {
    constructor(a: Int = 255, r: Int, g: Int, b: Int) : this(
        value = (a and 0xff shl 24) or (r and 0xff shl 16) or (g and 0xff shl 8) or (b and 0xff)
    )
}

typealias Predicate<T> = (T) -> Boolean

inline fun <reified T: Entity> World.getEntities(box: Box, noinline predicate: Predicate<T>? = null): List<T>
        = getEntitiesByClass(T::class.java, box, predicate)
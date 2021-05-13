package com.remodstudios.remodcore

import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.ai.goal.GoalSelector
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Position
import net.minecraft.util.math.Vec3d
import kotlin.math.PI

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
        = MathHelper.sqrt(x*x + y*y + z*z)

fun Float.toDeg() = this * 180 / PI
fun Float.toRad() = this * PI / 180
fun Double.toDeg() = this * 180 / PI
fun Double.toRad() = this * PI / 180

inline fun Boolean.ifTrueThenAlso(also: () -> Unit): Boolean {
    if (this) also()
    return this
}

inline fun MatrixStack.frame(block: () -> Unit) {
    this.push()
    block()
    this.pop()
}
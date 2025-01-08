package org.ricall.day20

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.abs
import kotlin.test.assertEquals

private val PARTICLE_REGEX = """^p=<([^>]+)>, v=<([^>]+)>, a=<([^>]+)>$""".toRegex()
private fun parseParticle(index: Int, line: String) = PARTICLE_REGEX.find(line)!!.run {
    val (p, v, a) = this.groupValues.drop(1).map(::parseVector)
    Particle(index, p, v, a)
}

private data class Particle(val index: Int, var position: Vector, var velocity: Vector, val acceleration: Vector) {
    fun tick() {
        velocity += acceleration
        position += velocity
    }
}

private fun parseVector(text: String) = text.split(",").map(String::toLong).run {
    val (x, y, z) = this
    Vector(x, y, z)
}

private data class Vector(val x: Long, val y: Long, val z: Long) {
    fun manhattanLength() = listOf(x, y, z).map(::abs).sum()
    operator fun plus(v: Vector) = Vector(x + v.x, y + v.y, z + v.z)
}

private fun parseInput(input: String) = input.lines().mapIndexed(::parseParticle)

private fun solvePartOne(input: String): Int {
    val particles = parseInput(input)
    repeat(1_000) {
        particles.forEach(Particle::tick)
    }
    return particles.minBy { it.position.manhattanLength() }.index
}

private fun solvePartTwo(input: String): Int {
    val particles = parseInput(input).toMutableList()
    repeat(100) {
        particles.forEach(Particle::tick)
        particles -= collidingParticles(particles)
    }
    return particles.count()
}

private fun collidingParticles(particles: List<Particle>) = buildSet {
    particles.forEachIndexed { index, particle ->
        for (compare in index + 1..<particles.size) {
            val particle2 = particles[compare]
            if (particle.position == particle2.position) {
                add(particle)
                add(particle2)
            }
        }
    }
}

class Day20 {

    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(
            """
            |p=<4,0,0>, v=<0,0,0>, a=<-2,0,0>
            |p=<3,0,0>, v=<2,0,0>, a=<-1,0,0>""".trimMargin()
        )

        assertEquals(1, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day20.txt").readText())

        assertEquals(457, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(
            """
            |p=<-6,0,0>, v=<3,0,0>, a=<0,0,0>
            |p=<-4,0,0>, v=<2,0,0>, a=<0,0,0>
            |p=<-2,0,0>, v=<1,0,0>, a=<0,0,0>
            |p=<3,0,0>, v=<-1,0,0>, a=<0,0,0>""".trimMargin()
        )

        assertEquals(1, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day20.txt").readText())

        assertEquals(448, result)
    }
}
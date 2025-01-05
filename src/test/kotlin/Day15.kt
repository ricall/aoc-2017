package org.ricall.day15

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun generator(seed: Int, factor: Int) = sequence {
    var next = seed
    while (true) {
        next = (next.toLong() * factor).mod(2147483647)
        yield(next)
    }
}

private fun createGenerators(input: String): Pair<Sequence<Int>, Sequence<Int>> {
    val seeds = input.lines().map { line -> line.split(" ").let { it[it.size - 1].toInt() } }

    return generator(seeds[0], 16807) to generator(seeds[1], 48271)
}

private fun matchesFor(iterations: Int, sequences: Pair<Sequence<Int>, Sequence<Int>>) =
    sequences.first.zip(sequences.second).take(iterations).count { (a, b) -> a.and(0xffff) == b.and(0xffff) }

private fun solvePartOne(input: String) = matchesFor(40_000_000, createGenerators(input))

private fun solvePartTwo(input: String) = createGenerators(input).let { (a, b) ->
    matchesFor(5_000_000, a.filter { it.and(0x03) == 0 } to b.filter { it.and(0x07) == 0 })
}

private val TEST_DATA = """
        |Generator A starts with 65
        |Generator B starts with 8921""".trimMargin()

class Day15 {
    @Test
    fun `part 1 test data`() {
        assertEquals(588, solvePartOne(TEST_DATA))
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day15.txt").readText())

        assertEquals(638, result)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(309, solvePartTwo(TEST_DATA))
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day15.txt").readText())

        assertEquals(343, result)
    }
}
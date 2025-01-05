package org.ricall.day13

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun String.parseLayers() = this.lines().map { it.split(": ").let { it[0].toInt() to it[1].toInt() } }

private fun positionAt(time: Int, size: Int) = time.mod(2 * (size - 1))

private fun solvePartOne(input: String) =
    input.parseLayers().sumOf { (depth, range) -> if (positionAt(depth, range) == 0) depth * range else 0 }

private fun solvePartTwo(input: String) = input.parseLayers().let { layers ->
    (0..Int.MAX_VALUE).find { layers.all { (depth, range) -> positionAt(depth + it, range) != 0 } }
}

class Day13 {
    private val TEST_DATA = """
        |0: 3
        |1: 2
        |4: 4
        |6: 4""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(TEST_DATA)

        assertEquals(24, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day13.txt").readText())

        assertEquals(2384, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(TEST_DATA)

        assertEquals(10, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day13.txt").readText())

        assertEquals(3921270, result)
    }
}
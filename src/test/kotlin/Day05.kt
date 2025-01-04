package org.ricall.day05

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun solve(input: String, updateOffset: (Int) -> Int): Int {
    val offsets = input.lines().map { it.toInt() }.toMutableList()
    var (index, count) = 0 to 0
    while (index in offsets.indices) {
        val offset = offsets[index]++
        offsets[index] = updateOffset(offset)
        index += offset
        count++
    }
    return count
}

private fun solvePartOne(input: String) = solve(input, { it + 1 })

private fun solvePartTwo(input: String) = solve(input, { if (it >= 3) it - 1 else it + 1 })

class Day05 {
    private val TEST_DATA = """
        |0
        |3
        |0
        |1
        |-3""".trimMargin()

    @Test
    fun `part 1 test data`() {
        assertEquals(5, solvePartOne(TEST_DATA))
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/Day05.txt").readText())

        assertEquals(326618, result)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(10, solvePartTwo(TEST_DATA))
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/Day05.txt").readText())

        assertEquals(21841249, result)
    }
}
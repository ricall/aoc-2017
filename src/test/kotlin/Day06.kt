package org.ricall.day06

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun reallocateBanks(blocks: List<Int>) = blocks.run {
    val largestBlock = max()
    var blockIndex = indexOf(largestBlock)
    toMutableList().let { newBlocks ->
        newBlocks[blockIndex++] = 0
        repeat(largestBlock) { newBlocks[blockIndex++ % size]++ } // Allocate block evenly across all blocks
        newBlocks.toList()
    }
}

private fun detectLoops(input: String): Pair<Int, Int> {
    var banks = input.split("\\s+".toRegex()).map(String::toInt)
    val cycles = mutableMapOf(banks to 0)
    for (count in (1..Int.MAX_VALUE)) {
        banks = reallocateBanks(banks)
        cycles[banks]?.let { return count to count - it }
        cycles[banks] = count
    }
    error("No loops detected")
}

private fun solvePartOne(input: String) = detectLoops(input).first

private fun solvePartTwo(input: String) = detectLoops(input).second

private const val TEST_DATA = "0 2 7 0"

class Day06 {
    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(TEST_DATA)

        assertEquals(5, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day06.txt").readText())

        assertEquals(7864, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(TEST_DATA)

        assertEquals(4, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day06.txt").readText())

        assertEquals(1695, result)
    }
}
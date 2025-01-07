package org.ricall.day17

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private fun solvePartOne(step: Int): Int = (1..2017).fold(0 to mutableListOf(0)) { (index, list), value ->
    val nextIndex = (index + step).mod(list.size) + 1
    list.add(nextIndex, value)
    nextIndex to list
}.let { (current, buffer) -> buffer[current + 1] }

private fun solvePartTwo(step: Int): Int = (1..50_000_000).fold(0 to 0) { (index, valueAtOne), value ->
    ((index + step).mod(value) + 1).let { it to if (it == 1) value else valueAtOne }
}.second

class Day17 {
    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(3)

        assertEquals(638, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day17.txt").readText().toInt())

        assertEquals(1547, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(3)

        assertEquals(1222153, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day17.txt").readText().toInt())

        assertEquals(31154878, result)
    }
}
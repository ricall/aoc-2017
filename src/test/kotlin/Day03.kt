package org.ricall.dayxx

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.abs
import kotlin.test.assertEquals

private fun spiral() = sequence {
    var (x, y) = 0 to 0
    var (dx, dy) = 0 to 1
    while (true) {
        yield(x to y)
        if (abs(x) == abs(y) && (x < 0 || y < 0) || (x >= 0 && 1 == (y - x))) {
            val tmp = dx
            dx = -dy
            dy = tmp
        }
        x += dx
        y += dy
    }
}

private fun solvePartOne(number: Int) = spiral().take(number).last().let { (a, b) -> abs(a) + abs(b) }
private fun solvePartTwo(number: Int): Int {
    val directions = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
    val grid = mutableMapOf<Pair<Int, Int>, Int>()
    fun fillGrid(position: Pair<Int, Int>): Int {
        grid[position] = when (position) {
            0 to 0 -> 1
            else -> directions
                .map { (dx, dy) -> position.first + dx to position.second + dy }
                .sumOf { grid.getOrDefault(it, 0) }
        }
        return grid[position]!!
    }
    return spiral().dropWhile { fillGrid(it) <= number }.first().let { grid[it]!! }
}

class Day03 {
    @Test
    fun `part 1 test data`() {
        assertEquals(0, solvePartOne(1))
        assertEquals(3, solvePartOne(12))
        assertEquals(2, solvePartOne(23))
        assertEquals(31, solvePartOne(1024))
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day03.txt").readText().toInt())

        assertEquals(430, result)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(806, solvePartTwo(747))
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day03.txt").readText().toInt())

        assertEquals(312453, result)
    }
}
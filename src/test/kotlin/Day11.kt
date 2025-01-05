package org.ricall.day11

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.abs
import kotlin.test.assertEquals

private data class HexLocation(val x: Int, val y: Int) {
    operator fun plus(other: HexLocation) = HexLocation(x + other.x, y + other.y)
    fun distance() = (abs(x) + abs(y)) / 2

    companion object {
        fun parseDelta(direction: String) = when (direction) {
            "n" -> HexLocation(0, 2)
            "s" -> HexLocation(0, -2)
            "ne" -> HexLocation(1, 1)
            "se" -> HexLocation(1, -1)
            "nw" -> HexLocation(-1, 1)
            "sw" -> HexLocation(-1, -1)
            else -> error("Unexpected direction: $direction")
        }
    }
}

private fun walk(input: String) = input.split(',')
    .map(HexLocation::parseDelta)
    .runningFold(HexLocation(0, 0)) { position, delta -> position + delta }

private fun solvePartOne(input: String) = walk(input).last().distance()
private fun solvePartTwo(input: String) = walk(input).maxOf(HexLocation::distance)

class Day11 {
    @Test
    fun `part 1 test data`() {
        assertEquals(3, solvePartOne("ne,ne,ne"))
        assertEquals(0, solvePartOne("ne,ne,sw,sw"))
        assertEquals(2, solvePartOne("ne,ne,s,s"))
        assertEquals(3, solvePartOne("se,sw,se,sw,sw"))
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day11.txt").readText())

        assertEquals(764, result)
    }

    @Test
    fun `part 2 test data`() {
        assertEquals(2, solvePartTwo("ne,ne,sw,sw"))
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day11.txt").readText())

        assertEquals(1532, result)
    }
}
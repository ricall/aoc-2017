package org.ricall.dayxx

import org.junit.jupiter.api.Test
import org.ricall.dayxx.Direction.DOWN
import java.io.File
import kotlin.test.assertEquals

typealias Location = Pair<Int, Int>

private enum class Direction(val delta: Location) {
    UP(0 to -1),
    DOWN(0 to 1),
    LEFT(-1 to 0),
    RIGHT(1 to 0)
}

private operator fun Pair<Int, Int>.plus(direction: Direction): Pair<Int, Int> =
    (this.first + direction.delta.first) to (this.second + direction.delta.second)

private const val SPACE = ' '

private fun solve(input: String): Pair<String, Int> {
    val diagram = input.lines()
    var (current, direction) = (diagram[0].indexOf('|') to 0) to DOWN
    val visited = mutableSetOf<Location>()
    val characters = mutableListOf<Char>()
    var steps = 0

    fun characterAt(location: Location): Char {
        if (location.second !in diagram.indices || location.first !in diagram[location.second].indices) {
            return SPACE
        }
        return diagram[location.second][location.first]
    }

    while (true) {
        visited.add(current)
        when (characterAt(current)) {
            '|', '-' -> {}
            '+' -> direction = Direction.entries.find {
                val next = current + it
                !visited.contains(next) && characterAt(next) != SPACE
            } ?: error("Cannot find direction out of $current")

            SPACE -> return characters.joinToString("") to steps
            else -> characters += characterAt(current)
        }
        current += direction
        steps++
    }
}

private fun solvePartOne(input: String) = solve(input).first
private fun solvePartTwo(input: String) = solve(input).second

class Day19 {
    private val TEST_DATA = """
        |     |          
        |     |  +--+    
        |     A  |  C    
        | F---|----E|--+ 
        |     |  |  |  D 
        |     +B-+  +--+ """.trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(TEST_DATA)

        assertEquals("ABCDEF", result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day19.txt").readText())

        assertEquals("HATBMQJYZ", result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(TEST_DATA)

        assertEquals(38, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day19.txt").readText())

        assertEquals(16332, result)
    }
}
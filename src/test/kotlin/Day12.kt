package org.ricall.day12

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private val PROGRAM_REGEX = """^(\d+) <-> (.*)$""".toRegex()
private fun parsePrograms(input: String) = input.lines().associate {
    PROGRAM_REGEX.find(it)?.let {
        val (source, pipes) = it.destructured
        source.toInt() to pipes.split(", ").map(String::toInt)
    } ?: error("Invalid program input: $it")
}

private fun associateGroups(input: String): Map<Int, Int> {
    val programs = parsePrograms(input)
    val visited = mutableSetOf<Int>()
    fun countProgramsInGroup(program: Int): Int {
        if (visited.contains(program)) {
            return 0
        }
        visited.add(program)
        return programs[program]!!.fold(1) { acc, value -> acc + countProgramsInGroup(value) }
    }
    return programs.keys.associateWith { countProgramsInGroup(it) }
}

private fun solvePartOne(input: String) = associateGroups(input)[0]!!
private fun solvePartTwo(input: String) = associateGroups(input).filterValues { it > 0 }.size

private val TEST_DATA = """
        |0 <-> 2
        |1 <-> 1
        |2 <-> 0, 3, 4
        |3 <-> 2, 4
        |4 <-> 2, 3, 6
        |5 <-> 6
        |6 <-> 4, 5""".trimMargin()

class Day12 {
    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(TEST_DATA)

        assertEquals(6, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day12.txt").readText())

        assertEquals(130, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(TEST_DATA)

        assertEquals(2, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day12.txt").readText())

        assertEquals(189, result)
    }
}
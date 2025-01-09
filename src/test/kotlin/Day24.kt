package org.ricall.day24

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private data class Component(val e1: Int, val e2: Int) {
    fun has(match: Int) = e1 == match || e2 == match
    fun other(match: Int) = when {
        e1 == match -> e2
        e2 == match -> e1
        else -> error("no match for $match")
    }
    fun sum() = e1 + e2
    override fun toString(): String = "$e1/$e2"
}

private fun solve(input: String): List<Pair<Int,Int>> {
    val components = input.lines().map { line -> line.split("/")
        .map(String::toInt)
        .let { Component(it[0], it[1]) }
    }
    fun bridgesMatching(match: Int, components: List<Component>): List<List<Int>> = buildList {
        for (component in components) {
            if (component.has(match)) {
                val componentSum = listOf(component.sum())
                add(componentSum)
                bridgesMatching(component.other(match), components - component).forEach { value ->
                    add(componentSum + value)
                }
            }
        }
    }
    return bridgesMatching(0, components).map { list -> list.size to list.sum() }
}

private fun solvePartOne(input: String) = solve(input).maxOf { it.second }
private fun solvePartTwo(input: String) = solve(input).sortedWith(compareBy({ -it.first }, { -it.second }))[0].second

class Day24 {
    private val TEST_DATA = """
        |0/2
        |2/2
        |2/3
        |3/4
        |3/5
        |0/1
        |10/1
        |9/10""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(TEST_DATA)

        assertEquals(31, result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day24.txt").readText())

        assertEquals(1656, result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(TEST_DATA)

        assertEquals(19, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day24.txt").readText())

        assertEquals(1642, result)
    }
}
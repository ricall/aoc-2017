package org.ricall.day07

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private data class Node(val name: String, val weight: Int, val children: List<String>)

private val LINE_REGEX = """^(\w+) [(](\d+)[)]( -> (.*))?$""".toRegex()
private fun parseNode(line: String) = LINE_REGEX.find(line)?.let {
    val (name, weight, _, children) = it.destructured
    Node(name, weight.toInt(), if (children.isNotEmpty()) children.split(", ") else emptyList())
}

private fun parseInput(input: String) = input.lines().mapNotNull(::parseNode)

private fun List<Node>.findRoot() = filter { it.children.isNotEmpty() }
    .find { candidate -> none { it.children.contains(candidate.name) } }

private fun List<Node>.nodeFor(name: String) = find { it.name == name } ?: error("Node $name not found")
private fun List<Node>.weight(name: String): Int =
    nodeFor(name).let { node -> node.weight + node.children.sumOf { name -> this.weight(name) } }

private fun solvePartOne(input: String) = parseInput(input).findRoot()?.name

private fun solvePartTwo(input: String): Int = parseInput(input).let { list ->
    var node = list.findRoot()!!
    var previousCorrectWeight = 0
    while (node.children.isNotEmpty()) {
        val weights = node.children.groupBy { list.weight(it) }
        val correctWeight = weights.filterValues { it.size > 1 }.keys.first()
        val incorrect = weights.filterValues { it.size == 1 }
        if (incorrect.isEmpty()) { // Children are balanced
            return node.weight - (list.weight(node.name) - previousCorrectWeight)
        }
        node = list.nodeFor(incorrect.values.first().first())
        previousCorrectWeight = correctWeight
    }
    error("tree is balanced")
}

class Day07 {
    private val TEST_DATA = """
        |pbga (66)
        |xhth (57)
        |ebii (61)
        |havc (66)
        |ktlj (57)
        |fwft (72) -> ktlj, cntj, xhth
        |qoyq (66)
        |padx (45) -> pbga, havc, qoyq
        |tknk (41) -> ugml, padx, fwft
        |jptl (61)
        |ugml (68) -> gyxo, ebii, jptl
        |gyxo (61)
        |cntj (57)""".trimMargin()

    @Test
    fun `part 1 test data`() {
        val result = solvePartOne(TEST_DATA)

        assertEquals("tknk", result)
    }

    @Test
    fun `part 1`() {
        val result = solvePartOne(File("./inputs/day07.txt").readText())

        assertEquals("gmcrj", result)
    }

    @Test
    fun `part 2 test data`() {
        val result = solvePartTwo(TEST_DATA)

        assertEquals(60, result)
    }

    @Test
    fun `part 2`() {
        val result = solvePartTwo(File("./inputs/day07.txt").readText())

        assertEquals(391, result)
    }
}